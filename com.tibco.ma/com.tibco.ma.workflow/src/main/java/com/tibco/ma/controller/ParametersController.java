package com.tibco.ma.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.query.Pager;
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
@RequestMapping("ma/1/parameters")
public class ParametersController extends BaseController<Parameters> {
	private static final Logger log = LoggerFactory
			.getLogger(ParametersController.class);

	@Resource
	private ParametersService service;

	@Resource
	private WorkService workService;

	@Resource
	private ConnectorService connectorService;

	@Log(operate = "save Parameters", modelName = "workflow")
	@ApiOperation(value = "save Parameters", notes = "save Parameters", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "json") @RequestBody JSONObject json,
			HttpServletRequest request) {
		try {
			if (!json.containsKey("connectorId")) {
				return ResponseUtils.alert("The connector id is required!");
			}

			String id = null;
			if (json.containsKey("id")) {
				id = json.get("id").toString();
			}
			JSONObject values = null;
			if (json.containsKey("values")) {
				values = JSONObject.fromObject(json.get("values").toString());
			}
			String connectorId = json.get("connectorId").toString();

			Parameters parameters = new Parameters();
			if (StringUtil.isEmpty(id)) {
				parameters.setConnectorId(connectorId);
				parameters.setCreateAt(System.currentTimeMillis() + "");
				parameters.setUpdateAt(System.currentTimeMillis() + "");
				parameters.setValues(values);
				service.save(parameters);
			} else {
				Document update = new Document("values", values);
				update.append("updateAt", System.currentTimeMillis() + "");
				service.updateById("parameters", id, update);
			}

			Work work = workService.findById(new ObjectId(json.get("workId")
					.toString()), Work.class);
			List<Map<String, Object>> connectors = work.getConnectors();
			if (connectors == null) {
				connectors = new ArrayList<Map<String, Object>>();
			}

			if (values != null && values.containsKey("Default")) {
				JSONObject jsonObject = (JSONObject) values.get("Default");
				String displayName = jsonObject.get("data").toString();
				Boolean label = false;
				if (StringUtil.notEmpty(id)) {
					for (Map<String, Object> map : connectors) {
						if ((map.get("parameterId").toString()).equals(id)) {
							if (!map.get("displayName").equals(displayName)) {
								map.put("displayName", displayName);
								workService.updateById("work", work.getId(),
										new Document("connectors", connectors));
							}
							label = true;
							break;
						}
					}
				}
				if (!label) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("connectorId", connectorId);
					map.put("displayName", displayName);
					map.put("parameterId", parameters.getId());
					connectors.add(map);
					workService.updateById("work", work.getId(), new Document(
							"connectors", connectors));
				}
			} else {
				Connector connector = connectorService.findById(new ObjectId(
						connectorId), Connector.class);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("connectorId", connectorId);
				map.put("displayName", connector.getName());
				map.put("parameterId",
						StringUtil.isEmpty(id) ? parameters.getId() : id);
				connectors.add(map);
				workService.updateById("work", work.getId(), new Document(
						"connectors", connectors));
			}

			return ResponseUtils
					.successWithValue(StringUtil.isEmpty(id) ? parameters : id);
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@ApiOperation(value = "delete parameters by id", notes = "delete parameters by id", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "deleteById", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(
			@ApiParam(value = "json") @RequestBody String json) {
		try {
			JSONObject jsonObject = JSONObject.fromObject(json);
			if (!jsonObject.containsKey("id"))
				return ResponseUtils.fail("id is required!");

			log.info("parameter id: {}", json);
			String id = jsonObject.get("id").toString();
			String workId = jsonObject.get("workId").toString();
			Work work = workService.findById(new ObjectId(workId), Work.class);
			List<Map<String, Object>> connectors = work.getConnectors();
			String[] ids = id.split(",");
			for (String pid : ids) {
				Query query = new Query(Criteria.where(
						MongoDBConstants.DOCUMENT_ID).is(new ObjectId(pid)));
				service.delete(query, Parameters.class);
				for (Map<String, Object> map : connectors) {
					if (map.get("parameterId").toString().equals(pid)) {
						connectors.remove(map);
						workService.updateById("work", workId, new Document(
								"connectors", connectors));
						break;
					}
				}
			}
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@Override
	public BaseService<Parameters> getService() {
		return service;
	}

	@Override
	public Query getQuery(org.json.simple.JSONObject json) {
		return new Query();
	}

	@Override
	public Class<Parameters> getEntityClass() {
		return Parameters.class;
	}

	@Override
	public Pager<Parameters> getPager() {
		return new Pager<Parameters>();
	}

	@Override
	public String getCollection() {
		return "parameters";
	}

}
