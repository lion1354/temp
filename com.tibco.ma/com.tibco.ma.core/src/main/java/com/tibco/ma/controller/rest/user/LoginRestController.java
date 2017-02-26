package com.tibco.ma.controller.rest.user;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tibco.ma.common.MaException;
import com.tibco.ma.common.DateQuery;
import com.tibco.ma.common.ResponseErrorCode;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.model.ClientUser;
import com.tibco.ma.service.ClientUserService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/1/user")
public class LoginRestController {
	private static Logger log = LoggerFactory
			.getLogger(LoginRestController.class);

	@Resource
	private ClientUserService userService;

	@ApiOperation(value = "login", notes = "login" ,consumes="application/json",produces="application/json")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> getJSONLogin(
			@ApiParam(value = "app id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String appKey,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "email password") @RequestBody String emailpwd,
			HttpServletRequest request) {
		long t = System.currentTimeMillis();
		try {
			log.info("Login service called at: "
					+ DateQuery.formatDateTime(new Date()));

			String email = "";
			String password = "";

			boolean nonJSONInput = false;

			JSONObject json;
			try {
				json = JSONObject.fromObject(emailpwd);
				email = json.get("email").toString();
				password = json.get("pwd").toString();
			} catch (JSONException e) {
				log.warn("Login input is not JSON!!!");
				nonJSONInput = true;
			}
			if (nonJSONInput) {
				int indexAnd = emailpwd.indexOf("&");
				email = emailpwd.substring(0, indexAnd).split("=")[1];
				password = emailpwd.substring(indexAnd + 1).split("=")[1];
			}
			log.info("Login email: " + email + ", password: " + password);

			// Log the user into tibbr & create session object
			ClientUser user = userService.login(email, password, appKey);
			log.info("login complete! spend "
					+ (System.currentTimeMillis() - t) + " ms.");
			return ResponseUtils.successWithValue(user);
		} catch (MaException e) {
			log.error("Login error!", e);
			return ResponseUtils.fail(ResponseErrorCode.LOGIN_ERROR.value(),
					e.getMessage(), HttpStatus.OK);
		}
	}
}
