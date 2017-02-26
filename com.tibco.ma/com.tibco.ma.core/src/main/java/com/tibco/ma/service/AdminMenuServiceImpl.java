package com.tibco.ma.service;

import java.util.List;
import java.util.TreeSet;

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
import com.tibco.ma.common.ValidateUtils;
import com.tibco.ma.dao.AdminMenuDao;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.model.AdminMenu;
import com.tibco.ma.model.MongoDBConstants;

@Service
public class AdminMenuServiceImpl extends BaseServiceImpl<AdminMenu> implements
		AdminMenuService {

	private static Logger log = LoggerFactory
			.getLogger(AdminMenuServiceImpl.class);

	@Resource
	private AdminMenuDao dao;

	@Override
	public BaseDao<AdminMenu> getDao() {
		return dao;
	}

	@Override
	public void save(JSONObject json) throws Exception {
		String name = (String) json.get("name");
		if (StringUtil.isEmpty(name)) {
			throw new Exception("name is empty");
		}
		if (StringUtil.isEmpty((String) json.get("code"))) {
			throw new Exception("code is empty");
		}
		if (StringUtil.isEmpty(json.get("order").toString())) {
			throw new Exception("order is empty");
		}

		if (StringUtil.isEmpty(json.get("menuLevel").toString())) {
			throw new Exception("menuLevel is empty");
		}

		log.debug("save json : {}", json.toJSONString());

		if (!json.containsKey("id")
				|| StringUtil.isEmpty(json.get("id").toString())) {
			int result = checkCode(json.get("code").toString());
			if (result > 0) {
				throw new Exception("code is exist");
			}
		}

		// step1: update exit order
		if (StringUtil.notEmpty(json.get("order").toString())) {
			int order = Integer.parseInt(json.get("order").toString());
			Query query = new Query();
			if (StringUtil.notEmpty((String) json.get("pCode"))) {
				query.addCriteria(Criteria.where("p_code")
						.is(json.get("pCode")));
			} else {
				query.addCriteria(Criteria.where("p_code").is(null));
			}

			query.addCriteria(Criteria.where("order").is(order));
			AdminMenu adminMenu = dao.findOne(query, AdminMenu.class);

			// update old order
			if (adminMenu != null) {
				if (!json.containsKey("id")
						|| !adminMenu.getId().equals(adminMenu.getId())) {
					Query queryAll = new Query();
					if (StringUtil.notEmpty((String) json.get("pCode"))) {
						queryAll.addCriteria(Criteria.where("p_code").is(
								json.get("pCode")));
					} else {
						queryAll.addCriteria(Criteria.where("p_code").is(null));
					}
					List<AdminMenu> adminMenus = dao.find(queryAll,
							AdminMenu.class);
					if (ValidateUtils.isValidate(adminMenus)) {
						for (AdminMenu aMenu : adminMenus) {
							if (aMenu.getOrder() >= order) {
								updateOrder(aMenu);
							}
						}
					}
				}
			}

			// saveOrupdate
			if (!json.containsKey("id")
					|| StringUtil.isEmpty(json.get("id").toString())) {
				AdminMenu newAdminMenu = new AdminMenu();
				initByJson(newAdminMenu, json);
				dao.save(newAdminMenu);
			} else {
				String code = json.get("code").toString();
				updatePCode(json.get("id").toString(), code);
				Query queryUpdate = new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
						new ObjectId(json.get("id").toString())));
				Update update = initUpdateByJson(json);
				dao.update(queryUpdate, update, AdminMenu.class);
			}
		}
	}

	private void updatePCode(String id, String newCode) throws Exception {
		if (StringUtil.notEmpty(id) && StringUtil.notEmpty(newCode)) {
			Query query = new Query();
			query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(id)));
			AdminMenu adminMenu = dao.findOne(query, AdminMenu.class);
			if (adminMenu != null) {
				List<AdminMenu> childMenus = getAdminMenusByPCode(adminMenu
						.getCode());
				if (ValidateUtils.isValidate(childMenus)) {
					for (AdminMenu childMenu : childMenus) {
						Update update = new Update();
						update.set("p_code", newCode);
						Query queryId = new Query();
						queryId.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
								new ObjectId(childMenu.getId())));
						dao.update(queryId, update, AdminMenu.class);
					}
				}
			}

		}
	}

	private List<AdminMenu> getAdminMenusByPCode(String pCode) {
		Query queryPCode = new Query();
		queryPCode.addCriteria(Criteria.where("p_code").is(pCode));
		List<AdminMenu> adminMenus = dao.find(queryPCode, AdminMenu.class);
		return adminMenus;
	}

	@Override
	public AdminMenu loadMenuById(String id) throws Exception {
		if (StringUtil.isEmpty(id)) {
			throw new Exception("id is empty");
		}
		Query query = new Query();
		query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(id)));
		AdminMenu adminMenu = dao.findOne(query, AdminMenu.class);
//		if (adminMenu != null && StringUtil.notEmpty(adminMenu.getP_code())) {
//			Query queryPCode = new Query();
//			queryPCode.addCriteria(Criteria.where("p_code").is(
//					adminMenu.getCode()));
//			List<AdminMenu> adminMenus = dao.find(queryPCode, AdminMenu.class);
//			TreeSet<AdminMenu> treeSetAdminMenu = new TreeSet<AdminMenu>();
//			for (AdminMenu adminMenu2 : adminMenus) {
//				treeSetAdminMenu.add(adminMenu2);
//			}
//			adminMenu.setChildren(treeSetAdminMenu);
//		}
		return adminMenu;
	}

	private void updateOrder(List<AdminMenu> list) {
		for (AdminMenu menu : list) {
			menu.setOrder(menu.getOrder() + 1);
			Query query = new Query();
			query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
					new ObjectId(menu.getId())));
			Update update = Update.update("order", menu.getOrder());
			dao.update(query, update, AdminMenu.class);
		}
	}

	@Override
	public List<AdminMenu> getMenuByParent(String pCode) {
		Query query = new Query();
		query.addCriteria(Criteria.where("p_code").is(pCode));
		List<AdminMenu> list = dao.find(query, AdminMenu.class);
		return list;
	}

	@Override
	public boolean exists(AdminMenu menu) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(menu.getName()));
		query.addCriteria(Criteria.where("p_code").is(menu.getP_code()));
		AdminMenu m = dao.findOne(query, AdminMenu.class);
		if (m == null) {
			return false;
		} else {
			return true;
		}
	}

	public void initByJson(AdminMenu adminMenu, JSONObject jsonObject) {
		if (jsonObject.containsKey("id")
				&& StringUtil.notEmpty((String) jsonObject.get("id"))) {
			adminMenu.setId((String) jsonObject.get("id"));
		}
		if (jsonObject.containsKey("name")
				&& StringUtil.notEmpty((String) jsonObject.get("name"))) {
			adminMenu.setName((String) jsonObject.get("name"));
		}
		if (jsonObject.containsKey("describe")
				&& StringUtil.notEmpty((String) jsonObject.get("describe"))) {
			adminMenu.setDescribe((String) jsonObject.get("describe"));
		}
		if (jsonObject.containsKey("menuLevel")
				&& StringUtil.notEmpty(jsonObject.get("menuLevel").toString())) {
			adminMenu.setMenuLevel(Integer.parseInt(jsonObject.get("menuLevel")
					.toString()));
		}
		if (jsonObject.containsKey("url")
				&& StringUtil.notEmpty((String) jsonObject.get("url"))) {
			adminMenu.setUrl((String) jsonObject.get("url"));
		}
		if (jsonObject.containsKey("order")
				&& StringUtil.notEmpty(jsonObject.get("order").toString())) {
			adminMenu.setOrder(Integer.parseInt(jsonObject.get("order")
					.toString()));
		}
		if (jsonObject.containsKey("code")
				&& StringUtil.notEmpty(jsonObject.get("code").toString())) {
			adminMenu.setCode((String) jsonObject.get("code"));
		}
		if (jsonObject.containsKey("pCode")
				&& StringUtil.notEmpty((String) jsonObject.get("pCode"))) {
			adminMenu.setP_code((String) jsonObject.get("pCode"));
		}
	}

	private Update initUpdateByJson(JSONObject jsonObject) {
		Update update = new Update();

		if (jsonObject.containsKey("name")
				&& StringUtil.notEmpty((String) jsonObject.get("name"))) {
			update.set("name", jsonObject.get("name"));
		}
		if (jsonObject.containsKey("describe")
				&& StringUtil.notEmpty((String) jsonObject.get("describe"))) {
			update.set("describe", jsonObject.get("describe"));
		}
		if (jsonObject.containsKey("menuLevel")
				&& StringUtil.notEmpty(jsonObject.get("menuLevel").toString())) {
			update.set("menuLevel",
					Integer.parseInt(jsonObject.get("menuLevel").toString()));
		}
		if (jsonObject.containsKey("url")
				&& StringUtil.notEmpty((String) jsonObject.get("url"))) {
			update.set("url", jsonObject.get("url"));
		}
		if (jsonObject.containsKey("order")
				&& StringUtil.notEmpty(jsonObject.get("order").toString())) {
			update.set("order",
					Integer.parseInt(jsonObject.get("order").toString()));
		}
		if (jsonObject.containsKey("code")
				&& StringUtil.notEmpty((String) jsonObject.get("code"))) {
			update.set("code", jsonObject.get("code"));
		}
		if (jsonObject.containsKey("pCode")
				&& StringUtil.notEmpty((String) jsonObject.get("pCode"))) {
			update.set("p_code", jsonObject.get("pCode"));
		}
		return update;
	}

	@Override
	public void delete(JSONObject json) throws Exception {
		Query q = new Query();
		q.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
				new ObjectId((String) json.get("id"))));
		AdminMenu adminMenu = dao.findOne(q, AdminMenu.class);
		if (adminMenu == null) {
			return;
		} else {
			List<AdminMenu> childMenu = getChildAdminMenu(adminMenu.getCode());
			if (!ValidateUtils.isValidate(childMenu)) {
				dao.remove(q, AdminMenu.class);
			} else {
				throw new Exception("exist child Menu");
			}
		}
	}

	public List<AdminMenu> getChildAdminMenu(String p_code) {
		Query query = new Query();
		query.addCriteria(Criteria.where("p_code").is(p_code));
		List<AdminMenu> lists = dao.find(query, AdminMenu.class);
		return lists;
	}

	private void updateOrder(AdminMenu adminMenu) throws Exception {
		if (adminMenu == null || StringUtil.isEmpty(adminMenu.getId())) {
			throw new Exception("adminMenu id is empty");
		}
		Query query = new Query();
		query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
				new ObjectId(adminMenu.getId())));
		Update update = new Update();
		update.set("order", adminMenu.getOrder() + 1);
		dao.update(query, update, AdminMenu.class);
	}

	@Override
	public int checkCode(String code) throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where("code").is(code));
		List<AdminMenu> lists = dao.find(query, AdminMenu.class);
		return ValidateUtils.isValidate(lists) ? lists.size() : 0;
	}
}
