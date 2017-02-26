package com.tibco.ma.controller.rest.core;

import net.sf.json.JSONArray;
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

import com.tibco.ma.common.ResponseErrorCode;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.mail.MailUtils;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/1/mailing")
public class CustomerMailingController {

	private static Logger log = LoggerFactory
			.getLogger(CustomerMailingController.class);

	@ApiOperation(value = "save class and values", notes = "save class and values", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "send", method = RequestMethod.POST)
	public ResponseEntity<?> send(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "body") @RequestBody String body)
			throws Exception {

		log.info(body);
		try {
			JSONObject json = JSONObject.fromObject(body);
			if (!json.containsKey("subject"))
				return ResponseUtils.fail(
						ResponseErrorCode.SENDING_MAIL_ERROR.value(),
						"No subject", HttpStatus.OK);
			if (!json.containsKey("message"))
				return ResponseUtils.fail(
						ResponseErrorCode.SENDING_MAIL_ERROR.value(),
						"No message", HttpStatus.OK);
			if (!json.containsKey("addresses"))
				return ResponseUtils.fail(
						ResponseErrorCode.SENDING_MAIL_ERROR.value(), "No addresses",
						HttpStatus.OK);
			String subject = json.getString("subject");
			String message = json.getString("message");
			String addresses = json.getString("addresses");
			System.out.println("json:" + json);
			String[] tos = {};
			Object addObj =  json.get("addresses");
			if(addObj instanceof JSONArray){
				tos = (String[]) ((JSONArray) addObj).toArray(tos);
			}
			if(addObj instanceof String){
				tos = new String[1];
				tos[0] = addresses;
			}
			MailUtils.instance().send(tos, message, subject);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("send mail to many person error : " + e);
			return ResponseUtils.fail(
					ResponseErrorCode.SENDING_MAIL_ERROR.value(),
					"Send mail to many person error : " + e, HttpStatus.OK);
		}
	}
}
