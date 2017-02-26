package com.tibco.ma.service;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tibco.ma.model.AdminUser;
import com.tibco.ma.model.App;

public interface AppService extends BaseService<App> {

	List<App> selectByUser(AdminUser user);

	void save(JSONObject input, AdminUser user) throws Exception;

	boolean exists(App app, AdminUser user);

	void saveOrUpdate(String id, String name, String description,
			String iconUrl, AdminUser user) throws Exception;

	String updateImage(String appId, String deleteUrl, CommonsMultipartFile logo)
			throws Exception;
}
