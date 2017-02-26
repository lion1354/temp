package com.popular.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.popular.responseutil.ResponseUtils;

/**
 * can access this api via following:<br>
 * http://localhost:8080/appserver/objects.json<br>
 * http://localhost:8080/appserver/objects.xml<br>
 * 
 * 
 * <br>
 * <h1>JSON</h1> <br>
 * curl -i --header "Content-Type: application/json" --header
 * "Accept: application/json" --header "client-agent: 2.0.0.99.AND.xhdpi"
 * --header "request_time: 1372207293819" --header
 * "client_id: 1fa2b759fc7a2cabc496b8f6af88b232" --header
 * "security_token: dbb6de396906fd77f166802dde6c56e2" -X GET --data {}
 * http://localhost:8080/appserver/objects
 * 
 * <br>
 * <h1>XML</h1> <br>
 * curl -i --header "Content-Type: application/xml" --header
 * "Accept: application/xml" --header "client-agent: 2.0.0.99.AND.xhdpi"
 * --header "request_time: 1372207293819" --header
 * "client_id: 1fa2b759fc7a2cabc496b8f6af88b232" --header
 * "security_token: dbb6de396906fd77f166802dde6c56e2" -X GET --data {}
 * http://localhost:8080/appserver/objects
 * 
 * @author quq
 * 
 */
@RestController
public class SampleService {
	private static Logger log = LoggerFactory.getLogger(SampleService.class);

	/**
	 * demo for success and return object array. <br>
	 * curl -i --header "Content-Type: application/json" --header
	 * "Accept: application/json" --header "client-agent: 2.0.0.99.AND.xhdpi"
	 * --header "request_time: 1372207293819" --header
	 * "client_id: 1fa2b759fc7a2cabc496b8f6af88b232" --header
	 * "security_token: dbb6de396906fd77f166802dde6c56e2" -X DELETE --data
	 * {'device':'sdsdfsdfs'} http://localhost:8080/appserver/objects/delete/1
	 * 
	 * @return
	 */
	@RequestMapping(value = "/objects/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		log.info("parameter id: {}", id);

		return ResponseUtils.success();

	}

	/**
	 * demo for success and return object array. <br>
	 * http://localhost:8080/appserver/objects.json?p=xxx
	 * 
	 * @return
	 */
	@RequestMapping(value = "/objects", method = RequestMethod.GET)
	public ResponseEntity<?> objects(String ppp) {
		log.info("parameter p: {}", ppp);

//		Device device = new Device();
//		device.setDevice("ipad");
//		device.setOs("ios");
//
//		List<Device> devices = new ArrayList<>();
//		devices.add(device);
//		devices.add(device);
//		devices.add(device);
//		devices.add(device);
//
//		return ResponseUtils.<Device> successWithValues(devices);
		return null;

	}

	/**
	 * demo for success with one object.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/object", method = RequestMethod.GET)
	public ResponseEntity<?> object() {
//		Device device = new Device();
//		device.setDevice("ipad");
//		device.setOs("ios");
//
//		return ResponseUtils.<Device> successWithValue(device);
		return null;

	}

	/**
	 * demo for put/post.
	 * 
	 * 
	 * <br>
	 * <h1>curl command</h1> <br>
	 * curl -i --header "Content-Type: application/xml" --header
	 * "Accept: application/xml" --header "client-agent: 2.0.0.99.AND.xhdpi"
	 * --header "request_time: 1372207293819" --header
	 * "client_id: 1fa2b759fc7a2cabc496b8f6af88b232" --header
	 * "security_token: dbb6de396906fd77f166802dde6c56e2" -X PUT --data
	 * {'device':'sdsdfsdfs'} http://localhost:8080/appserver/put
	 * 
	 * @return
	 */
	@RequestMapping(value = "/put", method = RequestMethod.PUT)
	public ResponseEntity<?> put(@RequestBody String input) {
		log.info("{}", input);

		return ResponseUtils.successWithStatus(HttpStatus.CREATED);

	}

	/**
	 * demo for error.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public ResponseEntity<?> error() {
//		Device device = new Device();
//		device.setDevice("ipad");
//		device.setOs("ios");
//
//		List<Device> devices = new ArrayList<>();
//		devices.add(device);
		return ResponseUtils.fail("internal server error");

	}

	/**
	 * demo for error code with error code.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/error404", method = RequestMethod.GET)
	public ResponseEntity<?> error404() {
//		Device device = new Device();
//		device.setDevice("ipad");
//		device.setOs("ios");
//
//		List<Device> devices = new ArrayList<>();
//		devices.add(device);
		return ResponseUtils
				.fail("internal server error", HttpStatus.NOT_FOUND);

	}

	@RequestMapping(value = "/customize", method = RequestMethod.GET)
	public ResponseEntity<?> customize() {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("message", "{a:1,b:2}");
//
//		List<FeedGroup> fgs = new ArrayList<>();
//		FeedGroup fg = new FeedGroup();
//		fg.setImgURL("http://www.baidu.com");
//		fg.setName("g1");
//
//		fgs.add(fg);
//		fgs.add(fg);
//
//		map.put("feedGroupList", fgs);
//
//		List<Map<String, Object>> values = new ArrayList<>();
//		return ResponseUtils.successWithValues(values);
		return null;

	}

}
