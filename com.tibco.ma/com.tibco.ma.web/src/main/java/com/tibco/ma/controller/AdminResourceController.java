package com.tibco.ma.controller;

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
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.AdminResources;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.AdminMenuService;
import com.tibco.ma.service.AdminResourceService;
import com.tibco.ma.service.BaseService;

@Controller
@RequestMapping(value = "ma/1/resource")
public class AdminResourceController extends BaseController<AdminResources> {

	private static Logger log = LoggerFactory
			.getLogger(AdminResourceController.class);

	@Resource
	private AdminMenuService menuService;

	@Resource
	private AdminResourceService resourceService;

	@RequestMapping(value = "get/one", method = RequestMethod.GET)
	public ResponseEntity<?> getById(
			@RequestParam(value = "id", required = true) String id) {
		AdminResources resource = null;
		try {
			resource = resourceService.findById(new ObjectId(id),
					AdminResources.class);
		} catch (Exception e) {
			log.error("{}", e);
		}
		return ResponseUtils.successWithValue(resource);
	}

	@Log(operate="save resources", modelName="security")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody(required = true) JSONObject json)
			throws Exception {
		try {
			if (json.containsKey("id")
					&& StringUtil.notEmpty(json.get("id").toString())) {
				//update modify by kalen at 2015.06.01
				Query query2 = new Query();
				query2.addCriteria(Criteria.where("name").is((String)json.get("name")));
				query2.addCriteria(Criteria.where("group_id").is((String)json.get("group_id")));
				AdminResources resources = resourceService.findOne(query2, AdminResources.class);
				if(resources==null || json.get("id").toString().equals(resources.getId())) {
					resourceService.save(json);
				}else {
					return ResponseUtils.fail("the resources is exist");
				}
			}else {
				//save  modify by kalen at 2015.06.01
				AdminResources resources = new AdminResources();
				resources.setGroup_id((String)json.get("group_id"));
				if(json.containsKey("name")) {
					resources.setName(json.get("name").toString());;
				}
				if(resourceService.exists(resources)) {
					return ResponseUtils.fail("the resources is exist");
				} else {
					resourceService.save(json);
				}
			}
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@Log(operate="delete resources", modelName="security")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(
			@RequestBody(required = true) JSONObject json) {
		try {
			resourceService.delete(
					new Query().addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
							new ObjectId((String)json.get("id")))),
					AdminResources.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@Override
	public BaseService<AdminResources> getService() {
		return resourceService;
	}

	@Override
	public Query getQuery(JSONObject json) {
		Query query = new Query();
		return query;
	}

	@Override
	public Class<AdminResources> getEntityClass() {
		return AdminResources.class;
	}

	@Override
	public Pager<AdminResources> getPager() {
		Pager<AdminResources> pager = new Pager<AdminResources>();
		pager.setPagesize(20);
		return pager;
	}

	@Override
	public String getCollection() {
		return "admin_resources";
	}

}
