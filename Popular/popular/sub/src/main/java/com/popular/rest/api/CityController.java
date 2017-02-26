package com.popular.rest.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.popular.responseutil.ResponseUtils;
import com.popular.service.CityService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import net.sf.json.JSONArray;

@Api(value = "city")
@RestController
@RequestMapping("/1/city")
public class CityController {
	private static Logger log = LoggerFactory.getLogger(CityController.class);
	
	@Autowired
	private CityService cityService;

	@ApiOperation(value = "city list", notes = "city list", consumes="application/json", produces = "application/json")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<?> getCityList(){
		try {
			return ResponseUtils.successWithValue(JSONArray.fromObject(cityService.getCityGroup()));
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}
}
