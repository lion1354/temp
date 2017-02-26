/**
 * Copyright © 2017, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.information.service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.BusinessException;
import cn.forp.framework.core.FORP;
import cn.forp.framework.core.util.MongoDB;
import cn.forp.framework.core.util.QiNiu;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.core.vo.PageSort;
import cn.forp.framework.platform.vo.User;
import cn.forp.information.vo.Consultation;
import com.alibaba.fastjson.JSONArray;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * 入驻商户管理Service
 *
 * @author	Bruce
 * @version	2017-02-18 14:49:06
 */
@Service
public class MerchantService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(MerchantService.class);

	/**
	 * 分页查询
	 *
	 * @param type		  商户类别
	 * @param name      名称
	 * @param fromDate  入驻到期日期
	 * @param toDate    入驻到期日期
	 * @param type		  状态
	 * @param ps		    分页排序信息
	 */
	public Page<Consultation> search(String type, String name, String fromDate, String toDate, PageSort ps) throws Exception
	{
		String sql = "select * from Sys_Consultation where FormType=?";
		JSONArray params = new JSONArray();
		params.add("1");

		// 商户类型
		if (StringUtils.isNotBlank(type))
		{
			sql += " and " + type + "=?";
			params.add("1");
		}

		// 名称
		if (StringUtils.isNotBlank(name))
		{
			sql += " and Name like ?";
			params.add("%" + name + "%");
		}

		// 入驻到期日期 - 开始日期
		if (StringUtils.isNotBlank(fromDate))
		{
			sql += " and EndDate>=?";
			params.add(DateUtils.parseDate(fromDate + " 00:00:00", FORP.PATTERN_DATE_TIME));
		}

		// 入驻到期日期 - 结束日期
		if (StringUtils.isNotBlank(toDate))
		{
			sql += " and EndDate<=?";
			params.add(DateUtils.parseDate(toDate + " 23:59:59", FORP.PATTERN_DATE_TIME));
		}

		lg.debug("SQL：{}", sql);
		return findByPage(sql, params.toArray(), Consultation.class, ps);
	}

	/**
	 * 添加
	 *
	 * @param sessionUser   操作人
	 * @param merchant      商户
	 * @param image         图片
	 */
	public void create(User sessionUser, Consultation merchant, MultipartFile image) throws Exception
	{
		// 重名检查
		if (isFieldDuplicate("Sys_Consultation", "Name", merchant.getName(), "FormType='" + merchant.getFormType() + "'"))
			throw new BusinessException("商户“" + merchant.getName() + "”已登记，请检查您的输入！");

		// 保存文件
		if (null != image && !image.isEmpty())
		{
			// 存入本地MongoDB
			DBObject metaData = new BasicDBObject();
			metaData.put("type", "merchant");
			String imageId = MongoDB.saveFile(null, image, metaData);
			merchant.setImageId(imageId);

			// 上传至七牛第三方存储
			QiNiu.upload(image, imageId);
		}

		// 保存数据库记录
		merchant.setCreateUserName(sessionUser.getUserName());
		merchant.setCreateDate(new Timestamp(System.currentTimeMillis()));
		insertIntoTable(merchant, null, new String[]{"lastModifyDate", "lastModifyUserName", "lastModifyUserId"});
	}

	/**
	 * 修改
	 *
	 * @param sessionUser   操作人
	 * @param merchant      商户
	 * @param image         图片
	 */
	public void update(User sessionUser, Consultation merchant, MultipartFile image) throws Exception
	{
		// 重名检查
		if (isFieldDuplicate("Sys_Consultation", "Name", merchant.getName(), "ID<>" + merchant.getId() + " and FormType='" + merchant.getFormType() + "'"))
			throw new BusinessException("商户“" + merchant.getName() + "”已登记，请检查您的输入！");

		// 更新文件
		String origImageId = getValueById("Sys_Consultation", "ImageId", merchant.getId());
		if (null != image && !image.isEmpty())
		{
			// 存入本地MongoDB
			DBObject metaData = new BasicDBObject();
			metaData.put("type", "merchant");
			String newImageId = MongoDB.saveFile(origImageId, image, metaData);

			// 上传至七牛第三方存储 TODO 删除原图片
			QiNiu.upload(image, newImageId);

			// 更新新图片ID
			merchant.setImageId(newImageId);
		}
		else
			merchant.setImageId(origImageId);

		merchant.setLastModifyUserId(sessionUser.getId());
		merchant.setLastModifyUserName(sessionUser.getUserName());
		merchant.setLastModifyDate(new Timestamp(System.currentTimeMillis()));
		updateTable(merchant, null, new String[]{"formType", "status", "createDate", "createUserName"});
	}
}