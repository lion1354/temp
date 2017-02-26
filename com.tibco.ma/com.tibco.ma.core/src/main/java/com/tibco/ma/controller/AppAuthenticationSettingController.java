package com.tibco.ma.controller;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.AppAuthenticationSetting;
import com.tibco.ma.service.AppAuthenticationSettingService;
import com.tibco.ma.service.BaseService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * 
 * @author aidan
 * 
 *         2015/7/3
 * 
 */
@Controller
@RequestMapping("ma/1/appauthenticationsetting")
public class AppAuthenticationSettingController extends BaseController<AppAuthenticationSetting> {
	private static Logger log = LoggerFactory.getLogger(AppAuthenticationSettingController.class);

	@Resource
	private AppAuthenticationSettingService service;

	@ApiOperation(value = "get app authentication by appId", notes = "get app authentication by appId")
	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ResponseEntity<?> get(@ApiParam(value = "app id") @RequestParam String appId) {
		if (StringUtil.isEmpty(appId))
			return ResponseUtils.fail("AppId is null");
		log.info(appId);

		Query query = new Query();
		query.addCriteria(Criteria.where("appId").is(appId));

		try {
			AppAuthenticationSetting setting = service.findOne(query, AppAuthenticationSetting.class);
			return ResponseUtils.successWithValue(setting);
		} catch (Exception e) {
			log.error("query AppAuthenticationSetting error : " + e);
			return ResponseUtils.fail("Query App Authentication Setting error : " + e.getMessage());
		}
	}

	@ApiOperation(value = "save app authentication", notes = "save app authentication")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "json", required = true) @RequestBody(required = true) JSONObject json) {
		if (!json.has("appId") || StringUtil.isEmpty(json.getString("appId")))
			return ResponseUtils.fail("AppId is null");

		Document query = new Document();
		query.append("appId", json.getString("appId"));

		Document setting = null;
		try {
			setting = service.getOne(getCollection(), query);

			if (setting == null) {
				setting = new Document();
				setting.append("appId", json.getString("appId"));
				if (json.has("facebookApplications"))
					setting.append("facebookApplications", json.getJSONArray("facebookApplications"));
				if (json.has("isAllowFacebookAuthentication"))
					setting.append("isAllowFacebookAuthentication", json.getBoolean("isAllowFacebookAuthentication"));
				if (json.has("twitterConsumerKeys") && StringUtil.notEmpty(json.getString("twitterConsumerKeys")))
					setting.append("twitterConsumerKeys", json.getString("twitterConsumerKeys"));
				if (json.has("isAllowTwitterAuthentication"))
					setting.append("isAllowTwitterAuthentication", json.getBoolean("isAllowTwitterAuthentication"));
				service.insertOne(getCollection(), setting);
			} else {
				setting = new Document();
				if (json.has("facebookApplications"))
					setting.append("facebookApplications", json.getJSONArray("facebookApplications"));
				if (json.has("isAllowFacebookAuthentication"))
					setting.append("isAllowFacebookAuthentication", json.getBoolean("isAllowFacebookAuthentication"));
				if (json.has("twitterConsumerKeys") && StringUtil.notEmpty(json.getString("twitterConsumerKeys")))
					setting.append("twitterConsumerKeys", json.getString("twitterConsumerKeys"));
				if (json.has("isAllowTwitterAuthentication"))
					setting.append("isAllowTwitterAuthentication", json.getBoolean("isAllowTwitterAuthentication"));
				service.updateMany(getCollection(), query, setting);
			}

			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("save AppAuthenticationSetting error : " + e);
			return ResponseUtils.fail("Save AppAuthenticationSetting error : " + e.getMessage());
		}
	}

	@Override
	public BaseService<AppAuthenticationSetting> getService() {
		// TODO Auto-generated method stub
		return service;
	}

	@Override
	public Query getQuery(org.json.simple.JSONObject json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<AppAuthenticationSetting> getEntityClass() {
		// TODO Auto-generated method stub
		return AppAuthenticationSetting.class;
	}

	@Override
	public Pager<AppAuthenticationSetting> getPager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCollection() {
		// TODO Auto-generated method stub
		return "app_authentication_setting";
	}

}
