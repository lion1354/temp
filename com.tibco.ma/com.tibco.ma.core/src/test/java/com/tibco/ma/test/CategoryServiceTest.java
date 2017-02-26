package com.tibco.ma.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.tibco.ma.dao.CategoryDao;
import com.tibco.ma.model.Category;
import com.tibco.ma.service.CategoryService;
import com.tibco.ma.service.SpecificationService;

public class CategoryServiceTest {
	private static ApplicationContext factory;
	private static SpecificationService specificationService;
	private static CategoryService categoryService;
	private static CategoryDao categorydao;

	@Before
	public void init() {
		try {
			factory = new ClassPathXmlApplicationContext(
					"classpath*:applicationContext-test.xml");

			categoryService = factory.getBean(CategoryService.class);
			categorydao = factory.getBean(CategoryDao.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testQuery() {
		try {
			List<Category> categories = categoryService.find(new Query(),
					Category.class);
			System.out.println(categories.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testSave() throws Exception {
		Category category = new Category();
		category.setComment("category01");
		category.setName("category01");
		categoryService.save(category);

		System.out.println(category);

		Query query2 = new Query();
		query2.addCriteria(Criteria.where("_id").is(category.getId()));
		categorydao.remove(query2, Category.class);

	}

}
