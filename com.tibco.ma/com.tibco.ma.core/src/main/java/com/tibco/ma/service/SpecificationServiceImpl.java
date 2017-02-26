package com.tibco.ma.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tibco.ma.common.ValidateUtils;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.SpecificationDao;
import com.tibco.ma.model.Category;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.Specification;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@Service
public class SpecificationServiceImpl extends BaseServiceImpl<Specification>
		implements SpecificationService {
	
	private static final Logger log = LoggerFactory
			.getLogger(SpecificationServiceImpl.class);
	
	@Autowired
	private SpecificationDao specificationDao;

	@Autowired
	private FileService fileService;

	@Override
	public BaseDao<Specification> getDao() {
		return specificationDao;
	}

	@Override
	public void add(JSONObject jsonObject) {
		Category category = new Category();

		String categoryId = (String) jsonObject.get("category");
		category.setId(categoryId);
		String comment = (String) jsonObject.get("comment");
		ArrayList jsonArray = (ArrayList) jsonObject.get("specList");
		List<Specification> newSpecifications = new ArrayList<Specification>();

		List<Specification> exsitSpecifications = new ArrayList<Specification>();

		Iterator<LinkedHashMap> iterator = jsonArray.iterator();
		List<String> existIds = new ArrayList<String>();
		while (iterator.hasNext()) {
			LinkedHashMap<String, Object> map = iterator.next();
			if (map.containsKey("id")) {
				Specification specification = new Specification();
				String id = (String) map.get("id");
				String name = (String) map.get("name");
				Float width = new Float(map.get("width") + "");
				Float heigth = new Float(map.get("height") + "");
				existIds.add(id);
				specification.setId(id);
				specification.setCategory(category);
				specification.setComment(comment);
				specification.setHeight(heigth);
				specification.setwidth(width);
				specification.setName(name);
				exsitSpecifications.add(specification);

			} else {
				Specification specification = new Specification();
				String name = (String) map.get("name");
				Float width = new Float(map.get("width") + "");
				Float heigth = new Float(map.get("height") + "");

				specification.setCategory(category);
				specification.setComment(comment);
				specification.setHeight(heigth);
				specification.setwidth(width);
				specification.setName(name);
				newSpecifications.add(specification);
			}
		}
		try {
			if (ValidateUtils.isValidate(exsitSpecifications)) {
				Query query = new Query();
				query.addCriteria(Criteria.where("category").is(category));

				List<Specification> specifications = specificationDao.find(
						query, Specification.class);
				List<Specification> needDeleteSpecification = new ArrayList<Specification>();
				for (Specification specification : specifications) {
					if (!existIds.contains(specification.getId())) {
						needDeleteSpecification.add(specification);
					}
				}
				if (ValidateUtils.isValidate(needDeleteSpecification)) {
					for (Specification specification : needDeleteSpecification) {
						Query queryDeleteSpecification = new Query();
						queryDeleteSpecification.addCriteria(Criteria.where(
								MongoDBConstants.DOCUMENT_ID).is(new ObjectId(specification.getId())));
						specificationDao.remove(queryDeleteSpecification,
								Specification.class);
						fileService
								.deleteBySpecification(specification.getId());
					}
				}
			}

			updateSpecification(exsitSpecifications);

			if (ValidateUtils.isValidate(newSpecifications)) {
				for (Specification specification : newSpecifications) {
					specificationDao.save(specification);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateSpecification(List<Specification> specifications)
			throws Exception {

		if (ValidateUtils.isValidate(specifications)) {
			for (Specification specification : specifications) {
				Query query = new Query();
				query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
						new ObjectId(specification.getId())));
				Update update = new Update();
				update.set("name", specification.getName())
						.set("width", specification.getwidth())
						.set("height", specification.getHeight())
						.set("comment", specification.getComment());
				specificationDao.update(query, update, Specification.class);
			}
		}
	}

	@Override
	public void deleteByCategory(String categoryId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("category").is(
				new Category(categoryId)));
		specificationDao.remove(query, Specification.class);
	}

}
