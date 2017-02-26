package com.tibco.ma.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.App;
import com.tibco.ma.model.CloudCode;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.Schedule;
import com.tibco.ma.service.BaseService;
import com.tibco.ma.service.CloudCodeService;
import com.tibco.ma.service.ScheduleService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * 
 * @author aidan
 * 
 *         2015/6/23
 * 
 */
@Controller
@RequestMapping("ma/1/cloudcode")
public class CloudCodeController extends BaseController<CloudCode> {
	private static Logger log = LoggerFactory
			.getLogger(CloudCodeController.class);

	@Resource
	private CloudCodeService service;

	@Resource
	private ConfigInfo configInfo;

	@Resource
	private ScheduleService scheduleService;

	/**
	 * find all functions by appId and functionType
	 * 
	 * @param appId
	 * @return
	 */
	@ApiOperation(value = "get all functions by appId", notes = "get all functions by appId")
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public ResponseEntity<?> all(
			@ApiParam(value = "appId") @RequestParam(value = "appId", required = true) String appId,
			@ApiParam(value = "functionType") @RequestParam(value = "functionType", required = false) String functionType) {

		if (StringUtil.isEmpty(appId))
			return ResponseUtils.fail("AppId is null");
		log.info(appId);

		Query query = new Query();
		query.addCriteria(Criteria.where("app").is(new App(appId)));
//		if (StringUtil.notEmpty(functionType)) {
//			query.addCriteria(Criteria.where("functionType").is(functionType));
//		}

		try {
			List<CloudCode> list = service.find(query, CloudCode.class);
			return ResponseUtils.successWithValues(list);
		} catch (Exception e) {
			log.error("query cloud code error : " + e);
			return ResponseUtils.fail("Query cloud code error : "
					+ e.getMessage());
		}
	}

	/**
	 * find functions by id
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "get one function by CloudCodeId", notes = "get all functions by CloudCodeId")
	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ResponseEntity<?> getOne(
			@ApiParam(value = "cloud code id") @RequestParam(value = "id", required = true) String CloudCodeId) {
		if (StringUtil.isEmpty(CloudCodeId))
			return ResponseUtils.fail("Function id is null");

		try {
			CloudCode code = service.findById(new ObjectId(CloudCodeId),
					CloudCode.class);
			return ResponseUtils.successWithValue(code);
		} catch (Exception e) {
			log.error("query cloud code error : " + e);
			return ResponseUtils.fail("Query cloud code error : "
					+ e.getMessage());
		}
	}

	/**
	 * save functions
	 */
	@ApiOperation(value = "save function", notes = "save function")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "json") @RequestBody JSONObject json) {
		if (!json.has("appId") || StringUtil.isEmpty(json.getString("appId")))
			return ResponseUtils.fail("AppId is null");
		if (json.has("id") && StringUtil.notEmpty(json.getString("id"))) {

			try {
				Query query = new Query();
				query.addCriteria(Criteria.where("_id").is(
						new ObjectId(json.getString("id"))));

				Update update = new Update();
				if (json.has("code")
						&& StringUtil.notEmpty(json.getString("code"))) {
					update.set("code", json.getString("code"));
					service.update(query, update, CloudCode.class);
				}
				if (json.has("name")
						&& StringUtil.notEmpty(json.getString("name"))) {
					CloudCode oldCloudcode = service
							.findById(new ObjectId(json.getString("id")),
									CloudCode.class);
					if (!oldCloudcode.getName().equals(json.getString("name"))) {
						Query queryExist = new Query();
						queryExist.addCriteria(Criteria.where("app").is(
								new App(json.getString("appId"))));
						queryExist.addCriteria(Criteria.where("name").is(
								json.getString("name")));
						CloudCode code = service.findOne(queryExist,
								CloudCode.class);
						if (code != null)
							return ResponseUtils.fail("The name has exist");
						else {
							update.set("name", json.getString("name"));
							service.update(query, update, CloudCode.class);
						}
					}
				}
//				if (json.has("functionType")
//						&& StringUtil.notEmpty(json.getString("functionType"))) {
//					update.set("functionType", json.getString("functionType"));
//					service.update(query, update, CloudCode.class);
//				}

				return ResponseUtils.success();
			} catch (Exception e) {
				log.error("error" + e);
				return ResponseUtils.fail("Error" + e.getMessage());
			}

		} else {
			if (!json.has("name") || StringUtil.isEmpty(json.getString("name")))
				return ResponseUtils.fail("Name is null");
//			if (!json.has("functionType")
//					|| StringUtil.isEmpty(json.getString("functionType")))
//				return ResponseUtils.fail("FunctionType is null");

			Query query = new Query();
			query.addCriteria(Criteria.where("app").is(
					new App(json.getString("appId"))));
			query.addCriteria(Criteria.where("name").is(json.getString("name")));

			try {
				CloudCode code = service.findOne(query, CloudCode.class);
				if (code != null)
					return ResponseUtils.fail("The name has exist");
				else {
					code = new CloudCode();
					code.setName(json.getString("name"));
//					code.setFunctionType(json.getString("functionType"));
					code.setApp(new App(json.getString("appId")));
					service.save(code);
					return ResponseUtils.successWithValue(code);
				}
			} catch (Exception e) {
				log.error("error" + e);
				return ResponseUtils.fail("Error" + e.getMessage());
			}
		}
	}

	@ApiOperation(value = "send function", notes = "send function")
	@RequestMapping(value = "send", method = RequestMethod.POST)
	public ResponseEntity<?> send(
			@ApiParam(value = "json") @RequestBody JSONObject json) {
		if (!json.has("functionId")
				|| StringUtil.isEmpty(json.getString("functionId")))
			return ResponseUtils.fail("Function id is null");

		CloudCode function = null;
		try {
			function = service
					.findById(new ObjectId(json.getString("functionId")),
							CloudCode.class);
		} catch (Exception e1) {
			log.error("find function error : " + e1);
			return ResponseUtils.successWithValue("Function id is incorrect!");
		}

		if (function == null)
			return ResponseUtils.successWithValue("Function id is incorrect!");

		RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
				.setSocketTimeout(1000 * 60 * 3)
				.setConnectTimeout(1000 * 60 * 3).build();

		CloseableHttpClient client = HttpClients
				.custom()
				.setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
				.setKeepAliveStrategy(
						DefaultConnectionKeepAliveStrategy.INSTANCE).build();

		HttpPost post = new HttpPost(configInfo.getNodejsFunctionsUrl()
				+ json.getString("functionId"));

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (json.has("param")) {
			JSONArray param = json.getJSONArray("param");
			if (param != null && param.size() != 0) {
				for (int i = 0; i < param.size(); i++) {
					params.add(new BasicNameValuePair(param.getJSONObject(i)
							.getString("key"), param.getJSONObject(i)
							.getString("value")));

				}
			}
		}

		String result = null;
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(params, "UTF-8");
			post.setEntity(entity);

			HttpResponse httpResponse = client.execute(post);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				result = EntityUtils.toString(httpEntity);
			}

			client.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			result = e.getMessage().toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = e.getMessage().toString();
		} catch (IOException e) {
			e.printStackTrace();
			result = e.getMessage().toString();
		}

		return ResponseUtils.successWithValue(result);
	}

	@ApiOperation(value = "delete cloud_code", notes = "delete cloud_code")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(
			@ApiParam(value = "function id", required = true) @RequestBody(required = true) JSONObject json) {
		if (!json.has("id") || StringUtil.isEmpty(json.getString("id")))
			return ResponseUtils.fail("Function id is null!");

		String id = json.getString("id");
		Query query = new Query();
		query.addCriteria(Criteria.where("functionIds").elemMatch(
				new Criteria().in(id)));

		try {
			List<Schedule> list = scheduleService.find(query, Schedule.class);

			if (list != null && list.size() != 0) {
				return ResponseUtils
						.alert("This function is in used, can't be deleted!");
			}

			query = new Query();
			query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(id));
			
			service.delete(query, CloudCode.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("query schedule error : " + e);
			return ResponseUtils.fail("Query schedule error : " + e);
		}
	}

	@Override
	public BaseService<CloudCode> getService() {
		// TODO Auto-generated method stub
		return service;
	}

	@Override
	public Query getQuery(org.json.simple.JSONObject json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<CloudCode> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pager<CloudCode> getPager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCollection() {
		// TODO Auto-generated method stub
		return "cloud_code";
	}

}
