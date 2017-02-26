package com.tibco.ma.service;

import java.util.List;

import org.bson.Document;

import com.mongodb.client.result.DeleteResult;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.AppSetting;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
public interface AppSettingService extends BaseService<AppSetting> {

	/**
	 * 
	 * @param value
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String validateSaveValue(String value, String type) throws Exception;

	/**
	 * 
	 * @param collectionName
	 * @param id
	 * @param appId
	 * @param value
	 * @param type
	 * @param key
	 * @throws Exception
	 */
	public void save(String collectionName, String id, String appId,
			String value, String type, String key, String description) throws Exception;
	/**
	 * 
	 * @param collectionNamme
	 * @param id
	 * @return DeleteResult
	 * @throws Exception
	 */
	public DeleteResult deleteValue(String collectionNamme, String id)
			throws Exception;

	/**
	 * 
	 * @param collectionName
	 * @param filter
	 * @param sort
	 * @param page
	 * @param pageSize
	 * @param appId
	 * @param projectURI
	 * @return
	 * @throws Exception
	 */
	public Pager<Document> valuesPage(String collectionName, Document filter,
			Document sort, int page, int pageSize, String appId,
			String projectURI) throws Exception;
	/**
	 * 
	 * @param collectionName
	 * @param filter
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	public List<Document> queryAppSetting(String collectionName,
			Document filter, Document sort) throws Exception;
	/**
	 * 
	 * @param collectionName
	 * @param appId
	 * @param type
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Document queryByIdTypeAndAppId(String collectionName, String appId,
			String type, String key) throws Exception;
	/**
	 * 
	 * @param collection
	 * @param value
	 * @throws Exception
	 */
	public void save(String collection, Document value) throws Exception;
	/**
	 * 
	 * @param collection
	 * @param id
	 * @param document
	 * @throws Exception
	 */
	public void update(String collection, String id, Document document)
			throws Exception;
	/**
	 * 
	 * @param collection
	 * @param key
	 * @param appId
	 * @throws Exception
	 */
	public void deleteByKey(String collection, String key, String appId)
			throws Exception;

}
