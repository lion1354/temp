package com.tibco.ma.service;

import org.json.simple.JSONObject;

import com.tibco.ma.model.AdminUser;

public interface AdminUserService extends BaseService<AdminUser>{

	void configRoles(JSONObject json);

	boolean exists(AdminUser inputU);

	void register(AdminUser user);

	void updateUserLoginInfo(AdminUser user);
	
}
