package com.tibco.ma.controller.rest.nodejs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.ResponseErrorCode;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.model.App;
import com.tibco.ma.model.CloudCode;
import com.tibco.ma.service.CloudCodeService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
@RequestMapping("/1/functions")
public class NodejsController {
	private static Logger log = LoggerFactory.getLogger(NodejsController.class);

	@Resource
	private ConfigInfo configInfo;

	@Resource
	private CloudCodeService cloudCodeService;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "find function", notes = "find function", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/{functionName}", method = RequestMethod.POST)
	public ResponseEntity<?> eval(
			@ApiParam(value = "app id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_key,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "request_time", required = false) @RequestHeader(value = "request_time", required = false) String request_time,
			@ApiParam(value = "client_id", required = false) @RequestHeader(value = "client_id", required = false) String client_id,
			@ApiParam(value = "client-agent", required = false) @RequestHeader(value = "client-agent", required = false) String client_agent,
			@ApiParam(value = "function name") @PathVariable(value = "functionName") String functionName,
			@ApiParam(value = "input") @RequestBody String input) {
		if (StringUtil.isEmpty(functionName))
			return ResponseUtils.fail(
					ResponseErrorCode.EVAL_CLOUDCODE_ERROR.ordinal(),
					"Function name is null", HttpStatus.OK);
		
		Query query = new Query();
		query.addCriteria(Criteria.where("app").is(new App(app_key)));
		query.addCriteria(Criteria.where("name").is(functionName));
		CloudCode function = null;
		try {
			function = cloudCodeService.findOne(query, CloudCode.class);
		} catch (Exception e1) {
			log.error("find function error : " + e1);
			return ResponseUtils.fail(
					ResponseErrorCode.EVAL_CLOUDCODE_ERROR.ordinal(),
					"Function name is incorrect!", HttpStatus.OK);
		}

		if (function == null)
			return ResponseUtils.fail(
					ResponseErrorCode.EVAL_CLOUDCODE_ERROR.ordinal(),
					"Function name is incorrect!", HttpStatus.OK);

		JSONObject json = JSONObject.fromObject(input);
		RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
				.setSocketTimeout(1000 * 60 * 3).setConnectTimeout(1000 * 60 * 3).build();

		CloseableHttpClient client = HttpClients
				.custom()
				.setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
				.setKeepAliveStrategy(
						DefaultConnectionKeepAliveStrategy.INSTANCE).build();

		HttpPost post = new HttpPost(configInfo.getNodejsFunctionsUrl()
				+ function.getId());

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Iterator<?> iterator = json.keys(); iterator.hasNext();) {
			String key = iterator.next().toString();
			params.add(new BasicNameValuePair(key, json.getString(key)));
		}
		
		Map<String, String> config = new HashMap<String, String>();
		config.put("X-MA-Application-Id", app_key);
		config.put("X-MA-REST-API-Key", rest_api_key);
		if(StringUtil.notEmpty(request_time))
			config.put("request_time", request_time);
		if(StringUtil.notEmpty(client_id))
			config.put("client_id", client_id);
		if(StringUtil.notEmpty(client_agent))
			config.put("client_agent", client_agent);
		
		params.add(new BasicNameValuePair("config", JSONObject.fromObject(config).toString()));

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
			if(!result.equals("")){
				JSONObject resObj = JSONObject.fromObject(result);
				if(resObj.containsKey("value")){
					result = resObj.get("value").toString();
					return ResponseUtils.successWithValue(JSONObject.fromObject(result));
				} else if(resObj.containsKey("values")){
					result = resObj.get("values").toString();
					return ResponseUtils.successWithValues(JSONArray.fromObject(result));
				} else {
					return ResponseUtils.successWithValue(JSONObject.fromObject(result));
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			result = e.getMessage().toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = e.getMessage().toString();
		} catch (IOException e) {
			e.printStackTrace();
			result = e.getMessage().toString();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return ResponseUtils.successWithValue(result);
	}
}
