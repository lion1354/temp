package com.tibco.ma.controller.rest.hybrid;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
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

import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.model.HyBrid;
import com.tibco.ma.service.HyBridService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/1/hybrid")
public class CustomHyBridController {
	private static Logger log = LoggerFactory
			.getLogger(CustomHyBridController.class);
	@Resource
	private HyBridService hyBridService;
	@Resource
	private ConfigInfo configInfo;

	@ApiOperation(value = "get hybrid Url", notes = "get hybrid Url", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "getUrl", method = RequestMethod.POST)
	public ResponseEntity<?> getUrl(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "body") @RequestBody String body,
			HttpServletRequest request) throws Exception {
		Document json = Document.parse(body);
		String name = json.getString("name");
		log.info("get hybrid Url by name, name is " + name);
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("name").is(name));
			query.addCriteria(Criteria.where("appId").is(app_id));
			HyBrid hybrid = hyBridService.findOne(query, HyBrid.class);
			String url = configInfo.getS3BucketUrl() + hybrid.getKey();
			return ResponseUtils.successWithValue(url);
		} catch (Exception e) {
			return ResponseUtils
					.fail("Visit page failed, the error message is "
							+ e.getMessage());
		}
	}

	@ApiOperation(value = "load all hybrids", notes = "load all hybrids", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public ResponseEntity<?> all(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			HttpServletRequest request) throws Exception {
		log.info("load all hybrids");
		Query query = new Query(Criteria.where("appId").is(app_id));
		List<HyBrid> hybrids = hyBridService.find(query, HyBrid.class);
		List<String> hyBridNames = new ArrayList<String>();
		for (HyBrid hybrid : hybrids) {
			hyBridNames.add(hybrid.getName());
		}
		return ResponseUtils.successWithValue(hyBridNames);
	}

}
