package com.tibco.ma.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.tibco.ma.common.Constants;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.NotificationRes;
import com.tibco.ma.service.BaseService;
import com.tibco.ma.service.NotificationResService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping("ma/1/notificationRes")
public class NotificationResController extends BaseController<NotificationRes> {

	private static final Logger log = LoggerFactory.getLogger(NotificationResController.class);

	@Resource
	private NotificationResService service;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "result view", notes = "result view")
	@RequestMapping(value = "resView", method = RequestMethod.GET)
	public ResponseEntity<?> resView(@ApiParam(value = "type") @RequestParam("type") String type,
			HttpServletRequest request) throws Exception {

		JSONObject json = new JSONObject();
		Query query = new Query();
		if (StringUtil.notEmpty(type)) {
			query.addCriteria(Criteria.where("type").is(type));
		}
		List<NotificationRes> list = service.find(query, NotificationRes.class);
		String current_url = request.getContextPath() + Constants.IMAGE_NOTIFICATION_SHOWRES;

		json.put("current_die_path", null);
		json.put("current_url", current_url);

		Map<String, Object> map = null;
		List<Map<String, Object>> file_list = new ArrayList<Map<String, Object>>();
		for (NotificationRes notificationRes : list) {
			map = new HashMap<String, Object>();
			map.put("datetime", notificationRes.getTime());
			map.put("dir_path", null);
			map.put("filename", notificationRes.getName());
			map.put("filesize", notificationRes.getSize());
			map.put("filetype", notificationRes.getFiletype());
			map.put("fileUrl", notificationRes.getUrl());
			map.put("has_file", false);
			map.put("is_dir", false);
			if ("video".equals(notificationRes.getType())) {
				map.put("is_photo", false);
			} else {
				map.put("is_photo", true);
			}
			file_list.add(map);
		}
		json.put("file_list", file_list);
		ResponseEntity<String> re = new ResponseEntity<String>(json.toString(), HttpStatus.OK);
		return re;
	}

	@ApiOperation(value = "get one", notes = "get one")
	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ResponseEntity<?> getOne(@ApiParam(value = "id") @RequestParam("id") String id) throws Exception {
		try {
			NotificationRes notificationRes = service.findById(new ObjectId(id), NotificationRes.class);
			return ResponseUtils.successWithValue(notificationRes);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@ApiOperation(value = "delete", notes = "delete")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(HttpServletRequest request, @ApiParam(value = "id") @RequestParam("id") String id) {
		try {
			if (StringUtil.isEmpty(id)) {
				throw new Exception("id is necessary");
			}
			service.deleteResourceByID(id);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}

	}

	@ApiOperation(value = "upload", notes = "upload")
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public ResponseEntity<?> upload(HttpServletRequest request, HttpServletResponse response) {
		try {
			NotificationRes res = new NotificationRes();
			res.setType(request.getParameter("resType"));

			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
					request.getServletContext());
			if (commonsMultipartResolver.isMultipart(request)) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						service.saveResource(file, res);
					}
				}
			}
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@Override
	public BaseService<NotificationRes> getService() {
		return service;
	}

	@Override
	public Query getQuery(JSONObject json) {
		Query query = new Query();
		return query;
	}

	@Override
	public Class<NotificationRes> getEntityClass() {
		return NotificationRes.class;
	}

	@Override
	public Pager<NotificationRes> getPager() {
		Pager<NotificationRes> pager = new Pager<NotificationRes>();
		pager.setPagesize(20);
		return pager;
	}

	@Override
	public String getCollection() {
		return "notificationRes";
	}

	@Override
	public void otherOperation(HttpServletRequest request, Pager<NotificationRes> pager) throws Exception {
		List<NotificationRes> list = pager.getData();
		for (NotificationRes notificationRes : list) {
			String url = "notificationRes" + File.separator + notificationRes.getName();
			notificationRes.setUrl(url);
		}
		pager.setData(list);
	}
}
