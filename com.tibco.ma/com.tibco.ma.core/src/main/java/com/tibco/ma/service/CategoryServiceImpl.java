package com.tibco.ma.service;

import java.io.Serializable;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.CategoryDao;
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
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements
		CategoryService {

	private static Logger log = LoggerFactory
			.getLogger(CategoryServiceImpl.class);
	@Resource
	private CategoryDao categoryDao;

	@Resource
	private SpecificationDao specificationDao;

	@Override
	public BaseDao<Category> getDao() {
		return categoryDao;
	}

	@Override
	public void deleteCategoryAndSpecificationByCategoryId(Serializable id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("category").is(
				new Category((String) id)));
		specificationDao.remove(query, Specification.class);

		Query query2 = new Query();
		query2.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(id));
		categoryDao.remove(query2, Category.class);
	}
}
