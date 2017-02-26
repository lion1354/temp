package com.popular.rest.api;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.popular.model.Message;
import com.popular.responseutil.ResponseUtils;
import com.popular.service.MessageService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.sf.json.JSONObject;

/**
 * 
 * @author Aidan
 *
 * @date 2016/7/21
 *
 */
@Api(value = "message")
@RestController
@RequestMapping("/1/message")
public class MessageController {
	private static Logger log = LoggerFactory
			.getLogger(MessageController.class);

	@Autowired
	private MessageService messageService;

	@ApiOperation(value = "get message by sendId and receiveId", notes = "get message by sendId and receiveId", httpMethod = "GET", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<?> getMessage(
			@ApiParam(name = "message", required = true) @RequestBody(required = true) String message) {
		try {
			JSONObject json = JSONObject.fromObject(message);
			if (!json.containsKey("sendId"))
				return ResponseUtils.fail("sendId is necessary!");
			if (!json.containsKey("receiveId"))
				return ResponseUtils.fail("receiveId is necessary!");
			Integer sendId = Integer.parseInt(json.getString("sendId"));
			Integer receiveId = Integer.parseInt(json.getString("receiveId"));
			List<Message> list = messageService
					.getMessageBySendIdAndReceiveId(sendId, receiveId);
			return ResponseUtils.successWithValue(list);
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@ApiOperation(value = "add message info", notes = "add message info", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addMessage(
			@ApiParam(name = "message", required = true) @RequestBody(required = true) String message) {
		try {
			JSONObject jsonObject = JSONObject.fromObject(message);
			Message obj = (Message) JSONObject.toBean(jsonObject,
					Message.class);
			obj.setSendTime(new Date());
			messageService.add(obj);
			Message msg = messageService.getMessageById(obj.getId());
			return ResponseUtils.successWithValue(msg);
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@ApiOperation(value = "delete message by sendId and receiveId", notes = "delete message by sendId and receiveId", httpMethod = "DELETE", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBySendIdAndReceiveId(
			@ApiParam(name = "message", required = true) @RequestBody(required = true) String message) {
		try {
			JSONObject json = JSONObject.fromObject(message);
			if (!json.containsKey("sendId"))
				return ResponseUtils.fail("sendId is necessary!");
			if (!json.containsKey("receiveId"))
				return ResponseUtils.fail("receiveId is necessary!");
			Integer sendId = Integer.parseInt(json.getString("sendId"));
			Integer receiveId = Integer.parseInt(json.getString("receiveId"));
			messageService.deleteBySendIdAndReceiveId(sendId, receiveId);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@ApiOperation(value = "delete message by id", notes = "delete message by id", httpMethod = "DELETE", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteById(
			@ApiParam(name = "id", required = true) @PathVariable(value = "id") int id) {
		try {
			messageService.deleteById(id);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}
}
