package com.tibco.ma.service;

import java.util.List;

import org.bson.Document;

import com.mongodb.client.result.UpdateResult;
import com.tibco.ma.model.core.EntityColType;

@SuppressWarnings("rawtypes")
public interface EntityService extends BaseService {
	public String getEntityCollectionName(String appId);

	public List<Document> getAllEntityByAppId(String appId) throws Exception;

	public Document getEntityById(String appId, String id) throws Exception;

	public Document getEntityByClassName(String appId, String className)
			throws Exception;

	public Document save(String appId, Document json) throws Exception;

	public void update(String appId, Document json) throws Exception;

	public Document createEntityColumn(String colName, String colType);

	public Document createEntityColumn(String colName, String colType,
			String relEntityId, String relEntityName, String relValuesCollection);

	public void updateColsById(String appId, String id, List<Document> cols)
			throws Exception;

	public Document getByClassName(String appId, String className)
			throws Exception;

	public UpdateResult removeColumn(String appId, String id, String colName)
			throws Exception;

	public String getColTypeByName(String appId, String id, String colName)
			throws Exception;

	public void relClass(Document ownEntity, Document relEntity,
			String colName, EntityColType relType) throws Exception;

	public Document save(String appId, String className, List<Document> cols)
			throws Exception;
	
	public void insertOneWithId(String collectionName, Document document)
			throws Exception;
}
