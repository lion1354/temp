package com.tibco.ma.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.util.JSON;
import com.tibco.ma.common.MD5Util;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.AdminMenu;
import com.tibco.ma.model.AdminResources;
import com.tibco.ma.model.AdminRole;
import com.tibco.ma.model.AdminUser;
import com.tibco.ma.model.PNTask;
import com.tibco.ma.service.AdminMenuService;
import com.tibco.ma.service.AdminResourceService;
import com.tibco.ma.service.AdminRoleService;
import com.tibco.ma.service.AdminUserService;

public class MongodbTest {

	private static Logger log = LoggerFactory.getLogger(MongodbTest.class);

	private static ApplicationContext factory;
	private static AdminUserService service;
	private static AdminRoleService roleService;
	private static AdminResourceService resourceService;
	private static AdminMenuService menuService;
	

	@Before
	public void init() {
		try {
			factory = new ClassPathXmlApplicationContext(
					"classpath*:applicationContext-test.xml");
			service = (AdminUserService) factory
					.getBean(AdminUserService.class);
			roleService = factory.getBean(AdminRoleService.class);
			resourceService = factory.getBean(AdminResourceService.class);
			menuService = factory.getBean(AdminMenuService.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Test
	@Ignore
	public void findAll() {
		try {
			Pattern pattern = Pattern.compile("^name");
			// DBObject object = new BasicDBObject("name", pattern);
			// DBObject object = new BasicDBObject("age", new
			// BasicDBObject("$gt", 10));
			Document document = new Document("age", new Document(
					QueryOperators.IN, new int[] { 25, 26, 27 }));
			List<Document> list = service.query("user", document, null);// "{\"name\":\"aa\"}"
			System.out.println(JSON.serialize(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void findResourcesByUser() {
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is("root"));
		try {
//			AdminUser user = service.findOne(query, AdminUser.class);
//			List<AdminResources> list = resourceService
//					.getResourcesByUser(user);
//			for (AdminResources resources : list) {
//				log.info(resources.getName());
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	@Ignore
	public void findParent() {
		String p_id = "55150bd0e76168d55b2f764e";
		List<AdminMenu> menus = menuService.getMenuByParent(p_id);
		for (AdminMenu menu : menus) {
			log.info(menu.getName());
		}
	}

	@Test
	@Ignore
	public void insert() {
		// String json = "{\"name\":\"\",\"age\":\"\",\"mail\":\"\"}";
		try {
			for (int i = 0; i < 30; i++) {
				Document object = new Document("name", "name" + i).append(
						"age", i).append("email", "name" + i + "@mail.com");
				// json = "{\"name\":\"name" + i + "\",\"age\":\"" + i
				// + "\",\"email\":\"name" + i + "@mail.com\"}";
				service.insertOne("user", object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void save() {
		try {
			AdminRole role1 = new AdminRole("root");
			roleService.save(role1);
			AdminRole role2 = new AdminRole("admin");
			roleService.save(role2);
			for (int i = 0; i < 30; i++) {
				AdminUser user = new AdminUser();
				user.setUsername("name" + i);
				user.setEmail(user.getUsername() + "@mail.com");
				user.setPassword("1");
				user.setType("test");
				List<AdminRole> roles = new ArrayList<AdminRole>();
				roles.add(role1);
				roles.add(role2);
				user.setRoles(roles);
				service.save(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void page() {
		try {
			for (int i = 1; i <= (service.count("user", null) % 5 == 0 ? service
					.count("user", null) / 5
					: service.count("user", null) / 5 + 1); i++) {
				Pager<String> page = service.page("user", null, null, i, 5);
				log.info(JSON.serialize(page));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void page2() throws Exception {
		AdminUser t = new AdminUser();
		t.setUsername("abc");
		t.setPassword("abc");
		service.save(t);
		BasicDBObject dbObj = new BasicDBObject();
		dbObj.put("sort", 1);

		Query query = new Query();
		List<AdminMenu> users = menuService
				.find(query, 20, 20, AdminMenu.class);
		log.info("size:" + users.size());

		Pager<String> page = menuService.page("menu", null, null, 2, 20);
		log.info(JSON.serialize(page));
	}

	@Test
	@Ignore
	public void getOne() {
		try {
			Document document = service.getOneById("user",
					"550a97d610362ce6149280f2");
			if (document != null) {
				log.info(document.toJson());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void update() {
		try {
			getOne();
			Document document = new Document("name", "username");
			UpdateResult result = service.updateById("user",
					"550a97d610362ce6149280f2", document);
			log.info(result.getUpsertedId().toString());
			getOne();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void delete() {
		try {
			DeleteResult result = service.deleteById("user",
					"550a944f10366aab4ca1a107");
			log.info(String.valueOf(result.getDeletedCount()));
			page();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void remove() {
		try {
			service.dropCollection("user");
			log.info("dropCollection");
		} catch (Exception e) {
			log.error("{}", e);
		}
	}

	public static void main(String[] args) {
		System.out.println(MD5Util.convertMD5Password("1"));
	}

}
