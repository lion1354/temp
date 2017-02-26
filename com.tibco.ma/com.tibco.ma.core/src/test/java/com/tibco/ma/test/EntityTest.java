package com.tibco.ma.test;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tibco.ma.service.EntityService;
import com.tibco.ma.service.ValuesService;

public class EntityTest {
	private static ApplicationContext factory;
	private static EntityService entityService;
	private static ValuesService valuesService;

	@Before
	public void init() {
		try {
			factory = new ClassPathXmlApplicationContext(
					"classpath*:applicationContext-test.xml");
			entityService = factory.getBean(EntityService.class);
			valuesService = factory.getBean(ValuesService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSave() throws Exception {
		Document json = new Document();
		json.put("name", "test");
		json.put("appId", "552744a17ea940fc8fe12e16");
		entityService.save("552744a17ea940fc8fe12e16", json);
	}

	@Test
	public void testUpdate() throws Exception {
		Document json = new Document();
		json.put("className", "test");
		json.put("id", "552e28d8dcf324268625abe7");

		JSONArray array = new JSONArray();
		JSONObject j1 = new JSONObject();
		j1.put("colName", "name");
		j1.put("colType", "String");
		array.add(j1);
		JSONObject j2 = new JSONObject();
		j2.put("colName", "age");
		j2.put("colType", "int");
		array.add(j2);
		JSONObject j3 = new JSONObject();
		j3.put("colName", "image");
		j3.put("colType", "Image");
		array.add(j3);

		json.put("cols", array);
		entityService.update("552e28d8dcf324268625abe7", json);
	}

	@Test
	public void testSaveValues() {
		Document document = null;

		try {
			document = new Document("entityId", new ObjectId(
					"552e28d8dcf324268625abe7")).append("name", "test")
					.append("age", 22)
					.append("image", "47f0ba6e-16f0-4788-9e88-8d02d2568db0");
			valuesService.insertOne("values", document);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void nameValidate() throws Exception {
		Document entity = entityService.getByClassName(
				"55304d7302f4c49cc58a366b", "test");
		System.out.println(entity == null ? "====" : entity.getObjectId("_id")
				.toString());
	}
}
