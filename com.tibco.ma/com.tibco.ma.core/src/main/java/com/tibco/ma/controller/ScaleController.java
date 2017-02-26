package com.tibco.ma.controller;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.ScaleStaticsValue;
import com.tibco.ma.service.BaseService;
import com.tibco.ma.service.OrderDispatcher;
import com.tibco.ma.service.ScaleService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * 
 * @author duke
 * 
 */
@Controller
@RequestMapping("ma/1/scale")
public class ScaleController extends BaseController<ScaleStaticsValue> {

	private static final Logger log = LoggerFactory
			.getLogger(ScaleController.class);

	@Resource
	private ScaleService service;

	@Resource
	private OrderDispatcher dispatcher;

	@Override
	public BaseService<ScaleStaticsValue> getService() {
		return service;
	}

	@Log(operate = "Get Scale by id", modelName = "Scale")
	@ApiOperation(value = "get one scale", notes = "get one scale")
	@RequestMapping(value = "load", method = RequestMethod.GET)
	public ResponseEntity<?> getOne(
			@ApiParam(value = "scale id") @RequestParam("id") String ScaleId)
			throws Exception {
		try {
			ScaleStaticsValue value = service.findById(new ObjectId(ScaleId),
					ScaleStaticsValue.class);
			return ResponseUtils.successWithValue(value);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Log(operate = "Delete Scale", modelName = "Scale")
	@ApiOperation(value = "delete scale", notes = "delete scale")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(
			@ApiParam(value = "json") @RequestBody JSONObject json) {
		try {
			if (json == null) {
				throw new Exception("id is necessary");
			}
			ArrayList<String> id = (ArrayList<String>) json.get("id");
			for (String string : id) {
				service.updateStatus(string);
			}
			dispatcher.init();
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}

	}

	@Log(operate = "Add Scale", modelName = "Scale")
	@ApiOperation(value = "save scale", notes = "save scale")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "json") @RequestBody JSONObject json) {
		try {
			if (json.containsKey("name") && json.get("name") != null) {
				String name = json.get("name").toString();
				if (name.contains(" ")) {
					return ResponseUtils
							.fail("The Scale Name can't have spaces, please check it!");
				}
			} else {
				return ResponseUtils.fail("The name is required!");
			}

			ScaleStaticsValue value = packToObj(json);
			Query query = new Query(Criteria.where("name").is(value.getName()));
			ScaleStaticsValue valueByName = service.findOne(query,
					ScaleStaticsValue.class);

			if (StringUtil.isEmpty(value.getId())) {
				if (valueByName != null) {
					return ResponseUtils
							.alert("The scale name is repeat, please input other name!");
				}
			} else {
				Query query1 = new Query(Criteria.where("name").is(
						value.getName()));
				query1.addCriteria(Criteria.where("id").is(
						new ObjectId(value.getId())));
				ScaleStaticsValue valueByIdName = service.findOne(query1,
						ScaleStaticsValue.class);
				if (valueByName != null && valueByIdName == null) {
					return ResponseUtils
							.alert("The scale name is repeat, please input other name!");
				}
			}

			service.saveValue(value);
			dispatcher.init();
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}

	}

	private ScaleStaticsValue packToObj(JSONObject json) {
		ScaleStaticsValue value = new ScaleStaticsValue();
		if (json.containsKey("id")) {
			value.setId(json.get("id").toString());
		}
		if (json.containsKey("orderCnt")) {
			value.setOrderCnt(Integer.parseInt(json.get("orderCnt").toString()));
		}
		if (json.containsKey("name")) {
			value.setName(json.get("name").toString());
		}
		if (json.containsKey("status")) {
			value.setStatus(Integer.parseInt(json.get("status").toString()));
		}

		return value;
	}

	@Override
	public Query getQuery(JSONObject json) {
		return new Query();
	}

	@Override
	public Class<ScaleStaticsValue> getEntityClass() {
		return ScaleStaticsValue.class;
	}

	@Override
	public Pager<ScaleStaticsValue> getPager() {
		Pager<ScaleStaticsValue> pager = new Pager<ScaleStaticsValue>();
		pager.setPagesize(20);
		return pager;
	}

	@Override
	public String getCollection() {
		return "scale";
	}

}
