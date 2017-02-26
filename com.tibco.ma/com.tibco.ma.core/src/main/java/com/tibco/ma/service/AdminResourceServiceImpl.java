package com.tibco.ma.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tibco.ma.common.StringUtil;
import com.tibco.ma.dao.AdminResourcesDao;
import com.tibco.ma.dao.AdminRoleDao;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.model.AdminResources;
import com.tibco.ma.model.AdminRole;
import com.tibco.ma.model.MongoDBConstants;

@Service
public class AdminResourceServiceImpl extends BaseServiceImpl<AdminResources>
		implements AdminResourceService {

	@Resource
	private AdminResourcesDao dao;

	@Resource
	private AdminRoleDao roleDao;

	@Override
	public BaseDao<AdminResources> getDao() {
		return dao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void save(JSONObject json) {
		if (!json.containsKey("id")
				|| StringUtil.isEmpty((String) json.get("id"))) {
			AdminResources resource = new AdminResources();
			resource.setDescription(json.get("description").toString());
			resource.setName(json.get("name").toString());
			resource.setUrl((List<String>) json.get("url"));
			resource.setGroup_id(json.get("group_id").toString());
			resource.setGroup_name(json.get("group_name").toString());
			dao.save(resource);
		} else {
			Query query = new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
					new ObjectId((String) json.get("id"))));
			Update update = Update.update("name", (String) json.get("name"))
					.set("url", (List<String>) json.get("url"))
					.set("group_id", (String) json.get("group_id"))
					.set("group_name", json.get("group_name").toString())
					.set("description", (String) json.get("description"));
			dao.update(query, update, AdminResources.class);
		}

	}

	@Override
	public boolean exists(AdminResources resources) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(resources.getName()));
		AdminResources res = dao.findOne(query, AdminResources.class);
		if (res == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<String> queryAdminRoleByResulece(String requestUrl) {
		List<String> namelist = new ArrayList<String>();
		Query query = new Query();
		String reqUrl = requestUrl.split("[?]")[0];
		query.addCriteria(Criteria.where("url").in(reqUrl));
		List<AdminResources> res = dao.find(query, AdminResources.class);
		if (res == null || res.size() == 0) {
			namelist.add("null");
			return namelist;
		}

		List<ObjectId> idList = new ArrayList<ObjectId>();
		for (AdminResources resource : res) {
			idList.add(new ObjectId(resource.getId()));
		}
		query = new Query();
		query.addCriteria(Criteria.where("resources").elemMatch(
				Criteria.where(MongoDBConstants.DOCUMENT_ID).in(idList)));
		List<AdminRole> list = roleDao.find(query, AdminRole.class);
		for (AdminRole adminRole : list) {
			namelist.add(adminRole.getId());
		}
		if (namelist.size() == 0) {
			namelist.add("null");
		}
		return namelist;
	}

}
