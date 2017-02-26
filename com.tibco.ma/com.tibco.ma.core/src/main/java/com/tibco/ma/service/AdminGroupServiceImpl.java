package com.tibco.ma.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tibco.ma.common.StringUtil;
import com.tibco.ma.dao.AdminGroupDao;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.model.AdminGroup;
import com.tibco.ma.model.AdminResources;
import com.tibco.ma.model.AdminRole;
import com.tibco.ma.model.MongoDBConstants;

@Service
public class AdminGroupServiceImpl extends BaseServiceImpl<AdminGroup>
		implements AdminGroupService {

	private static Logger log = LoggerFactory
			.getLogger(AdminGroupServiceImpl.class);

	@Resource
	private AdminGroupDao dao;

	@Resource
	private AdminResourceService resourceService;

	@Override
	public BaseDao<AdminGroup> getDao() {
		return dao;
	}

	@Override
	public void save(JSONObject json) {
		try {
			if (!json.containsKey("id")
					|| StringUtil.isEmpty((String) json.get("id"))) {
				AdminGroup group = new AdminGroup();
				group.setName(json.get("name").toString());
				group.setModule_id(json.get("module_id").toString());
				group.setModule_name(json.get("module_name").toString());
				group.setAdm_group_id(json.get("adm_group_id").toString());
				group.setAdm_group_name(json.get("adm_group_name").toString());
				group.setDescription(json.get("description").toString());
				group.setDate(new Date());
				dao.save(group);
			} else {
				Query query = new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
						new ObjectId((String) json.get("id"))));
				AdminGroup group = dao.findOne(query, AdminGroup.class);
				if (!json.get("module_id").toString()
						.equals(group.getModule_id())) {
					updateModuleByParentID(group.getId(), json.get("module_id")
							.toString(), json.get("module_name").toString());
				}

				Update update = Update
						.update("name", json.get("name").toString())
						.set("adm_group_id",
								json.get("adm_group_id").toString())
						.set("adm_group_name",
								json.get("adm_group_name").toString())
						.set("description", json.get("description").toString());
				dao.update(query, update, AdminGroup.class);
			}
		} catch (Exception e) {
			log.error("{}", e);
		}

	}

	private void updateModuleByParentID(String parentID, String moudleID,
			String moduleNmae) {
		Query queryGroup = new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
				new ObjectId(parentID)));
		Update update = Update.update("module_id", moudleID).set("module_name",
				moduleNmae);
		dao.update(queryGroup, update, AdminGroup.class);

		Query queryModule = new Query(Criteria.where("adm_group_id").is(
				parentID));
		List<AdminGroup> list = dao.find(queryModule, AdminGroup.class);
		for (AdminGroup adminGroup : list) {
			updateModuleByParentID(adminGroup.getId(), moudleID, moduleNmae);
		}

	}

	@Override
	public List<AdminGroup> getGroupByParent(String adm_group_id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("adm_group_id").is(adm_group_id));
		List<AdminGroup> list = dao.find(query, AdminGroup.class);
		return list;
	}

	@Override
	public boolean exists(AdminGroup group) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(group.getName()));
		query.addCriteria(Criteria.where("adm_group_id").is(
				group.getAdm_group_id()));
		AdminGroup m = dao.findOne(query, AdminGroup.class);
		if (m == null) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> getGroupAndResByRole(AdminRole role)
			throws Exception {
		List<JSONObject> list = new ArrayList<JSONObject>();

		List<AdminResources> resources = role.getResources();
		Query queryGroup = new Query();
		queryGroup.with(new Sort(new Order("date")));
		List<AdminGroup> groups = dao.find(queryGroup, AdminGroup.class);
		for (AdminGroup adminGroup : groups) {
			JSONObject group = new JSONObject();
			group.put("id", adminGroup.getId());
			group.put("name", adminGroup.getName());

			JSONObject parent = new JSONObject();
			parent.put("id", adminGroup.getAdm_group_id());
			group.put("parent", parent);

			Query query = new Query(Criteria.where("group_id").is(
					adminGroup.getId()));
			List<AdminResources> resList = resourceService.find(query,
					AdminResources.class);
			for (AdminResources adminResources : resList) {
				JSONObject res = new JSONObject();
				res.put("id", adminResources.getId());
				res.put("name", adminResources.getName());
				JSONObject parent1 = new JSONObject();
				parent1.put("id", adminResources.getGroup_id());
				res.put("parent", parent1);
				group.put("checked", false);

				if (isContain(resources, adminResources.getId())) {
					res.put("checked", true);
					group.put("checked", true);
				} else {
					res.put("checked", false);
				}

				list.add(res);
			}
			list.add(group);
		}
		return list;
	}

	private boolean isContain(List<AdminResources> resources, String id) {
		if (resources == null || resources.size() == 0) {
			return false;
		}

		for (AdminResources adminResources : resources) {
			if (adminResources.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

}
