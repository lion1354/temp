/**
 * Copyright © 2015, HuaYu iStudy Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.thread;

import cn.forp.framework.core.FORP;
import cn.forp.framework.core.thread.BasePeriodThread;
import cn.forp.framework.core.util.DateUtil;
import cn.forp.framework.core.util.Redis;
import cn.forp.framework.platform.service.DBBackupLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库备份后台服务
 *
 * @author	Bruce
 * @version	2012-08-31 09:18:03
 */
public class DatabaseBackup extends BasePeriodThread
{
  /**
   * Logger for this class
   */
	private static final Logger lg = LoggerFactory.getLogger(DatabaseBackup.class);

	/**
	 * 构造初始化
	 * 
	 * @param name	服务名称
	 */
	public DatabaseBackup(String name)
	{
		super(name);
	}

	/* (non-Javadoc)
	 * @see com.chinasoftware.core.thread.BasePeriodThread#getWaitSeconds()
	 */
	public long getWaitSeconds()
	{
		// 轮询间隔：1小时
		return 60 * 60;
	}

	/* (non-Javadoc)
	 * @see com.chinasoftware.core.thread.BasePeriodThread#doWork()
	 */
	@Override
	public void doWork() throws Exception
	{
		DBBackupLogService service = FORP.SPRING_CONTEXT.getBean(DBBackupLogService.class);
		// 时间 + 备份周期
		if (isTimeToBackup() && service.canBackupDatabaseNow())
			service.backup();
		else
			lg.debug("数据库备份时间未到，不备份！");
	}

	/**
	 * 检查备份时间
	 */
	public boolean isTimeToBackup() throws Exception
	{
		int nowTime = Integer.parseInt(DateUtil.getTodayTime().substring(0, 5).replaceAll(":", ""));
		int backupTime = Integer.parseInt(Redis.getHashMap(FORP.CACHE_DOMAIN_PROFILE + 1, null, "db.backup.times").get(0).replaceAll(":", ""));
		lg.debug("{} <---> {}", nowTime, backupTime);

		if (nowTime >= backupTime)
			return true;
		else
			return false;
	}
}