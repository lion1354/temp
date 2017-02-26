package com.tibco.ma.service;

import org.json.simple.JSONObject;

import com.tibco.ma.model.AdminModule;

public interface AdminModuleService extends BaseService<AdminModule> {

	void save(JSONObject json) throws Exception;

}
