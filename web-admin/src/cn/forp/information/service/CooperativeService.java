/**
 * Copyright © 2017, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.information.service;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.BusinessException;
import cn.forp.framework.core.util.MongoDB;
import cn.forp.framework.core.util.QiNiu;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.core.vo.PageSort;
import cn.forp.framework.platform.vo.User;
import cn.forp.information.vo.Cooperative;

/**
 * 合作社展台管理Service
 *
 * @author Apple
 * @version 2017-02-18 12:09:06
 */
@Service
public class CooperativeService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(CooperativeService.class);

	/**
	 * 分页查询
	 */
	public Page<Cooperative> search(String name, String contactPerson, int status, PageSort ps) throws Exception
	{
		String sql = "select * from Sys_Cooperative where 1=1";
		JSONArray params = new JSONArray();

		if (StringUtils.isNotBlank(name))
		{
			sql += " and Name like ?";
			params.add("%" + name + "%");
		}

		if (StringUtils.isNotBlank(contactPerson))
		{
			sql += " and ContactPerson like ?";
			params.add("%" + contactPerson + "%");
		}

		if (-1 != status)
		{
			sql += " and Status=?";
			params.add(status);
		}
		lg.debug("SQL：{}", sql);

		return findByPage(sql, params.toArray(), Cooperative.class, ps);
	}

	/**
	 * 添加
	 */
	public void create(User sessionUser, Cooperative cooperative, MultipartFile image) throws Exception
	{
		// 重名检查
		if (isFieldDuplicate("Sys_Cooperative", "Name", cooperative.getName(), null))
			throw new BusinessException("合作社展台“" + cooperative.getName() + "”已登记，请检查您的输入！");

		// 保存文件
		if (null != image && !image.isEmpty())
		{
			// 存入本地MongoDB
			DBObject metaData = new BasicDBObject();
			metaData.put("type", "merchant");
			String imageId = MongoDB.saveFile(null, image, metaData);
			cooperative.setImageId(imageId);

			// 上传至七牛第三方存储
			QiNiu.upload(image, imageId);
		}

		if (StringUtils.isBlank(cooperative.getImageId()))
		{
			cooperative.setImageId("");
		}

		// 保存数据库记录

		cooperative.setCreateUserName(sessionUser.getUserName());
		cooperative.setCreateDate(new Timestamp(System.currentTimeMillis()));
		insertIntoTable(cooperative, null, null);
	}

	/**
	 * 修改
	 */
	public void update(User sessionUser, Cooperative cooperative, MultipartFile image) throws Exception
	{
		// 重名检查
		if (isFieldDuplicate("Sys_Cooperative", "Name", cooperative.getName(), "ID<>" + cooperative.getId()))
			throw new BusinessException("合作社展台“" + cooperative.getName() + "”已登记，请检查您的输入！");
		// 更新文件
		String origImageId = getValueById("Sys_Consultation", "ImageId", cooperative.getId());
		if (null != image && !image.isEmpty())
		{
			// 存入本地MongoDB
			DBObject metaData = new BasicDBObject();
			metaData.put("type", "merchant");
			String newImageId = MongoDB.saveFile(origImageId, image, metaData);

			// 上传至七牛第三方存储 TODO 删除原图片
			QiNiu.upload(image, newImageId);

			// 更新新图片ID
			cooperative.setImageId(newImageId);
		}
		else
			cooperative.setImageId(origImageId);

		if (StringUtils.isBlank(cooperative.getImageId()))
		{
			cooperative.setImageId("");
		}

		cooperative.setLastModifyUserId(sessionUser.getId());
		cooperative.setLastModifyUserName(sessionUser.getUserName());
		cooperative.setLastModifyDate(new Timestamp(System.currentTimeMillis()));
		updateTable(cooperative, null, new String[] { "createDate", "createUserName" });
	}
}