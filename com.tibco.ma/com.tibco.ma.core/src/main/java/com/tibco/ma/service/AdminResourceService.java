package com.tibco.ma.service;

import java.util.List;

import org.json.simple.JSONObject;

import com.tibco.ma.model.AdminResources;

public interface AdminResourceService extends BaseService<AdminResources> {

	

	void save(JSONObject json);
	
	boolean exists(AdminResources resources);

	List<String> queryAdminRoleByResulece(String requestUrl); 
}
