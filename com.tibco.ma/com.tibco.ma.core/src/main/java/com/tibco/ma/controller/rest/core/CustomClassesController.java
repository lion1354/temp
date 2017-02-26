package com.tibco.ma.controller.rest.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tibco.ma.common.MaException;
import com.tibco.ma.common.ResponseErrorCode;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.core.EntityColType;
import com.tibco.ma.model.core.MaDataType;
import com.tibco.ma.model.core.MaPointer;
import com.tibco.ma.model.core.QueryType;
import com.tibco.ma.service.EntityService;
import com.tibco.ma.service.ValuesService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/1/classes")
public class CustomClassesController {

	private static Logger log = LoggerFactory
			.getLogger(CustomClassesController.class);

	@Resource
	private EntityService entityService;

	@Resource
	private ValuesService valuesService;

	/*
	 * if class not exist, create class and save vaules. colType include String,
	 * Number, Boolean, Date, Image, File, GeoPoint, Array, Object, not include
	 * Pointer, Relation
	 * 
	 * @RequestBody String body e.g.
	 * 
	 * { "name" : {"__type":"String", "__value":"Zhangsan"}, "age" :
	 * {"__type":"Number", "__value":30}, "isMan" : {"__type":"Boolean",
	 * "__value":true}, "birthday" : {"__type":"Date",
	 * "__value":"1436947892000"}, "position" : {"__type" : "GeoPoint",
	 * "__value" : {"latitude" : "11.11", "longitude" :"22.22"}},
	 * "GraduateSchool" : {"__type":"Array", "__value":["one","two","three"]},
	 * "testObj" : {"__type":"Object", "__value":{"key1":"value1",
	 * "key2":"value2"}}, "testFile" : {"__type":"File", "__value":{"groupId" :
	 * "1112223334445555", "comment" : "testFile","url" : "www.baidu.com"}} }
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "save class and values", notes = "save class and values",consumes="application/json",produces="application/json")
	@RequestMapping(value = "{className}", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "className") @PathVariable("className") String className,
			@ApiParam(value = "body") @RequestBody String body)
			throws Exception {
		log.debug("app_id: {} , className : {}", app_id, className);
		log.debug("body : {}", body);

		try {
			Document json = Document.parse(body);

			List<Document> cols = new ArrayList<Document>();
			Set<String> set = json.keySet();
			for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
				String name = iterator.next();
				Object values = json.get(name);
				EntityColType type = null;
				if (values instanceof Document) {
					Document column = (Document) values;
					if (column.containsKey(MaDataType.KEY_TYPE) && column.containsKey(MaDataType.KEY_VALUE)) {
						type = EntityColType.valueOf(((Document) json.get(name)).getString(MaDataType.KEY_TYPE));
					} else {
						type = valuesService.judgeType(values);
					}
				} else {
					type = valuesService.judgeType(values);
				}
				Document col = entityService.createEntityColumn(name, type.name());
				cols.add(col);
			}

			Document entity = entityService.getEntityByClassName(app_id, className);
			// if class not exist, create class
			if (entity == null) {
				entity = entityService.save(app_id, className, cols);
			} else {
				List<Document> newColumnsList = new ArrayList<Document>();
				List<Document> columns = (ArrayList<Document>) entity
						.get(MongoDBConstants.ENTITY_COLS);
				for (Document col : cols) {
					boolean tag = false;
					for (Document column : columns) {
						if (column.getString(MongoDBConstants.COLUMN_COLNAME).equals(
								col.getString(MongoDBConstants.COLUMN_COLNAME))) {
							tag = true;
							break;
						}
					}
					if (!tag) {
						newColumnsList.add(col);
					}
				}
				if (newColumnsList.size() != 0) {
					columns.addAll(newColumnsList);
					entityService.updateColsById(app_id,
							entity.getString(MongoDBConstants.DOCUMENT_ID),
							columns);
				}
			}

			Document result = null;
			// save values
			if (json != null) {
				result = valuesService.save(app_id,
						entity.getString(MongoDBConstants.DOCUMENT_ID),
						className, json);
			}
			return ResponseUtils.successWithValue(result);
		} catch (Exception e) {
			log.error("error : " + e);
			return ResponseUtils.fail(ResponseErrorCode.ADD_CLASS_ERROR.value(),
							"add class values error : " + e.getMessage(), HttpStatus.OK);
		}
	}
	
	@ApiOperation(value = "update class and values", notes = "update class and values",consumes="application/json",produces="application/json")
	@RequestMapping(value = "{className}/{objectId}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "className") @PathVariable("className") String className,
			@ApiParam(value = "objectId") @PathVariable("objectId") String objectId,
			@ApiParam(value = "body") @RequestBody String body)
			throws Exception {
		log.debug("app_id: {} , className : {} , objectId : {}", app_id, className, objectId);
		log.debug("body : {}", body);
		try {
			Document document = Document.parse(body);
			Document entity = entityService.getEntityByClassName(app_id, className);
			if (entity == null) {
				return ResponseUtils.fail(ResponseErrorCode.UPDATE_CLASS_ERROR.value(),
						"no entity", HttpStatus.OK);
			}

			String valuesCollectionName = valuesService.getValuesCollectionName(app_id, className);
			Document values = valuesService.getOneById(valuesCollectionName, objectId);
			if (values == null) {
				return ResponseUtils.fail(ResponseErrorCode.UPDATE_CLASS_ERROR.value(), 
						"objectId is incorrect", HttpStatus.OK);
			}

			valuesService.update(entity, objectId, document);

			Document result = valuesService.getOneById(valuesCollectionName, objectId);
			return ResponseUtils.successWithValue(result);
		} catch (Exception e) {
			log.error("error : " + e);
			return ResponseUtils.fail(ResponseErrorCode.UPDATE_CLASS_ERROR.value(), 
					"Update class values error : " + e.getMessage(), HttpStatus.OK);
		}
	}

	@ApiOperation(value = "delete class value", notes = "delete class value",consumes="application/json",produces="application/json")
	@RequestMapping(value = "/{className}/{objectIds}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "className") @PathVariable("className") String className,
			@ApiParam(value = "objectIds") @PathVariable("objectIds") String objectIds,
			@ApiParam(value = "body") @RequestBody String body)
			throws Exception {
		log.debug("app_id: {} , className : {} , objectIds : {} , body : {}", app_id, className, objectIds, body);
		String ownEntityId = null;
		String ownColName = null;
		if (StringUtil.notEmpty(body)) {
			Document json = Document.parse(body);
			ownEntityId = json.getString("ownEntityId");
			ownColName = json.getString("ownColName");
		}
		if (StringUtil.notEmpty(objectIds)) {
			Document entity = entityService.getEntityByClassName(app_id, className);
			if (entity == null) {
				return ResponseUtils.fail(ResponseErrorCode.UPDATE_CLASS_ERROR.value(),
						"no entity", HttpStatus.OK);
			}
			String valuesCollectionName = valuesService.getValuesCollectionName(app_id, className);
			String entityId = entity.getString(MongoDBConstants.DOCUMENT_ID);
			String[] ids = objectIds.split(",");
			for (String id : ids) {
				Document values = valuesService.getOneById(valuesCollectionName, id);
				if (values == null) {
					return ResponseUtils.fail(ResponseErrorCode.DELETE_CLASS_ERROR.value(),
							"not found", HttpStatus.OK);
				}
				valuesService.deleteValue(app_id, id, entityId, ownEntityId, ownColName);
			}
		}

		return ResponseUtils.success();
	}

	@ApiOperation(value = "get one", notes = "get one", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/{className}/{objectId}", method = RequestMethod.GET)
	public ResponseEntity<?> get(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "className") @PathVariable("className") String className,
			@ApiParam(value = "objectId") @PathVariable("objectId") String objectId)
			throws Exception {
		log.debug("app_id: {} , className : {} , objectId : {}", app_id, className, objectId);
		if (StringUtil.isEmpty(className)) {
			return ResponseUtils.fail(ResponseErrorCode.GET_CLASS_ERROR.value(), 
					"no class", HttpStatus.OK);
		}
		Document entity = entityService.getEntityByClassName(app_id, className);
		if (entity == null) {
			return ResponseUtils.fail(ResponseErrorCode.GET_CLASS_ERROR.value(), 
					"no class", HttpStatus.OK);
		}
		if (StringUtil.isEmpty(objectId))
			return ResponseUtils.fail(ResponseErrorCode.GET_CLASS_ERROR.value(),
					"the objectId is incorrect", HttpStatus.OK);
		try {
			Document values = valuesService.getOneById(
					valuesService.getValuesCollectionName(app_id, className),
					objectId);
			return ResponseUtils.successWithValue(values);
		} catch (Exception e) {
			log.error("query class error : " + e);
			return ResponseUtils.fail(
					ResponseErrorCode.GET_CLASS_ERROR.value(),
					"Query class error : " + e, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "find all", notes = "find all", produces = "application/json")
	@RequestMapping(value = "/{className}", method = RequestMethod.GET)
	public ResponseEntity<?> find(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "className") @PathVariable("className") String className,
			@ApiParam(value = "where", required = false) @RequestParam(value = "where", required = false) String where,
			@ApiParam(value = "order", required = false) @RequestParam(value = "order", required = false) String order,
			@ApiParam(value = "limit", required = false) @RequestParam(value = "limit", required = false) Integer limit,
			@ApiParam(value = "skip", required = false) @RequestParam(value = "skip", required = false) Integer skip,
			@ApiParam(value = "keys", required = false) @RequestParam(value = "keys", required = false) String keys,
			@ApiParam(value = "include", required = false) @RequestParam(value = "include", required = false) String include,
			@ApiParam(value = "count", required = false) @RequestParam(value = "count", required = false) Integer count)
			throws Exception {
		log.debug(
				"find all: \n className : {} \n where : {} \n order : {} \n limit : {} \n skip : {} \n keys : {} \n include : {} \n count : {}",
				className, where, order, limit, skip, keys, include, count);

		Document entity = entityService.getEntityByClassName(app_id, className);
		if (entity == null) {
			return ResponseUtils.fail(ResponseErrorCode.GET_CLASS_ERROR.value(), 
					"no entity", HttpStatus.OK);
		}
		try {
			String valuesCollectionName = valuesService.getValuesCollectionName(app_id, className);
			List<Document> list = new ArrayList<Document>();
			// filter
			Document filter = new Document();
			if (StringUtil.notEmpty(where)) {
				Set<String> keysSet = Document.parse(where).keySet();
				for (String key : keysSet) {
					if (Document.parse(where).get(key) instanceof Document) {
						filter.append(key,
								getFindFilterDoc(new Document().append(key, (Document) Document.parse(where).get(key)),
										app_id).get(key));
					} else {
						filter.append(key, Document.parse(where).get(key));
					}
				}
				// filter = getFindFilterDoc(Document.parse(where), app_id);
			}
			filter.append(MongoDBConstants.VALUES_ENTITYID, 
					entity.getString(MongoDBConstants.DOCUMENT_ID));
			// sort
			Document sort = getFindSortDoc(order);
			// skip
			if (skip == null || skip.intValue() < 0) {
				skip = 0;
			}
			// limit
			if (limit == null || limit.intValue() < 0) {
				limit = 100;
			} else if (limit.intValue() > 1000) {
				limit = 10000;
			}
			// projection
			Document projection = getFindProjectionDoc(keys);
			// count
			boolean getCount = false;
			if (count != null && count.intValue() == 1) {
				getCount = true;
			}
			// include
			String[] includeArray = null;
			if (StringUtil.notEmpty(include)) {
				includeArray = include.split(",");
			}

			if (limit.intValue() > 0) {
				list = valuesService.valuesQuery(app_id, valuesCollectionName,
						filter, sort, skip, limit, projection, includeArray);
			}
			if (getCount) {
				long valuesCount = valuesService.count(valuesCollectionName, filter);
				return ResponseUtils.successWithValue(new Document("results",
						list).append("count", valuesCount));
			} else {
				return ResponseUtils.successWithValues(list);
			}
		} catch (Exception e) {
			log.error("query class error : " + e);
			return ResponseUtils.fail(
					ResponseErrorCode.GET_CLASS_ERROR.value(),
					"Query class error : " + e, HttpStatus.OK);
		}
	}

	private Document getFindFilterDoc(Document where, String appId)
			throws MaException {
		Document filter = new Document();
		String whereJson = where.toJson();
		if (whereJson.indexOf(QueryType.SELECT.getType()) < 0 && whereJson.indexOf(QueryType.DONTSELECT.getType()) < 0
				&& whereJson.indexOf(QueryType.INQUERY.getType()) < 0 && whereJson.indexOf(QueryType.NINQUERY.getType()) < 0
				&& whereJson.indexOf(QueryType.RELATETO.getType()) < 0) {
			filter = where;
		} else {
			Set<String> keysSet = where.keySet();
			for (String key : keysSet) {
				Object doc = where.get(key);
				if (key.equals(QueryType.RELATETO.getType()) && doc instanceof Document) {
					filter = getResultByRelatedTo((Document) doc, appId);
				} else {
					if (doc instanceof Document) {
						Set<String> keys = ((Document) doc).keySet();
						for (String type : keys) {
							Object docu = ((Document) doc).get(type);
							if (type.equals(QueryType.SELECT.getType()) && docu instanceof Document) {
								filter = filter.append(key, formatObjectIdFilter(key, getResultBySelect(true, (Document) docu, appId)));
							}
							if (type.equals(QueryType.DONTSELECT.getType()) && docu instanceof Document) {
								filter = filter.append(key, formatObjectIdFilter(key, getResultBySelect(false, (Document) docu, appId)));
							}
							if (type.equals(QueryType.INQUERY.getType()) && docu instanceof Document) {
								filter = filter.append(key, formatObjectIdFilter(key, getResultByInquery(true, (Document) docu, appId)));
							}
							if (type.equals(QueryType.NINQUERY.getType()) && docu instanceof Document) {
								filter = filter.append(key, formatObjectIdFilter(key, getResultByInquery(false, (Document) docu, appId)));
							}
						}
					}
				}
			}
		}
		return filter;
	}
	
	private Object formatObjectIdFilter(String key, Object filter){
		try {
			if("_id".equals(key)){
				Set keys = ((Document) filter).keySet();
				for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
					String k = (String) iterator.next();
					if(k.equals(QueryType.IN.getType())) {
						ArrayList arr = (ArrayList) ((Document) filter).get(k);
						for (int i = 0; i < arr.size(); i++) {
							ObjectId _id = new ObjectId(arr.get(i).toString());
							arr.remove(i);
							arr.add(i, _id);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("error: " + e);
		}
		return filter;
	}

	private Object getResultBySelect(boolean isSelect, Document document,
			String appId) throws MaException {
		if (!document.containsKey("query")) {
			throw new MaException("No query!");
		}
		Document query = (Document) document.get("query");
		if (!query.containsKey("className")) {
			throw new MaException("No class name!");
		}
		String className = query.getString("className");
		if (!query.containsKey("where")) {
			throw new MaException("No where!");
		}
		Document where = (Document) query.get("where");
		if (!document.containsKey("key")) {
			throw new MaException("No key!");
		}
		String key = document.getString("key");
		Document keys = null;
		if ("_id".equals(key)) {
			keys = new Document(key, 1);
		} else {
			keys = new Document(key, 1).append("_id", 0);
		}
		List<Document> result = valuesService.query(appId, className, where, keys, new Document("createAt", -1));
		String doc = "";
		if (isSelect){
			doc += "{\"" + QueryType.IN.getType() + "\" : ";
		} else {
			doc += "{\"" + QueryType.NIN.getType() + "\" : ";   
		}
		if (result != null && result.size() != 0) {
			JSONArray json = new JSONArray();
			for (int i = 0; i < result.size(); i++) {
				Object value = result.get(i).get(key);
				if(value instanceof ObjectId){
					json.add(value.toString());
				}else{
					json.add(value);
				}
			}
			doc += json.toString();
		} else {
			doc += new JSONArray().toString();
		}
		doc += "}";
		return Document.parse(doc);
	}

	private Object getResultByInquery(boolean isInquery, Document document,
			String appId) throws MaException {
		if (!document.containsKey("className")) {
			throw new MaException("No class name!");
		}
		String className = document.getString("className");
		if (!document.containsKey("where")) {
			throw new MaException("No where!");
		}
		Document where = (Document) document.get("where");
		where.remove("entity");
		Document entity = null;
		try {
			entity = entityService.getEntityByClassName(appId, className);
		} catch (Exception e) {
			log.error("Query entity error :" + e);
		}
		if (entity == null) {
			return null;
		}
		List<Document> result = valuesService.query(appId, className, where,
				null, null);
		String doc = "";
		if (isInquery){
			doc += "{\"" + QueryType.IN.getType() + "\" : [";
		} else {
			doc += "{\"" + QueryType.NIN.getType() + "\" : [";
		}
		if (result != null && result.size() != 0) {
			for (int i = 0; i < result.size(); i++) {
				String _id = result.get(i).getString(MongoDBConstants.DOCUMENT_ID);
				Document pointer = new Document(MongoDBConstants.COLUMN_RELENTITYID, 
						entity.getString(MongoDBConstants.DOCUMENT_ID));
				pointer.append(MongoDBConstants.COLUMN_RELENTITYNAME, className);
				pointer.append(MaPointer.RELATEDID, _id);
				pointer.append(MaDataType.KEY_TYPE, EntityColType.Pointer.name());
				if(i < result.size() - 1) {
					doc += pointer.toJson() + ",";
				} else {
					doc += pointer.toJson();
				}
			}
		} else {
			doc += "[]";
		}
		doc += "]}";
		return Document.parse(doc);
	}

	@SuppressWarnings("unchecked")
	private Document getResultByRelatedTo(Document document, String appId)
			throws MaException {
		if (!document.containsKey("object")) {
			throw new MaException("no object!");
		}
		if (!document.containsKey("key")) {
			throw new MaException("no key!");
		}
		Document filter = new Document();
		Object object = document.get("object");
		String key = document.getString("key");
		String className = null;
		if (object instanceof Document){
			Document objectDoc = (Document) object;
			if(!objectDoc.containsKey("objectId")){
				throw new MaException("no owningId!");
			}
			if(!objectDoc.containsKey("className")){
				throw new MaException("no className!");
			}
			String owningId = objectDoc.getString("objectId");
			className = objectDoc.getString("className");
			filter.append(MongoDBConstants.RELVALUES_OWNINGID, owningId);
		} else {
			throw new MaException("Error object!");
		}
		List<Document> relatedList = null;
		try {
			String relValuesCollectionName = valuesService.getRelValuesCollectionName(appId, className, key);
			relatedList = valuesService.query(relValuesCollectionName, filter, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String ids = "{\"" + QueryType.OR.getType() + "\" : [";
		if (relatedList != null && relatedList.size() != 0) {
			for (int i = 0; i < relatedList.size(); i++) {
				Document idDoc = new Document(MongoDBConstants.DOCUMENT_ID,
						new ObjectId(relatedList.get(i).getString(MongoDBConstants.RELVALUES_RELATEDID)));
				if(i < relatedList.size() - 1) {
					ids += idDoc.toJson() + ",";
				} else {
					ids += idDoc.toJson();
				}
			}
		} else {
			ids += new Document(MongoDBConstants.DOCUMENT_ID, null).toJson();
		}
		ids += "]}";
		return Document.parse(ids);
	}

	private Document getFindSortDoc(String order) {
		Document sort = null;
		if (StringUtil.notEmpty(order)) {
			sort = new Document();
			String[] orderArray = order.split(",");
			for (String str : orderArray) {
				if (str.startsWith("-")) {
					sort.append(str.substring(1), MongoDBConstants.ORDERBY_DESC);
				} else {
					sort.append(str, MongoDBConstants.ORDERBY_ASC);
				}
			}
		}
		return sort;
	}

	private Document getFindProjectionDoc(String keys) {
		Document projection = null;
		if (StringUtil.notEmpty(keys)) {
			projection = new Document(MongoDBConstants.DOCUMENT_ID, 0);
			String[] keysArray = keys.split(",");
			for (String str : keysArray) {
				projection.append(str, 1);
			}
		}
		return projection;
	}

	/*
	 * @RequestBody String body e.g. { "relEntityName": "${relEntityName}",
	 * "colName": "${colName}" }
	 */
	@ApiOperation(value = "save relation target class", notes = "save relation target class",consumes="application/json",produces="application/json")
	@RequestMapping(value = "relationTargetClass/{className}", method = RequestMethod.POST)
	public ResponseEntity<?> relationTargetClass(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "className") @PathVariable("className") String className,
			@ApiParam(value = "body") @RequestBody String body)
			throws Exception {
		return relClass(app_id, className, body, EntityColType.Relation);
	}

	@ApiOperation(value = "save pointer target class", notes = "save pointer target class",consumes="application/json",produces="application/json")
	@RequestMapping(value = "pointerTargetClass/{className}", method = RequestMethod.POST)
	public ResponseEntity<?> pointerTargetClass(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "className") @PathVariable("className") String className,
			@ApiParam(value = "body") @RequestBody String body)
			throws Exception {
		return relClass(app_id, className, body, EntityColType.Pointer);
	}

	private ResponseEntity<?> relClass(String app_id, String className,
			String body, EntityColType relType) throws Exception {
		log.debug("app_id: {} , className : {}", app_id, className);
		log.debug("body : {}", body);
		Document doc = Document.parse(body);
		Document ownEntity = entityService.getEntityByClassName(app_id, className);
		if (ownEntity == null) {
			return ResponseUtils.fail(
					ResponseErrorCode.UPDATE_CLASS_ERROR.value(), "No entity",
					HttpStatus.OK);
		}
		Document relEntity = entityService.getEntityByClassName(app_id, doc.getString("relEntityName"));
		if (relEntity == null) {
			return ResponseUtils.fail(
					ResponseErrorCode.UPDATE_CLASS_ERROR.value(),
					"No relation target entity", HttpStatus.OK);
		}

		String colName = doc.getString("colName");
		String colType = entityService.getColTypeByName(app_id,
				ownEntity.getString(MongoDBConstants.DOCUMENT_ID), colName);
		if (colType != null) {
			return ResponseUtils.fail(
					ResponseErrorCode.UPDATE_CLASS_ERROR.value(),
					"Column name already exists", HttpStatus.OK);
		}
		entityService.relClass(ownEntity, relEntity, colName, relType);
		return ResponseUtils.success();
	}

	/*
	 * @RequestBody String body e.g. { "relValuesIds": "[${relvaluesIds}]",
	 * "colName": "${colName}" }
	 */
	@ApiOperation(value = "save relation values", notes = "save relation values",consumes="application/json",produces="application/json")
	@RequestMapping(value = "relationValues/{className}/{valuesId}", method = RequestMethod.POST)
	public ResponseEntity<?> relationValues(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "className") @PathVariable("className") String className,
			@ApiParam(value = "valuesId") @PathVariable("valuesId") String valuesId,
			@ApiParam(value = "body") @RequestBody String body)
			throws Exception {
		log.debug("app_id: {} , className: {} , valuesId: {}", app_id, className, valuesId);
		log.debug("body : {}", body);
		Document doc = Document.parse(body);
		valuesService.relValues(app_id, className, valuesId, doc.get("relValuesIds"), 
				doc.getString("colName"), EntityColType.Relation);
		return ResponseUtils.success();
	}

	/*
	 * @RequestBody String body e.g. { "potValuesId": "${potValuesId}",
	 * "colName": "${colName}" }
	 */
	@ApiOperation(value = "save pointer values", notes = "save pointer values",consumes="application/json",produces="application/json")
	@RequestMapping(value = "pointerValues/{className}/{valuesId}", method = RequestMethod.POST)
	public ResponseEntity<?> pointerValues(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "className") @PathVariable("className") String className,
			@ApiParam(value = "valuesId") @PathVariable("valuesId") String valuesId,
			@ApiParam(value = "body") @RequestBody String body)
			throws Exception {
		log.debug("app_id: {} , className: {} , valuesId: {}", app_id, className, valuesId);
		log.debug("body : {}", body);
		Document doc = Document.parse(body);
		valuesService.relValues(app_id, className, valuesId, doc.get("potValuesId"), 
				doc.getString("colName"), EntityColType.Pointer);
		return ResponseUtils.success();
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "save class and values", notes = "save class and values",consumes="application/json",produces="application/json")
	@RequestMapping(value = "/multivalues/{className}", method = RequestMethod.POST)
	public ResponseEntity<?> multivalues(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "className") @PathVariable("className") String className,
			@ApiParam(value = "body") @RequestBody String body)
			throws Exception {
		log.debug("app_id: {} , className : {}", app_id, className);
		log.debug("body : {}", body);

		try {
			JSONArray arr = JSONArray.fromObject(body);
			
			List<Document> resultlist = new ArrayList<Document>();
			for (int i = 0; i < arr.size(); i++) {
				Document json = Document.parse(arr.get(i).toString());
				
				List<Document> cols = new ArrayList<Document>();
				Set<String> set = json.keySet();
				for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
					String name = iterator.next();
					Object values = json.get(name);
					EntityColType type = null;
					if (values instanceof Document) {
						Document column = (Document) values;
						if (column.containsKey(MaDataType.KEY_TYPE) && column.containsKey(MaDataType.KEY_VALUE)) {
							type = EntityColType.valueOf(((Document) json.get(name)).getString(MaDataType.KEY_TYPE));
						} else {
							type = valuesService.judgeType(values);
						}
					} else {
						type = valuesService.judgeType(values);
					}
					Document col = entityService.createEntityColumn(name, type.name());
					cols.add(col);
				}

				Document entity = entityService.getEntityByClassName(app_id, className);
				// if class not exist, create class
				if (entity == null) {
					entity = entityService.save(app_id, className, cols);
				} else {
					List<Document> newColumnsList = new ArrayList<Document>();
					List<Document> columns = (ArrayList<Document>) entity
							.get(MongoDBConstants.ENTITY_COLS);
					for (Document col : cols) {
						boolean tag = false;
						for (Document column : columns) {
							if (column.getString(MongoDBConstants.COLUMN_COLNAME).equals(
									col.getString(MongoDBConstants.COLUMN_COLNAME))) {
								tag = true;
								break;
							}
						}
						if (!tag) {
							newColumnsList.add(col);
						}
					}
					if (newColumnsList.size() != 0) {
						columns.addAll(newColumnsList);
						entityService.updateColsById(app_id,
								entity.getString(MongoDBConstants.DOCUMENT_ID),
								columns);
					}
				}

				Document result = null;
				// save values
				if (json != null) {
					result = valuesService.save(app_id,
							entity.getString(MongoDBConstants.DOCUMENT_ID),
							className, json);
					resultlist.add(result);
				}
			}

			return ResponseUtils.successWithValues(resultlist);
		} catch (Exception e) {
			log.error("error : " + e);
			return ResponseUtils.fail(ResponseErrorCode.ADD_CLASS_ERROR.value(),
							"add class values error : " + e.getMessage(), HttpStatus.OK);
		}
	}
	
	@SuppressWarnings("unused")
	private EntityColType judgeType(Object obj) {
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

	/**
	 * e.g.
	 * {"{$className1}":{"name":"ss","age":111},"{$className2}":[{"name":"b1"
	 * ,"age":"b2"},{"name":"b2"}],"relation":{"__type":"Pointer","__colName":
	 * "$colName","__className":"B","__relEntityName":"C"}}
	 * {"{$className1}":{"name"
	 * :"ss","age":111},"{$className2}":[{"name":"b1","age" :"b2"},{"name"
	 * :"b2"}],"relation":{"__type":"Pointer","__colName":"$colName"
	 * ,"__className":"B","__relEntityName":"C"}}
	 * 
	 * * {"{$className1}":{"name":"ss","age":111},"{$className2}":[{"name":"b1"
	 * ,"age":"b2"},{"name":"b2"}],"relation":{"__type":"Relation","__colName":
	 * "$colName","__className":"B","__relEntityName":"C"}}
	 *
	 * @param app_id
	 * @param rest_api_key
	 * @param body
	 * @return
	 * @throws Exception
	 */
//	@ApiOperation(value = "save class ,values and relation&pointer", notes = "save class ,values and relation&pointer", consumes = "application/json", produces = "application/json")
//	@RequestMapping(value = "saveData", method = RequestMethod.POST)
//	public ResponseEntity<?> saveDataAndRelation(
//			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
//			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
//			@ApiParam(value = "body") @RequestBody String body)
//			throws Exception {
//		Document doc = Document.parse(body);
//		saveClassAndBuildReation(app_id, doc);
//		return ResponseUtils.success();
//	}

//	private void saveClassAndBuildReation(String app_id, Document doc)
//			throws Exception {
//		Document firstResult = new Document();
//		List<Document> secondResult = new ArrayList<Document>();
//		int count = 0;
//		String relClassName = null;
//		String relEntityName = null;
//		String colName = null;
//		String type = null;
//		String firstClassName = null;
//		String secondClassName = null;
//		ArrayList<String> relValuesIds = new ArrayList<String>();
//
//		for (Iterator<?> iterator = doc.keySet().iterator(); iterator.hasNext();) {
//			String className = iterator.next().toString();
//			count++;
//			if (count == 1) {
//				firstClassName = className;
//				Document json = (Document) doc.get(className);
//				log.debug("app_id: {} , className : {}", app_id, className);
//				log.debug("body : {}", json);
//				Entity entity = buildEntity(app_id, className, json);
//				// save values
//				if (json != null) {
//					firstResult = valuesService.save(entity.getId(), json);
//				}
//				continue;
//			}
//			if (className.equals("relation")) {
//				Document json = (Document) doc.get(className);
//				type = (String) json.get("__type");
//				colName = json.getString("__colName");
//				relClassName = json.getString("__className");
//				relEntityName = json.getString("__relEntityName");
//				continue;
//			}
//			if (count == 2) {
//				secondClassName = className;
//				log.debug("app_id: {} , className : {}", app_id, className);
//				Object object = doc.get(className);
//				if (object instanceof List) {
//					List<Document> lists = (List<Document>) object;
//					for (Document document : lists) {
//						Entity entity = buildEntity(app_id, className, document);
//						Document result = null;
//						if (document != null) {
//							result = valuesService.save(entity.getId(),
//									document);
//						}
//						if (result != null) {
//							secondResult.add(result);
//						}
//					}
//				}
//				if (object instanceof Document) {
//					Entity entity = buildEntity(app_id, className,
//							(Document) object);
//					Document result = null;
//					if (object != null) {
//						result = valuesService.save(entity.getId(),
//								(Document) object);
//					}
//					if (result != null) {
//						secondResult.add(result);
//					}
//				}
//			}
//		}
//		if (secondResult.size() > 0) {
//			for (Document docResult : secondResult) {
//				String objectId = docResult.getObjectId("_id").toString();
//				relValuesIds.add(objectId);
//			}
//		}
//
//		if (StringUtil.notEmpty(type)) {
//			EntityColType colType = EntityColType.valueOf(type);
//			Document relationDoc = new Document();
//			relationDoc.append("colName", colName);
//			switch (colType) {
//			case Pointer: {
//				if (StringUtil.notEmpty(firstClassName)
//						&& firstClassName.equals(relClassName)) {
//					relationDoc.append("relEntityName", secondClassName);
//					relClass(app_id, firstClassName, relationDoc.toJson(),
//							EntityColType.Pointer);
//
//					valuesService.relValues(firstResult.getObjectId("_id")
//							.toString(), secondResult.get(0).getObjectId("_id")
//							.toString(), colName, EntityColType.Pointer);
//
//				} else {
//					relationDoc.append("relEntityName", firstClassName);
//					relClass(app_id, secondClassName, relationDoc.toJson(),
//							EntityColType.Pointer);
//					for (String relValuesId : relValuesIds) {
//						valuesService.relValues(relValuesId, firstResult
//								.getObjectId("_id").toString(), colName,
//								EntityColType.Pointer);
//					}
//				}
//				break;
//			}
//			case Relation: {
//				relationDoc.append("relEntityName", secondClassName);
//				relClass(app_id, firstClassName, relationDoc.toJson(),
//						EntityColType.Relation);
//				if (StringUtil.notEmpty(firstClassName)
//						&& firstClassName.equals(relClassName)) {
//					valuesService.relValues(firstResult.getObjectId("_id")
//							.toString(), relValuesIds, colName,
//							EntityColType.Relation);
//				}
//				break;
//			}
//			default:
//				break;
//			}
//		}
//	}
}