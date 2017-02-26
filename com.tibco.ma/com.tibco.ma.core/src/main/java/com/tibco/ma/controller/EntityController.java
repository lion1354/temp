package com.tibco.ma.controller;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import com.mongodb.util.JSON;
import com.tibco.ma.common.JSONStrFormatUtils;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.Category;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.core.EntityColType;
import com.tibco.ma.service.BaseService;
import com.tibco.ma.service.CategoryService;
import com.tibco.ma.service.EntityService;
import com.tibco.ma.service.SpecificationService;
import com.tibco.ma.service.ValuesService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * 
 * @author aidan
 * 
 */
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping("ma/1/entity")
public class EntityController extends BaseController {

	private static final Logger log = LoggerFactory
			.getLogger(EntityController.class);

	@Resource
	private EntityService entityService;

	@Resource
	private ValuesService valuesService;

	@Resource
	private SpecificationService specificationService;

	@Autowired
	private CategoryService categoryService;

	@Override
	public BaseService getService() {
		return entityService;
	}

	/**
	 * 
	 * @param appId
	 * @return
	 */
	@ApiOperation(value = "get all entity", notes = "get all entity")
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<?> all(
			@ApiParam(value = "appId", required = true) @RequestParam(value = "appId", required = true) String appId) {
		List<Document> list = null;
		try {
			list = entityService.getAllEntityByAppId(appId);
		} catch (Exception e) {
			log.error("all: {}", e);
		}
		return ResponseUtils.successWithValues(list);
	}

	/**
	 * 
	 * @param appId
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "get entity by id", notes = "get entity by id")
	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	public ResponseEntity<?> getById(
			@ApiParam(value = "appId", required = true) @RequestParam(value = "appId", required = true) String appId,
			@ApiParam(value = "id", required = true) @RequestParam(value = "id", required = true) String id) {
		Document entity = null;
		try {
			entity = entityService.getEntityById(appId, id);
		} catch (Exception e) {
			log.error("getById: {}", e);
		}
		return ResponseUtils.successWithValue(entity);
	}

	/**
	 * 
	 * @param document
	 *            { "_id": "${_id}", "appId": "${appId}", "className":
	 *            "${className}", "comment": "${comment}", "col": { "colName":
	 *            "${colName}", "colType": "${colType}", "relEntityId":
	 *            "${relEntityId}", "relEntityName": "${relEntityName}" } }
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Log(operate = "Save Class", modelName = "Core")
	@ApiOperation(value = "save or update entity", notes = "save or update entity")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "document", required = true) @RequestBody(required = true) Document document) {
		try {
			String id = document.getString(MongoDBConstants.DOCUMENT_ID);
			String appId = document.getString(MongoDBConstants.ENTITY_APPID);
			if (StringUtil.notEmpty(id)) {
				// validate
				Object col = document.get("col");
				Document colDoc = new Document();
				if (col != null && col instanceof LinkedHashMap) {
					LinkedHashMap<String, String> colList = (LinkedHashMap<String, String>) document
							.get("col");
					for (Map.Entry<String, String> entry : colList.entrySet()) {
						colDoc.append(entry.getKey(), entry.getValue());
					}
				}
				if (col != null && col instanceof Document) {
					colDoc = (Document) col;
				}
				String colName = colDoc
						.getString(MongoDBConstants.COLUMN_COLNAME);
				if (!colNameValidate(appId, id, colName)) {
					return ResponseUtils
							.alert("The Column Name has already existed!");
				}
				// update
				entityService.update(appId, document);
				return ResponseUtils.success();
			} else {
				// validate
				String className = document
						.getString(MongoDBConstants.ENTITY_CLASSNAME);
				if (StringUtil.isEmpty(className)) {
					return ResponseUtils
							.alert("The Class Name must not empty!");
				}
				if (!classNameValidate(appId, className)) {
					return ResponseUtils
							.alert("The Class Name has already existed!");
				}
				// save
				Document entity = entityService.save(appId, document);
				return ResponseUtils.successWithValue(entity);
			}
		} catch (Exception e) {
			log.error("save error: {}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private boolean colNameValidate(String appId, String id, String colName)
			throws Exception {
		if (StringUtil.notEmpty(colName)) {
			Document entity = entityService.getEntityById(appId, id);
			List<Document> cols = (ArrayList<Document>) entity
					.get(MongoDBConstants.ENTITY_COLS);
			if (cols != null && cols.size() > 0) {
				for (Document col : cols) {
					String name = col
							.getString(MongoDBConstants.COLUMN_COLNAME);
					if (name.equals(colName)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean classNameValidate(String appId, String className)
			throws Exception {
		Document entity = entityService.getByClassName(className, appId);
		if (entity != null) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param document
	 *            { "_id": "${_id}", "appId": "${appId}" }
	 * @return
	 */
	@Log(operate = "Delete Class", modelName = "Core")
	@ApiOperation(value = "delete entity", notes = "delete entity")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(
			@ApiParam(value = "document", required = true) @RequestBody(required = true) Document document) {
		try {
			String id = document.getString(MongoDBConstants.DOCUMENT_ID);
			String appId = document.getString(MongoDBConstants.ENTITY_APPID);
			// validate
			if (StringUtil.isEmpty(id)) {
				return ResponseUtils.fail("Select a class please");
			}

			Document entity = entityService.getEntityById(appId, id);
			String className = entity
					.getString(MongoDBConstants.ENTITY_CLASSNAME);
//			if (StringUtil.notEmptyAndEqual(className,
//					MongoDBConstants.SYSTEM_USER_COLLECTION_NAME)) {
//				return ResponseUtils
//						.alert("This is system user class, do not be delete");
//			}

			Document filter = new Document(MongoDBConstants.VALUES_ENTITYID, id);
			long count = valuesService.count(
					valuesService.getValuesCollectionName(appId, className),
					filter);
			if (count > 0) {
				return ResponseUtils.alert("This class had been used");
			}
			// delete
			entityService.deleteById(
					entityService.getEntityCollectionName(appId), id);
			specificationService.deleteByCategory(id);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("delete: {}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 
	 * @return
	 */
	@ApiOperation(value = "get entity column type", notes = "get entity column type")
	@RequestMapping(value = "/getColType", method = RequestMethod.GET)
	public ResponseEntity<?> getColType() {
		EntityColType[] typeArray = EntityColType.values();
		List<String> typeList = new ArrayList<String>();
		for (EntityColType type : typeArray) {
			typeList.add(type.name());
		}
		return ResponseUtils.successWithValues(typeList);
	}

	/**
	 * 
	 * @param document
	 *            { "_id": "${_id}", "appId": "${appId}", "colName":
	 *            "${colName}" }
	 * @return
	 */
	@Log(operate = "Remove Column", modelName = "Core")
	@ApiOperation(value = "remove column", notes = "remove column")
	@RequestMapping(value = "/removeColumn", method = RequestMethod.POST)
	public ResponseEntity<?> removeColumn(
			@ApiParam(value = "document", required = true) @RequestBody(required = true) Document document) {
		try {
			String appId = document.getString(MongoDBConstants.ENTITY_APPID);
			String id = document.getString(MongoDBConstants.DOCUMENT_ID);
			String colName = document
					.getString(MongoDBConstants.COLUMN_COLNAME);
			// validate
			if (StringUtil.isEmpty(colName)) {
				return ResponseUtils.alert("Please select one Column!");
			}
			// delete
			entityService.removeColumn(appId, id, colName);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("removeColumn: {}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@Log(operate = "Export entity class", modelName = "Core")
	@ApiOperation(value = "Export entity classn", notes = "Export entity class")
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(HttpServletRequest request, HttpServletResponse response) {
		String appId = request.getParameter("appId");
		String id = request.getParameter("id");
		try {
			ServletOutputStream outputStream = response.getOutputStream();
			ZipOutputStream zos = new ZipOutputStream(new DataOutputStream(
					outputStream));

			String collection = appId + MongoDBConstants.ENTITY_COLLECTION_NAME;
			Document filter = new Document(MongoDBConstants.ENTITY_APPID, appId);
			filter.append("_id", new ObjectId(id));

			Document document = entityService.getOne(collection, filter);
			String valuesCollection = document.getString("valuesCollection");
			String className = document.getString("className");
			ArrayList<Document> colsList = (ArrayList<Document>) document
					.get("cols");
			for (Document doc : colsList) {
				String relValuesCollection = doc
						.getString("relValuesCollection");
				if (relValuesCollection != null) {
					List<Document> relValuesCollectionList = entityService
							.query(relValuesCollection, null, null);// 56444eb7187657194c438577_sf_T_Join
					String relZipFile = doc.getString("colName") + "_"
							+ className + "_Join" + ".json";
					ZipEntry zipEntry = new ZipEntry(relZipFile);
					zos.putNextEntry(zipEntry);
					zos.write(JSONStrFormatUtils.format(
							JSONArray.fromObject(relValuesCollectionList)
									.toString()).getBytes());

					String relEntityName = doc.getString("relEntityName");
					String relEntityId = doc.getString("relEntityId");

					// filter data by relatedIds
					List<ObjectId> relatedIds = new ArrayList<ObjectId>();
					for (int i = 0; i < relValuesCollectionList.size(); i++) {
						Document dm = relValuesCollectionList.get(i);
						relatedIds.add(new ObjectId(dm.getString("relatedId")));
					}
					Document relEntityFilter = new Document("_id",
							new Document(QueryOperators.IN, relatedIds));
					List<Document> relEntityList = entityService.query(appId
							+ "_" + relEntityName + "_values", relEntityFilter,
							null);
					zipEntry = new ZipEntry(relEntityName + ".json");
					zos.putNextEntry(zipEntry);
					zos.write(JSONStrFormatUtils.format(
							JSONArray.fromObject(relEntityList).toString())
							.getBytes());
				}
			}
			List<Document> valuesList = entityService.query(valuesCollection,
					null, null);

			response.setHeader("content-disposition", "attachment;filename="
					+ appId + ".zip");

			ZipEntry zipEntry = new ZipEntry(className + ".json");
			zos.putNextEntry(zipEntry);
			zos.write(JSONStrFormatUtils.format(
					JSONArray.fromObject(valuesList).toString()).getBytes());
			zos.flush();
			zos.close();

		} catch (Exception e) {
			log.error("all: {}", e);
		}
	}

	@ApiOperation(value = "import data", notes = "import data from json file")
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public ResponseEntity<?> importProcess(
			HttpServletRequest request,
			@ApiParam(value = "document", required = true) @RequestBody(required = true) Document document)
			throws Exception {
		try {
			String appId = document.getString("appId");
			String fileName = document.getString("fileName");
			String type = document.getString("classType");// custom relation
			String realPath = request.getSession().getServletContext()
					.getRealPath("/");
			log.info("realPath:" + realPath);
			String content = FileUtils.readFileToString(new File(realPath
					+ "/upload/" + fileName));
			Object obj = JSON.parse(content);
			if ("relation".equals(type)) {
				String relatedField = document.getString("relatedField");
				String relationOwner = document.getString("relationOwner");
				String relationTarget = document.getString("relationTarget");

				String collectionName = appId + "_" + relatedField + "_"
						+ relationOwner + "_Join";
				if (obj instanceof BasicDBList) {
					BasicDBList bdList = (BasicDBList) obj;
					for (int i = 0; i < bdList.size(); i++) {
						BasicDBObject value = (BasicDBObject) bdList.get(i);
						Document doc = new Document();
						doc.putAll(value);

						Document joinDoc = valuesService.getOneById(
								collectionName, value.getString("_id"));
						log.info("joinDoc:" + joinDoc);
						if (null == joinDoc) {
							doc.put("_id", new ObjectId(doc.getString("_id")));
							entityService.insertOneWithId(collectionName, doc);
						}
					}
				}
				//add relation col
				Document relationOwnerDoc = entityService.getByClassName(appId, relationOwner);
				Document relationTargetDoc = entityService.getByClassName(appId, relationTarget);
				if(relationOwnerDoc != null && relationTargetDoc != null){
					List<Document> cols = (ArrayList<Document>) relationOwnerDoc
							.get(MongoDBConstants.ENTITY_COLS);
					boolean colExist = false;
					if (cols != null && cols.size() > 0) {
						for (Document col : cols) {
							String name = col.getString(MongoDBConstants.COLUMN_COLNAME);
							if (name.equals(relatedField)) {
								colExist = true;
							}
						}
					}
					if(!colExist){
						Document relationCol = new Document();
						relationCol.append("colName", relatedField);
						relationCol.append("colType", "Relation");
						relationCol.append("relEntityId", relationTargetDoc.getString("_id"));
						relationCol.append("relEntityName", relationTarget);
						relationCol.append("relValuesCollection", collectionName);
						cols.add(relationCol);
						
						// update
						log.info("relationOwnerDoc:" + relationOwnerDoc);
						entityService.updateOne(appId+"_entity", new Document("_id",new ObjectId(relationOwnerDoc.getString("_id"))), relationOwnerDoc);
					}
					
				}
			}
			if ("custom".equals(type)) {
				String name = document.getString("newName");
				String collectionName = appId + "_" + name + "_values";

				if (obj instanceof BasicDBObject) {

				}
				if (obj instanceof BasicDBList) {
					BasicDBList bdList = (BasicDBList) obj;
					String objId = "";
					Document classDoc = entityService.getByClassName(appId,
							name);
					log.info("classDoc:" + classDoc);

					if (classDoc == null) {
						List<Document> cols = new ArrayList<Document>();
						for (int i = 0; i < bdList.size(); i++) {
							BasicDBObject value = (BasicDBObject) bdList.get(i);
							Set<Entry<String, Object>> entrySet = value
									.entrySet();
							for (Entry<String, Object> entry : entrySet) {
								Class clazz = entry.getValue().getClass();
								String classType = "Object";
								if ("java.lang.String".equals(clazz.getName())) {
									classType = "String";
								}
								if ("java.lang.Double".equals(clazz.getName())) {
									classType = "Number";
								}
								if ("java.lang.Integer".equals(clazz.getName())) {
									classType = "Number";
								}
								if ("java.lang.Long".equals(clazz.getName())) {
									classType = "Number";
								}
								if ("java.lang.Boolean".equals(clazz.getName())) {
									classType = "Boolean";
								}
								if ("com.mongodb.BasicDBList".equals(clazz
										.getName())) {
									classType = "Array";
								}
								if ("com.mongodb.BasicDBObject".equals(clazz
										.getName())) {
									BasicDBObject bd = (BasicDBObject) entry
											.getValue();
									if (null != bd.getString("__type")) {
										classType = bd.getString("__type");
									}
								}

								Document col = new Document();
								col.put("colName", entry.getKey());
								col.put("colType", classType);
								if (!cols.contains(col)
										&& !"_id".equals(entry.getKey())
										&& !"entityId".equals(entry.getKey())
										&& !"createAt".equals(entry.getKey())
										&& !"updateAt".equals(entry.getKey())) {
									cols.add(col);
								}
							}
						}

						Document entityDoc = new Document();
						entityDoc.put("appId", appId);
						entityDoc.put("className", name);
						entityDoc.put("cols", cols);
						entityDoc.put("comment", name);
						entityDoc.put("valuesCollection", collectionName);
						entityService.insertOne(appId + "_" + "entity",
								entityDoc);
						objId = entityDoc.getObjectId("_id").toString();

						// if type image,add category and
						for (Document col : cols) {
							if ("Image".equals(col.getString("colType"))) {
								JSONObject specification = new JSONObject();

								String categoryName = col.getString("colName")
										+ objId;
								Category category = new Category();
								category.setName(categoryName);
								categoryService.save(category);
								
								specification.put("categoryName", categoryName);
								specification.put("comment", "default spec");
								specification.put("category", category.getId());
								
								List<LinkedHashMap> specList = new ArrayList<LinkedHashMap>();
								LinkedHashMap spec = new LinkedHashMap();
								spec.put("name", "default");
								spec.put("width", "330");
								spec.put("height", "480");
								specList.add(spec);
								specification.put("specList", specList);
								
								specificationService.add(specification);
							}
						}

					}

					for (int i = 0; i < bdList.size(); i++) {
						BasicDBObject value = (BasicDBObject) bdList.get(i);
						Document doc = new Document();
						doc.putAll(value);
						if (classDoc == null) {
							// if null add to entity
							doc.put("_id", new ObjectId(doc.getString("_id")));
							doc.put("entityId", objId);
							entityService.insertOneWithId(collectionName, doc);
						} else {
							Document dbDoc = entityService.getOneById(
									collectionName, doc.getString("_id"));
							if (dbDoc != null) {
								entityService.updateOne(
										collectionName,
										new Document("_id", new ObjectId(doc
												.getString("_id"))), doc);
							} else {
								doc.put("_id",
										new ObjectId(doc.getString("_id")));
								entityService.insertOneWithId(collectionName,
										doc);
							}
						}
					}
				}
			}
			return ResponseUtils.success();
		} catch (Exception ex) {
			log.error("import entity class: {}", ex);
			return ResponseUtils.fail(ex.getMessage());
		} finally {
			String realPath = request.getSession().getServletContext()
					.getRealPath("/");
			String fileName = document.getString("fileName");
			FileUtils.deleteQuietly(new File(realPath + "/upload/" + fileName));
		}
	}

	@ApiOperation(value = "upload data", notes = "add data from json file")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<?> upload(HttpServletRequest request)
			throws Exception {
		try {
			String appId = request.getParameter("appId");
			// String name = request.getParameter("name");
			// String type = request.getParameter("type");// custom relation
			log.info("appId:" + appId);
			// log.info("name:" + name);
			// log.info("type:" + type);
			Map<String, Object> map = new HashMap<String, Object>();

			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			if (multipartResolver.isMultipart(request)) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						String myFileName = file.getOriginalFilename();
						InputStream inputStream = file.getInputStream();
						String content = IOUtils.toString(inputStream);

						String realPath = request.getSession()
								.getServletContext().getRealPath("/");
						log.info("realPath:" + realPath);
						File dir = new File(realPath + "/upload");
						if (!dir.exists()) {
							dir.mkdirs();
						}
						String saveFileName = System.currentTimeMillis()
								+ ".json";
						file.transferTo(new File(realPath + "upload/"
								+ saveFileName));
						map.put("fileName", saveFileName);
						Object obj = JSON.parse(content);

						Set<String> owningSet = new LinkedHashSet<String>();
						Set<String> relatedSet = new LinkedHashSet<String>();
						Set<String> relatedFieldSet = new LinkedHashSet<String>();
						if (obj instanceof BasicDBList) {
							BasicDBList bdList = (BasicDBList) obj;
							for (int i = 0; i < bdList.size(); i++) {
								BasicDBObject value = (BasicDBObject) bdList
										.get(i);
								String owningId = value.getString("owningId");
								String relatedId = value.getString("relatedId");
								if (!StringUtil.isEmpty(owningId)
										&& !StringUtil.isEmpty(relatedId)) {
									List<Document> allEntity = entityService
											.getAllEntityByAppId(appId);
									for (Document entity : allEntity) {
										String className = entity
												.getString("className");
										String valuesCollection = appId + "_"
												+ className + "_values";
										Document owningDoc = valuesService
												.getOneById(valuesCollection,
														owningId);
										if (null != owningDoc) {
											owningSet.add(className);
											ArrayList<Document> cols = (ArrayList<Document>) entity
													.get("cols");
											for (Document col : cols) {
												if ("Relation".equals(col
														.getString("colType"))) {
													relatedFieldSet
															.add(col.getString("colName"));
												}
											}
										}
										Document relatedDoc = valuesService
												.getOneById(valuesCollection,
														relatedId);
										if (null != relatedDoc) {
											relatedSet.add(className);
										}
									}
								}
							}
						}
						map.put("relatedSet", relatedSet);
						map.put("owningSet", owningSet);
						map.put("relatedFieldSet", relatedFieldSet);
					}
				}

			}
			return ResponseUtils.successWithValue(map);
		} catch (Exception ex) {
			log.error("import entity class: {}", ex);
			return ResponseUtils.fail(ex.getMessage());
		}
	}

	@Override
	public Query getQuery(JSONObject json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pager getPager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCollection() {
		return null;
	}

}
