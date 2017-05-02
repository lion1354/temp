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
import org.springframework.jdbc.support.rowset.SqlRowSet;
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
import cn.forp.information.vo.Technology;

/**
 * 新技术管理Service
 *
 * @author Apple
 * @version 2017-02-18 14:49:06
 */
@Service
public class TechnologyService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(TechnologyService.class);

	/**
	 * 分页查询
	 *
	 * @param title
	 *            信息标题
	 * @param category
	 *            技术信息类别
	 * @param ps
	 *            分页排序信息
	 */
	public Page<Technology> search(String title, int category, PageSort ps) throws Exception
	{
		// 查询
		String sql = "select * from Sys_Technology where 1=1";
		JSONArray params = new JSONArray();

		if (StringUtils.isNotBlank(title))
		{
			sql += " and Title like ?";
			params.add("%" + title + "%");
		}

		if (-1 != category)
		{
			sql += " and Category=?";
			params.add(category);
		}
		
		ps.setSortName("LastModifyDate");
		ps.setSortOrder("desc");
		lg.debug("SQL：{}", sql);

		Page<Technology> page = findByPage(sql, params.toArray(), Technology.class, ps);

		// 读取文档内容
		for (Technology t : page.getRows())
		{
			t.setInfo(MongoDB.loadDocument(t.getDocId(), "information.technology"));
		}

		return page;
	}

	/**
	 * 添加
	 *
	 * @param sessionUser
	 *            操作人
	 * @param technology
	 *            新技术
	 * @param image
	 *            图片
	 */
	public void create(User sessionUser, Technology technology, MultipartFile image) throws Exception
	{
		// 重名检查
		if (isFieldDuplicate("Sys_Technology", "Title", technology.getTitle(),
				"Category='" + technology.getCategory() + "'"))
			throw new BusinessException("新技术“" + technology.getTitle() + "”已登记，请检查您的输入！");

		// 保存图片
		if (null != image && !image.isEmpty())
		{
			// 存入本地MongoDB
			DBObject metaData = new BasicDBObject();
			metaData.put("type", "technology");
			String imageId = MongoDB.saveFile(null, image, metaData);
			technology.setImageId(imageId);

			// 上传至七牛第三方存储
			QiNiu.upload(image, imageId);
		}

		// 保存文本内容
		String docId = MongoDB.saveDocument(technology.getInfo(), "information.technology");
		technology.setDocId(docId);

		// 保存数据库记录
		technology.setCreateUserName(sessionUser.getUserName());
		technology.setCreateDate(new Timestamp(System.currentTimeMillis()));
		technology.setLastModifyUserId(sessionUser.getId());
		technology.setLastModifyUserName(sessionUser.getUserName());
		technology.setLastModifyDate(new Timestamp(System.currentTimeMillis()));
		insertIntoTable(technology);
	}

	/**
	 * 修改
	 *
	 * @param sessionUser
	 *            操作人
	 * @param technology
	 *            新技术
	 * @param image
	 *            图片
	 */
	public void update(User sessionUser, Technology technology, MultipartFile image) throws Exception
	{
		// 重名检查
		if (isFieldDuplicate("Sys_Technology", "Title", technology.getTitle(),
				"ID<>" + technology.getId() + " and Category='" + technology.getCategory() + "'"))
			throw new BusinessException("新技术“" + technology.getTitle() + "”已登记，请检查您的输入！");

		SqlRowSet rs = jdbc.queryForRowSet("select ImageId, DocId from Sys_Technology where ID=?", technology.getId());
		rs.next();
		// 更新文件
		String origImageId = rs.getString("ImageId");
		if (null != image && !image.isEmpty())
		{
			// 存入本地MongoDB
			DBObject metaData = new BasicDBObject();
			metaData.put("type", "technology");
			String newImageId = MongoDB.saveFile(origImageId, image, metaData);

			// 上传至七牛第三方存储 TODO 删除原图片
			QiNiu.upload(image, newImageId);

			// 更新新图片ID
			technology.setImageId(newImageId);
		}
		else
			technology.setImageId(origImageId);

		// 更新文本内容
		technology.setDocId(rs.getString("DocId"));
		MongoDB.updateDocument(technology.getDocId(), technology.getInfo(), "information.technology");

		technology.setLastModifyUserId(sessionUser.getId());
		technology.setLastModifyUserName(sessionUser.getUserName());
		technology.setLastModifyDate(new Timestamp(System.currentTimeMillis()));
		updateTable(technology, null, new String[] {"createDate", "createUserName"});
	}
}