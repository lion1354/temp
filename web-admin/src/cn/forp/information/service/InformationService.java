/**
 * Copyright © 2017, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.information.service;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.BusinessException;
import cn.forp.framework.core.FORP;
import cn.forp.framework.core.util.MongoDB;
import cn.forp.framework.core.util.QiNiu;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.core.vo.PageSort;
import cn.forp.framework.platform.vo.User;
import cn.forp.information.vo.Information;

/**
 * 信息管理Service
 *
 * @author Apple
 * @version 2017-02-18 14:49:06
 */
@Service
public class InformationService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(InformationService.class);

	/**
	 * 分页查询
	 * 
	 * @param title
	 * @param fromDate
	 * @param toDate
	 * @param category
	 * @param type
	 * @param ps
	 * @return
	 * @throws Exception
	 */
	public Page<Information> search(String title, String fromDate, String toDate, String category, String status,
			PageSort ps) throws Exception
	{
		String sql = "select * from Sys_Publish_Info where 1=1";
		JSONArray params = new JSONArray();

		if (StringUtils.isNotBlank(title))
		{
			sql += " and Title like ?";
			params.add("%" + title + "%");
		}

		// 开始日期
		if (StringUtils.isNotBlank(fromDate))
		{
			sql += " and LastModifyDate>=?";
			params.add(DateUtils.parseDate(fromDate + " 00:00:00", FORP.PATTERN_DATE_TIME));
		}

		// 结束日期
		if (StringUtils.isNotBlank(toDate))
		{
			sql += " and LastModifyDate<=?";
			params.add(DateUtils.parseDate(toDate + " 23:59:59", FORP.PATTERN_DATE_TIME));
		}

		if (!"-1".equals(category))
		{
			sql += " and Category=?";
			params.add(category);
		}

		if (!"-1".equals(status))
		{
			sql += " and Status=?";
			params.add(status);
		}
		lg.debug("SQL：{}", sql);
		return findByPage(sql, params.toArray(), Information.class, ps);
	}

	/**
	 * 添加
	 */
	public void create(User sessionUser, Information information, MultipartFile image) throws Exception
	{
		// 重名检查
		if (isFieldDuplicate("Sys_Publish_Info", "Title", information.getTitle(),
				"Category='" + information.getCategory() + "'"))
			throw new BusinessException("新信息“" + information.getTitle() + "”已登记，请检查您的输入！");

		// 保存文件
		if (null != image && !image.isEmpty())
		{
			// 存入本地MongoDB
			DBObject metaData = new BasicDBObject();
			metaData.put("type", "information");
			String imageId = MongoDB.saveFile(null, image, metaData);
			information.setImageId(imageId);

			// 上传至七牛第三方存储
			QiNiu.upload(image, imageId);
		}

		// 保存数据库记录
		information.setStatus(0);
		information.setCreateUserName(sessionUser.getUserName());
		information.setCreateDate(new Timestamp(System.currentTimeMillis()));
		information.setLastModifyUserId(sessionUser.getId());
		information.setLastModifyUserName(sessionUser.getUserName());
		information.setLastModifyDate(new Timestamp(System.currentTimeMillis()));
		insertIntoTable(information, null, null);
	}

	/**
	 * 修改
	 */
	public void update(User sessionUser, Information information, MultipartFile image) throws Exception
	{
		// 重名检查
		if (isFieldDuplicate("Sys_Publish_Info", "Title", information.getTitle(),
				"ID<>" + information.getId() + " and Category='" + information.getCategory() + "'"))
			throw new BusinessException("新信息“" + information.getTitle() + "”已登记，请检查您的输入！");

		// 更新文件
		String origImageId = getValueById("Sys_Publish_Info", "ImageId", information.getId());
		if (null != image && !image.isEmpty())
		{
			// 存入本地MongoDB
			DBObject metaData = new BasicDBObject();
			metaData.put("type", "information");
			String newImageId = MongoDB.saveFile(origImageId, image, metaData);

			// 上传至七牛第三方存储 TODO 删除原图片
			QiNiu.upload(image, newImageId);

			// 更新新图片ID
			information.setImageId(newImageId);
		}
		else
		{
			information.setImageId(origImageId);
		}
		information.setStatus(0);
		information.setLastModifyUserId(sessionUser.getId());
		information.setLastModifyUserName(sessionUser.getUserName());
		information.setLastModifyDate(new Timestamp(System.currentTimeMillis()));
		updateTable(information, null, new String[] { "createDate", "createUserName" });
	}

	public void changeStatus(String id, int status) throws Exception
	{
		lg.info("更新信息{}状态为：{}", id, status);
		jdbc.update("update Sys_Publish_Info set Status=?, LastModifyDate=? where Id=?", status, new Date(), id);
	}
}