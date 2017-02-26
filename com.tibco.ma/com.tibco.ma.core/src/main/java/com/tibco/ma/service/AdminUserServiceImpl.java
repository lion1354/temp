package com.tibco.ma.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tibco.ma.common.DateQuery;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.dao.AdminRoleDao;
import com.tibco.ma.dao.AdminUserDao;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.model.AdminRole;
import com.tibco.ma.model.AdminUser;
import com.tibco.ma.model.MongoDBConstants;

@Service
public class AdminUserServiceImpl extends BaseServiceImpl<AdminUser> implements
		AdminUserService {

	private static Logger log = LoggerFactory
			.getLogger(AdminUserServiceImpl.class);

	@Resource
	private AdminUserDao dao;

	@Resource
	private AdminRoleDao roleDao;

	@Override
	public BaseDao<AdminUser> getDao() {
		return dao;
	}

	@Override
	public void configRoles(JSONObject json) {
		try {
			String user_id = (String) json.get("user_id");
			String roleStr = (String) json.get("roleStr");
			AdminUser user = dao.findById(new ObjectId(user_id),
					AdminUser.class);
			if (StringUtil.notEmpty(roleStr)) {
				String[] roles = roleStr.split(",");
				List<AdminRole> list = new ArrayList<AdminRole>();
				for (int i = 0; i < roles.length; i++) {
					list.add(new AdminRole(roles[i]));
				}
				user.setRoles(list);
			} else {
				if (user.getRoles() != null && user.getRoles().size() != 0) {
					user.setRoles(null);
				}
			}
			Query query = new Query();
			query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(user_id)));
			Update update = Update.update("roles", user.getRoles());
			dao.update(query, update, AdminUser.class);
		} catch (Exception e) {
			log.error("{}", e);
		}

	}

	@Override
	public boolean exists(AdminUser inputU) {
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(inputU.getUsername()));
		AdminUser user = dao.findOne(query, AdminUser.class);
		if (user == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void register(AdminUser user) {
		AdminRole free = roleDao.findOne(
				new Query().addCriteria(Criteria.where("name").is("free")),
				AdminRole.class);
		List<AdminRole> roles = new ArrayList<AdminRole>();
		roles.add(free);
		user.setRoles(roles);
		dao.save(user);
	}

	@Override
	public void updateUserLoginInfo(AdminUser user) {
		user.setLoginTimes(user.getLoginTimes()==null?0:user.getLoginTimes() + 1);
		user.setLastLoginTime(DateQuery.formatDateTime(new Date()));
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(user.getUsername()));
		int times = 0;
		if (user.getLoginTimes() != null)
			times = user.getLoginTimes();
		Update update = Update.update("loginTimes", times + 1).set(
				"lastLoginTime", DateQuery.formatDateTime(new Date()));
		dao.update(query, update, AdminUser.class);
	}
}
