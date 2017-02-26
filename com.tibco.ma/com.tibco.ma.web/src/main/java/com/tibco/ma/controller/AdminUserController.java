package com.tibco.ma.controller;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tibco.ma.common.MD5Util;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.AdminUser;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.AdminUserService;
import com.tibco.ma.service.BaseService;

@Controller
@RequestMapping("ma/1/user")
public class AdminUserController extends BaseController<AdminUser> {

	private static final Logger log = LoggerFactory
			.getLogger(AdminUserController.class);

	@Resource
	private AdminUserService service;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getUserName", method = RequestMethod.GET)
	public ResponseEntity<?> getName() {
		try {
			JSONObject json = new JSONObject();
			String username = super.getUserName();
			json.put("username", username);
			json.put("status", "OK");
			return ResponseUtils.successWithValue(json);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@Log(operate = "save user roles", modelName = "security")
	@RequestMapping(value = "configroles", method = RequestMethod.POST)
	public ResponseEntity<?> configroles(
			@RequestBody(required = true) JSONObject json) {
		try {
			service.configRoles(json);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ResponseEntity<?> getOne(@RequestParam("id") String id)
			throws Exception {
		try {
			AdminUser user = service
					.findById(new ObjectId(id), AdminUser.class);
			return ResponseUtils.successWithValue(user);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@Log(operate = "delete user", modelName = "security")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(
			@RequestBody(required = true) JSONObject json) {
		JSONObject ret = new JSONObject();

		try {
			if (StringUtil.isEmpty((String) json.get("id"))) {
				log.error("id is necessary");
				throw new Exception("Id is necessary");
			}
			AdminUser user = service.findById(
					new ObjectId((String) json.get("id")), AdminUser.class);
			if (user != null && user.getRoles() != null) {
				ret.put("error", "user binding the roles");
			} else {
				Query query = new Query();
				query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
						new ObjectId((String) json.get("id"))));
				service.delete(query, AdminUser.class);
				ret.put("result", "success");
			}
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
		return ResponseUtils.successWithValue(ret.toJSONString());
	}

	@Log(operate = "save user", modelName = "security")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody JSONObject json) {// @ModelAttribute("user")
																	// user

		try {
			AdminUser user = new AdminUser();

			if (json.containsKey("username"))
				user.setUsername((String) json.get("username"));
			if (json.containsKey("password"))
				user.setPassword(MD5Util.convertMD5Password((String) json
						.get("password")));
			if (json.containsKey("email"))
				user.setEmail((String) json.get("email"));
			if (!json.containsKey("id")
					|| StringUtil.isEmpty((String) json.get("id"))) {
				// save
				if (service.exists(user)) {
					return ResponseUtils.fail("The user is exist");
				} else {
					service.save(user);
				}
			} else {
				// update
				Query query2 = new Query();
				query2.addCriteria(Criteria.where("username").is(
						(String) json.get("username")));
				AdminUser u = service.findOne(query2, AdminUser.class);
				if (u == null || ((String) json.get("id")).equals(u.getId())) {
					Query query = new Query();
					query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
							new ObjectId((String) json.get("id"))));
					Update update = Update.update("username",
							user.getUsername()).set("email", user.getEmail());
					service.update(query, update, AdminUser.class);
				} else {
					return ResponseUtils.fail("The user is exist");
				}
			}
		} catch (Exception e) {
			log.error("{}", e);
			ResponseUtils.fail(e.getMessage());
		}

		return ResponseUtils.success();
	}

	public static void main(String[] args) {
		System.out.println(MD5Util.convertMD5Password("1"));
		;
	}

	@Override
	public BaseService<AdminUser> getService() {
		return service;
	}

	@Override
	public Query getQuery(JSONObject json) {
		Query query = new Query();
		return query;
	}

	@Override
	public Class<AdminUser> getEntityClass() {
		return AdminUser.class;
	}

	@Override
	public Pager<AdminUser> getPager() {
		Pager<AdminUser> pager = new Pager<AdminUser>();
		pager.setPagesize(20);
		return pager;
	}

	@Override
	public String getCollection() {
		return "admin_user";
	}

	@RequestMapping(value = "findByUsername", method = RequestMethod.POST)
	public ResponseEntity<?> findByUsername(
			@RequestBody net.sf.json.JSONObject json) {
		if (!json.has("username"))
			return ResponseUtils.fail("Username is null");
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(
				(String) json.get("username")));
		AdminUser user = null;
		try {
			user = service.findOne(query, AdminUser.class);
		} catch (Exception e) {
			return ResponseUtils.fail("Username is incorrect");
		}
		if (user == null)
			return ResponseUtils.fail("Username is incorrect");
		return ResponseUtils.successWithValue(user);
	}

	@RequestMapping(value = "updateByUsername", method = RequestMethod.POST)
	public ResponseEntity<?> updateByUsername(
			@RequestBody net.sf.json.JSONObject json) {

		if (!json.has("username"))
			return ResponseUtils.fail("Username is null");
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(
				(String) json.get("username")));
		AdminUser user = null;
		try {
			user = service.findOne(query, AdminUser.class);
		} catch (Exception e) {
			return ResponseUtils.fail("Username is incorrect");
		}
		if (user == null)
			return ResponseUtils.fail("Username is incorrect");

		Update update = null;
		if(json.has("telephone"))
			update = Update.update("telephone", json.getString("telephone"));
		if(json.has("address"))
			update = update.set("address", json.getString("address"));
		if(json.has("zip"))
			update = update.set("zip", json.getString("zip"));
		if(json.has("company"))
			update = update.set("company", json.getString("company"));
		
		try {
			service.update(query, update, AdminUser.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("update error", e);
			return ResponseUtils.fail("Update error" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "changePWD", method = RequestMethod.POST)
	public ResponseEntity<?> resetPWD(@RequestBody net.sf.json.JSONObject json) {
		String newPWD = null;
		if(json.has("newPWD"))
			newPWD = json.getString("newPWD");
		else
			return ResponseUtils.fail("NewPWD id is null");
		String reNewPWD = null;
		if(json.has("reNewPWD"))
			reNewPWD = json.getString("reNewPWD");
		else
			return ResponseUtils.fail("ReNewPWD id is null");
		
		AdminUser user = null;
		if(json.has("username")){
			String username = json.getString("username");
			Query query = new Query();
			query.addCriteria(Criteria.where("username").is(username));
			
			try {
				user = service.findOne(query, AdminUser.class);
			} catch (Exception e) {
				log.error("get user error:", e);
				return ResponseUtils.fail("Username is incorrect");
			}
			
			String oldPWD = null;
			if(json.has("oldPWD"))
				oldPWD = json.getString("oldPWD");
			else
				return ResponseUtils.fail("OldPWD is null");
			
			if(!MD5Util.convertMD5Password(oldPWD).equals(user.getPassword()))
				return ResponseUtils.fail("OldPWD is incorrect");
			
			if(!newPWD.equals(reNewPWD))
				return ResponseUtils.fail("Two times the password is different");
			
			Update update = Update.update("password", MD5Util.convertMD5Password(newPWD));
			try {
				service.update(query, update, AdminUser.class);
				return ResponseUtils.success();
			} catch (Exception e) {
				log.error("reset password error:", e);
				return ResponseUtils.fail("Reset password error :" + e.getMessage());
			}
			
		} else {
			return ResponseUtils.fail("Username is null");
		}
	}
}
