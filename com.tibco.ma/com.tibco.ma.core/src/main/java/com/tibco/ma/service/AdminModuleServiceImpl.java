package com.tibco.ma.service;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tibco.ma.common.StringUtil;
import com.tibco.ma.dao.AdminModuleDao;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.model.AdminModule;
import com.tibco.ma.model.MongoDBConstants;

@Service
public class AdminModuleServiceImpl extends BaseServiceImpl<AdminModule> implements
		AdminModuleService {

	private static Logger log = LoggerFactory.getLogger(AdminModuleServiceImpl.class);

	@Resource
	private AdminModuleDao dao;

	@Override
	public BaseDao<AdminModule> getDao() {
		return dao;
	}

	@Override
	public void save(JSONObject json) {
		try {
			if (!json.containsKey("id")
					|| StringUtil.isEmpty((String) json.get("id"))) {	
				AdminModule module = new AdminModule();
				module.setName(json.get("name").toString());
				module.setDescription(json.get("description").toString());
				dao.save(module);
			} else {
				Query query = new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
						new ObjectId((String)json.get("id"))));
				Update update = Update
						.update("name", json.get("name").toString())
						.set("description", json.get("description").toString());
				dao.update(query, update, AdminModule.class);
			}
		} catch (Exception e) {
			log.error("{}", e);
		}
	}

}
