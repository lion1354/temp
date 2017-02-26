package com.tibco.ma.controller.rest.analytics;

import javax.annotation.Resource;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tibco.ma.common.ResponseErrorCode;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.AnalyticsService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/1/custAnalytics")
public class RestAnalyticsController {

	private static Logger log = LoggerFactory
			.getLogger(RestAnalyticsController.class);

	@Resource
	private AnalyticsService analyticsService;

	@ApiOperation(value = "save user page analytics data", notes = "save user page analytics data", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "userpage", method = RequestMethod.POST)
	public ResponseEntity<?> saveUserPage(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "json", required = true) @RequestBody(required = true) String json) {

		log.info(json);
		Document document = Document.parse(json);
		document.append("createdatetime", System.currentTimeMillis());
		document.append("appId", app_id);
		try {
			analyticsService.insertOne("user_page_analytics", document);

			Document one = analyticsService.getOne("user_page_analytics",
					document);
			one.put(MongoDBConstants.DOCUMENT_ID, one.getObjectId(MongoDBConstants.DOCUMENT_ID).toString());
			return ResponseUtils.successWithValue(one);
		} catch (Exception e) {
			log.error("save user page analytics data error : " + e);
			return ResponseUtils.fail(
					ResponseErrorCode.ADD_USER_PAGE_ANALYTICS_ERROR.value(),
					"Add user page analytics error : " + e.getMessage(),
					HttpStatus.OK);
		}
	}

	@ApiOperation(value = "save user action analytics data", notes = "save user action analytics data", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "useraction", method = RequestMethod.POST)
	public ResponseEntity<?> saveUserAction(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "json", required = true) @RequestBody(required = true) String json) {
		log.info(json);
		Document document = Document.parse(json);
		document.append("createdatetime", System.currentTimeMillis());
		document.append("appId", app_id);
		try {
			analyticsService.insertOne("user_action_analytics", document);

			Document one = analyticsService.getOne("user_action_analytics",
					document);
			one.put(MongoDBConstants.DOCUMENT_ID, one.getObjectId(MongoDBConstants.DOCUMENT_ID).toString());
			return ResponseUtils.successWithValue(one);
		} catch (Exception e) {
			log.error("save user page analytics data error : " + e);
			return ResponseUtils.fail(
					ResponseErrorCode.ADD_USER_ACTION_ANALYTICS_ERROR.value(),
					"Add user page analytics error : " + e.getMessage(),
					HttpStatus.OK);
		}
	}
}
