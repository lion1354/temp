package com.tibco.ma.controller.rest.push;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javapns.notification.PushedNotification;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.gridfs.GridFSDBFile;
import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.DateQuery;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.pns.PNHelper;
import com.tibco.ma.model.App;
import com.tibco.ma.model.CloudCode;
import com.tibco.ma.model.Credential;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.CloudCodeService;
import com.tibco.ma.service.CredentialService;
import com.tibco.ma.service.EntityService;
import com.tibco.ma.service.ResourceGridFSService;
import com.tibco.ma.service.ScheduleService;
import com.tibco.ma.service.ValuesService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/1/custDevice")
public class CustomerDeviceController {

	private static final Logger log = LoggerFactory
			.getLogger(CustomerDeviceController.class);

	@Resource
	private CredentialService credentialService;
	@Resource
	private ResourceGridFSService resourceGridFSService;
	@Resource
	private CloudCodeService cloudCodeService;
	@Resource
	private ScheduleService scheduleService;
	@Resource
	private ConfigInfo configInfo;
	@Resource
	private ValuesService valuesService;
	@Resource
	private EntityService entityService;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "registration", notes = "registration", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "registration", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "appId", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String appId,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "clientId", required = true) @RequestHeader(value = "client_id", required = true) String clientId,
			@ApiParam(value = "clientAgent", required = true) @RequestHeader(value = "client-agent", required = true) String clientAgent,
			@ApiParam(value = "json") @RequestBody JSONObject json) {
		try {
			Document device = entityService.getEntityByClassName(appId,
					MongoDBConstants.SYSTEM_DEVICE_COLLECTION_NAME);
			List<Document> colList = (ArrayList<Document>) device
					.get(MongoDBConstants.ENTITY_COLS);

			Document values = new Document();
			values.append("appId", appId);
			values.append("deviceId", clientId);
			values.append("clientAgent", clientAgent);
			values.append("isActive", true);
			for (Document column : colList) {
				String colName = column
						.getString(MongoDBConstants.COLUMN_COLNAME);
				if (json.containsKey(column
						.getString(MongoDBConstants.COLUMN_COLNAME))) {
					values.append(colName, json.get(colName));
				}
			}

			Document document = new Document(MongoDBConstants.VALUES_ENTITYID,
					device.getString(MongoDBConstants.DOCUMENT_ID)).append(
					"deviceId", clientId).append(MongoDBConstants.ENTITY_APPID,
					appId);
			String valuesCollectionName = valuesService
					.getValuesCollectionName(appId,
							MongoDBConstants.SYSTEM_DEVICE_COLLECTION_NAME);
			Document valueOld = valuesService.getOne(valuesCollectionName,
					document);
			if (valueOld != null) {
				valuesService.updateById(valuesCollectionName,
						valueOld.getString(MongoDBConstants.DOCUMENT_ID),
						values);
			} else {
				valuesService.save(appId,
						device.getString(MongoDBConstants.DOCUMENT_ID),
						MongoDBConstants.SYSTEM_DEVICE_COLLECTION_NAME, values);
			}

			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "invite", notes = "invite", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "invite", method = RequestMethod.POST)
	public ResponseEntity<?> invite(
			HttpServletRequest request,
			HttpServletResponse response,
			@ApiParam(value = "appId", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String appId,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "json") @RequestBody JSONObject json) {
		try {
			JSONObject result = new JSONObject();

			if (StringUtil.isEmpty(appId)) {
				if (json.containsKey("appId")) {
					appId = json.get("appId").toString();
				} else {
					return ResponseUtils.fail("The APPID can't be null!");
				}
			}
			String message = null;
			if (json.containsKey("message")) {
				message = json.get("message").toString();
			}
			String url = null;
			if (json.containsKey("url")) {
				url = json.get("url").toString();
			}
			String imageUrl = null;
			if (json.containsKey("imageUrl")) {
				imageUrl = json.get("imageUrl").toString();
			}

			Boolean sendNow = null;
			if (json.containsKey("sendNow")) {
				sendNow = (Boolean) json.get("sendNow");
			}
			Boolean silent = false;
			if (json.containsKey("silent")) {
				silent = (Boolean) json.get("silent");
			}
			Map<String, Object> dataInfo = new HashMap<String, Object>();
			if (json.containsKey("datainfo")) {
				dataInfo = (Map<String, Object>) json.get("datainfo");
			}

			if (sendNow == null || sendNow.equals(true)) {

				Credential credential = credentialService.findOne(new Query(
						Criteria.where("app").is(new App(appId))),
						Credential.class);
				if (credential == null) {
					return ResponseUtils
							.fail("You need add the Credential first!");
				}

				Document query = new Document();
				ArrayList<String> deviceList = (ArrayList<String>) json
						.get("deviceList");
				ArrayList<ObjectId> idList = new ArrayList<ObjectId>();
				for (String id : deviceList) {
					idList.add(new ObjectId(id));
				}
				query.append(MongoDBConstants.DOCUMENT_ID, new Document("$in",
						idList));

				String valuesCollectionName = valuesService
						.getValuesCollectionName(appId,
								MongoDBConstants.SYSTEM_DEVICE_COLLECTION_NAME);
				List<Document> list = valuesService.query(valuesCollectionName,
						query, null);

				List<String> listAndroid = new ArrayList<String>();
				List<String> listIOS = new ArrayList<String>();
				for (Document document : list) {
					String os = document.getString("os");
					String deviceToken = document.getString("deviceToken");
					if (StringUtil.notEmpty(deviceToken)) {
						if ("android".equals(os)) {
							listAndroid.add(deviceToken);
						}
						if ("ios".equals(os)) {
							listIOS.add(deviceToken);
						}
					}
				}

				if (listAndroid != null && listAndroid.size() > 0) {
					if (StringUtil.isEmpty(credential.getApiKey())) {
						return ResponseUtils
								.fail("The Andriod Credential is null, please add Credential");
					}
				}
				if (listIOS != null && listIOS.size() > 0) {
					if (StringUtil.isEmpty(credential.getPath())) {
						return ResponseUtils
								.fail("The IOS Credential is null, please add Credential");
					}
				}

				if (listAndroid != null && listAndroid.size() > 0) {
					Map<String, Object> results = PNHelper.sendGCM(message,
							url, imageUrl, dataInfo, "1",
							credential.getApiKey(), listAndroid);
					JSONObject androidResult = new JSONObject();
					androidResult.put("success", results.get("success"));
					androidResult.put("failed", results.get("failed"));
					if ((int) (results.get("failed")) > 0) {
						List<Map<String, Object>> errorResults = (List<Map<String, Object>>) results
								.get("result");
						List<Map<String, Object>> failedUserList = new ArrayList<Map<String, Object>>();
						for (Map<String, Object> map : errorResults) {
							String token = (String) map.get("token");
							for (Document document : list) {
								String deviceToken = document
										.getString("deviceToken");
								if (token.equals(deviceToken)) {
									String userId = document
											.getString("userId");
									String id = document.getString("_id");
									Map<String, Object> user = new HashMap<String, Object>();
									user.put("userId", userId);
									user.put("id", id);
									user.put("errormsg", map.get("errorMsg"));
									failedUserList.add(user);
									break;
								}
							}
						}
						androidResult.put("failedUser", failedUserList);
					}
					result.put("android", androidResult);
				}

				if (listIOS != null && listIOS.size() > 0) {
					JSONObject iosResult = new JSONObject();

					Query queryFS = new Query(Criteria.where("filename").is(
							credential.getPath()));
					GridFSDBFile fsdbFile = resourceGridFSService
							.querySFDBFileOne(queryFS);
					Map<String, List<PushedNotification>> map = PNHelper
							.sendAPNS(listIOS, fsdbFile.getInputStream(),
									credential.getPassword(),
									credential.getIsProduction(), message,
									dataInfo, silent);

					List<PushedNotification> failedNotifications = map
							.get("failed");
					List<PushedNotification> successfulNotifications = map
							.get("success");

					iosResult.put("success", successfulNotifications.size());
					iosResult.put("failed", failedNotifications.size());
					if (failedNotifications.size() > 0) {
						List<Map<String, Object>> failedUserList = new ArrayList<Map<String, Object>>();
						for (PushedNotification pushedNotification : failedNotifications) {
							String token = pushedNotification.getDevice()
									.getToken();
							for (Document document : list) {
								String deviceToken = document
										.getString("deviceToken");
								if (token.equals(deviceToken)) {
									String userId = document
											.getString("userId");
									String id = document.getString("_id");
									Map<String, Object> user = new HashMap<String, Object>();
									user.put("userId", userId);
									user.put("id", id);
									user.put("errormsg", pushedNotification
											.getException().getMessage());
									failedUserList.add(user);
									break;
								}
							}
						}
						iosResult.put("failedUser", failedUserList);
					}
					result.put("ios", iosResult);
				}

			} else {
				CloudCode code = new CloudCode();
				code.setName("Notification_"
						+ DateQuery.getCurrentTimeInMillis());
				code.setApp(new App(appId));
				json.put("appId", appId);
				code.setCode(buildCode(request, json));
				cloudCodeService.save(code);

				Document schedule = new Document();
				JSONObject app = new JSONObject();
				app.put(MongoDBConstants.DOCUMENT_ID, new ObjectId(appId));
				app.put("ops", false);
				schedule.append("app", app);
				String[] functionIds = new String[] { code.getId() };
				schedule.append("functionIds", Arrays.toString(functionIds));
				schedule.append("name",
						"Schedule_" + DateQuery.getCurrentTimeInMillis());
				schedule.append("rule", json.get("rule"));
				schedule.append("status", 1);
				scheduleService.insertOne("schedule", schedule);
			}

			return ResponseUtils.successWithValue(result);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private String buildCode(HttpServletRequest request, JSONObject json)
			throws Exception {
		String url = getIP(request) + "/1/custDevice/invite";
		json.put("sendNow", true);
		String data = json.toJSONString();
		String callback = "function(data,status){}";
		String code = "function onRequest(request, response, modules) {\n";
		code += "$.post(" + url + "," + data + "," + callback + ");";
		code += "\n}";
		return code;
	}

	public String getIP(HttpServletRequest request) throws Exception {
		String url = configInfo.getEmailServerPath() + request.getContextPath();
		return url;
	}

}
