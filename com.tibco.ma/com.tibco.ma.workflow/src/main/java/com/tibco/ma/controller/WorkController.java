package com.tibco.ma.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tibco.ma.common.Constants;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.engine.WorkflowEngine;
import com.tibco.ma.engine.core.Execution;
import com.tibco.ma.model.Connector;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.Parameters;
import com.tibco.ma.model.Work;
import com.tibco.ma.service.BaseService;
import com.tibco.ma.service.ConnectorService;
import com.tibco.ma.service.ParametersService;
import com.tibco.ma.service.WorkService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping("ma/1/work")
public class WorkController extends BaseController<Work> {
	private static final Logger log = LoggerFactory
			.getLogger(WorkController.class);

	@Resource
	private WorkService service;

	@Resource
	private WorkflowEngine engine;

	@Resource
	private ParametersService parametersService;

	@Resource
	private ConnectorService connectorService;

	@Log(operate = "load works", modelName = "workflow")
	@ApiOperation(value = "load works", notes = "load works", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "load", method = RequestMethod.GET)
	public ResponseEntity<?> load(
			@ApiParam(value = "App id", required = true) @RequestParam(value = "appId", required = true) String appId,
			HttpServletRequest request) {
		try {
			Query query = new Query(Criteria.where("appId").is(appId));
			List<Work> list = service.find(query, Work.class);
			return ResponseUtils.successWithValues(list);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@ApiOperation(value = "load connectors by id", notes = "load connectors by id", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "loadConnectors", method = RequestMethod.GET)
	public ResponseEntity<?> LoadConnectorsById(
			@ApiParam(value = "work id", required = true) @RequestParam(value = "id", required = true) String id,
			HttpServletRequest request) {
		try {
			Work work = service.findById(new ObjectId(id), Work.class);
			List<Map<String, Object>> list = work.getConnectors();
			if (list != null) {
				for (Map<String, Object> map : list) {
					Connector connector = connectorService.findById(
							new ObjectId(map.get("connectorId").toString()),
							Connector.class);
					if (map.get("displayName") != null
							&& StringUtil.notEmpty(map.get("displayName")
									.toString())) {
						connector.setName(map.get("displayName").toString());
					}
					connector.setIconUrl(request.getContextPath()
							+ Constants.IMAGE_CONNECTOR_SHOWRES
							+ connector.getIconUrl());
					Parameters parameters = parametersService.findById(
							new ObjectId(map.get("parameterId").toString()),
							Parameters.class);
					if (parameters.getValues() != null) {
						connector.setConfigurations(parameters.getValues());
					}
					map.put("connector", connector);
				}
				return ResponseUtils.successWithValues(list);
			} else {
				return ResponseUtils.success();
			}
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Log(operate = "save work", modelName = "workflow")
	@ApiOperation(value = "save work", notes = "save work", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "json") @RequestBody JSONObject json,
			HttpServletRequest request) {
		try {
			String name = null;
			String appId = json.get("appId").toString();
			String id = null;
			if (json.containsKey("id")) {
				id = json.get("id").toString();
			}
			if (json.containsKey("name")) {
				name = json.get("name").toString();
				Query queryByName = new Query(Criteria.where("appId").is(appId));
				queryByName.addCriteria(Criteria.where("name").is(name));
				Work workByName = service.findOne(queryByName, Work.class);
				if (workByName != null && !(workByName.getId().equals(id))) {
					return ResponseUtils.alert("The name has exist!");
				}
			} else {
				return ResponseUtils.alert("The name is required!");
			}

			List<Map<String, Object>> connectors = null;
			if (json.containsKey("connectors")) {
				connectors = (List<Map<String, Object>>) json.get("connectors");
			}

			Work work = new Work();
			if (StringUtil.isEmpty(id)) {
				work.setName(name);
				work.setConnectors(connectors);
				work.setAppId(appId);
				String time = System.currentTimeMillis() + "";
				work.setCreateAt(time);
				work.setUpdateAt(time);
				service.save(work);
				return ResponseUtils.successWithValue(work);
			} else {
				Query query = new Query(Criteria.where(
						MongoDBConstants.DOCUMENT_ID).is(new ObjectId(id)));
				Update update = Update.update("name", name);
				if (connectors != null) {
					update.set("connectors", connectors);
				}
				update.set("updateAt", System.currentTimeMillis() + "");
				service.update(query, update, Work.class);
				return ResponseUtils.successWithValue(id);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@Log(operate = "execute work", modelName = "workflow")
	@ApiOperation(value = "execute work", notes = "execute work", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "execute", method = RequestMethod.POST)
	public ResponseEntity<?> execute(
			@ApiParam(value = "workId", name = "work id") @RequestBody(required = true) net.sf.json.JSONObject workId) {
		Execution execution = engine.startWorkflowById(workId
				.getString("workId"));
		return ResponseUtils.successWithValue(execution);
	}

	@ApiOperation(value = "delete work by id", notes = "delete work by id", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "deleteById", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(
			@ApiParam(value = "json") @RequestBody String json) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(json);
			if (!jsonObject.containsKey("id"))
				return ResponseUtils.fail("id is required!");

			log.info("work id: {}", json);
			String id = jsonObject.get("id").toString();
			String[] ids = id.split(",");
			for (String wId : ids) {
				Work work = service.findById(new ObjectId(wId), Work.class);
				service.delete(
						new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID)
								.is(new ObjectId(id))), Work.class);
				List<Map<String, Object>> connectors = work.getConnectors();
				if (connectors != null && connectors.size() > 0) {
					List<String> cIds = new ArrayList<String>();
					for (Map<String, Object> connector : connectors) {
						cIds.add(connector.get("parameterId").toString());
					}
					parametersService.delete(
							new Query(Criteria.where(
									MongoDBConstants.DOCUMENT_ID).in(cIds)),
							Parameters.class);
				}

			}
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@Override
	public BaseService<Work> getService() {
		return service;
	}

	@Override
	public Query getQuery(JSONObject json) {
		return new Query();
	}

	@Override
	public Class<Work> getEntityClass() {
		return Work.class;
	}

	@Override
	public Pager<Work> getPager() {
		return new Pager<Work>();
	}

	@Override
	public String getCollection() {
		return "connector";
	}

}
