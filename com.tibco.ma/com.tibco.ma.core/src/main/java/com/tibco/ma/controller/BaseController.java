package com.tibco.ma.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.BaseService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * 
 * @author aidan
 * 
 */
public abstract class BaseController<T> {
	private static final Logger log = LoggerFactory
			.getLogger(BaseController.class);

	protected abstract BaseService<T> getService();

	protected abstract Query getQuery(JSONObject json);

	protected abstract Class<T> getEntityClass();

	protected abstract Pager<T> getPager();

	protected abstract String getCollection();

	protected void otherOperation(HttpServletRequest request, Pager<T> pager)
			throws Exception {

	}

	protected void otherOperation(HttpServletRequest request, T t)
			throws Exception {

	}

	protected String getUserName() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		log.info(userDetails.toString());
		String username = userDetails.getUsername();
		return username;
	}

	/**
	 * print
	 * 
	 * @param o
	 */
	protected void print(Object o) {
		System.out.println(o);
	}

	/**
	 * Package the return value
	 * 
	 * @param result
	 * @param status
	 * @return
	 */
	protected ResponseEntity<?> resultEntity(String result, HttpStatus status) {
		return new ResponseEntity<String>(result, status);
	}

	/**
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "get one by id", notes = "get one by id", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "getOneById", method = RequestMethod.GET)
	public ResponseEntity<?> getOneById(
			@ApiParam(value = "object id", required = true) @RequestParam(value = "id", required = true) String id,
			HttpServletRequest request) {
		try {
			T t = getService().findById(new ObjectId(id), getEntityClass());
			otherOperation(request, t);
			return ResponseUtils.successWithValue(t);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@ApiOperation(value = "delete by id", notes = "delete by id", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "deleteById", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(
			@ApiParam(value = "json") @RequestBody String json) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(json);
			if (!jsonObject.containsKey("id"))
				return ResponseUtils.fail("id is required!");

			log.info("parameter id: {}", json);
			String id = jsonObject.get("id").toString();

			if (id.indexOf(",") != -1) {
				String[] ids = id.split(",");
				List<ObjectId> list = new ArrayList<ObjectId>();
				for (int i = 0; i < ids.length; i++) {
					list.add(new ObjectId(ids[i]));
				}
				getService().delete(
						new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID)
								.in(list)), getEntityClass());
			} else {
				getService().delete(
						new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID)
								.is(new ObjectId(id))), getEntityClass());
			}
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 
	 * @param nowpage
	 * @param pagesize
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "query data list", notes = "query data list", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<?> queryPage(
			@ApiParam(value = "now page", required = false) @RequestParam(value = "nowpage", required = false) String nowpage,
			@ApiParam(value = "page size", required = false) @RequestParam(value = "pagesize", required = false) String pagesize,
			HttpServletRequest request) {
		try {
			Query query = getQuery(null);
			Pager<T> pager = getPager();
			pager.setNowpage(nowpage);
			if (StringUtils.isNotEmpty(pagesize)) {
				pager.setPagesize(Integer.parseInt(pagesize));
			} else {
				pager.setPagesize(20);
			}
			BaseService<T> baseService = getService();
			List<T> list = baseService.find(query,
					(int) ((pager.getNowpage() - 1) * pager.getPagesize()),
					(int) pager.getPagesize(), getEntityClass());
			long count = baseService.count(query, getEntityClass());
			pager.setCountrow(count);
			pager.setMaxPageIndex(10);
			long countpage = pager.getCountrow() % pager.getPagesize() == 0 ? pager
					.getCountrow() / pager.getPagesize()
					: pager.getCountrow() / pager.getPagesize() + 1;
			pager.setCountpage(countpage);
			pager.showPage();
			pager.setData(list);
			otherOperation(request, pager);
			return ResponseUtils.successWithValue(pager);

		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 
	 * @param request
	 * @param json
	 * @return
	 */
	@ApiOperation(value = "query data list", notes = "query data list", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/page", method = RequestMethod.POST)
	public ResponseEntity<?> queryPage2(
			HttpServletRequest request,
			@ApiParam(value = "query param", required = false) @RequestBody(required = false) JSONObject json) {
		try {
			String nowpage = null, pagesize = null;
			if (json != null) {
				if (json.containsKey("nowpage")) {
					nowpage = json.get("nowpage").toString();
				}
				if (json.containsKey("pagesize")) {
					pagesize = json.get("pagesize").toString();
				}
			}
			Query query = getQuery(json);
			Pager<T> pager = getPager();
			pager.setNowpage(nowpage);
			if (StringUtils.isNotEmpty(pagesize)) {
				pager.setPagesize(Integer.parseInt(pagesize));
			} else {
				pager.setPagesize(20);
			}
			BaseService<T> baseService = getService();
			List<T> list = baseService.find(query,
					(int) ((pager.getNowpage() - 1) * pager.getPagesize()),
					(int) pager.getPagesize(), getEntityClass());
			long count = baseService.count(query, getEntityClass());
			pager.setCountrow(count);
			pager.setMaxPageIndex(10);
			long countpage = pager.getCountrow() % pager.getPagesize() == 0 ? pager
					.getCountrow() / pager.getPagesize()
					: pager.getCountrow() / pager.getPagesize() + 1;
			pager.setCountpage(countpage);
			pager.showPage();
			pager.setData(list);
			otherOperation(request, pager);
			return ResponseUtils.successWithValue(pager);

		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}
}
