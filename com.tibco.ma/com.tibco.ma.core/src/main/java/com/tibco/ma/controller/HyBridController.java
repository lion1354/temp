package com.tibco.ma.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tibco.ma.common.AWSS3Util;
import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.Constants;
import com.tibco.ma.common.HtmlUtil;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.AdminUser;
import com.tibco.ma.model.HyBrid;
import com.tibco.ma.service.AdminUserService;
import com.tibco.ma.service.AppService;
import com.tibco.ma.service.BaseService;
import com.tibco.ma.service.HyBridService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * 
 * @author duke
 * 
 */
@Controller
@RequestMapping("ma/1/hybrid")
public class HyBridController extends BaseController<HyBrid> {

	private static final Logger log = LoggerFactory
			.getLogger(HyBridController.class);

	@Resource
	private HyBridService service;
	@Resource
	private ConfigInfo configInfo;
	@Resource
	private AppService appService;
	@Resource
	private AdminUserService adminUserService;

	@Override
	public BaseService<HyBrid> getService() {
		return service;
	}

	/**
	 * find all functions by appId and functionType
	 * 
	 * @param appId
	 * @return
	 */
	@ApiOperation(value = "get all hybrids by appId", notes = "get all hybrids by appId")
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public ResponseEntity<?> all(
			@ApiParam(value = "appId") @RequestParam(value = "appId", required = true) String appId) {

		if (StringUtil.isEmpty(appId))
			return ResponseUtils.fail("AppId is null");
		log.info(appId);

		Query query = new Query();
		query.addCriteria(Criteria.where("appId").is(appId));

		try {
			List<HyBrid> list = service.find(query, HyBrid.class);
			return ResponseUtils.successWithValues(list);
		} catch (Exception e) {
			log.error("query hybrids code error : " + e);
			return ResponseUtils
					.fail("Query hybrids error : " + e.getMessage());
		}
	}

	@Log(operate = "Get hybrid by id", modelName = "hybrid")
	@ApiOperation(value = "get one hybrid", notes = "get one hybrid")
	@RequestMapping(value = "load", method = RequestMethod.GET)
	public ResponseEntity<?> getOne(
			@ApiParam(value = "hybrid id") @RequestParam("id") String hybridId)
			throws Exception {
		try {
			HyBrid value = service.findById(new ObjectId(hybridId),
					HyBrid.class);
			return ResponseUtils.successWithValue(value);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@Log(operate = "Create source url by name and appID", modelName = "hybrid")
	@ApiOperation(value = "Create source url", notes = "Create source url")
	@RequestMapping(value = "createUrl", method = RequestMethod.GET)
	public ResponseEntity<?> createUrl(
			@ApiParam(value = "hybrid id") @RequestParam(value = "id", required = true) String id,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			HyBrid hyBrid = service.findById(new ObjectId(id), HyBrid.class);
			String url = configInfo.getS3BucketUrl() + hyBrid.getKey();
			return ResponseUtils.successWithValue(url);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Log(operate = "Delete Hybrid", modelName = "Hybrid")
	@ApiOperation(value = "delete hybrid", notes = "delete hybrid")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(
			@ApiParam(value = "json") @RequestBody JSONObject json) {
		try {
			if (json == null) {
				throw new Exception("id is necessary");
			}
			ArrayList<String> ids = (ArrayList<String>) json.get("id");
			for (String id : ids) {
				Query query = new Query(Criteria.where("_id").is(
						new ObjectId(id)));
				HyBrid hyBrid = service.findOne(query, HyBrid.class);
				service.delete(query, HyBrid.class);
				AWSS3Util.deleteByKey(hyBrid.getKey());
			}
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}

	}

	@Log(operate = "Save Hybrid", modelName = "Hybrid")
	@ApiOperation(value = "save hybrid", notes = "save hybrid")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "json") @RequestBody JSONObject json,
			HttpServletRequest request) {
		try {
			String name = null;
			if (!json.containsKey("name")) {
				return ResponseUtils.fail("The name is required!");
			} else {
				name = json.get("name").toString();
			}
			String appId = null;
			if (!json.containsKey("appId")) {
				return ResponseUtils.fail("The appId is required!");
			} else {
				appId = json.get("appId").toString();
			}

			String id = null;
			if (json.containsKey("id")) {
				id = json.get("id").toString();
			}

			Query query = new Query(Criteria.where("name").is(name));
			query.addCriteria(Criteria.where("appId").is(appId));
			HyBrid valueByName = service.findOne(query, HyBrid.class);

			if (valueByName != null && !valueByName.getId().equals(id)) {
				return ResponseUtils
						.alert("The hybrid name is repeat, please input other name!");
			}

			String content = "";
			if (StringUtil.notEmpty(id)) {
				HyBrid brid = service.findById(new ObjectId(id), HyBrid.class);
				if (!name.equals(brid.getName())) {
					AWSS3Util.deleteByKey(brid.getKey());
				}
				if (brid.getContent() != null) {
					content = brid.getContent();
				}
			}

			if (json.containsKey("content")) {
				content = json.get("content").toString();
			}
			String code = HtmlUtil.bodyBuild(name, content);
			File file = createFile(
					Constants.STATICHTML + "_" + name.replaceAll(" ", ""), code);

			AdminUser user = adminUserService.findOne(
					new Query(Criteria.where("username").is(getUserName())),
					AdminUser.class);
			String key = Constants.STATICHTML + "/" + user.getId() + "/"
					+ appId + "/" + name.replaceAll(" ", "") + ".html";

			AWSS3Util.UploadFile(key, file, true);

			HyBrid hyBrid = null;
			if (StringUtil.isEmpty(id)) {
				hyBrid = new HyBrid();
				hyBrid.setAppId(appId);
				hyBrid.setName(name);
				hyBrid.setContent(content);
				hyBrid.setKey(key);
				hyBrid.setCreateAt(System.currentTimeMillis() + "");
				hyBrid.setUpdateAt(System.currentTimeMillis() + "");
				service.save(hyBrid);
			} else {
				Query queryById = new Query(Criteria.where("_id").is(
						new ObjectId(id)));
				Update update = Update.update("name", name);
				if (json.containsKey("content")) {
					update.set("content", content);
				}
				update.set("key", key);
				update.set("updateAt", System.currentTimeMillis() + "");
				service.update(queryById, update, HyBrid.class);
			}

			return ResponseUtils
					.successWithValue(StringUtil.isEmpty(id) ? hyBrid : id);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}

	}

	private static File createFile(String name, String code) throws IOException {
		File file = File.createTempFile(name, ".html");
		file.deleteOnExit();

		Writer writer = new OutputStreamWriter(new FileOutputStream(file));
		writer.write(code);
		writer.close();

		return file;
	}

	@Override
	public Query getQuery(JSONObject json) {
		return new Query();
	}

	@Override
	public Class<HyBrid> getEntityClass() {
		return HyBrid.class;
	}

	@Override
	public Pager<HyBrid> getPager() {
		Pager<HyBrid> pager = new Pager<HyBrid>();
		pager.setPagesize(20);
		return pager;
	}

	@Override
	public String getCollection() {
		return "hybrid";
	}

}
