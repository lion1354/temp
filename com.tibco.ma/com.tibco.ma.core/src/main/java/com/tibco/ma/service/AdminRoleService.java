package com.tibco.ma.service;

import org.json.simple.JSONObject;

import com.tibco.ma.model.AdminRole;

public interface AdminRoleService extends BaseService<AdminRole> {

	void save(JSONObject json);

	void saveAuth(JSONObject json);

	void update(JSONObject json);

	boolean exists(AdminRole role);

}
