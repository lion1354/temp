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
import com.tibco.ma.dao.AdminRoleDao;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.model.AdminResources;
import com.tibco.ma.model.AdminRole;
import com.tibco.ma.model.MongoDBConstants;

@Service
public class AdminRoleServiceImpl extends BaseServiceImpl<AdminRole> implements
		AdminRoleService {

	@Resource
	private AdminRoleDao dao;

	@Resource
	private AdminResourceService resourceService;

	@Override
	public BaseDao<AdminRole> getDao() {
		return dao;
	}

	@Override
	public void save(JSONObject json) {
		AdminRole role = new AdminRole();
		role.setDescription((String) json.get("description"));
		role.setName((String) json.get("name"));
		dao.save(role);
	}

	@Override
	public void update(JSONObject json) {
		String id = (String) json.get("id");
		Query query = new Query();
		query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(id)));
		Update update = Update.update("name", (String) json.get("name")).set(
				"description", (String) json.get("description"));
		dao.update(query, update, AdminRole.class);
	}

	@Override
	public void saveAuth(JSONObject json) {
		String role_id = (String) json.get("role_id");
		String resourceStr = (String) json.get("resourceStr");
		AdminRole role = dao.findById(new ObjectId(role_id), AdminRole.class);
		List<AdminResources> list = null;
		try {
			list = resourceService.find(new Query(), AdminResources.class);
		} catch (Exception e) {
			list = new ArrayList<AdminResources>();
		}
		if (StringUtil.notEmpty(resourceStr)) {
			String[] resource_ids = resourceStr.split(",");
			List<AdminResources> resources = new ArrayList<AdminResources>();
			for (int i = 0; i < resource_ids.length; i++) {
				for (AdminResources adminResources : list) {
					if (resource_ids[i].equals(adminResources.getId())) {
						resources.add(new AdminResources(resource_ids[i]));
						continue;
					}
				}
			}
			role.setResources(resources);
		} else {
			if (role.getResources() != null && role.getResources().size() != 0)
				role.setResources(null);
		}
		Query query = new Query();
		query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(role_id)));
		Update update = Update.update("resources", role.getResources());
		dao.update(query, update, AdminRole.class);
	}

	@Override
	public boolean exists(AdminRole inputRole) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(inputRole.getName()));
		AdminRole role = dao.findOne(query, AdminRole.class);
		if (role == null) {
			return false;
		} else {
			return true;
		}
	}

}
