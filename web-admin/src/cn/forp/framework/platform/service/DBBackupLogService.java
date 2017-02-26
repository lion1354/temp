/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.FORP;
import cn.forp.framework.core.util.DateUtil;
import cn.forp.framework.core.util.Redis;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.core.vo.PageSort;
import cn.forp.framework.platform.vo.DatabaseBackupLog;
import oracle.sql.TIMESTAMP;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 数据库备份日志Service
 *
 * @author	Bruce
 * @version	2016-08-03 14:30:59
 */
@Service
public class DBBackupLogService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(DBBackupLogService.class);

	/**
	 * 分页查询日志列表
	 * 
	 * @param fromDate	开始日期
	 * @param toDate		结束日期
	 * @param result		结果
	 * @param ps			  分页排序信息
	 */
	public Page<DatabaseBackupLog> search(String fromDate, String toDate, int result, PageSort ps) throws Exception
	{
		lg.debug("开始日期：{}", fromDate);
		lg.debug("结束日期：{}", toDate);

		List<Object> params = new ArrayList<>();
		String sql = "select * from Forp_DBBackup where ID>0";

		// 开始日期
		if (StringUtils.isNotBlank(fromDate))
		{
			sql += " and CreateDate>=?";
			params.add(DateUtils.parseDate(fromDate + " 00:00:00", FORP.PATTERN_DATE_TIME));
		}

		// 结束日期
		if (StringUtils.isNotBlank(toDate))
		{
			sql += " and CreateDate<=?";
			params.add(DateUtils.parseDate(toDate + " 23:59:59", FORP.PATTERN_DATE_TIME));
		}

		// 结果
		if (-1 != result)
		{
			// 成功
			if (0 == result)
			{
				sql += " and Result=?";
				params.add(0);
			}
			else
			{
				// 失败
				sql += " and Result!=?";
				params.add(0);
			}
		}

		lg.debug("SQL：{}", sql);
		return findByPage(sql, params.toArray(new Object[0]), DatabaseBackupLog.class, ps);
	}

	/**
	 * 是否现在就备份数据库
	 * 
	 * @return boolean
	 */
	public boolean canBackupDatabaseNow() throws Exception
	{
		boolean needBackup = false;

		SqlRowSet rs = jdbc.queryForRowSet("select Max(CreateDate) as CreateDate from Forp_DBBackup where Result=?", 0);
		rs.next();

		if (null == rs.getObject("CreateDate"))
			needBackup = true;
		else
		{
			Timestamp lastBackupDate;
			if ("Oracle".equalsIgnoreCase(FORP.DB_TYPE))
				lastBackupDate = ((TIMESTAMP) rs.getObject("CreateDate")).timestampValue();
			else
				lastBackupDate = rs.getTimestamp("CreateDate");

			// 备份参数取Domain=1的企业）
			Map<String, String> params = Redis.getAllHashMap(FORP.CACHE_DOMAIN_PROFILE + 1, null);
			// 格式化时间部分
			String dateTime = DateFormatUtils.format(lastBackupDate, FORP.PATTERN_DATE) + " " + params.get("db.backup.times") + ":00";
			Date lDate = DateUtils.parseDate(dateTime, FORP.PATTERN_DATE_TIME);
			// 下一次备份日期
			Date nextBackupDate = DateUtils.addDays(lDate, new Integer(params.get("db.backup.period")));
			Date now = new Date();
			lg.debug("下一次备份日期：{}，当前日期：{}", DateFormatUtils.format(nextBackupDate, FORP.PATTERN_DATE_TIME), DateFormatUtils.format(now, FORP.PATTERN_DATE_TIME));
			if (now.getTime() >= nextBackupDate.getTime())
				needBackup = true;

			lg.debug("Backup now: {}", needBackup);
		}

		return needBackup;
	}

	/**
	 * 添加数据库备份日志
	 * 
	 * @param log 日志信息
	 */
	public void addDatabaseBackupLog(DatabaseBackupLog log) throws Exception
	{
		jdbc.update("insert into Forp_DBBackup (CreateDate, FileName, FileSize, BeginTime, EndTime, Result) values(?, ?, ?, ?, ?, ?)",
				new Date(), log.getFileName(), log.getFileSize(), log.getBeginTime(), log.getEndTime(), log.getResult());
	}

	/**
	 * 备份数据库
	 */
	public void backup() throws Exception
	{
		lg.info("\r\n");
		lg.info("开始备份数据库：");

		// 检查备份目录
		String backupFolder = FORP.WEB_APP_PATH + "/disk-file/db-backup/" + DateUtil.getTodayDate().substring(0, 7);
		File folder = new File(backupFolder);
		if (!folder.exists())
		{
			folder.mkdirs();
			lg.info("初始化备份文件目录：{}", backupFolder);
		}

		String nowStr = DateUtil.getTodayDate() + "_" + DateUtil.getTodayTime().replaceAll(":", "-");
		String backupFileName = nowStr + ".dmp";
		// File logFile = null;
		// Oracle：exp ipt/system@ipt owner=ipt file=d:\backup\voicemate_backup_2006-4-20_12-00-00.dmp log=d:\backup\voicemate_backup_2006-4-20_12-00-00.log
		String cmd = "exp " + FORP.DB_USER_NAME + "/" + FORP.DB_PASSWORD + "@" + FORP.DB_NAME + " " + "owner="
				+ FORP.DB_USER_NAME + " " + "file=" + backupFolder + "/" + backupFileName + " " + "log=" + backupFolder + "/" + nowStr + ".log";

		if ("MySQL".equalsIgnoreCase(FORP.DB_TYPE))
		{
			// MySQL：mysqldump -uajin -p -h192.168.1.2 -P3306 --default-character-set=utf8 test_db >C:/TEMP/Test.sql
			backupFileName = nowStr + ".sql";
			// logFile = new File(backupFolder + "/" + nowStr + ".log");
			// if (!logFile.exists())
			// logFile.createNewFile();

			cmd = FORP.WEB_APP_PATH + "/third/mysqldump.exe --default-character-set=utf8 -P" + FORP.DB_PORT + " -u" + FORP.DB_USER_NAME + " " +
						"-p" + FORP.DB_PASSWORD + " -h" + FORP.DB_IP + " " + FORP.DB_NAME + " " +
						"-r \"" + backupFolder + "/" + backupFileName + "\"";
		}

		lg.debug("数据库备份命令：{}", cmd);
		Timestamp beginTime = new Timestamp(System.currentTimeMillis());

		Process proc = Runtime.getRuntime().exec("cmd.exe /C " + cmd);
		String logs = IOUtils.toString(proc.getErrorStream(), "GBK");
		lg.debug(logs);

		// // MySQL备份命令不提供自动生成日志文件，手工记录日志内容（命令输出为空，暂时不生成日志文件）
		// if (null != logFile)
		// {
		// IOUtils.write(logs.toString().getBytes(), new FileOutputStream(logFile));
		// lg.info("手工创建备份日志文件：" + logFile.getAbsolutePath());
		// }

		// 等待命令执行完毕，然后返回外部命令执行的结果
		proc.waitFor();
		// 0 - 成功，其它 - 失败
		int result = (proc.exitValue());
		lg.info("备份结果：{}", 0 == result ? "成功" : "失败");

		// 压缩备份文件
		String zipFileName = FilenameUtils.getBaseName(backupFileName) + ".rar";
		if (0 == result)
		{
			FileInputStream backupFIS = new FileInputStream(backupFolder + "/" + backupFileName);

			ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(backupFolder + "/" + zipFileName));
			zip.putNextEntry(new ZipEntry(backupFileName));
			zip.write(IOUtils.toByteArray(backupFIS));
			zip.closeEntry();
			zip.close();

			// 删除原始备份文件
			IOUtils.closeQuietly(backupFIS);
			FileUtils.deleteQuietly(new File(backupFolder + "/" + backupFileName));
		}

		Timestamp endTime = new Timestamp(System.currentTimeMillis());
		lg.info("备份开始时间：{}", DateFormatUtils.format(beginTime, FORP.PATTERN_DATE_TIME));
		lg.info("备份结束时间：{}", DateFormatUtils.format(endTime, FORP.PATTERN_DATE_TIME));

		DatabaseBackupLog log = new DatabaseBackupLog();
		log.setFileName(zipFileName);
		File dmpFile = new File(backupFolder + "/" + zipFileName);
		log.setFileSize(dmpFile.length());
		log.setBeginTime(beginTime);
		log.setEndTime(endTime);
		log.setResult(result);
		// 记录备份日志
		addDatabaseBackupLog(log);
	}

	/**
	 * 删除备份日志和磁盘文件
	 * 
	 * @param id  日志ID
	 */
	public void delete(Long id) throws Exception
	{
		SqlRowSet rs = jdbc.queryForRowSet("select FileName, CreateDate from Forp_DBBackup where ID=?", id);
		rs.next();

		String fileName = rs.getString("FileName");
		Timestamp createDate;
		if ("Oracle".equalsIgnoreCase(FORP.DB_TYPE))
			createDate = ((TIMESTAMP) rs.getObject("CreateDate")).timestampValue();
		else
			createDate = rs.getTimestamp("CreateDate");

		// 备份目录
		String backupFolder = FORP.WEB_APP_PATH + "/disk-file/db-backup/" + DateFormatUtils.format(createDate, "yyyy-MM");

		// 删除磁盘文件
		FileUtils.deleteQuietly(new File(backupFolder + "/" + fileName));
		FileUtils.deleteQuietly(new File(backupFolder + "/" + fileName.replaceAll(".rar", ".log")));
		// 删除数据库记录
		jdbc.update("delete from Forp_DBBackup where ID=?", id);
	}
}