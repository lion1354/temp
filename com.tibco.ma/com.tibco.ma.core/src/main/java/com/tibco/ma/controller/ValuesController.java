package com.tibco.ma.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;
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

import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.core.EntityColType;
import com.tibco.ma.service.BaseService;
import com.tibco.ma.service.EntityService;
import com.tibco.ma.service.ValuesService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@SuppressWarnings("rawtypes")
@Controller
@RequestMapping("ma/1/values")
public class ValuesController extends BaseController {
	private static final Logger log = LoggerFactory
			.getLogger(ValuesController.class);

	@Resource
	private ValuesService valuesService;

	@Resource
	private EntityService entityService;
	@Autowired
	private ConfigInfo configInfo;

	@Override
	public BaseService getService() {
		return valuesService;
	}

	/**
	 * 
	 * @param document
	 *            { "nowpage": ${nowpage}, "pagesize": ${pagesize}, "appId":
	 *            "${appId}", "entityId": "${entityId}", "ownColName":
	 *            "${ownColName}", "owningId": "${owningId}", "relatedId":
	 *            "${relatedId}" }
	 * @return
	 */
	@ApiOperation(value = "query values", notes = "query values")
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public ResponseEntity<?> query(
			@ApiParam(value = "document", required = true) @RequestBody(required = true) Document document) {
		try {
			log.debug("query values document: {}", document.toJson());
			String appId = document.getString(MongoDBConstants.ENTITY_APPID);
			String entityId = document
					.getString(MongoDBConstants.VALUES_ENTITYID);
			Integer nowpage = document.getInteger("nowpage");
			Integer pagesize = document.getInteger("pagesize");
			if (nowpage == null || nowpage.intValue() < 1) {
				nowpage = 1;
			}
			if (pagesize == null || pagesize.intValue() < 1) {
				pagesize = MongoDBConstants.PAGE_SIZE;
			}
			document.remove("nowpage");
			document.remove("pagesize");
			document.remove("appId");
			document.put(MongoDBConstants.VALUES_ENTITYID, entityId);
			Document sort = new Document(MongoDBConstants.VALUES_CREATEAT,
					MongoDBConstants.ORDERBY_DESC);

			Pager<Document> pager = valuesService.valuesPage(appId, entityId,
					document, sort, nowpage, pagesize);
			List<Document> data = pager.getData();
			for (Document doc : data) {
				for (String key : doc.keySet()) {
					Object obj = doc.get(key);
					if (obj instanceof Document) {
						Document val = (Document) obj;
						if ("Date".equals(val.getString("__type"))) {
							Object iso = val.get("iso");
							doc.put(key, iso);
						}
					}
				}
			}
			return ResponseUtils.successWithValue(pager);
		} catch (Exception e) {
			log.error("query: {}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 
	 * @param document
	 *            { "appId": "${appId}", "entityId": "${entityId}", "colName":
	 *            "${colName}", }
	 * @return
	 */
	@ApiOperation(value = "query all values", notes = "query all values")
	@RequestMapping(value = "/all", method = RequestMethod.POST)
	public ResponseEntity<?> all(
			@ApiParam(value = "document", required = true) @RequestBody(required = true) Document document) {
		try {
			log.debug("query filter values document: {}", document.toJson());
			String appId = document.getString(MongoDBConstants.ENTITY_APPID);
			String entityId = document
					.getString(MongoDBConstants.VALUES_ENTITYID);
			ArrayList<String> colsList = (ArrayList<String>) document
					.get("colName");

			Document filter = new Document();
			filter.append(MongoDBConstants.VALUES_ENTITYID, entityId);

			Document entity = entityService.getEntityById(appId, entityId);
			String valuesCollection = entity.getString("valuesCollection");

			Document keys = new Document();
			for (String col : colsList) {
				keys.put(col, 1);
			}
			List<Document> list = valuesService.query(valuesCollection,
					new Document(MongoDBConstants.VALUES_ENTITYID, entityId),
					keys, null);
			for (Document doc : list) {
				for (String key : doc.keySet()) {
					Object obj = doc.get(key);
					if (obj instanceof Document) {
						Document val = (Document) obj;
						if ("Image".equals(val.getString("__type"))) {
							Object obj2 = val.get("urls");
							if (obj2 != null) {
								ArrayList<String> arrList = (ArrayList<String>) obj2;
								ArrayList<String> newList = new ArrayList<String>();
								for (String str : arrList) {
									String newUrl = configInfo
											.getEmailServerPath() + str;
									newList.add(newUrl);
								}
								val.put("urls", newList);
							}
						}
						if ("File".equals(val.getString("__type"))) {
							String url = val.getString("url");
							if (StringUtils.isNotBlank(url)) {
								val.put("url", configInfo.getEmailServerPath()
										+ url);
							}
						}
					}
				}
			}

			log.info("list:" + list);
			return ResponseUtils.successWithValue(list);
		} catch (Exception e) {
			log.error("query: {}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 
	 * @param document
	 *            { "_id": "${_id}", "appId": "${appId}", "entityId":
	 *            "${entityId}", "colName": "${colName}", "value": "${value}",
	 *            "ownEntityId": "${ownEntityId}", "ownValuesId":
	 *            "${ownValuesId}", "ownColName": "${ownColName}", }
	 * @return
	 */
	@Log(operate = "Save Values", modelName = "Core")
	@ApiOperation(value = "save values", notes = "save values")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "document", required = true) @RequestBody(required = true) Document document) {
		try {
			// values
			String appId = document.getString(MongoDBConstants.ENTITY_APPID);
			String id = document.getString(MongoDBConstants.DOCUMENT_ID);
			String entityId = document
					.getString(MongoDBConstants.VALUES_ENTITYID);
			String colName = document
					.getString(MongoDBConstants.COLUMN_COLNAME);
			String value = document.getString("value");
			// relation values
			String ownEntityId = document.getString("ownEntityId");
			String ownValuesId = document.getString("ownValuesId");
			String ownColName = document.getString("ownColName");
			String msg = validateSaveValue(appId, entityId, colName, value);
			if (StringUtil.notEmpty(msg)) {
				return ResponseUtils.alert(msg);
			}
			valuesService.save(appId, id, entityId, colName, value,
					ownEntityId, ownValuesId, ownColName);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("save: {}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	private String validateSaveValue(String appId, String entityId,
			String colName, String value) throws Exception {
		String colType = entityService.getColTypeByName(appId, entityId,
				colName);
		String message = null;
		// validate is number type
		if (colType.equals(EntityColType.Number.name())) {
			Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
			if (!pattern.matcher(value).matches()) {
				message = "It's not number. Please re-enter the number!";
			}
		}
		// validate is object(json) type
		else if (colType.equals(EntityColType.Object.name())) {
			try {
				Document.parse(value);
			} catch (Exception e) {
				message = "It's not Object. Please re-enter the Object!";
			}
		}
		return message;
	}

	/**
	 * 
	 * @param document
	 *            { "appId": "${appId}", "entityId": "${entityId}",
	 *            "ownEntityId": "${ownEntityId}", "ownColName":
	 *            "${ownColName}", "_ids": [${_ids}] }
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Log(operate = "Delete Values", modelName = "Core")
	@ApiOperation(value = "delete values", notes = "delete values")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(
			@ApiParam(value = "document", required = true) @RequestBody(required = true) Document document) {
		try {
			List<String> ids = document.get("_ids", List.class);
			if (ids != null && ids.size() > 0) {
				String appId = document
						.getString(MongoDBConstants.ENTITY_APPID);
				String entityId = document
						.getString(MongoDBConstants.VALUES_ENTITYID);
				String ownEntityId = document.getString("ownEntityId");
				String ownColName = document.getString("ownColName");
				for (String id : ids) {
					valuesService.deleteValue(appId, id, entityId, ownEntityId,
							ownColName);
				}
				return ResponseUtils.success();
			} else {
				return ResponseUtils.alert("Select a value please");
			}

		} catch (Exception e) {
			log.error("delete: {}", e);
			return ResponseUtils.fail("Delete error " + e.getMessage());
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
		// TODO Auto-generated method stub
		return null;
	}

}
