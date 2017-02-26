package com.tibco.ma.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tibco.ma.common.Constants;
import com.tibco.ma.common.MD5Util;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.AdminRole;
import com.tibco.ma.model.AdminUser;
import com.tibco.ma.model.App;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.AdminRoleService;
import com.tibco.ma.service.AdminUserService;
import com.tibco.ma.service.AppService;
import com.tibco.ma.service.BaseService;
import com.tibco.ma.service.CoreGridFSService;
import com.tibco.ma.service.EntityService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping("ma/1/app")
public class AppController extends BaseController<App> {
	private static final Logger log = LoggerFactory.getLogger(AppController.class);

	@Resource
	private AppService appService;

	@Resource
	private AdminRoleService roleService;

	@Resource
	private AdminUserService userService;

	@Resource
	private EntityService entityService;

	@Resource
	private CoreGridFSService coreGridFSService;

	@Override
	public BaseService<App> getService() {
		return appService;
	}

	@ApiOperation(value = "get all app", notes = "get all app")
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public ResponseEntity<?> all(HttpServletRequest request) {
		try {
			String rootPath = request.getContextPath();
			String username = getUserName();
			Query query = new Query();
			query.addCriteria(Criteria.where("username").is(username));
			AdminUser user = null;
			try {
				user = userService.findOne(query, AdminUser.class);
			} catch (Exception e) {
				log.error("{}", e);
			}
			List<App> list = appService.selectByUser(user);
			if (user.getAppIds() != null && user.getAppIds().size() != 0) {
				List<ObjectId> appids = new ArrayList<ObjectId>();
				for (String id : user.getAppIds()) {
					appids.add(new ObjectId(id));
				}
				query = new Query();
				query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).in(appids));
				List<App> apps = appService.find(query, App.class);
				if (apps != null)
					for (App app : apps) {
						app.setIsCollApp("true");
						AdminUser ownUser = userService.findById(new ObjectId(app.getUser().getId()), AdminUser.class);
						app.setOwnUserName(ownUser.getUsername());
						app.setOwnUserEmail(ownUser.getEmail());
						app.setCollEmail(user.getEmail());
					}
				list.addAll(apps);
			}
			for (App app : list) {
				if (StringUtil.notEmpty(app.getIconUrl())) {
					app.setIconUrl(rootPath + Constants.IMAGE_SHOW_GRIDFS_PREFIX + app.getIconUrl());
				}
			}
			return ResponseUtils.successWithValues(list);
		} catch (Exception e) {
			log.error("Error : " + e);
			return ResponseUtils.fail("Error : " + e);
		}
	}

	@ApiOperation(value = "get app by appId", notes = "get app by appId")
	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ResponseEntity<?> getById(
			@ApiParam(value = "app id", required = true) @RequestParam(value = "id", required = true) String appId,
			HttpServletRequest request) {
		App app = null;
		String rootPath = request.getContextPath();
		try {
			app = appService.findById(new ObjectId(appId), App.class);
			if (app != null) {
				if (StringUtil.notEmpty(app.getIconUrl())) {
					app.setIconUrl(rootPath + Constants.IMAGE_SHOW_GRIDFS_PREFIX + app.getIconUrl());
				}
			}
		} catch (Exception e) {
			log.error("{}", e);
		}
		return ResponseUtils.successWithValue(app);
	}

	@Log(operate = "remove collaborators", modelName = "core")
	@ApiOperation(value = "remove collaborators", notes = "remove collaborators", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "removeCollaborators", method = RequestMethod.POST)
	public ResponseEntity<?> removeCollaborators(
			@ApiParam(value = "app id", required = false) @RequestParam(value = "id", required = false) String appId,
			@ApiParam(value = "collaborators email", required = false) @RequestParam(value = "email", required = false) String email) {
		try {
			if (StringUtil.isEmpty(appId))
				return ResponseUtils.alert("No app id");
			if (StringUtil.isEmpty(email))
				return ResponseUtils.alert("No email");

			AdminUser user = userService.findOne(new Query(Criteria.where("email").is(email)), AdminUser.class);
			if (user == null)
				return ResponseUtils.alert("The email is not platform user email!");

			List<String> appIds = new ArrayList<String>();
			if (user.getAppIds() != null && user.getAppIds().size() != 0) {
				appIds = user.getAppIds();
				appIds.remove(appId);
				App app = appService.findById(new ObjectId(appId), App.class);
				if (app.getCollaborators() != null && app.getCollaborators().size() != 0) {
					List<String> colls = app.getCollaborators();
					if (colls.contains(email)) {
						colls.remove(email);
					}
					appService.update(new Query(Criteria.where("id").is(new ObjectId(appId))),
							Update.update("collaborators", colls), App.class);
				}
			} else {
				return ResponseUtils.alert("The collaborator no apps");
			}

			user.setAppIds(appIds);
			userService.update(new Query(Criteria.where("email").is(email)), Update.update("appIds", appIds),
					AdminUser.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("Error : " + e);
			return ResponseUtils.fail("Error :" + e);
		}
	}

	@Log(operate = "add collaborators", modelName = "core")
	@ApiOperation(value = "add collaborators", notes = "add collaborators", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "addCollaborators", method = RequestMethod.POST)
	public ResponseEntity<?> addCollaborators(
			@ApiParam(value = "app id", required = false) @RequestParam(value = "id", required = false) String appId,
			@ApiParam(value = "collaborators email", required = false) @RequestParam(value = "email", required = false) String email) {
		try {
			if (StringUtil.isEmpty(appId))
				return ResponseUtils.alert("No app id");
			if (StringUtil.isEmpty(email))
				return ResponseUtils.alert("No email");
			AdminUser user = userService.findOne(new Query(Criteria.where("email").is(email)), AdminUser.class);
			if (user == null)
				return ResponseUtils.alert("The email is not platform user email!");
			List<String> appIds = new ArrayList<String>();
			if (user.getAppIds() != null && user.getAppIds().size() != 0)
				appIds = user.getAppIds();
			if (appIds.size() != 0) {
				if (!appIds.contains(appId)) {
					appIds.add(appId);
					App app = appService.findById(new ObjectId(appId), App.class);
					if (app.getCollaborators() != null && app.getCollaborators().size() != 0) {
						List<String> colls = app.getCollaborators();
						if (!colls.contains(email)) {
							colls.add(email);
						}
						appService.update(new Query(Criteria.where("id").is(new ObjectId(appId))),
								Update.update("collaborators", colls), App.class);
					} else {
						List<String> colls = new ArrayList<String>();
						colls.add(email);
						appService.update(new Query(Criteria.where("id").is(new ObjectId(appId))),
								Update.update("collaborators", colls), App.class);
					}
				}
			} else {
				appIds.add(appId);
				App app = appService.findById(new ObjectId(appId), App.class);
				if (app.getCollaborators() != null && app.getCollaborators().size() != 0) {
					List<String> colls = app.getCollaborators();
					if (!colls.contains(email)) {
						colls.add(email);
					}
					appService.update(new Query(Criteria.where("id").is(new ObjectId(appId))),
							Update.update("collaborators", colls), App.class);
				} else {
					List<String> colls = new ArrayList<String>();
					colls.add(email);
					appService.update(new Query(Criteria.where("id").is(new ObjectId(appId))),
							Update.update("collaborators", colls), App.class);
				}
			}
			user.setAppIds(appIds);
			userService.update(new Query(Criteria.where("email").is(email)), Update.update("appIds", appIds),
					AdminUser.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("Error : " + e);
			return ResponseUtils.fail("Error :" + e);
		}
	}

	@Log(operate = "save apps", modelName = "core")
	@ApiOperation(value = "save app", notes = "save app")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "app logo", required = false) @RequestParam(value = "logo", required = false) CommonsMultipartFile logo,
			@ApiParam(value = "app id", required = false) @RequestParam(value = "id", required = false) String appId,
			@ApiParam(value = "app name", required = false) @RequestParam(value = "name", required = false) String name,
			@ApiParam(value = "app describe", required = false) @RequestParam(value = "describe", required = false) String describe,
			@ApiParam(value = "app deleteUrl", required = false) @RequestParam(value = "deleteUrl", required = false) String deleteUrl,
			HttpServletRequest request) {
		try {
			String username = getUserName();
			Query query = new Query();
			query.addCriteria(Criteria.where("username").is(username));
			AdminUser user = null;
			try {
				user = userService.findOne(query, AdminUser.class);
				// check app if exists
				if (StringUtil.notEmpty(appId)) {
					// update
					Query query2 = new Query();
					query2.addCriteria(Criteria.where("user").is(new AdminUser(user.getId())));
					query2.addCriteria(Criteria.where("name").is(name));
					App app = appService.findOne(query2, App.class);
					if (app == null || (appId).equals(app.getId())) {
						String iconUrl = null;
						if (StringUtil.notEmpty(deleteUrl)) {

							if (deleteUrl.contains(request.getContextPath() + Constants.IMAGE_SHOW_GRIDFS_PREFIX)) {
								deleteUrl = deleteUrl
										.replace(request.getContextPath() + Constants.IMAGE_SHOW_GRIDFS_PREFIX, "");
							}
						}
						iconUrl = appService.updateImage(appId, deleteUrl, logo);
						appService.saveOrUpdate(appId, name, describe, iconUrl, new AdminUser(user.getId()));
					} else {
						return ResponseUtils.fail("The app is exist");
					}
				} else {
					// save
					App app = new App();
					if (StringUtil.notEmpty(name)) {
						app.setName(name);
					}
					if (appService.exists(app, new AdminUser(user.getId()))) {
						return ResponseUtils.fail("The app is exist");
					} else {
						String iconUrl = null;
						if (logo != null) {
							iconUrl = coreGridFSService.save(logo.getInputStream(), logo.getOriginalFilename());
						}
						appService.saveOrUpdate(appId, name, describe, iconUrl, new AdminUser(user.getId()));
					}
				}
			} catch (Exception e) {
				log.error("{}", e);
				return ResponseUtils.fail(e.getMessage());
			}
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@ApiOperation(value = "reset app masterKey", notes = "reset app masterKey")
	@RequestMapping(value = "reset/masterkey", method = RequestMethod.POST)
	public ResponseEntity<?> resetMasterKey(
			@ApiParam(value = "app json", required = true) @RequestBody(required = true) net.sf.json.JSONObject json) {
		if (StringUtil.isEmpty((String) json.get("id")))
			return ResponseUtils.fail("No id");
		if (StringUtil.isEmpty((String) json.get("password")))
			return ResponseUtils.fail("No password");
		String password = (String) json.get("password");
		String username = getUserName();
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
		AdminUser user = null;
		try {
			user = userService.findOne(query, AdminUser.class);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
		if (!user.getPassword().equals(MD5Util.convertMD5Password(password)))
			return ResponseUtils.alert("The password is not correct!");

		Update update = Update.update("masterKey", StringUtil.getUUID());
		try {
			appService.update(
					new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(json.getString("id")))),
					update, App.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("reset master key error : ", e);
			return ResponseUtils.fail("Reset master key error : " + e.getMessage());
		}
	}

	@Log(operate = "delete apps", modelName = "security")
	@ApiOperation(value = "delete app", notes = "delete app")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(
			@ApiParam(value = "delete json", required = true) @RequestBody(required = true) JSONObject json) {
		try {
			if (StringUtil.isEmpty((String) json.get("id")))
				return ResponseUtils.fail("No id");
			if (StringUtil.isEmpty((String) json.get("password")))
				return ResponseUtils.fail("No password");

			String password = (String) json.get("password");
			String username = getUserName();
			Query query = new Query();
			query.addCriteria(Criteria.where("username").is(username));
			AdminUser user = null;
			try {
				user = userService.findOne(query, AdminUser.class);
			} catch (Exception e) {
				log.error("{}", e);
				return ResponseUtils.fail(e.getMessage());
			}
			if (!user.getPassword().equals(MD5Util.convertMD5Password(password)))
				return ResponseUtils.alert("The password is not correct!");

			List<Document> entitys = entityService.getAllEntityByAppId((String) json.get("id"));
			if (entitys != null && entitys.size() != 0)
				return ResponseUtils.alert("This app is in use, can't be deleted!");
			App app = appService.findOne(
					new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId((String) json.get("id")))),
					App.class);
			String iconUrl = app.getIconUrl();
			if (iconUrl != null) {
				coreGridFSService.deleteSFDBFile(new Query()
						.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(iconUrl))));
			}

			appService.delete(
					new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId((String) json.get("id")))),
					App.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}

	}

	@Override
	public Query getQuery(JSONObject json) {
		String username = getUserName();
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
		AdminUser user = null;
		try {
			user = userService.findOne(query, AdminUser.class);
		} catch (Exception e) {
			log.error("{}", e);
		}
		List<AdminRole> roles = user.getRoles();
		boolean tag = false;
		AdminRole root = null;
		try {
			root = roleService.findOne(new Query().addCriteria(Criteria.where("name").is("root")), AdminRole.class);
		} catch (Exception e) {
			log.error("{}", e);
		}
		for (AdminRole role : roles) {
			if (root.getId().equals(role.getId())) {
				tag = true;
				break;
			}
		}
		if (tag) {
			return new Query();
		} else {
			return new Query().addCriteria(Criteria.where("user").is(new AdminUser(user.getId())));
		}
	}

	@Override
	public Class<App> getEntityClass() {
		return App.class;
	}

	@Override
	public Pager<App> getPager() {
		Pager<App> pager = new Pager<App>();
		pager.setPagesize(20);
		return pager;
	}

	@Override
	public String getCollection() {
		return "app";
	}

	@Override
	public void otherOperation(HttpServletRequest request, Pager<App> pager) throws Exception {
		String username = getUserName();
		for (App app : pager.getData()) {
			try {
				AdminUser u = userService.findById(new ObjectId(app.getUser().getId()), AdminUser.class);
				app.setUser(u);
				if (u.getUsername().equals(username)) {
					app.setOps(true);
				} else {
					app.setOps(false);
				}
			} catch (Exception e) {
				log.error("{}", e);
			}
		}
	}

}
