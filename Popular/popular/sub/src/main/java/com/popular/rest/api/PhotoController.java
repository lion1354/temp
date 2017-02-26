package com.popular.rest.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.popular.model.Photo;
import com.popular.responseutil.ResponseUtils;
import com.popular.service.PhotoService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Api(value = "photo")
@RestController
@RequestMapping("/1/photo")
public class PhotoController {
	private static Logger log = LoggerFactory.getLogger(PhotoController.class);

	@Autowired
	private PhotoService photoService;

	@ApiOperation(value = "save photo", notes = "save photo", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> savePhoto(HttpServletRequest request,
			@ApiParam(name = "infos", required = true) @RequestBody(required = true) String infos) {
		try {
			List<Photo> photoList = new ArrayList<Photo>();
			log.debug("info : {}", infos);
			JSONObject json = JSONObject.fromObject(infos);
			if (!json.containsKey("userId"))
				return ResponseUtils.fail("userId is necessary!");
			if (!json.containsKey("photos"))
				return ResponseUtils.fail("info is necessary!");
			Integer userId = Integer.parseInt(json.getString("userId"));
			JSONArray array = json.getJSONArray("photos");
			for (int i = 0; i < array.size(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				String photoUrl = jsonObject.getString("photoUrl");
				String des = jsonObject.getString("des");
				Photo photo = new Photo(userId, photoUrl, des);
				photoList.add(photo);
			}
			List<Photo> list = photoService.savePhoto(request, photoList);
			return ResponseUtils.successWithValues(list);
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@ApiOperation(value = "delete photo by userId", notes = "delete photo by userId", httpMethod = "DELETE", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePhotoByUserId(HttpServletRequest request,
			@ApiParam(name = "userId", required = true) @PathVariable(value = "userId") int userId) {
		try {
			log.debug("userId : {}", userId);
			photoService.deletePhotoByUserId(request, userId);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@ApiOperation(value = "delete photo by ids", notes = "delete photo by ids", httpMethod = "DELETE", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePhotoByIds(HttpServletRequest request,
			@ApiParam(name = "ids", required = true) @RequestBody(required = true) String ids) {
		try {
			log.debug("ids : {}", ids);
			List<Integer> list = new ArrayList<Integer>();
			JSONObject jsonObject = JSONObject.fromObject(ids);
			String idsStr = jsonObject.getString("ids");
			String[] array = idsStr.split(",");
			for (String str : array) {
				list.add(Integer.parseInt(str));
			}
			photoService.deletePhotoByIds(request, list);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@ApiOperation(value = "get photo by userId", notes = "get photo by userId", httpMethod = "GET", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/getphotos/{userId}", method = RequestMethod.GET)
	public ResponseEntity<?> getPhotoByUserId(HttpServletRequest request,
			@ApiParam(name = "userId", required = true) @PathVariable(value = "userId") int userId) {
		try {
			List<Photo> list = photoService.getPhotoByUserId(request, userId);
			return ResponseUtils.successWithValues(list);
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@ApiOperation(value = "get photo by id", notes = "get photo by id", httpMethod = "GET", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/getphoto/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getPhotoById(HttpServletRequest request,
			@ApiParam(name = "id", required = true) @PathVariable(value = "id") int id) {
		try {
			Photo photo = photoService.getPhotoById(request, id);
			return ResponseUtils.successWithValue(photo);
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}
}
