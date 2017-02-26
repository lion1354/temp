package com.tibco.ma.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.QueryOperators;
import com.mongodb.client.model.IndexOptions;
import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.Constants;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.ClientUserDao;
import com.tibco.ma.dao.ValuesDao;
import com.tibco.ma.model.ClientUser;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.core.EntityColType;
import com.tibco.ma.model.core.MaDataType;
import com.tibco.ma.model.core.MaDate;
import com.tibco.ma.model.core.MaFile;
import com.tibco.ma.model.core.MaGeoPoint;
import com.tibco.ma.model.core.MaImage;
import com.tibco.ma.model.core.MaPointer;
import com.tibco.ma.model.core.MaRelation;

@SuppressWarnings("rawtypes")
@Service
public class ValuesServiceImpl extends BaseServiceImpl implements ValuesService {

	private static final double EARTH_MEAN_RADIUS_MILE = 3958.8000000000002D;

	private static Logger log = LoggerFactory
			.getLogger(ValuesServiceImpl.class);

	@Resource
	private ValuesDao dao;

	@Override
	public BaseDao getDao() {
		return dao;
	}

	@Resource
	private EntityService entityService;

	@Resource
	private FileGroupService fileGroupService;

	@Resource
	private AppSettingService appSettingService;

	@Resource
	private ConfigInfo configInfo;

	@Resource
	private ClientUserDao clientUserDao;

	@Override
	public String getValuesCollectionName(String appId, String className) {
		return appId + MongoDBConstants.COLLECTION_NAME_SEPARATOR + className
				+ MongoDBConstants.VALUES_COLLECTION_NAME;
	}

	@Override
	public String getRelValuesCollectionName(String appId, String ownClassName,
			String colName) {
		return appId + MongoDBConstants.COLLECTION_NAME_SEPARATOR + colName
				+ MongoDBConstants.COLLECTION_NAME_SEPARATOR + ownClassName
				+ MongoDBConstants.RELATED_VALUES_COLLECTION_NAME;
	}

	@Override
	public String getGeoIndexName(String appId, String className, String colName) {
		return appId + MongoDBConstants.COLLECTION_NAME_SEPARATOR + className
				+ MongoDBConstants.COLLECTION_NAME_SEPARATOR + colName
				+ MongoDBConstants.GEO_INDEX_NAME;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteValue(String appId, String id, String entityId,
			String ownEntityId, String ownColName) throws Exception {
		Document entity = entityService.getEntityById(appId, entityId);
		String className = entity.getString(MongoDBConstants.ENTITY_CLASSNAME);
		List<Document> cols = (ArrayList<Document>) entity
				.get(MongoDBConstants.ENTITY_COLS);
		String valuesCollectionName = getValuesCollectionName(appId, className);
		Document values = getOneById(valuesCollectionName, id);
		if (StringUtil.notEmptyAndEqual(className,
				MongoDBConstants.SYSTEM_USER_COLLECTION_NAME)) {
			String user_id = values.getString("uniqueKey");
			if (StringUtil.notEmpty(user_id)) {
				ClientUser clientUser = null;
				try {
					clientUser = clientUserDao.findById(new ObjectId(user_id),
							ClientUser.class);
				} catch (IllegalArgumentException argumentException) {
					log.info(argumentException.getMessage());
				}
				if (clientUser != null) {
					clientUserDao.deleteOne("client_user",
							new Document(MongoDBConstants.DOCUMENT_ID,
									new ObjectId(user_id)));
				}
			}
		}

		for (Document col : cols) {
			String colType = col.getString(MongoDBConstants.COLUMN_COLTYPE);
			String colName = col.getString(MongoDBConstants.COLUMN_COLNAME);
			// delete Image files and File
			if (colType.equals(EntityColType.Image.name())
					|| colType.equals(EntityColType.File.name())) {
				if (values.containsKey(colName)) {
					Document colValues = (Document) values.get(colName);
					String groupId = colValues.getString(MaFile.GROUPID);
					fileGroupService.deleteByGroupId(groupId, false);
				}
			}
			// delete relation values by colType, if no Document then Drop
			// Collection
			if (colType.equals(EntityColType.Relation.name())) {
				Document filter = new Document(new Document(
						MongoDBConstants.RELVALUES_OWNINGID, id));
				deleteManyAndDropCollection(
						getRelValuesCollectionName(appId, className, colName),
						filter);
			}
		}
		// delete relation values by ownEntityId and ownColName, if no Document
		// then Drop Collection
		if (StringUtil.notEmpty(ownEntityId) && StringUtil.notEmpty(ownColName)) {
			Document ownEntity = entityService
					.getEntityById(appId, ownEntityId);
			String ownClassName = ownEntity
					.getString(MongoDBConstants.ENTITY_CLASSNAME);
			Document filter = new Document(new Document(
					MongoDBConstants.RELVALUES_RELATEDID, id));
			deleteManyAndDropCollection(
					getRelValuesCollectionName(appId, ownClassName, ownColName),
					filter);
		}

		// delete value, if no Document then Drop Collection
		deleteManyAndDropCollection(valuesCollectionName, new Document(
				MongoDBConstants.DOCUMENT_ID, new ObjectId(id)));
	}

	@Override
	public void save(String appId, String id, String entityId, String colName,
			String value, String ownEntityId, String ownValuesId,
			String ownColName) throws Exception {
		Document entity = entityService.getEntityById(appId, entityId);
		String className = entity.getString(MongoDBConstants.ENTITY_CLASSNAME);
		String valuesCollectionName = getValuesCollectionName(appId, className);
		long time = System.currentTimeMillis();
		Document document = new Document(MongoDBConstants.VALUES_ENTITYID,
				entityId);
		if (StringUtil.isEmpty(id)) {
			document.append(MongoDBConstants.VALUES_CREATEAT, time).append(
					MongoDBConstants.VALUES_UPDATEAT, time);
			getSaveDocByType(appId, entityId, document, colName, value);
			insertOne(valuesCollectionName, document);
		} else {
			document.append(MongoDBConstants.VALUES_UPDATEAT, time);
			if (StringUtil.notEmptyAndEqual(className,
					MongoDBConstants.SYSTEM_USER_COLLECTION_NAME)) {
				Document user = getOneById(valuesCollectionName, id);
				if (colName.equals("username") || colName.equals("password")
						|| colName.equals("email")
						|| colName.equals("uniqueKey")) {
					// this four column is not allow to change the values, but
					// need to save values at first time
					if (StringUtil.isEmpty(user.getString("username"))
							|| StringUtil.isEmpty(user.getString("password"))
							|| StringUtil.isEmpty(user.getString("uniqueKey"))
							|| StringUtil.isEmpty(user.getString("email"))) {
						getSaveDocByType(appId, entityId, document, colName,
								value);
						updateById(valuesCollectionName, id, document);
					}
				} else {
					getSaveDocByType(appId, entityId, document, colName, value);
					updateById(valuesCollectionName, id, document);
					// change the client user activate state
					if (colName.equals("isActivate")) {
						Query query = new Query();
						query.addCriteria(Criteria.where(
								MongoDBConstants.DOCUMENT_ID).is(
								new ObjectId(user.getString("uniqueKey"))));
						Update update = Update.update("state",
								Boolean.parseBoolean(value) ? "1" : "0");
						clientUserDao.update(query, update, ClientUser.class);
					}
				}
			} else {
				getSaveDocByType(appId, entityId, document, colName, value);
				updateById(valuesCollectionName, id, document);
			}
		}

		// if ownValuesId not null, relation values save
		if (StringUtil.notEmpty(ownEntityId)
				&& StringUtil.notEmpty(ownValuesId)
				&& StringUtil.notEmpty(ownColName)) {
			String relatedId = null;
			if (StringUtil.isEmpty(id)) {
				relatedId = document.getObjectId(MongoDBConstants.DOCUMENT_ID)
						.toString();
			} else {
				relatedId = id;
			}
			Document ownEntity = entityService
					.getEntityById(appId, ownEntityId);
			saveRelValues(appId,
					ownEntity.getString(MongoDBConstants.ENTITY_CLASSNAME),
					ownColName, ownValuesId, relatedId);
		}
	}

	@SuppressWarnings("unchecked")
	private void getSaveDocByType(String appId, String entityId,
			Document document, String colName, String value) throws Exception {
		String colType = entityService.getColTypeByName(appId, entityId,
				colName);
		EntityColType colTypeEnum = EntityColType.valueOf(colType);
		switch (colTypeEnum) {
		// {"${colName}" : "${value}"}
		case String: {
			document.append(colName, value);
			break;
		}
		// {"${colName}" : ${value}}
		case Number: {
			if (value.indexOf(".") != -1) {
				document.append(colName, Double.valueOf(value));
			} else {
				document.append(colName, Long.valueOf(value));
			}
			break;
		}
		// {"${colName}" : ${value}}
		case Boolean: {
			document.append(colName, Boolean.valueOf(value));
			break;
		}
		// {"${colName}" : {"iso" : ${timestamp}, "__type" : "Date"}}
		case Date: {
			MaDate maDate = new MaDate(value);
			document.append(colName, maDate.toMap());
			break;
		}
		// {"${colName}" : {"groupId" : "${groupId}", "comment" : "${comment}",
		// "urls" : "${urls}", "__type" : "Image"}}
		case Image: {
			MaImage maImage = new MaImage(Document.parse(value));
			document.append(colName, maImage.toMap());
			break;
		}
		// {"${colName}" : {"groupId" : "${groupId}", "comment" : "${comment}",
		// "url" : "${url}", "__type" : "File"}}
		case File: {
			MaFile maFile = new MaFile(Document.parse(value));
			document.append(colName, maFile.toMap());
			break;
		}
		// {"${colName}" : {"latitude" : ${latitude}, "longitude" :
		// ${longitude}, "__type" : "GeoPoint"}}
		case GeoPoint: {
			MaGeoPoint maGeoPoint = new MaGeoPoint(Document.parse(value));
			document.append(colName, maGeoPoint.toMap());
			// Create Geo Index to values collection
			createGeoIndex(appId, entityId, colName);
			break;
		}
		// {"${colName}" : [${array value}]}
		case Array: {
			try {
				Document valueDoc = Document.parse("{" + MaDataType.KEY_VALUE
						+ " : " + value + "}");
				document.append(colName, valueDoc.get(MaDataType.KEY_VALUE));
			} catch (Exception e) {
				document.append(colName, value);
			}
			break;
		}
		// {"${colName}" : ${json value}}
		case Object: {
			Document valueDoc = Document.parse(value);
			document.append(colName, valueDoc);
			break;
		}
		// {"${colName}" : {"relEntityId" : "${relEntityId}", "relEntityName" :
		// "${relEntityName}", "relatedId" : "${relatedId}", "__type" :
		// "Pointer"}}
		case Pointer: {
			Document entity = entityService.getEntityById(appId, entityId);
			List<Document> cols = (ArrayList<Document>) entity
					.get(MongoDBConstants.ENTITY_COLS);
			if (cols != null && cols.size() > 0) {
				for (Document col : cols) {
					String name = col
							.getString(MongoDBConstants.COLUMN_COLNAME);
					if (name != null && name.equals(colName)) {
						String relEntityId = col
								.getString(MongoDBConstants.COLUMN_RELENTITYID);
						String relEntityName = col
								.getString(MongoDBConstants.COLUMN_RELENTITYNAME);
						MaPointer maPointer = new MaPointer(relEntityId,
								relEntityName, value);
						document.append(colName, maPointer.toMap());
						break;
					}
				}
			}
			break;
		}
		// {"${colName}" : {"relEntityId" : "${relEntityId}", "relEntityName" :
		// "${relEntityName}", "relValuesCollection" : "${relValuesCollection}",
		// "__type" : "Relation"}}
		case Relation: {
			Document entity = entityService.getEntityById(appId, entityId);
			List<Document> cols = (ArrayList<Document>) entity
					.get(MongoDBConstants.ENTITY_COLS);
			if (cols != null && cols.size() > 0) {
				for (Document col : cols) {
					String name = col
							.getString(MongoDBConstants.COLUMN_COLNAME);
					if (name != null && name.equals(colName)) {
						String className = entity
								.getString(MongoDBConstants.ENTITY_CLASSNAME);
						String relEntityId = col
								.getString(MongoDBConstants.COLUMN_RELENTITYID);
						String relEntityName = col
								.getString(MongoDBConstants.COLUMN_RELENTITYNAME);
						String relValuesCollection = getRelValuesCollectionName(
								appId, className, colName);
						MaRelation maRelation = new MaRelation(relEntityId,
								relEntityName, relValuesCollection);
						document.append(colName, maRelation.toMap());
						break;
					}
				}
			}
			break;
		}
		default:
			break;
		}
	}

	private void saveRelValues(String appId, String ownClassName,
			String colName, String owningId, String relatedId) throws Exception {
		String relValuesCollectionName = getRelValuesCollectionName(appId,
				ownClassName, colName);
		Document doc = new Document(MongoDBConstants.RELVALUES_OWNINGID,
				owningId).append(MongoDBConstants.RELVALUES_RELATEDID,
				relatedId);
		long count = count(relValuesCollectionName, doc);
		if (count == 0) {
			insertOne(relValuesCollectionName, doc);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document save(String appId, String entityId, String className,
			Document values) throws Exception {
		long time = System.currentTimeMillis();
		String valuesCollectionName = getValuesCollectionName(appId, className);
		Document document = new Document(MongoDBConstants.VALUES_ENTITYID,
				entityId).append(MongoDBConstants.VALUES_CREATEAT, time)
				.append(MongoDBConstants.VALUES_UPDATEAT, time);
		Set<String> keySet = values.keySet();
		for (String key : keySet) {
			Object value = values.get(key);
			if (value instanceof Document) {
				Document column = (Document) value;
				if (column.containsKey(MaDataType.KEY_VALUE)) {
					getSaveDocByType(appId, entityId, document, key,
							((Document) values.get(key))
									.get(MaDataType.KEY_VALUE));
				} else {
					getSaveDocByType(appId, entityId, document, key, column);
				}
			} else {
				getSaveDocByType(appId, entityId, document, key, value);
			}
		}
		insertOne(valuesCollectionName, document);

		Document filter = Document.parse(document.toJson());
		filter.remove(MongoDBConstants.VALUES_CREATEAT);
		filter.remove(MongoDBConstants.VALUES_UPDATEAT);

		List<Document> list = query(valuesCollectionName, filter,
				new Document(MongoDBConstants.VALUES_CREATEAT,
						MongoDBConstants.ORDERBY_DESC));
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	private void getSaveDocByType(String appId, String entityId,
			Document document, String colName, Object value) throws Exception {
		String colType = entityService.getColTypeByName(appId, entityId,
				colName);
		EntityColType colTypeEnum = EntityColType.valueOf(colType);
		switch (colTypeEnum) {
		// {"${colName}" : "${value}"}
		case String: {
			document.append(colName, value);
			break;
		}
		// {"${colName}" : ${value}}
		case Number: {
			document.append(colName, value);
			break;
		}
		// {"${colName}" : ${value}}
		case Boolean: {
			document.append(colName, value);
			break;
		}
		// {"${colName}" : {"iso" : ${timestamp}, "__type" : "Date"}}
		case Date: {
			MaDate maDate = new MaDate(value);
			document.append(colName, maDate.toMap());
			break;
		}
		// {"${colName}" : {"groupId" : "${groupId}", "comment" : "${comment}",
		// "url" : "${url}", "__type" : "File"}}
		case File: {
			if (value instanceof Document) {
				String url = (String) ((Document) value).get("url");
				if (url.contains(Constants.IMAGE_REST_CORE_SHOWRESOURCE)) {
					String[] datas = url.split("=");
					if (datas.length > 1) {
						String fileData = datas[datas.length - 1];
						if (StringUtil.notEmpty(fileData)) {
							url = configInfo.getEmailContextPath()
									+ Constants.IMAGE_REST_CORE_SHOWRESOURCE
									+ fileData;
						}
					}
				}
				((Document) value).put("url", url);
				MaFile maFile = new MaFile((Document) value);
				document.append(colName, maFile.toMap());
			}
			break;
		}
		// {"${colName}" : {"latitude" : "${latitude}", "longitude" :
		// "${longitude}", "__type" : "GeoPoint"}}
		case GeoPoint: {
			if (value instanceof Document) {
				MaGeoPoint maGeoPoint = new MaGeoPoint((Document) value);
				document.append(colName, maGeoPoint.toMap());
				// Create Geo Index to values collection
				createGeoIndex(appId, entityId, colName);
			}
			break;
		}
		// {"${colName}" : [${array value}]}
		case Array: {
			document.append(colName, value);
			break;
		}
		// {"${colName}" : ${json value}}
		case Object: {
			document.append(colName, value);
			break;
		}
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Document entity, String id, Document document)
			throws Exception {
		Document update = new Document(MongoDBConstants.VALUES_UPDATEAT,
				System.currentTimeMillis());
		String appId = entity.getString(MongoDBConstants.ENTITY_APPID);
		String entityId = entity.getString(MongoDBConstants.DOCUMENT_ID);
		String className = entity.getString(MongoDBConstants.ENTITY_CLASSNAME);
		List<Document> cols = new ArrayList<Document>();
		Set<String> colNameSet = document.keySet();
		for (String colName : colNameSet) {
			String colType = entityService.getColTypeByName(appId, entityId,
					colName);
			Object colDoc = document.get(colName);
			if (StringUtil.isEmpty(colType)) {
				Document col = null;
				if (colDoc instanceof Document) {
					if (((Document) colDoc).containsKey(MaDataType.KEY_TYPE)
							&& ((Document) colDoc)
									.containsKey(MaDataType.KEY_VALUE)) {
						col = entityService.createEntityColumn(colName,
								((Document) colDoc)
										.getString(MaDataType.KEY_TYPE));
					} else {
						col = entityService.createEntityColumn(colName,
								judgeType(colDoc).name());
					}
				} else {
					col = entityService.createEntityColumn(colName,
							judgeType(colDoc).name());
				}
				cols.add(col);
			}
		}

		List<Document> colsList = (ArrayList<Document>) entity
				.get(MongoDBConstants.ENTITY_COLS);
		if (cols.size() != 0) {
			colsList.addAll(cols);
			entityService.updateColsById(appId, entityId, colsList);
		}

		for (String colName : colNameSet) {
			Object colDoc = document.get(colName);
			if (colDoc instanceof Document) {
				if (((Document) colDoc).containsKey(MaDataType.KEY_TYPE)
						&& ((Document) colDoc)
								.containsKey(MaDataType.KEY_VALUE)) {
					getSaveDocByType(appId, entityId, update, colName,
							((Document) colDoc).get(MaDataType.KEY_VALUE));
				} else {
					getSaveDocByType(appId, entityId, update, colName, colDoc);
				}
			} else {
				getSaveDocByType(appId, entityId, update, colName, colDoc);
			}
		}

		log.debug("update json: {}", update.toJson());
		updateById(getValuesCollectionName(appId, className), id, update);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Pager<Document> valuesPage(String appId, String entityId,
			Document filter, Document sort, int page, int pageSize)
			throws Exception {
		List<Document> list = null;
		Document entity = entityService.getEntityById(appId, entityId);
		String className = entity.getString(MongoDBConstants.ENTITY_CLASSNAME);

		Pager<Document> pager = new Pager<Document>();
		pager.setNowpage(page);
		pager.setPagesize(pageSize);

		String ownColName = filter.getString("ownColName");
		// query Relation
		String owningId = filter.getString(MongoDBConstants.RELVALUES_OWNINGID);
		// query Pointer
		String relatedId = filter
				.getString(MongoDBConstants.RELVALUES_RELATEDID);

		// if owningId not null, query relValues relatedId
		if (StringUtil.notEmpty(owningId) && StringUtil.notEmpty(ownColName)) {
			filter.remove(MongoDBConstants.RELVALUES_OWNINGID);
			filter.remove("ownColName");
			// className = getRelEntityName(entity, ownColName);
			List<Document> relValuesList = query(
					getRelValuesCollectionName(appId, className, ownColName),
					new Document(MongoDBConstants.RELVALUES_OWNINGID, owningId),
					null);
			className = getRelEntityName(entity, ownColName);
			List<ObjectId> relatedIds = new ArrayList<ObjectId>();
			for (Document relValues : relValuesList) {
				relatedIds.add(new ObjectId(relValues
						.getString(MongoDBConstants.RELVALUES_RELATEDID)));
			}
			filter.append(MongoDBConstants.DOCUMENT_ID, new Document(
					QueryOperators.IN, relatedIds));
		}
		// if relatedId not null, query Pointer
		else if (StringUtil.notEmpty(relatedId)
				&& StringUtil.notEmpty(ownColName)) {
			filter.remove(MongoDBConstants.RELVALUES_RELATEDID);
			filter.remove("ownColName");
			className = getRelEntityName(entity, ownColName);
			filter.append(MongoDBConstants.DOCUMENT_ID, new ObjectId(relatedId));
		}

		String collectionName = getValuesCollectionName(appId, className);
		log.debug("query values, collectionName : {},  filter : {}",
				collectionName, filter.toJson());

		filter.remove("entityId");
		list = getDao().page(collectionName, filter, sort, page, pageSize);
		long count = getDao().count(collectionName, filter);

		pager.setCountrow(count);
		pager.setMaxPageIndex(10);
		long countpage = pager.getCountrow() % pager.getPagesize() == 0 ? pager
				.getCountrow() / pager.getPagesize() : pager.getCountrow()
				/ pager.getPagesize() + 1;
		pager.setCountpage(countpage);
		pager.showPage();
		pager.setData(list);
		return pager;
	}

	@SuppressWarnings("unchecked")
	private String getRelEntityName(Document entity, String colName) {
		List<Document> cols = (ArrayList<Document>) entity
				.get(MongoDBConstants.ENTITY_COLS);
		if (cols != null && cols.size() > 0) {
			for (Document col : cols) {
				String name = col.getString(MongoDBConstants.COLUMN_COLNAME);
				if (name.equals(colName)) {
					return col.getString(MongoDBConstants.COLUMN_RELENTITYNAME);
				}
			}
		}
		return null;
	}

	@Override
	public List<Document> valuesQuery(String appId, String collectionName,
			Document filter, Document sort, Integer skip, Integer limit,
			Document projection, String[] includeArray) throws Exception {
		List<Document> list = dao.query(collectionName, filter, sort, skip,
				limit, projection);
		if (list != null && list.size() > 0) {
			for (Document document : list) {
				if (includeArray != null && includeArray.length > 0) {
					for (String include : includeArray) {
						String[] infos = include.split("\\.");
						Object obj = document.get(infos[0]);
						if (obj != null && obj instanceof Document) {
							Document doc = ((Document) obj);
							if (EntityColType.Pointer.name().equals(
									doc.getString(MaDataType.KEY_TYPE))) {
								Document relValues = getOneById(
										getValuesCollectionName(
												appId,
												doc.getString(MongoDBConstants.COLUMN_RELENTITYNAME)),
										doc.getString(MaPointer.RELATEDID));
								if (relValues != null) {
									if (infos.length > 1) {
										doc.append(infos[1],
												relValues.get(infos[1]));
									} else {
										Set<String> keySet = relValues.keySet();
										for (String key : keySet) {
											doc.append(key, relValues.get(key));
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return list;
	}

	@Override
	public EntityColType judgeType(Object obj) {
		if (obj instanceof Boolean) {
			return EntityColType.valueOf("Boolean");
		} else if (obj instanceof String) {
			return EntityColType.valueOf("String");
		} else if (obj instanceof Integer || obj instanceof Long
				|| obj instanceof Float || obj instanceof Double) {
			return EntityColType.valueOf("Number");
		} else if (obj instanceof Document) {
			return EntityColType.valueOf("Object");
		} else {
			return EntityColType.valueOf("String");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void relValues(String appId, String className, String owningId,
			Object relIdObj, String colName, EntityColType relType)
			throws Exception {
		String valuesCollectionName = getValuesCollectionName(appId, className);
		Document document = new Document(MongoDBConstants.VALUES_UPDATEAT,
				System.currentTimeMillis());
		Document owningValue = getOneById(valuesCollectionName, owningId);
		String owningEntityId = owningValue
				.getString(MongoDBConstants.VALUES_ENTITYID);
		if (relType.equals(EntityColType.Pointer)) {
			getSaveDocByType(appId, owningEntityId, document, colName,
					(String) relIdObj);
			updateById(valuesCollectionName, owningId, document);
		} else if (relType.equals(EntityColType.Relation)) {
			List<String> relIdList = (ArrayList<String>) relIdObj;
			for (String relId : relIdList) {
				saveRelValues(appId, className, colName, owningId, relId);
			}
			getSaveDocByType(appId, owningEntityId, document, colName, null);
			updateById(valuesCollectionName, owningId, document);
		}
	}

	@Override
	public List<Document> query(String appId, String className,
			Document filter, Document keys, Document sort) {
		Document entity = null;
		try {
			entity = entityService.getEntityByClassName(appId, className);
			filter.append(MongoDBConstants.VALUES_ENTITYID,
					entity.getString(MongoDBConstants.DOCUMENT_ID));
			List<Document> result = dao.query(
					getValuesCollectionName(appId, className), filter, sort,
					null, null, keys);
			return result;
		} catch (Exception e) {
			log.error("error : " + e);
			return null;
		}
	}

	public List<Document> query(String collectionName, Document filter,
			Document keys, Document sort) {
		Document entity = null;
		try {
			List<Document> result = dao.query(collectionName, filter, sort,
					null, null, keys);
			return result;
		} catch (Exception e) {
			log.error("error : " + e);
			return null;
		}
	}

	@Override
	public void createGeoIndex(String appId, String entityId, String colName)
			throws Exception {
		Document entity = entityService.getEntityById(appId, entityId);
		String className = entity.getString(MongoDBConstants.ENTITY_CLASSNAME);
		String valuesCollectionName = getValuesCollectionName(appId, className);
		String geoIndexName = getGeoIndexName(appId, className, colName);
		if (!isIndexExist(valuesCollectionName, geoIndexName)) {
			Document document = new Document(colName, "2d").append("min", -180)
					.append("max", 180);
			createIndex(valuesCollectionName, document,
					new IndexOptions().name(geoIndexName));
		}
	}

	@Override
	public void dropGeoIndex(String appId, String className, String colName)
			throws Exception {
		String geoIndexName = this.getGeoIndexName(appId, className, colName);
		String valuesCollectionName = getValuesCollectionName(appId, className);
		if (isIndexExist(valuesCollectionName, geoIndexName)) {
			dropIndex(valuesCollectionName, geoIndexName);
		}
	}

	/*
	 * add by Aaron. 2015/8/21
	 */
	@Override
	public List<Document> findWithCircle(String collectionName,
			String locField, MaGeoPoint center, double radius, Integer limit)
			throws Exception {
		LinkedList<Object> circle = new LinkedList<Object>();
		circle.addLast(Arrays.asList(center.getLatitude(),
				center.getLongitude()));
		circle.addLast(radius / EARTH_MEAN_RADIUS_MILE);

		Document query = new Document();
		query.put(locField, new Document("$geoWithin", new Document(
				"$centerSphere", circle)));

		log.info("MongoDB Geo query withinCircle : {}", query.toJson());
		return dao.query(collectionName, query, null, null, limit, null);
	}

	@Override
	public List<Document> findWithBox(String collectionName, String locField,
			MaGeoPoint bottomLeft, MaGeoPoint upperRight, Integer limit)
			throws Exception {
		LinkedList<Object> box = new LinkedList<>();

		box.addLast(Arrays.asList(bottomLeft.getLatitude(),
				bottomLeft.getLongitude()));
		box.addLast(Arrays.asList(upperRight.getLatitude(),
				upperRight.getLongitude()));

		Document query = new Document();
		query.put(locField, new Document("$geoWithin",
				new Document("$box", box)));

		log.info("MongoDB Geo query withinBox : {}", query.toJson());
		return dao.query(collectionName, query, null, null, limit, null);
	}

	@Override
	public List<Document> findWithPolygon(String collectionName,
			String locField, List<MaGeoPoint> polygon, Integer limit)
			throws Exception {
		LinkedList<Object> points = new LinkedList<>();
		for (MaGeoPoint point : polygon) {
			points.addLast(Arrays.asList(point.getLatitude(),
					point.getLongitude()));
		}

		Document query = new Document();
		query.put(locField, new Document("$geoWithin", new Document("$polygon",
				points)));

		log.info("MongoDB Geo query withinPolygon : {}", query.toJson());
		return dao.query(collectionName, query, null, null, limit, null);
	}

	@Override
	public List<Document> findNearSphere(String collectionName,
			String locField, MaGeoPoint center, double minDistance,
			double maxDistance, Integer limit) throws Exception {
		Document query = new Document();
		query.put(
				locField,
				new Document("$nearSphere", Arrays.asList(center.getLatitude(),
						center.getLongitude())).append("$minDistance",
						minDistance / EARTH_MEAN_RADIUS_MILE).append(
						"$maxDistance", maxDistance / EARTH_MEAN_RADIUS_MILE));

		log.info("MongoDB Geo query nearSphere : {}", query.toJson());
		return dao.query(collectionName, query, null, null, limit, null);
	}

	@Override
	public List<Document> findNear(String collectionName, String locField,
			MaGeoPoint center, double maxDistance, Integer limit)
			throws Exception {
		Document query = new Document();
		query.put(
				locField,
				new Document("$near", Arrays.asList(center.getLatitude(),
						center.getLongitude())).append("$maxDistance",
						maxDistance / EARTH_MEAN_RADIUS_MILE));

		log.info("MongoDB Geo Query near : {}", query.toJson());
		return dao.query(collectionName, query, null, null, limit, null);
	}

}
