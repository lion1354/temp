/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.controller;

import cn.forp.framework.core.FORP;
import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.controller.IForm;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.platform.service.DBBackupLogService;
import cn.forp.framework.platform.vo.DatabaseBackupLog;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 数据库备份日志Controller
 *
 * @author		Bruce
 * @version	2016年8月3日 下午2:49:29
 */
@RestController
public class DBBackupLogController extends BaseController
{
	/**
	 * Logger
	 */
	private static final Logger lg = LoggerFactory.getLogger(DBBackupLogController.class);
	/**
	 * 日志Service
	 */
	@Autowired
	protected DBBackupLogService service = null;

	/**
	* 分页查询
	*
	* @param req   请求参数
	*/
	@RequestMapping(path="/platform/database/log", method=RequestMethod.POST)
	public Page<DatabaseBackupLog> search(HttpServletRequest req) throws Exception
	{
	  IForm form = getFormWrapper(req);
		return service.search(form.get("fromDate"), form.get("toDate"),
				(StringUtils.isBlank(form.get("result")) ? -1 : form.getInt("result")), getPageSort(req));
	}

	/**
	 * 备份数据库
	 *
	 * @param req   请求参数
	 */
	@RequestMapping("/platform/database/backup")
	public String backup(HttpServletRequest req) throws Exception
	{
		service.backup();
		// 操作日志
		service.writeSystemLog(getSessionUser(req), req, "备份数据库", "备份结果：成功", null);

		return success("OK");
	}

	/**
	 * 删除备份日志和磁盘文件
	 *
	 * @param id    主键
	 * @param req   请求参数
	 */
	@RequestMapping(path="/platform/database/log/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable Long id, HttpServletRequest req) throws Exception
	{
		service.delete(id);
		// 操作日志
		service.writeSystemLog(getSessionUser(req), req, "删除数据库备份记录", "记录ID：" + id, null);

		return success("OK");
	}

	/**
	 * 下载数据库备份文件
	 *
	 * @param createDate  备份日期
	 * @param fileName    文件名称
	 * @param req         请求参数
	 */
	@RequestMapping(path = "/platform/database/log/{createDate}/{fileName}")
	public ResponseEntity<byte[]> download(@PathVariable String createDate, @PathVariable String fileName, HttpServletRequest req) throws Exception
	{
		lg.debug("FileName: {}，CreateDate：{}", fileName, createDate);
		// createDate = URLDecoder.decode(createDate, "ISO-8859-1");

		File dmp = new File(FORP.WEB_APP_PATH + "/disk-file/db-backup/" + createDate.substring(0, 7) + "/" + fileName.replace("-rar", ".rar"));
		if (dmp.exists())
		{
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", new String(fileName.replace("-rar", ".rar").getBytes(), "iso-8859-1"));

			InputStream is = new FileInputStream(dmp);
			ResponseEntity<byte[]> file = new ResponseEntity<>(IOUtils.toByteArray(is), headers, HttpStatus.CREATED);
			IOUtils.closeQuietly(is);
			// 操作日志
			service.writeSystemLog(getSessionUser(req), req, "下载数据库备份文件", "文件名称：" + fileName, null);

			return file;
		}
		else
			lg.warn("无效的数据库备份文件，请联系系统管理员！");

		return null;
	}
}