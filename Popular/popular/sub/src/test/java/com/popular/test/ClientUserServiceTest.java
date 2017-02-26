package com.popular.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.popular.model.City;
import com.popular.service.CityService;
import com.popular.service.ClientUserService;

public class ClientUserServiceTest {

	private static ApplicationContext factory;
	private static ClientUserService userService;
	private static CityService cityService;

	@Before
	public void init() {
		try {
			factory = new ClassPathXmlApplicationContext(
					"classpath*:applicationContext-test.xml");
			userService = factory.getBean(ClientUserService.class);
			cityService = factory.getBean(CityService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void find() {
		try {
//			ClientUser user = userService
//					.getClientUserByPhoneNumber("13572477846");
//
//			System.out.println(user.toString());
			List<City> list = cityService.getCity();
			for (City city : list) {
				System.out.println(city.getId() + " " + city.getName() + " "
						+ city.getSpelling() + " " + city.getInitial());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
