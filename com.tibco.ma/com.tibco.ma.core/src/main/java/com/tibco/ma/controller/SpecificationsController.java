package com.tibco.ma.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.ValidateUtils;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.model.Category;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.Specification;
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
@RequestMapping("ma/1/specification")
public class SpecificationsController {

	private static final Logger log = LoggerFactory
			.getLogger(SpecificationsController.class);
	@Autowired
	private SpecificationService specificationService;

	@Autowired
	private CategoryService categoryService;

	/**
	 * status:OK
	 * 
	 * @param jsonObject
	 *            { categoryId:${category} comment:${comment}
	 * 
	 *            { specList:[ id:${id} name:${name} width:${width}
	 *            height:${height}
	 * 
	 *            ],... } }
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Log(operate = "Add Specification", modelName = "Core")
	@ApiOperation(value = "add specification", notes = "add specification")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ResponseEntity<?> add(
			@ApiParam(value = "json") @RequestBody JSONObject jsonObject) {
		try {
			ArrayList<LinkedHashMap> jsonArray = (ArrayList<LinkedHashMap>) jsonObject
					.get("specList");
			List<String> names = new ArrayList<String>();
			for (LinkedHashMap map : jsonArray) {
				String name = map.get("name").toString();
				names.add(name);
			}

			for (LinkedHashMap map : jsonArray) {
				String name = map.get("name").toString();
				int count = Collections.frequency(names, name);
				if (count != 1) {
					return ResponseUtils
							.alert("The specification name is repeat, please check!");
				}
			}

			String categoryId = jsonObject.get("category").toString();

			for (LinkedHashMap specification : jsonArray) {
				String id = null;
				if (specification.get("id") != null) {
					id = specification.get("id").toString();
				}
				String name = specification.get("name").toString();
				Query query = new Query(Criteria.where("name").is(name));
				query.addCriteria(Criteria.where("category").is(
						new Category(categoryId)));
				Specification spe = specificationService.findOne(query,
						Specification.class);
				if (spe != null) {
					if (!spe.getId().equals(id)) {
						return ResponseUtils
								.alert("The specification name is repeat, please check!");
					}
				}
			}

			specificationService.add(jsonObject);
			log.debug("add success");
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseUtils.fail("Error");
		}
	}

	/**
	 * 
	 * @param jsonObject
	 *            { id:${id} categoryId:${category} comment:${comment}
	 *            name:${name} width:${width} height:${height} }
	 * @return
	 */
	@Log(operate = "Update Specification", modelName = "Core")
	@ApiOperation(value = "update specification", notes = "update specification")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public ResponseEntity<?> update(
			@ApiParam(value = "json") @RequestBody JSONObject jsonObject) {
		Category category = new Category();
		String categoryId = (String) jsonObject.get("category");
		category.setId(categoryId);
		String comment = (String) jsonObject.get("comment");
		String name = ((String) jsonObject.get("name")).trim();
		Float width = new Float(jsonObject.get("width") + "");
		Float heigth = new Float(jsonObject.get("height") + "");
		String id = (String) jsonObject.get("id");
		Specification specification = new Specification();
		specification.setId(id);
		specification.setCategory(category);
		specification.setComment(comment);
		specification.setHeight(heigth);
		specification.setName(name);
		specification.setwidth(width);

		Query query = new Query();
		query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
				new ObjectId(id)));
		Update update = new Update();
		update.set("name", specification.getName())
				.set("width", specification.getwidth())
				.set("height", specification.getHeight())
				.set("comment", specification.getComment());
		try {
			specificationService.update(query, update, Specification.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 
	 * @param json
	 *            { id:${id} }
	 * @return
	 */
	@Log(operate = "Delete Specification", modelName = "Core")
	@ApiOperation(value = "delete specification", notes = "delete specification")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(
			@ApiParam(value = "json") @RequestBody JSONObject json) {
		String id = (String) json.get("id");
		Query query = new Query();
		query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(id));
		try {
			specificationService.delete(query, Specification.class);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return ResponseUtils.success();
	}

	/**
	 * @deprecated getAll
	 * @return
	 */
	@ApiOperation(value = "get all specification", notes = "get all specification")
	@RequestMapping(value = "getAll", method = RequestMethod.POST)
	public ResponseEntity<?> getAllSpecifications() {
		try {
			List<Specification> specifications = specificationService.find(
					new Query(), Specification.class);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			for (Specification specification : specifications) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", specification.getId());

				if (specification.getCategory() != null) {
					Category category = categoryService.findOne(
							new Query().addCriteria(Criteria.where(
									MongoDBConstants.DOCUMENT_ID).is(
									specification.getCategory().getId())),
							Category.class);
					map.put("category", category != null ? category.getName()
							: null);
				} else {
					map.put("category", null);
				}
				map.put("name", specification.getName());
				map.put("width", specification.getwidth());
				map.put("height", specification.getHeight());
				map.put("comment", specification.getComment());
				dataList.add(map);
			}
			return ResponseUtils.successWithValues(dataList);

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 
	 * @param _id
	 * @return
	 */
	@ApiOperation(value = "query specification by id", notes = "query specification by id")
	@RequestMapping(value = "queryByID", method = RequestMethod.GET)
	public ResponseEntity<?> queryById(
			@ApiParam(value = "specification id") @RequestParam(value = "id") String specificationId) {
		try {
			List<Specification> specifications = specificationService.find(
					new Query().addCriteria(Criteria.where(
							MongoDBConstants.DOCUMENT_ID).is(
							new ObjectId(specificationId))),
					Specification.class);

			for (Specification specification : specifications) {

				if (specification.getCategory() != null) {
					Category category = categoryService.findOne(
							new Query().addCriteria(Criteria.where(
									MongoDBConstants.DOCUMENT_ID).is(
									specification.getCategory().getId())),
							Category.class);
					specification.setCategory(category);
				}
			}

			Specification specification = ValidateUtils
					.isValidate(specifications) ? specifications.get(0) : null;
			return ResponseUtils.successWithValue(specification);
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 
	 * @param categoryId
	 * @return
	 */
	@ApiOperation(value = "query by category id", notes = "query by category id")
	@RequestMapping(value = "queryByCategorID", method = RequestMethod.GET)
	public ResponseEntity<?> queryByCategoryID(
			@ApiParam(value = "category id") @RequestParam(value = "id") String categoryId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("category").is(
				new Category(categoryId)));
		try {
			List<Specification> specifications = specificationService.find(
					query, Specification.class);
			return ResponseUtils.successWithValues(specifications);
		} catch (Exception e) {
			log.error("{}", e);
			e.printStackTrace();
			return ResponseUtils.fail(e.getMessage());
		}
	}
}
