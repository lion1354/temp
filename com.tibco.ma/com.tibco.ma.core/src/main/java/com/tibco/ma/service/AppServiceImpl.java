package com.tibco.ma.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tibco.ma.common.Constants;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.dao.AdminRoleDao;
import com.tibco.ma.dao.AppDao;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.EntityDao;
import com.tibco.ma.model.AdminRole;
import com.tibco.ma.model.AdminUser;
import com.tibco.ma.model.App;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.core.EntityColType;

@Service
public class AppServiceImpl extends BaseServiceImpl<App> implements AppService {

	@Resource
	private AppDao dao;

	@Resource
	private AdminRoleDao roleDao;

	@Resource
	private CoreGridFSService coreGridFSService;

	@Resource
	private AppSettingService appSettingService;

	@Resource
	private EntityService entityService;

	@Resource
	private ValuesService valuesService;

	@Resource
	private EntityDao entityDao;

	@Override
	public BaseDao<App> getDao() {
		return dao;
	}

	@Override
	public List<App> selectByUser(AdminUser user) {
		List<AdminRole> roles = user.getRoles();
		boolean tag = false;
		AdminRole root = roleDao.findOne(
				new Query().addCriteria(Criteria.where("name").is("root")),
				AdminRole.class);
		for (AdminRole role : roles) {
			if (root.getId().equals(role.getId())) {
				tag = true;
				break;
			}
		}
		if (tag) {
			return dao.find(new Query(), App.class);
		} else {
			return dao.find(
					new Query().addCriteria(Criteria.where("user").is(
							new AdminUser(user.getId()))), App.class);
		}
	}

	@Override
	public void save(JSONObject json, AdminUser user) throws Exception {
		if (json.containsKey("id")
				&& StringUtil.notEmpty((String) json.get("id"))) {
			String id = (String) json.get("id");
			App app = dao.findById(new ObjectId(id), App.class);
			Query query = new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(id)));
			Update update = Update.update("name", json.get("name")).set(
					"describe", json.get("describe"));
			if (StringUtil.isEmpty(app.getAccessKey()))
				update.set("accessKey", StringUtil.getUUID());
			if (StringUtil.isEmpty(app.getMasterKey()))
				update.set("masterKey", StringUtil.getUUID());
			if (StringUtil.isEmpty(app.getRestApiKey()))
				update.set("restApiKey", StringUtil.getUUID());
			if (StringUtil.isEmpty(app.getSecretKey()))
				update.set("secretKey", StringUtil.getUUID());
			dao.update(query, update, App.class);
		} else {
			App app = new App();
			app.setName((String) json.get("name"));
			app.setDescribe((String) json.get("describe"));
			app.setUser(user);
			app.setAccessKey(StringUtil.getUUID());
			app.setMasterKey(StringUtil.getUUID());
			app.setRestApiKey(StringUtil.getUUID());
			app.setSecretKey(StringUtil.getUUID());
			dao.save(app);
		}
	}

	@Override
	public boolean exists(App inputApp, AdminUser user) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(inputApp.getName()));
		query.addCriteria(Criteria.where("user").is(user));
		App app = dao.findOne(query, App.class);
		if (app == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void saveOrUpdate(String id, String name, String description,
			String iconUrl, AdminUser user) throws Exception {
		if (StringUtil.notEmpty(id)) {
			App app = dao.findById(new ObjectId(id), App.class);
			Query query = new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(id)));
			Update update = Update.update("name", name).set("describe",
					description);
			update.set("iconUrl", iconUrl);
			if (StringUtil.isEmpty(app.getAccessKey()))
				update.set("accessKey", StringUtil.getUUID());
			if (StringUtil.isEmpty(app.getMasterKey()))
				update.set("masterKey", StringUtil.getUUID());
			if (StringUtil.isEmpty(app.getRestApiKey()))
				update.set("restApiKey", StringUtil.getUUID());
			if (StringUtil.isEmpty(app.getSecretKey()))
				update.set("secretKey", StringUtil.getUUID());
			dao.update(query, update, App.class);

			initUser(id);
			initDevice(id);
		} else {
			App app = new App();
			app.setName(name);
			app.setDescribe(description);
			app.setIconUrl(iconUrl);
			app.setUser(user);
			app.setAccessKey(StringUtil.getUUID());
			app.setMasterKey(StringUtil.getUUID());
			app.setRestApiKey(StringUtil.getUUID());
			app.setSecretKey(StringUtil.getUUID());
			dao.save(app);
			id = app.getId();

			initUser(id);
			initDevice(id);
		}

		// initAppConfig(id, Constants.APP_CONFIG_TIMEZONE,
		// TimeZone.getDefault()
		// .getDisplayName(false, TimeZone.SHORT));
		// initAppConfig(id, Constants.APP_CONFIG_LIMIT_SIZE_KEY,
		// Constants.APP_CONFIG_LIMIT_SIZE);
	}

	private void initUser(String appId) throws Exception {
		String entityCollectionName = entityService
				.getEntityCollectionName(appId);
		Document user = entityService.getEntityByClassName(appId,
				MongoDBConstants.SYSTEM_USER_COLLECTION_NAME);
		if (user == null) {
			List<Document> cols = new ArrayList<Document>();
			cols.add(entityService.createEntityColumn("username",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("password",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("email",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("uniqueKey",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("isActivate",
					EntityColType.Boolean.name()));

			user = new Document();
			user.append(MongoDBConstants.ENTITY_APPID, appId);
			user.append(MongoDBConstants.ENTITY_CLASSNAME,
					MongoDBConstants.SYSTEM_USER_COLLECTION_NAME);
			user.append(MongoDBConstants.ENTITY_COLS, cols);
			user.append(MongoDBConstants.ENTITY_COMMENT, "user");
			user.append(MongoDBConstants.ENTITY_VALUES_COLLECTION,
					valuesService.getValuesCollectionName(appId,
							MongoDBConstants.SYSTEM_USER_COLLECTION_NAME));
			entityService.insertOne(entityCollectionName, user);
		} else {
			List<Document> cols = new ArrayList<Document>();
			cols.add(entityService.createEntityColumn("username",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("password",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("email",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("uniqueKey",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("isActivate",
					EntityColType.Boolean.name()));
			entityService.updateColsById(appId,
					user.getString(MongoDBConstants.DOCUMENT_ID), cols);
		}
	}

	private void initDevice(String appId) throws Exception {
		String entityCollectionName = entityService
				.getEntityCollectionName(appId);
		Document device = entityService.getEntityByClassName(appId,
				MongoDBConstants.SYSTEM_DEVICE_COLLECTION_NAME);
		if (device == null) {
			List<Document> cols = new ArrayList<Document>();
			cols.add(entityService.createEntityColumn("appId",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("deviceId",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("deviceToken",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("isActive",
					EntityColType.Boolean.name()));
			cols.add(entityService.createEntityColumn("os",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("osVersion",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("clientAgent",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("device",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("userId",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("timedAlertStatus",
					EntityColType.Boolean.name()));
			cols.add(entityService.createEntityColumn("arenaAlertStatus",
					EntityColType.Boolean.name()));

			device = new Document();
			device.append(MongoDBConstants.ENTITY_APPID, appId);
			device.append(MongoDBConstants.ENTITY_CLASSNAME,
					MongoDBConstants.SYSTEM_DEVICE_COLLECTION_NAME);
			device.append(MongoDBConstants.ENTITY_COLS, cols);
			device.append(MongoDBConstants.ENTITY_COMMENT, "device");
			device.append(MongoDBConstants.ENTITY_VALUES_COLLECTION,
					valuesService.getValuesCollectionName(appId,
							MongoDBConstants.SYSTEM_DEVICE_COLLECTION_NAME));
			entityService.insertOne(entityCollectionName, device);
		} else {
			List<Document> cols = new ArrayList<Document>();
			cols.add(entityService.createEntityColumn("appId",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("deviceId",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("deviceToken",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("isActive",
					EntityColType.Boolean.name()));
			cols.add(entityService.createEntityColumn("os",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("osVersion",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("clientAgent",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("device",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("userId",
					EntityColType.String.name()));
			cols.add(entityService.createEntityColumn("timedAlertStatus",
					EntityColType.Boolean.name()));
			cols.add(entityService.createEntityColumn("arenaAlertStatus",
					EntityColType.Boolean.name()));
			entityService.updateColsById(appId,
					device.getString(MongoDBConstants.DOCUMENT_ID), cols);
		}
	}

	/**
	 * init App Config
	 * 
	 * @param app
	 * @throws Exception
	 */
	private void initAppConfig(String appId, String key, String value)
			throws Exception {
		// init time zone
		// ConfigProperty configProperty = configPropertyService
		// .queryByKeyAndAppId(Constants.APP_CONFIG_TIMEZONE, appId);
		// if (configProperty == null
		// || StringUtil.isEmpty(configProperty.getValue())) {
		// configProperty = new ConfigProperty();
		// configProperty.setKey(key);// Constants.APP_CONFIG_TIMEZONE
		// configProperty.setValue(value);//
		// TimeZone.getDefault().getDisplayName(false,TimeZone.SHORT)
		// configPropertyService.saveConfigProperty(configProperty, appId);
		// }

		Document doc = appSettingService.queryByIdTypeAndAppId("app_setting",
				appId, "String", Constants.APP_CONFIG_TIMEZONE);
		if (doc == null || StringUtil.isEmpty(doc.getString("value"))) {
			appSettingService.save("app_setting", null, appId, value, "String",
					key, "TIMEZONE");
		}

	}

	@Override
	public String updateImage(String appId, String deleteUrl,
			CommonsMultipartFile logo) throws Exception {
		String iconUrl = null;
		// don't update icon
		if (StringUtil.notEmpty(appId) && StringUtil.isEmpty(deleteUrl)
				&& logo == null) {
			App appOld = findById(new ObjectId(appId), App.class);
			if (appOld != null) {
				iconUrl = appOld.getIconUrl();
			}
		}
		// delete icon
		if (StringUtil.notEmpty(deleteUrl)) {
			coreGridFSService.deleteById(deleteUrl);
			iconUrl = null;
		}
		// update icon
		if (logo != null) {
			iconUrl = coreGridFSService.save(logo.getInputStream(),
					logo.getOriginalFilename());
		}

		return iconUrl;

	}
}
