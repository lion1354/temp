package com.popular.rest.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.popular.model.Flower;
import com.popular.responseutil.ResponseUtils;
import com.popular.service.FlowerService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.sf.json.JSONObject;

/**
 * 
 * @author Aidan
 *
 * @Date 2016年7月27日
 */
@Api("flower")
@RestController
@RequestMapping("/1/flower")
public class FlowerController {

	private static Logger log = LoggerFactory.getLogger(FlowerController.class);
	
	@Autowired
	private FlowerService flowerService;
	
	/**
	 * 送花
	 * @param flower
	 * @return
	 */
	@ApiOperation(value = "send flower", notes = "send flower", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public ResponseEntity<?> sendFlower(
			@ApiParam(name = "flower", required = true) @RequestBody(required = true) String flower) {
		try {
			Flower info = new Flower(JSONObject.fromObject(flower));
			if(info.getSender() == null)
				return ResponseUtils.fail("sender is necessary!");
			if(info.getRecipient() == null)
				return ResponseUtils.fail("recipient is necessary!");
			flowerService.send(info);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}
	
}
