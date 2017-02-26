package com.tibco.ma.controller.rest.user;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tibco.ma.common.Constants;
import com.tibco.ma.common.MaException;
import com.tibco.ma.common.ResponseErrorCode;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.model.ClientUser;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/1/user")
public class FacebookLoginRestController extends
		AbstractThirdPartyLoginRestController {
	private static Logger log = LoggerFactory
			.getLogger(FacebookLoginRestController.class);

	private String access_token = "";

	@ApiOperation(value = "facebook login", notes = "facebook login" ,consumes="application/json",produces="application/json")
	@RequestMapping(value = "/fblogin", method = RequestMethod.POST)
	public ResponseEntity<?> getMyInfo(
			@ApiParam(value = "app id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String appKey,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "input") @RequestBody String input,
			HttpServletRequest request) {
		JSONObject json;
		try {
			json = JSONObject.fromObject(input);
			access_token = json.getString("access_token");
			if (StringUtil.isEmpty(access_token)) {
				return ResponseUtils.fail(
						ResponseErrorCode.NOT_FOUND_ACCESS_TOKEN.value(),
						"Facebook access_token is required!", HttpStatus.OK);
			}
			if (StringUtil.isEmpty(appKey)) {
				return ResponseUtils.fail(
						ResponseErrorCode.NOT_FOUND_APP_KEY.value(),
						"App key is required!", HttpStatus.OK);
			}
			return doLogin(appKey, request);
		} catch (Exception e) {
			log.error("", e);
			return ResponseUtils.fail(
					ResponseErrorCode.FACE_BOOK_LOGIN_ERROR.value(),
					e.getMessage(), HttpStatus.OK);
		}
	}

	@Override
	public ClientUser genUser() throws MaException {
		String fb_id = null, first_name = null, last_name = null;
		RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
				.setSocketTimeout(1000 * 60 * 3).setConnectTimeout(1000 * 60 * 3).build();
		CloseableHttpClient client = HttpClients
				.custom()
				.setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
				.setKeepAliveStrategy(
						DefaultConnectionKeepAliveStrategy.INSTANCE).build();

		HttpGet get = new HttpGet(Constants.FBURLME);
		get.addHeader("Accept", "*/*");
		get.addHeader("Content-Type", "application/json");

		URIBuilder uriBuilder = new URIBuilder(get.getURI()).setParameter(
				"access_token", access_token);

		String output = "";
		try {
			get.setURI(uriBuilder.build());

			try {
				CloseableHttpResponse resp = client.execute(get);
				output = IOUtils.toString(resp.getEntity().getContent());
			} catch (UnsupportedOperationException e) {
				throw new MaException(e);
			} catch (IOException e) {
				throw new MaException(e);
			}
		} catch (URISyntaxException e) {
			throw new MaException(e);
		}

		log.info("Facebook output: " + output);

		JSONObject jsonMe = JSONObject.fromObject(output);
		if (jsonMe.has("id")) {
			fb_id = jsonMe.getString("id");
		} else {
			log.error("there is no key named 'id' in the facebook me info!!!");
			throw new MaException(
					"There is no key named 'id' in the facebook me info!!!");
		}

		if (jsonMe.has("first_name")) {
			first_name = jsonMe.getString("first_name");
		} else {
			first_name = fb_id;
		}
		if (jsonMe.has("last_name")) {
			last_name = jsonMe.getString("last_name");
		} else {
			last_name = fb_id;
		}

		String email = jsonMe.optString("email");
		if (StringUtil.isEmpty(email)) {
			log.warn("No email info from facebook feed!");
			// if the facebook doesn't return the email, use the id to assemble
			// a fake email
			email = fb_id + "_fb@facebook.com";
		}
		ClientUser ret = new ClientUser();
		ret.setEmail(email);
		ret.setFirstname(first_name);
		ret.setLastname(last_name);
		ret.setId(fb_id);
		return ret;
	}

	@Override
	public String thirdpartName() {
		return "facebook";
	}

}
