package com.tibco.ma.controller;

import java.util.ArrayList;
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
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.AdminRole;
import com.tibco.ma.model.AdminUser;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.AdminRoleService;
import com.tibco.ma.service.AdminUserService;
import com.tibco.ma.service.BaseService;

@Controller
@RequestMapping(value = "ma/1/role")
public class AdminRoleController extends BaseController<AdminRole> {

	private static Logger log = LoggerFactory.getLogger(AdminRoleController.class);

	@Resource
	private AdminRoleService roleService;

	@Resource
	private AdminUserService userService;

	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ResponseEntity<?> getById(
			@RequestParam(value = "id", required = true) String id) {
		AdminRole role = null;
		try {
			role = roleService.findById(new ObjectId(id), AdminRole.class);
		} catch (Exception e) {
			log.error("{}", e);
		}
		return ResponseUtils.successWithValue(role);
	}

	@Log(operate="save auth", modelName="security")
	@RequestMapping(value = "auth", method = RequestMethod.POST)
	public ResponseEntity<?> saveAuth(@RequestBody JSONObject json) {
		try {
			roleService.saveAuth(json);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@Log(operate="save role", modelName="security")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody JSONObject json) {
		try {
			if (json.containsKey("id")
					&& StringUtil.notEmpty(json.get("id").toString())) {
				Query query2 = new Query();
				query2.addCriteria(Criteria.where("name").is(
						(String) json.get("name")));
				AdminRole role = roleService.findOne(query2, AdminRole.class);
				if (role == null || json.get("id").toString().equals(role.getId())) {
					roleService.update(json);
				} else {
					return ResponseUtils.fail("The role is exist");
				}
			} else {
				AdminRole role = new AdminRole();
				if(json.containsKey("name")) {
					role.setName((String)json.get("name"));
				}
				if(roleService.exists(role)) {
					return ResponseUtils.fail("The role is exist");
				} else {
					roleService.save(json);
				}
			}
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "get/user", method = RequestMethod.GET)
	public ResponseEntity<?> getByUser(
			@RequestParam(value = "id", required = true) String user_id) {
		try {
			AdminUser user = userService.findById(new ObjectId(user_id), AdminUser.class);
			List<AdminRole> roles = user.getRoles();
			List<AdminRole> list = roleService.find(new Query(), AdminRole.class);
			for (AdminRole role : list) {
				if (isContains(roles, role))
					role.setChecked(true);
				else
					role.setChecked(false);
			}
			return ResponseUtils.successWithValues(list);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@Log(operate="delete roles", modelName="security")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(
			@RequestBody(required = true) JSONObject json) {
		try {
			if (StringUtil.isEmpty(json.get("id").toString()))
				return ResponseUtils.fail("No id");
			AdminRole role = roleService.findById(new ObjectId(json.get("id")
					.toString()), AdminRole.class);
			List<AdminUser> users = userService.find(new Query(), AdminUser.class);
			List<AdminRole> useRoles = new ArrayList<AdminRole>();
			for (AdminUser user : users) {
				if (user.getRoles() != null && user.getRoles().size() != 0)
					useRoles.addAll(user.getRoles());
			}
			if (isContains(useRoles, role))
				return ResponseUtils.fail("Had use");
			roleService.delete(
					new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
							new ObjectId(json.get("id").toString()))),
					AdminRole.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	private boolean isContains(List<AdminRole> roles, AdminRole role) {
		if (roles == null || roles.size() == 0)
			return false;
		boolean tag = false;
		for (AdminRole r : roles) {
			if (r.getId().equals(role.getId())) {
				tag = true;
				break;
			}
		}
		return tag;
	}

	@Override
	public BaseService<AdminRole> getService() {
		return roleService;
	}

	@Override
	public Query getQuery(JSONObject json) {
		Query query = new Query();
		return query;
	}

	@Override
	public Class<AdminRole> getEntityClass() {
		return AdminRole.class;
	}

	@Override
	public Pager<AdminRole> getPager() {
		Pager<AdminRole> pager = new Pager<AdminRole>();
		pager.setPagesize(20);
		return pager;
	}

	@Override
	public String getCollection() {
		return "admin_role";
	}

}
