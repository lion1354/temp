package com.tibco.ma.controller;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.model.Category;
import com.tibco.ma.service.CategoryService;
import com.tibco.ma.service.SpecificationService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@Controller
@RequestMapping("ma/1/category")
public class CategoryController {
	private static final Logger log = LoggerFactory
			.getLogger(CategoryController.class);
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SpecificationService specificationService;
	/**
	 * 
	 * @param jsonObject
	 * {
	 * 	categoryName:${categoryName}
	 * }
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Log(operate = "Add category", modelName = "Core")
	@ApiOperation(value = "add category", notes = "add category")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ResponseEntity<?> add(@ApiParam(value = "jsonObject") @RequestBody JSONObject jsonObject) {
//		log.info("jsonObject:" + jsonObject);
		String categoryName = (String) jsonObject.get("categoryName");
		Category category = new Category();
		category.setName(categoryName);
		try {
			categoryService.save(category);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.fail("Create category fail");
		}
		jsonObject.put("category", category.getId());
		specificationService.add(jsonObject);

		return ResponseUtils.success();
	}

	/**
	 * 
	 * @param categoryName
	 * @return
	 */
	@ApiOperation(value = "query category by name", notes = "query category by name")
	@RequestMapping(value = "queryByName", method = RequestMethod.GET)
	public ResponseEntity<?> queryByName(@RequestParam("categoryName") String categoryName) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(categoryName));
		try {
			Category category = categoryService.findOne(query, Category.class);
			return ResponseUtils.successWithValue(category);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.fail(e.getMessage());
		}
	}

}
