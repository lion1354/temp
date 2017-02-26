package com.tibco.ma.controller.rest.core;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tibco.ma.common.ResponseErrorCode;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.ValidateUtils;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.AppSettingService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@RestController
@RequestMapping("/1/appSetting")
public class CustomAppSettingController {
	private static Logger log = LoggerFactory
			.getLogger(CustomAppSettingController.class);
	@Autowired
	private AppSettingService appSettingService;

	
	/**
	 * e.g. {"key":"${key}","key":"String","value":"1111111"}
	 * type :  "Date" :  value:1436947892000
	 * 			Boolean    value:true   or   false
	 * 			GeoPoint  	value: {"latitude":"11,11","longitude":"22.22"}
	 * 			Array		value:["1","2","3"]
	 * 			Object		value:{"key1":"value1","key2":"value2"}
	 * 
	 * @param app_id
	 * @param rest_api_key
	 * @param body
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "save app setting", notes = "save app Setting", consumes = "application/json", produces = "application/json")
	@Log(operate = "Save appSetting", modelName = "Setting")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "body") @RequestBody String body)
			throws Exception {
		try {
			Document document = Document.parse(body);
			document.append("appId", app_id);
			String key = document.getString("key");
			Document filter = new Document();
			filter.append("appId", new ObjectId(app_id));
			filter.append("key", key);
			List<Document> result = appSettingService.queryAppSetting(
					getCollection(), filter, null);
			if (result == null || result.size() < 1) {
				appSettingService.save(getCollection(), document);
			} else {
				return ResponseUtils.fail(
						ResponseErrorCode.APP_CONFIG_ADD_ERROR,
						"Add app setting error : key is exist", HttpStatus.OK);
			}
			return ResponseUtils.success();
		} catch (Exception e) {
			return ResponseUtils.fail(ResponseErrorCode.APP_CONFIG_ADD_ERROR,
					"Add app setting error :" + e.getMessage(), HttpStatus.OK);
		}
	}
		/**
		 * e.g. {value:{$value}}
		 * @param app_id
		 * @param rest_api_key
		 * @param key
		 * @param body
		 * @return
		 * @throws Exception
		 */
	@Log(operate = "Update appSetting", modelName = "Setting")
	@ApiOperation(value = "update app setting", notes = "update app setting", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/update/{key}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "key", required = true) @PathVariable("key") String key,
			@ApiParam(value = "body") @RequestBody String body)
			throws Exception {
		try {
			log.debug("appId :{}", app_id);
			log.debug("body :{}", body);
			Document filter = new Document();
			filter.append("key", key);
			filter.append("appId", new ObjectId(app_id));
			List<Document> documents = appSettingService.queryAppSetting(
					getCollection(), filter, null);
			if (documents == null || documents.size() < 1) {
				return ResponseUtils.fail(
						ResponseErrorCode.APP_CONFIG_UPDATE_ERROR.value(),
						"Update app setting error :" + key + "is not exist",
						HttpStatus.OK);
			}
			Document result = documents.get(0);
			Document values = Document.parse(body);
			values.append("appId", app_id);
			values.append("key", key);
			values.append("type", result.get("type"));
			appSettingService.update(getCollection(), result.getString(MongoDBConstants.DOCUMENT_ID),
					values);
			return ResponseUtils.success();
		} catch (Exception e) {
			return ResponseUtils.fail(
					ResponseErrorCode.APP_CONFIG_UPDATE_ERROR.value(),
					"Update app setting error :" + e.getMessage(),
					HttpStatus.OK);
		}
	}

	@Log(operate = "Delete appSetting", modelName = "Setting")
	@ApiOperation(value = "delete app setting", notes = "delete app setting", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/delete/{ids}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "ids") @PathVariable(value = "ids") String idStr)
			throws Exception {
		try {
			String[] idst = idStr.split(",");
			List<String> ids = Arrays.asList(idst);
			if (ids != null && ids.size() > 0) {
				for (String id : ids) {
					appSettingService.deleteValue(getCollection(), id);
				}
			} else {
				return ResponseUtils.fail(
						ResponseErrorCode.APP_CONFIG_DELETE_ERROR.value(),
						"Is is empty", HttpStatus.OK);
			}
			return ResponseUtils.success();
		} catch (Exception e) {
			return ResponseUtils.fail(
					ResponseErrorCode.APP_CONFIG_DELETE_ERROR.value(),
					"Delete app setting error :" + e.getMessage(),
					HttpStatus.OK);
		}
	}

	@Log(operate = "Delete appSettingBy Key", modelName = "Setting")
	@ApiOperation(value = "delete app setting by key", notes = "delete app setting by key", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/deleteByKey/{key}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteByKey(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "key") @PathVariable(value = "key") String key)
			throws Exception {
		appSettingService.deleteByKey(getCollection(), key, app_id);
		return ResponseUtils.success();
	}

	@ApiOperation(value = "get app by id", notes = "get app by id", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "get/{settingId}", method = RequestMethod.GET)
	public ResponseEntity<?> get(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@PathVariable(value = "settingId") String id,
			HttpServletRequest request) throws Exception {

		Document document = new Document();
		document.append(MongoDBConstants.DOCUMENT_ID, new ObjectId(id));
		document.append("appId", new ObjectId(app_id));
		List<Document> documents = appSettingService.queryAppSetting(
				getCollection(), document, null);
		if (ValidateUtils.isValidate(documents)) {
			return ResponseUtils.successWithValue(documents.get(0).toJson());
		} else {
			return ResponseUtils.successWithValue(null);
		}

	}

	@ApiOperation(value = "get app configuration by key", notes = "get app configuration by key", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "getByKey/{key}", method = RequestMethod.GET)
	public ResponseEntity<?> getByKey(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@PathVariable(value = "key") String key, HttpServletRequest request)
			throws Exception {
		Document document = new Document();
		document.append("appId", new ObjectId(app_id));
		document.append("key", key);
		List<Document> documents = appSettingService.queryAppSetting(
				getCollection(), document, null);
		if (ValidateUtils.isValidate(documents)) {
			return ResponseUtils.successWithValue(documents.get(0).toJson());
		} else {
			return ResponseUtils.successWithValue(null);
		}
	}

	@ApiOperation(value = "get all appsetting", notes = "get all app setting", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<?> find(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "q", required = false) @RequestParam(value = "q", required = false) String q,
			@ApiParam(value = "s", required = false) @RequestParam(value = "s", required = false) String s,
			@ApiParam(value = "page", required = false) @RequestParam(value = "page", required = false) Integer page,
			@ApiParam(value = "pageSize", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) throws Exception {
		log.debug("query :{}", q);
		try {
			Document sort = new Document();
			if (s != null) {
				sort = Document.parse(s);
			}
			Document filter = new Document();
			if (q != null) {
				filter = Document.parse(q);
			}
			String appId = null;

			if (!filter.containsKey("appId")) {
				if (StringUtil.notEmpty(app_id)) {
					appId = app_id;
				}
			} else {
				appId = filter.getString("appId");
				filter.remove("appId");
			}
			filter.append("appId", new ObjectId(appId));

			if (page != null && pageSize != null) {
				if (pageSize > 1000)
					pageSize = 1000;
			} else if (page == null && pageSize == null) {
				page = 1;
				pageSize = 100;
			} else if (page == null && pageSize != null) {
				page = 1;
				if (pageSize > 1000)
					pageSize = 1000;
			} else if (page != null && pageSize == null) {
				pageSize = 100;
			}
			String projectURI = request.getContextPath();

			Pager<Document> pager = appSettingService.valuesPage(
					getCollection(), filter, sort, page, pageSize, app_id,
					projectURI);
			List<Document> dataList = pager.getData();

			return ResponseUtils.successWithValues(dataList);
		} catch (Exception e) {
			return ResponseUtils
					.fail(ResponseErrorCode.APP_CONFIG_GET_ERROR.value(),
							"Query app setting error :" + e.getMessage(),
							HttpStatus.OK);
		}
	}

	@ApiOperation(value = "uniquecheck key", notes = "uniquecheck key", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "uniqueCheck/{key}", method = RequestMethod.GET)
	public ResponseEntity<?> check(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@PathVariable(value = "key") String key) {
		Document query = new Document();
		query.append("appId", new ObjectId(app_id));
		query.append("key", key);
		try {
			long count = appSettingService.count(getCollection(), query);
			if (count != 1) {
				return ResponseUtils.fail(
						ResponseErrorCode.APP_CONFIG_UNIQUECHECK_ERROR.value(),
						"It is not unique", HttpStatus.OK);
			} else {
				return ResponseUtils.success();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseUtils.fail(
					ResponseErrorCode.APP_CONFIG_UNIQUECHECK_ERROR.value(),
					"Unique check error :" + e.getMessage(), HttpStatus.OK);
		}
	}

	public String getCollection() {
		return "app_setting";
	}

}
