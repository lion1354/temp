package com.tibco.ma.test;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tibco.ma.model.App;
import com.tibco.ma.model.Credential;
import com.tibco.ma.service.CredentialService;

public class CredentialTest {
	private static ApplicationContext factory;
	private static CredentialService service;

	@Before
	public void init() {
		try {
			factory = new ClassPathXmlApplicationContext(
					"classpath*:applicationContext-test.xml");
			service = factory.getBean(CredentialService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void find() {
		try {

			// List<Credential> list = service.find(new Query(),0,20,
			// Credential.class);
			// System.out.println(list.size());
			Credential credential = service.findById(new ObjectId(
					"553863324319aef38b063901"), Credential.class);
			System.out.println("-------------");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void save() {
		try {
			Credential credential = new Credential();
			credential.setApp(new App("552744a17ea940fc8fe12e16"));
			credential.setPath("111111111");
			service.save(credential);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
