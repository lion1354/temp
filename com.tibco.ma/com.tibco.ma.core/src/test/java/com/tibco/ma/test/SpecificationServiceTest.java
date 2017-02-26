package com.tibco.ma.test;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.tibco.ma.model.Category;
import com.tibco.ma.model.Specification;
import com.tibco.ma.service.CategoryService;
import com.tibco.ma.service.SpecificationService;

public class SpecificationServiceTest {
	private static ApplicationContext factory;
	private static SpecificationService specificationService;
	private static CategoryService categoryService;

	@Before
	public void init() {
		try {
			factory = new ClassPathXmlApplicationContext(
					"classpath*:applicationContext-test.xml");

			specificationService = factory.getBean(SpecificationService.class);
			categoryService = factory.getBean(CategoryService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSave() throws Exception {
		Specification specification = new Specification();
		specification.setComment("aa");
		specification.setwidth(22.33f);
		specification.setHeight(23.22f);
		specification.setName("aaa");
		// specification.setPictureUrl("aaa");
		Category category = new Category();
		category.setComment("aaa");
		category.setName("aaa");
		categoryService.save(category);
		specification.setCategory(new Category(category.getId()));
		specificationService.save(specification);

		System.out.println(specification);

		Query query = new Query();
		query.addCriteria(Criteria.where("category").is(
				new Category(category.getId())));
		specificationService.delete(query, Specification.class);

		// Query query = new Query();
		// query.addCriteria(Criteria.where("category").is(
		// new Category((String)id)));
		//
		// specificationDao.remove(query, Specification.class);
		// categoryDao.delete(collection, object)

	}

	@Test
	public void testSaveAndUpdate() throws Exception {
		Specification specification = new Specification();
		specification.setComment("aa");
		specification.setwidth(22.33f);
		specification.setHeight(23.22f);
		specification.setName("test01");
		Specification specification1 = new Specification();
		specification1.setComment("bb");
		specification1.setwidth(22.33f);
		specification1.setHeight(23.22f);
		specification1.setName("test02");
		Specification specification2 = new Specification();
		specification2.setComment("cc");
		specification2.setwidth(22.33f);
		specification2.setHeight(23.22f);
		specification2.setName("test03");

		Specification specification3 = new Specification();
		specification2.setComment("cc");
		specification2.setwidth(22.33f);
		specification2.setHeight(23.22f);
		specification2.setName("test04");

		Category category = new Category();
		category.setComment("categoryTest");
		category.setName("categoryTest");
		categoryService.save(category);
		specification.setCategory(new Category(category.getId()));
		specification1.setCategory(new Category(category.getId()));
		specification2.setCategory(new Category(category.getId()));

		specificationService.save(specification);
		specificationService.save(specification1);
		specificationService.save(specification2);

		Query query = new Query();
		query.addCriteria(Criteria.where("category").is(
				new Category(category.getId())));
		query.addCriteria(Criteria.where("_id").ne(
				new ObjectId(specification.getId())));
		specificationService.delete(query, Specification.class);

	}

}
