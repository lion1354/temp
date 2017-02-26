package com.tibco.ma.service;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tibco.ma.common.DateQuery;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.CredentialDao;
import com.tibco.ma.model.Credential;
import com.tibco.ma.model.MongoDBConstants;

@Service
public class CredentialServiceImpl extends BaseServiceImpl<Credential>
		implements CredentialService {

	@Resource
	private CredentialDao dao;

	@Override
	public BaseDao<Credential> getDao() {
		return dao;
	}

	@Override
	public Credential saveCred(Credential credential) throws Exception {
		if (StringUtil.isEmpty(credential.getId())) {
			String time = DateQuery.formatPSTDatetime(System
					.currentTimeMillis());
			credential.setTime(time);
			dao.save(credential);
		} else {
			Query query = new Query();
			query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
					new ObjectId(credential.getId())));
			Update update = new Update();
			if (StringUtil.notEmpty(credential.getPath())) {
				update.set("path", credential.getPath());
			}
			if (StringUtil.notEmpty(credential.getPassword())) {
				update.set("password", credential.getPassword());
			}
			if (StringUtil.notEmpty(credential.getApp().getId())) {
				update.set("app", credential.getApp());
			}
			if (StringUtil.notEmpty(credential.getApiKey())) {
				update.set("apiKey", credential.getApiKey());
			}
			if (StringUtil.notEmpty(credential.getProjectNumber())) {
				update.set("projectNumber", credential.getProjectNumber());
			}
			if (credential.getIsProduction() != null) {
				update.set("isProduction", credential.getIsProduction());
			}
			dao.update(query, update, Credential.class);
		}
		return credential;
	}

}
