/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.util;

import cn.forp.framework.core.FORP;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * MongoDB全局类
 *
 * @author	Bruce
 * @version	2016年5月21日 下午3:15:46
 */
public class MongoDB
{
	/**
	 * Log4j logger
	 */
	private final static Logger lg = LoggerFactory.getLogger(MongoDB.class);

	// =================================================================
	//		Document
	// =================================================================

	/**
	 * 新增Document
	 * 
	 * @param doc							文档内容
	 * @param collectionName	集合名称
	 */
	public static String saveDocument(String doc, String collectionName) throws Exception
	{
		lg.info("新增Document：{}", collectionName);

		BasicDBObject obj = new BasicDBObject();
		obj.put("doc", doc);

		MongoTemplate mgt = (MongoTemplate) FORP.SPRING_CONTEXT.getBean("documentTemplate");
		mgt.save(obj, collectionName);

		return obj.getObjectId("_id").toString();
	}

	/**
	 * 更新Document
	 * 
	 * @param id              文档ID
	 * @param doc             文档内容
	 * @param collectionName  集合名称
	 */
	public static void updateDocument(String id, String doc, String collectionName) throws Exception
	{
		lg.info("修改Document：{}[{}]", id, collectionName);

		if (StringUtils.isNotBlank(id))
		{
			MongoTemplate mgt = (MongoTemplate) FORP.SPRING_CONTEXT.getBean("documentTemplate");
			mgt.updateFirst(new Query(Criteria.where("_id").is(id)), Update.update("doc", doc), collectionName);
		}
	}

	/**
	 * 加载Document
	 * 
	 * @param id              文档ID
	 * @param collectionName  集合名称
	 */
	public static String loadDocument(String id, String collectionName) throws Exception
	{
		lg.info("加载Document：{}[{}]", id, collectionName);

		if (StringUtils.isNotBlank(id))
		{
			MongoTemplate mgt = (MongoTemplate) FORP.SPRING_CONTEXT.getBean("documentTemplate");
			BasicDBObject obj = mgt.findOne(new Query(Criteria.where("_id").is(id)), BasicDBObject.class, collectionName);

			return obj.getString("doc");
		}
		else
			return "";
	}

	/**
	 * 删除Document
	 * 
	 * @param id              文档ID
	 * @param collectionName  集合名称
	 */
	public static void deleteDocument(String id, String collectionName) throws Exception
	{
		lg.info("删除Document：{}[{}]", id, collectionName);
		MongoTemplate mgt = (MongoTemplate) FORP.SPRING_CONTEXT.getBean("documentTemplate");
		mgt.remove(new Query(Criteria.where("_id").is(id)), collectionName);
	}

	// =================================================================
	//		GridFS
	// =================================================================

	/**
	 * 存储附件（如果存在原附件，系统会自动先删除然后再插入一条新的记录）
	 *
	 * @param bytes				  附件字节数组
	 * @param id						原附件ID（新增时传入null）
	 * @param fileName			原始文件名称
	 * @param contentType		资源mine类型
	 * @param metaData			MongoDB元数据
	 * @return MongoDB ID
	 */
	public static String saveFile(byte[] bytes, String id, String fileName, String contentType, DBObject metaData) throws Exception
	{
		GridFsTemplate gridFs = (GridFsTemplate) FORP.SPRING_CONTEXT.getBean("gridFsTemplate");

		// 检查是否存在原附件
		if (StringUtils.isNotBlank(id))
		{
			// TODO MongoDB暂时不支持update操作，所以更新时先删除再插入新的记录。
			gridFs.delete(new Query(Criteria.where("_id").is(id)));
		}

		InputStream is = new ByteArrayInputStream(bytes);
		String fileId = gridFs.store(is, fileName, contentType, metaData).getId().toString();
		IOUtils.closeQuietly(is);

		return fileId;
	}

	/**
	 * 存储附件（如果存在原附件，系统会自动先删除然后再插入一条新的记录）
	 *
	 * @param id				原附件ID（新增时传入null）
	 * @param file			附件
	 * @param metaData	MongoDB元数据
	 * @return MongoDB  ID
	 */
	public static String saveFile(String id, MultipartFile file, DBObject metaData) throws Exception
	{
		GridFsTemplate gridFs = (GridFsTemplate) FORP.SPRING_CONTEXT.getBean("gridFsTemplate");
		// 检查是否存在原附件
		if (StringUtils.isNotBlank(id))
		{
			// TODO MongoDB暂时不支持update操作，所以更新时先删除再插入新的记录。
			gridFs.delete(new Query(Criteria.where("_id").is(id)));
		}

		InputStream is = file.getInputStream();
		String fileId = gridFs.store(is, file.getName(), file.getContentType(), metaData).getId().toString();
		IOUtils.closeQuietly(is);

		return fileId;
	}

	/**
	 * 删除附件
	 * 
	 * @param id	附件ID
	 */
	public static void deleteFile(String id) throws Exception
	{
		GridFsTemplate gridFs = (GridFsTemplate) FORP.SPRING_CONTEXT.getBean("gridFsTemplate");
		gridFs.delete(new Query(Criteria.where("_id").is(id)));
	}
}