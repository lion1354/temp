package com.popular.rest.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.popular.common.AppConfig;
import com.popular.common.sms.SmsUtil;
import com.popular.common.util.StringUtils;
import com.popular.responseutil.ResponseUtils;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.sf.json.JSONObject;

/**
 * 
 * @author Aidan
 *
 *         2016/7/25
 */
@Api(value = "sms")
@RestController
@RequestMapping("/1/sms")
public class SmsController {

	private static Logger log = LoggerFactory.getLogger(SmsController.class);

	@Autowired
	private AppConfig appConfig;

	/**
	 * 短信验证
	 * @param type
	 * @param phoneAndCode
	 * @return
	 */
	@ApiOperation(value = "check sms code", notes = "check sms code", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public ResponseEntity<?> checkCode(@RequestHeader(value = "client_type", required = true) String type,
			@ApiParam(name = "phoneAndCode", required = true) @RequestBody(required = true) String phoneAndCode) {

		try {
			JSONObject param = JSONObject.fromObject(phoneAndCode);
			String phone = param.getString("phoneNumber");
			String code = param.getString("code");
			log.debug(phone + "," + code);
			
			if(StringUtils.isEmpty(type))
				return ResponseUtils.fail("Client type is necessary!");
			if(StringUtils.isEmpty(phone))
				return ResponseUtils.fail("Phone number is necessary!");
			if(StringUtils.isEmpty(code))
				return ResponseUtils.fail("Code is necessary!");
			
			String uri = "https://webapi.sms.mob.com/sms/verify";
			String params = "appkey=" + appConfig.getAppkey_android();

			if (StringUtils.notEmptyAndEqual("ios", type))
				params = "appkey=" + appConfig.getAppkey_ios();

			params += "&amp;phone=" + phone + "&amp;zone=" + appConfig.getZone() + "&amp;&amp;code=" + code;

			JSONObject result = JSONObject.fromObject(SmsUtil.requestData(uri, params));

			if (result == null) {
				return ResponseUtils.fail("Code is wrong!");
			}
			if (200 == result.getInt("status"))
				return ResponseUtils.success();
			else
				return ResponseUtils.fail("Code is wrong!");
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}
}
