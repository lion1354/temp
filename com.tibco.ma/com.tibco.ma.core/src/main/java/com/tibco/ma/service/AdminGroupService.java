package com.tibco.ma.service;

import java.util.List;

import org.json.simple.JSONObject;

import com.tibco.ma.model.AdminGroup;
import com.tibco.ma.model.AdminRole;

public interface AdminGroupService extends BaseService<AdminGroup> {

	void save(JSONObject json) throws Exception;

	List<AdminGroup> getGroupByParent(String adm_group_id);
	
	public boolean exists(AdminGroup group);
	
	List<JSONObject> getGroupAndResByRole(AdminRole role) throws Exception;

}
