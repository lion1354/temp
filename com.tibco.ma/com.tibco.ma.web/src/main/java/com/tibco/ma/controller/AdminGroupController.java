package com.tibco.ma.controller;

import java.util.List;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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
import com.tibco.ma.model.AdminGroup;
import com.tibco.ma.model.AdminRole;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.AdminGroupService;
import com.tibco.ma.service.AdminRoleService;
import com.tibco.ma.service.BaseService;

@Controller
@RequestMapping("ma/1/group")
public class AdminGroupController extends BaseController<AdminGroup> {
	private static final Logger log = LoggerFactory
			.getLogger(AdminGroupController.class);

	@Resource
	private AdminGroupService groupService;

	@Resource
	private AdminRoleService roleService;

	@RequestMapping(value = "load", method = RequestMethod.GET)
	public ResponseEntity<?> load() {
		try {
			Query query = new Query();
			query.with(new Sort(new Order("date")));
			List<AdminGroup> lists = groupService.find(query, AdminGroup.class);
			return ResponseUtils.successWithValue(lists);
		} catch (Exception e) {
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "getOne", method = RequestMethod.GET)
	public ResponseEntity<?> getOne(
			@RequestParam(value = "id", required = true) String id) {
		try {
			AdminGroup group = groupService.findById(new ObjectId(id),
					AdminGroup.class);
			return ResponseUtils.successWithValue(group);
		} catch (Exception e) {
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "all", method = RequestMethod.GET)
	public ResponseEntity<?> getAll(
			@RequestParam(value = "role_id", required = true) String role_id) {
		try {
			AdminRole role = roleService.findById(new ObjectId(role_id),
					AdminRole.class);
			List<JSONObject> list = groupService.getGroupAndResByRole(role);
			return ResponseUtils.successWithValue(list);
		} catch (Exception e) {
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody JSONObject jsonObject) {
		try {
			groupService.save(jsonObject);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e.getMessage());
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(@RequestBody JSONObject jsonObject) {
		try {
			if (!jsonObject.containsKey("id")) {
				ResponseUtils.fail("ID can't be null");
			}
			Query query = new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
					new ObjectId(jsonObject.get("id").toString())));
			groupService.delete(query, AdminGroup.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("{}", e.getMessage());
			return ResponseUtils.fail(e.getMessage());
		}
	}

	public String getCollection() {
		return "admin_group";
	}

	@Override
	public BaseService<AdminGroup> getService() {
		return groupService;
	}

	@Override
	public Query getQuery(JSONObject json) {
		Query query = new Query();
		query.with(new Sort(new Order("date")));
		return query;
	}

	@Override
	public Class<AdminGroup> getEntityClass() {
		return AdminGroup.class;
	}

	@Override
	public Pager<AdminGroup> getPager() {
		Pager<AdminGroup> pager = new Pager<AdminGroup>();
		pager.setPagesize(20);
		return pager;
	}

}
