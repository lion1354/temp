package com.tibco.ma.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.UpdateResult;
import com.tibco.ma.common.DocumentUtil;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.EntityDao;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.core.EntityColType;
import com.tibco.ma.model.core.MaFile;

@SuppressWarnings("rawtypes")
@Service
public class EntityServiceImpl extends BaseServiceImpl implements EntityService {

	private static Logger log = LoggerFactory
			.getLogger(EntityServiceImpl.class);

	@Resource
	private AppService appService;

	@Resource
	private ValuesService valuesService;

	@Resource
	private FileGroupService fileGroupService;

	@Resource
	private EntityDao dao;

	@Override
	public BaseDao getDao() {
		return dao;
	}
	
	@Override
	public String getEntityCollectionName(String appId) {
		return appId + MongoDBConstants.ENTITY_COLLECTION_NAME;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getAllEntityByAppId(String appId) throws Exception {
		Document filter = new Document(MongoDBConstants.ENTITY_APPID, appId);
		return query(getEntityCollectionName(appId), filter, null);
	}
	
	@Override
	public Document getEntityById(String appId, String id) throws Exception {
		return getOneById(getEntityCollectionName(appId), id);
	}
	
	public Document getEntityByClassName(String appId, String className)
			throws Exception {
		Document filter = new Document(MongoDBConstants.ENTITY_APPID, appId).
				append(MongoDBConstants.ENTITY_CLASSNAME, className);
		return getOne(getEntityCollectionName(appId), filter);
	}

	@Override
	public Document save(String appId, Document json) throws Exception {
		log.debug("save Entity - appId: {}, json:{}", appId, json);
		String className = json.getString(MongoDBConstants.ENTITY_CLASSNAME);
		String comment = json.getString(MongoDBConstants.ENTITY_COMMENT);
		
		Document document = new Document();
		document.append(MongoDBConstants.ENTITY_APPID, appId);
		document.append(MongoDBConstants.ENTITY_CLASSNAME, className);
		document.append(MongoDBConstants.ENTITY_COLS, Collections.emptyList());
		if (comment != null) {
			document.append(MongoDBConstants.ENTITY_COMMENT, comment);
		}
		document.append(MongoDBConstants.ENTITY_VALUES_COLLECTION,
				valuesService.getValuesCollectionName(appId, className));
		dao.insertOne(getEntityCollectionName(appId), document);
		DocumentUtil.convertObjectIdToString(document);
		return document;
	}
	
	@Override
	public Document save(String appId, String className, List<Document> cols)
			throws Exception {
		Document entity = new Document();
		entity.append(MongoDBConstants.ENTITY_APPID, appId);
		entity.append(MongoDBConstants.ENTITY_CLASSNAME, className);
		entity.append(MongoDBConstants.ENTITY_COLS, cols);
		entity.append(MongoDBConstants.ENTITY_COMMENT, "save " + className);
		entity.append(MongoDBConstants.ENTITY_VALUES_COLLECTION,
				valuesService.getValuesCollectionName(appId, className));
		dao.insertOne(getEntityCollectionName(appId), entity);
		DocumentUtil.convertObjectIdToString(entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(String appId, Document json) throws Exception {
		log.debug("update Entity - appId: {}, json:{}", appId, json.toJson());
		String id = json.getString(MongoDBConstants.DOCUMENT_ID);
		String className = json.getString(MongoDBConstants.ENTITY_CLASSNAME);
		Object colObject =json.get("col");
		
		Document col = null;
		if (colObject != null && colObject instanceof LinkedHashMap) {
			LinkedHashMap<String, String> colList = (LinkedHashMap<String, String>) json.get("col");
			col = new Document();
			for (Map.Entry<String, String> entry : colList.entrySet()) {
				col.append(entry.getKey(), entry.getValue());
			}
		}
		if (colObject != null && colObject instanceof Document) {
			col = (Document) colObject;
		}
		// update cols
		if (col != null && !col.isEmpty()) {
			String colName = col.getString(MongoDBConstants.COLUMN_COLNAME);
			String colType = col.getString(MongoDBConstants.COLUMN_COLTYPE);
			Document colDocument = null;
			if (colType.equals(EntityColType.Relation.name()) || colType.equals(EntityColType.Pointer.name())) {
				// {"colName" : "${colName}", "colType" : "${colType}", "relEntityId" : "${relEntityId}", "relEntityName" : "${relEntityName}", "relValuesCollection" : "${relValuesCollection}"}
				String relEntityId = col.getString(MongoDBConstants.COLUMN_RELENTITYID);
				String relEntityName = col.getString(MongoDBConstants.COLUMN_RELENTITYNAME);
				String relValuesCollection = null;
				if (colType.equals(EntityColType.Relation.name())) {
					relValuesCollection = valuesService.getRelValuesCollectionName(appId, className, colName);
				}
				colDocument = createEntityColumn(colName, colType, relEntityId, relEntityName, relValuesCollection);
			} else {
				// {"colName" : "${colName}", "colType" : "${colType}"}
				colDocument = createEntityColumn(colName, colType);
			}
			
			Document filter = new Document(MongoDBConstants.DOCUMENT_ID, new ObjectId(id));
			Document update = new Document(MongoDBConstants.ENTITY_COLS, colDocument);
			addArrayField(getEntityCollectionName(appId), filter, update);
		}
	}
	
	@Override
	public Document createEntityColumn(String colName, String colType) {
		return this.createEntityColumn(colName, colType, null, null, null);
	}
	
	@Override
	public Document createEntityColumn(String colName, String colType,
			String relEntityId, String relEntityName, String relValuesCollection) {
		Document document = new Document();
		document.append(MongoDBConstants.COLUMN_COLNAME, colName);
		document.append(MongoDBConstants.COLUMN_COLTYPE, colType);
		if (relEntityId != null && relEntityName != null) {
			document.append(MongoDBConstants.COLUMN_RELENTITYID, relEntityId);
			document.append(MongoDBConstants.COLUMN_RELENTITYNAME, relEntityName);
			
		}
		if (relValuesCollection != null && colType.equals(EntityColType.Relation.name())) {
			document.append(MongoDBConstants.COLUMN_RELATION_COLLECTION, relValuesCollection);
		}
		return document;
	}

	@Override
	public void updateColsById(String appId, String id, List<Document> cols)
			throws Exception {
		Document update = new Document(MongoDBConstants.ENTITY_COLS, cols);
		updateById(getEntityCollectionName(appId), id, update);
	}

	@Override
	public Document getByClassName(String appId, String className)
			throws Exception {
		Document filter = new Document(MongoDBConstants.ENTITY_CLASSNAME,
				className);
		return getOne(getEntityCollectionName(appId), filter);
	}

	@Override
	public UpdateResult removeColumn(String appId, String id, String colName)
			throws Exception {
		Document entity = getEntityById(appId, id);
		String className = entity.getString(MongoDBConstants.ENTITY_CLASSNAME);
		// According to Column type do something
		String colType = getColTypeByName(appId, id, colName);
		if (colType != null) {
			// If colType is Image, delete the image file
			if (colType.equals(EntityColType.Image.name())) {
				delFiles(valuesService.getValuesCollectionName(appId, className), id, colName);
			}
			// If colType is GeoPoint, delete the GeoIndex
			else if (colType.equals(EntityColType.GeoPoint.name())) {
				valuesService.dropGeoIndex(appId, className, colName);
			}
			// If colType is File, delete the file
			else if (colType.equals(EntityColType.File.name())) {
				delFiles(valuesService.getValuesCollectionName(appId, className), id, colName);
			}
			// If colType is Relation, drop the relation collection
			else if (colType.equals(EntityColType.Relation.name())) {
				dropCollection(valuesService.getRelValuesCollectionName(appId, className, colName));
			}
		}
		
		//if the entity is _User, this five column dose not be delete
		if (!(StringUtil.notEmptyAndEqual(className, MongoDBConstants.SYSTEM_USER_COLLECTION_NAME) 
				&& ("username".equals(colName) || "password".equals(colName) || "email".equals(colName) 
						|| "uniqueKey".equals(colName) || "isActivate".equals(colName)))) {
			// delete values Column
			valuesService.removeField(valuesService.getValuesCollectionName(appId, className), null, colName);
			// delete entity Column
			return removeColFieldByName(getEntityCollectionName(appId), id, colName);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private void delFiles(String collectionName, String id, String colName)
			throws Exception {
		List<Document> values = query(collectionName, new Document(
				MongoDBConstants.VALUES_ENTITYID, id), null);
		for (Document value : values) {
			if (value.containsKey(colName)) {
				Object colObj = value.get(colName);
				if (colObj instanceof Document) {
					Document col = (Document) colObj;
					fileGroupService.deleteByGroupId(col.getString(MaFile.GROUPID), true);
				}
			}
		}
	}

	private UpdateResult removeColFieldByName(String collectionName,
			String id, String colName) throws Exception {
		Document filter = new Document(MongoDBConstants.DOCUMENT_ID, new ObjectId(id));
		Document update = new Document(MongoDBConstants.ENTITY_COLS, 
				new Document(MongoDBConstants.COLUMN_COLNAME, colName));
		return removeArrayField(collectionName, filter, update);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getColTypeByName(String appId, String id, String colName)
			throws Exception {
		Document entity = getEntityById(appId, id);
		List<Document> cols = (ArrayList<Document>) entity.get(MongoDBConstants.ENTITY_COLS);
		for (Document col : cols) {
			if (colName.equals(col.getString(MongoDBConstants.COLUMN_COLNAME))) {
				return col.getString(MongoDBConstants.COLUMN_COLTYPE);
			}
		}
		return null;
	}

	@Override
	public void relClass(Document ownEntity, Document relEntity, String colName,
			EntityColType relType) throws Exception {
		String ownId = ownEntity.getString(MongoDBConstants.DOCUMENT_ID);
		String appId = ownEntity.getString(MongoDBConstants.ENTITY_APPID);
		String ownClassName = ownEntity.getString(MongoDBConstants.ENTITY_CLASSNAME);
		String relEntityId = relEntity.getString(MongoDBConstants.DOCUMENT_ID);
		String relEntityName = relEntity.getString(MongoDBConstants.ENTITY_CLASSNAME);
		String relValuesCollection = null;
		if (relType.equals(EntityColType.Relation)) {
			relValuesCollection = valuesService.getRelValuesCollectionName(appId, ownClassName, colName);
		}
		Document relCol = createEntityColumn(colName, relType.name(),
				relEntityId, relEntityName, relValuesCollection);
		Document filter = new Document(MongoDBConstants.DOCUMENT_ID, new ObjectId(ownId));
		Document update = new Document(MongoDBConstants.ENTITY_COLS, relCol);
		addArrayField(getEntityCollectionName(appId), filter, update);
	}

	@Override
	public void insertOneWithId(String collectionName, Document document)
			throws Exception {
		dao.insertOneWithId(collectionName,document);
	}
}
