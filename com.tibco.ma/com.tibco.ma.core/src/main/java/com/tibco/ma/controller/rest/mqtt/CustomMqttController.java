package com.tibco.ma.controller.rest.mqtt;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.mqtt.MqttUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/1/mqtt")
public class CustomMqttController {
	private static Logger log = LoggerFactory
			.getLogger(CustomMqttController.class);

	@ApiOperation(value = "mqtt publish", notes = "mqtt publish", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "publish", method = RequestMethod.POST)
	public ResponseEntity<?> publish(
			@ApiParam(value = "app_id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String app_id,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "body") @RequestBody String body,
			HttpServletRequest request) throws Exception {
		Document json = Document.parse(body);
		String topic = json.getString("topic");
		log.info("The publish topic: " + topic);
		if (StringUtil.isEmpty(topic)) {
			return ResponseUtils.fail("The publish topic can't be null!");
		}
		String content = json.getString("content");
		Boolean isRetained = false;
		if (json.containsKey("isRetained")) {
			isRetained = json.getBoolean("isRetained");
		}
		try {
			MqttUtil.instance().publish(topic, content, isRetained);
			return ResponseUtils.success();
		} catch (Exception e) {
			return ResponseUtils.fail("Publish failed, the error message: "
					+ e.getMessage());
		}
	}

}
