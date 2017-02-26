package com.tibco.ma.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.AppSettingService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 20151013
 */
@Controller
@RequestMapping("ma/1/appSetting")
public class AppSettingController {

	private static final Logger log = LoggerFactory
			.getLogger(AppSettingController.class);
	@Autowired
	private AppSettingService appSettingService;
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param document
	 * {
	 * 	 	nowpage:${nowpage}
	 * 		pagesize:${pagesize}
	 * 		appId:${appId}
	 * }
	 * @return
	 */
	@ApiOperation(value = "query app setting", notes = "query app setting")
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public ResponseEntity<?> query(HttpServletRequest request,
			HttpServletResponse response,
			@ApiParam(value = "query document") @RequestBody Document document) {
		try {
			Integer nowPage = document.getInteger("nowpage");
			Integer pageSize = document.getInteger("pagesize");
			if (nowPage == null || nowPage < 1) {
				nowPage = 1;
			}
			if (pageSize == null || pageSize < 1) {
				pageSize = 20;
			}
			log.debug("query :{}", document);
			String appId = document.getString("appId");
			document.put("appId", new ObjectId(appId));
			document.remove("nowpage");
			document.remove("pagesize");
			String projectURI = request.getContextPath();

			Pager<Document> pager = appSettingService.valuesPage(
					getCollection(), document, new Document(), nowPage,
					pageSize, appId, projectURI);
			return ResponseUtils.successWithValue(pager);
		} catch (Exception e) {
			log.error("query :{ }", e);
			return ResponseUtils.fail(e.getMessage());

		}

	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @param document
	 * {
	 * 		id:${id}
	 * 		key:${key}
	 * 		appId:${appId}
	 * 		value:${value}
	 * 		type:${type}
	 * }
	 * @return
	 */
	@Log(operate = "Save appSetting", modelName = "Setting")
	@ApiOperation(value = "save app setting", notes = "save app setting")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> save(HttpServletRequest request,
			HttpServletResponse response,
			@ApiParam(value = "save document") @RequestBody Document document) {
		try {
			String id = document.getString("id");
			String key = document.getString("key");
			String appId = document.getString("appId");
			String value = document.getString("value");
			String appType = document.getString("type");
			String description = document.getString("description");
			String msg = appSettingService.validateSaveValue(value, appType);
			if (StringUtil.notEmpty(msg)) {
				log.error("{}", msg);
				return ResponseUtils.alert(msg);
			}
			appSettingService.save(getCollection(), id, appId, value, appType,
					key, description);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error(" save: {}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param document
	 * {
	 * 		appId:${appId}
	 * 		key:${key}
	 * }
	 * @return
	 */
	@ApiOperation(value = "check app setting unique", notes = "check app setting unique")
	@RequestMapping(value = "uniqueCheck", method = RequestMethod.POST)
	public ResponseEntity<?> check(HttpServletRequest request,
			HttpServletResponse response,
			@ApiParam(value = "document") @RequestBody Document document) {
		String appId = document.getString("appId");
		String key = document.getString("key");
		Document query = new Document();
		query.append("appId", new ObjectId(appId));
		query.append("key", key);
		try {
			long count = appSettingService.count(getCollection(), query);
			return ResponseUtils.successWithValue(count);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.fail(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param doc
	 * {
	 * 		_id:${id}
	 * 		appId:${appId}
	 * }
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "load app setting, method is post", notes = "load app setting, method is post")
	@RequestMapping(value = "/load", method = RequestMethod.POST)
	public ResponseEntity<?> load(
			@ApiParam(value = "document") @RequestBody Document doc,
			HttpServletRequest request) throws Exception {
		String id = doc.getString("id");
		String appId = doc.getString("appId");
		Document document = new Document();
		document.append(MongoDBConstants.DOCUMENT_ID, new ObjectId(id));
		document.append("appId", new ObjectId(appId));
		String projectURI = request.getContextPath();

		List<Document> documents = appSettingService.queryAppSetting(
				getCollection(), document, null);
		
		return ResponseUtils.successWithValues(documents);

	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param document
	 * {
	 *    ids:
	 * }
	 * @return
	 */
	@Log(operate = "Delete appSetting", modelName = "Setting")
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "delete app setting, method is post", notes = "delete app setting, method is post")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(HttpServletRequest request,
			HttpServletResponse response,
			@ApiParam(value = "document") @RequestBody Document document) {
		try {

			List<String> ids = document.get("ids", List.class);
			log.debug("delete ids : {}", ids);
			if (ids != null && ids.size() > 0) {
				for (String id : ids) {
					appSettingService.deleteValue(getCollection(), id);
				}
				return ResponseUtils.success();
			} else {
				log.error("data error");
				return ResponseUtils.alert("Data error");
			}

		} catch (Exception e) {
			log.error("delete :{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	public String getCollection() {
		return "app_setting";
	}

}
