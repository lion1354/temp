package com.tibco.ma.controller;

import java.util.List;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.AdminModule;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.AdminModuleService;
import com.tibco.ma.service.BaseService;

@Controller
@RequestMapping("ma/1/module")
public class AdminModuleController extends BaseController<AdminModule>{
	private static final Logger log = LoggerFactory
			.getLogger(AdminModuleController.class);

	@Resource
	private AdminModuleService moduleService;
	
	@RequestMapping(value = "getOne", method = RequestMethod.GET)
	public ResponseEntity<?> getOne(@RequestParam(value = "id", required = true) String id) {
		try {
			AdminModule module = moduleService.findById(new ObjectId(id), AdminModule.class);
			return ResponseUtils.successWithValue(module);
		} catch (Exception e) {
			return ResponseUtils.fail(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "load", method = RequestMethod.GET)
	public ResponseEntity<?> load() {
		try {
			List<AdminModule> lists = moduleService.find(new Query(),
					AdminModule.class);
			return ResponseUtils.successWithValue(lists);
		} catch (Exception e) {
			return ResponseUtils.fail(e.getMessage());
		}
	}
	

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody JSONObject jsonObject) {
		try {
			moduleService.save(jsonObject);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e.getMessage());
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(@RequestBody JSONObject jsonObject) {
		try {
			if(!jsonObject.containsKey("id")){
				ResponseUtils.fail("ID can't be null");
			}
			Query query = new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(jsonObject.get("id").toString())));
			moduleService.delete(query , AdminModule.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("{}", e.getMessage());
			return ResponseUtils.fail(e.getMessage());
		}
	}

	public String getCollection() {
		return "admin_module";
	}

	@Override
	public BaseService<AdminModule> getService() {
		return moduleService;
	}

	@Override
	public Query getQuery(JSONObject json) {
		return new Query();
	}

	@Override
	public Class<AdminModule> getEntityClass() {
		return AdminModule.class;
	}

	@Override
	public Pager<AdminModule> getPager() {
		Pager<AdminModule> pager = new Pager<AdminModule>();
		pager.setPagesize(20);
		return pager;
	}

}
