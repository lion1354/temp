package com.tibco.ma.test;

import java.util.List;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.query.Query;

import com.tibco.ma.model.AdminUser;
import com.tibco.ma.model.App;
import com.tibco.ma.service.AppService;

public class AppTest {
	private static ApplicationContext factory;
	private static AppService appService;

	@Before
	public void init() {
		try {
			factory = new ClassPathXmlApplicationContext(
					"classpath*:applicationContext-test.xml");
			appService = factory.getBean(AppService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void find() {
		try {
			List<App> list = appService.find(new Query(), App.class);
			for (App app : list) {
				System.out.println(app.toString());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void save() {
		JSONObject json = new JSONObject();
		json.put("name", "app1");
		json.put("describe", "app1");
		json.put("apiKey", "AIzaSyAeDhjHSIHFSWiK8wQum55CxKz-njxSUK0");
		json.put("projectNumber", "121739891208");

		AdminUser user = new AdminUser("551b9910a1185ea7e3fdf8af");
		try {
			appService.save(json, user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void update() {
		try {
			App app = appService.findById(new ObjectId(
					"552351e10f1137026dbee5aa"), App.class);
			System.out.println(app.toString());
			JSONObject json = new JSONObject();
			json.put("id", "552351e10f1137026dbee5aa");
			json.put("name", app.getName());
			json.put("describe", app.getDescribe());
			json.put("apiKey", "AIzaSyAeDhjHSIHFSWiK8wQum55CxKz-njxSUK0");
			json.put("projectNumber", "121739891208");

			appService.save(json, null);
			app = appService.findById(new ObjectId("552351e10f1137026dbee5aa"),
					App.class);
			System.out.println(app.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
