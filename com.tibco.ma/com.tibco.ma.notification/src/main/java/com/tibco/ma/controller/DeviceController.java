package com.tibco.ma.controller;

import javax.annotation.Resource;

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

import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.App;
import com.tibco.ma.model.Device;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.BaseService;
import com.tibco.ma.service.DeviceService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping("ma/1/device")
public class DeviceController extends BaseController<Device> {

	private static final Logger log = LoggerFactory.getLogger(DeviceController.class);

	@Resource
	private DeviceService service;

	@ApiOperation(value = "get device", notes = "get device")
	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ResponseEntity<?> getOne(@ApiParam(value = "device id") @RequestParam("id") String id) throws Exception {
		try {
			Device device = service.findById(new ObjectId(id), Device.class);
			return ResponseUtils.successWithValue(device);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@ApiOperation(value = "delete device", notes = "delete device")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(@ApiParam(value = "device id") @RequestParam("id") String id) {
		try {
			if (StringUtil.isEmpty(id)) {
				throw new Exception("id is necessary");
			}
			Query query = new Query();
			query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(id)));
			service.delete(query, Device.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}

	}

	@Override
	public BaseService<Device> getService() {
		return service;
	}

	@Override
	public Query getQuery(JSONObject json) {
		Query query = new Query();
		if (json != null && json.containsKey("appID")) {
			query.addCriteria(Criteria.where("app").is(new App((String) json.get("appID"))));
		}
		return query;
	}

	@Override
	public Class<Device> getEntityClass() {
		return Device.class;
	}

	@Override
	public Pager<Device> getPager() {
		Pager<Device> pager = new Pager<Device>();
		pager.setPagesize(20);
		return pager;
	}

	@Override
	public String getCollection() {
		return "device";
	}

}
