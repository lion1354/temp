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

import com.tibco.ma.common.DateQuery;
import com.tibco.ma.common.ResponseErrorCode;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.model.ClientUser;
import com.tibco.ma.service.ClientUserService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/1/user")
public class RegisterRestController {
	private static Logger log = LoggerFactory
			.getLogger(RegisterRestController.class);

	@Resource
	private ClientUserService userService;

	@ApiOperation(value = "register", notes = "register" ,consumes="application/json",produces="application/json")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> getJSONRegister(
			@ApiParam(value = "app id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = false) String appKey,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "email password") @RequestBody String emailpwd,
			HttpServletRequest request) {
		try {
			long receivedMS = System.currentTimeMillis();
			log.info("REGISTER REQUEST RECEIVED: "
					+ DateQuery.formatDateTime(new Date()));

			if (StringUtil.isEmpty(appKey)) {
				log.error("app key is necessary!");
				return ResponseUtils.fail(
						ResponseErrorCode.REGISTER_ERROR.value(),
						"App key is necessary!", HttpStatus.OK);
			}

			String email = "";
			String password = "";
			boolean autoActivate = false;

			boolean nonJSONInput = false;

			JSONObject json;
			try {
				json = JSONObject.fromObject(emailpwd);
				email = json.get("email").toString().toLowerCase();
				password = json.get("pwd").toString();
				if (json.has("auto_activate")) {
					autoActivate = StringUtil.notEmptyAndEqual(
							json.optString("auto_activate"), "true") ? true
							: false;
					log.warn("auto_activate: " + autoActivate);
				}
			} catch (JSONException e) {
				log.error("", e);
				nonJSONInput = true;
			}

			if (nonJSONInput) {
				int indexAnd = emailpwd.indexOf("&");
				email = emailpwd.substring(0, indexAnd).split("=")[1];
				log.debug("email: {}", email);
				password = emailpwd.substring(indexAnd + 1).split("=")[1];
			}

			ClientUser user = userService.register(email, password, appKey,
					request);
			long finishedMS = System.currentTimeMillis();
			log.info("RegisterService spend " + (finishedMS - receivedMS)
					+ " ms, and reply OK.");
			return ResponseUtils.successWithValue(user);
		} catch (Exception e) {
			log.error("register Exception: ", e);
			return ResponseUtils.fail(ResponseErrorCode.REGISTER_ERROR.value(),
					e.getMessage(), HttpStatus.OK);
		}
	}
}
