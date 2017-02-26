package com.tibco.ma.controller.rest.push;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tibco.ma.common.HtmlUtil;
import com.tibco.ma.common.ResponseErrorCode;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.model.PNTask;
import com.tibco.ma.service.NotificationService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/1/custNotification")
public class CustomerNotificationController {

	private static final Logger log = LoggerFactory
			.getLogger(CustomerNotificationController.class);

	@Resource
	private NotificationService service;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "find result", notes = "find result", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "findRes", method = RequestMethod.GET)
	public ResponseEntity<?> findRes(
			@ApiParam(value = "id") @RequestParam("id") String id)
			throws Exception {
		try {
			JSONObject json = new JSONObject();
			PNTask pnTask = service.findById(new ObjectId(id), PNTask.class);
			String content = HtmlUtil.bodyBuild(null, pnTask.getContent());
			json.put("content", content);
			ResponseEntity<String> re = new ResponseEntity<String>(
					json.toString(), HttpStatus.OK);
			return re;
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(
					ResponseErrorCode.FIND_PNTASK_ERROR.value(),
					e.getMessage(), HttpStatus.OK);
		}
	}

}
