package com.tibco.ma.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.Constants;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.Connector;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.BaseService;
import com.tibco.ma.service.ConnectorService;
import com.tibco.ma.service.WorkflowGridFSService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * 
 * @author Aidan
 * 
 */
@Controller
@RequestMapping("ma/1/connector")
public class ConnectorController extends BaseController<Connector> {
	private static final Logger log = LoggerFactory
			.getLogger(ConnectorController.class);

	@Resource
	private ConnectorService service;

	@Resource
	private WorkflowGridFSService gridfsService;

	@Resource
	private ConfigInfo config;

	@Log(operate = "load connectors", modelName = "workflow")
	@ApiOperation(value = "load connectors", notes = "load connectors", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "load", method = RequestMethod.GET)
	public ResponseEntity<?> load(HttpServletRequest request) {
		try {
			List<Connector> list = service.find(null, Connector.class);
			for (Connector connector : list) {
				connector.setIconUrl(request.getContextPath()
						+ Constants.IMAGE_CONNECTOR_SHOWRES
						+ connector.getIconUrl());
			}
			return ResponseUtils.successWithValues(list);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@Log(operate = "save connector", modelName = "workflow")
	@ApiOperation(value = "save connector", notes = "save connector", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "connector icon", required = false) @RequestParam(value = "logo", required = false) CommonsMultipartFile logo,
			@ApiParam(value = "connector name", required = true) @RequestParam(value = "name", required = true) String name,
			@ApiParam(value = "connector id", required = false) @RequestParam(value = "id", required = false) String id,
			@ApiParam(value = "connector configurations", required = false) @RequestParam(value = "configurations", required = false) String configurations,
			HttpServletRequest request) {
		log.debug("connector name: " + name);
		try {
			Query queryByName = new Query(Criteria.where("name").is(name));
			Connector connector = service
					.findOne(queryByName, getEntityClass());
			if (connector != null && (!connector.getId().equals(id))) {
				return ResponseUtils.alert("The connector has exist!");
			}

			if (StringUtil.notEmpty(id)) {
				Document update = new Document();
				update.append("name", name);
				if (StringUtil.notEmpty(configurations)) {
					update.append("configurations",
							JSONObject.fromObject(configurations));
				}
				update.append("updateAt", System.currentTimeMillis() + "");

				if (logo != null) {
					Connector conn = service.findById(new ObjectId(id),
							Connector.class);
					if (conn.getIconUrl() != null) {
						gridfsService.deleteById(conn.getIconUrl());
					}

					String iconUrl = gridfsService.save(logo.getInputStream(),
							logo.getOriginalFilename());
					update.append("iconUrl", iconUrl);
				}
				service.updateById("connector", id, update);
				return ResponseUtils.success();
			} else {
				connector = new Connector();
				connector.setName(name);
				JSONObject json = new JSONObject();
				JSONObject def = new JSONObject();
				def.put("key", "DisplayName");
				def.put("type", "string");
				if (StringUtil.notEmpty(configurations)) {
					json = JSONObject.fromObject(configurations);
					if (!json.containsKey("Default")) {
						json.put("Default", def);
					}
				} else {
					json.put("Default", def);
				}

				connector.setConfigurations(json);
				connector.setCreateAt(System.currentTimeMillis() + "");
				connector.setUpdateAt(System.currentTimeMillis() + "");

				if (logo != null) {
					String iconUrl = gridfsService.save(logo.getInputStream(),
							logo.getOriginalFilename());
					connector.setIconUrl(iconUrl);
				}
				service.save(connector);
				return ResponseUtils.success();

			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@Override
	@ApiOperation(value = "delete by id", notes = "delete by id", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "deleteById", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(
			@ApiParam(value = "json") @RequestBody String json) {
		try {
			JSONObject jsonObject = JSONObject.fromObject(json);
			if (!jsonObject.containsKey("id"))
				return ResponseUtils.fail("id is required!");
			log.info("parameter id: {}", json);

			String[] ids = jsonObject.getString("id").split(",");
			for (String id : ids) {
				Connector connector = service.findById(new ObjectId(id),
						Connector.class);
				if (connector != null && connector.getIconUrl() != null) {
					gridfsService.deleteById(connector.getIconUrl());
					service.delete(
							new Query(Criteria.where(
									MongoDBConstants.DOCUMENT_ID).is(
									new ObjectId(id))), Connector.class);
				}
			}

			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@Override
	public BaseService<Connector> getService() {
		// TODO Auto-generated method stub
		return service;
	}

	@Override
	public Query getQuery(org.json.simple.JSONObject json) {
		// TODO Auto-generated method stub
		return new Query();
	}

	@Override
	public Class<Connector> getEntityClass() {
		// TODO Auto-generated method stub
		return Connector.class;
	}

	@Override
	public Pager<Connector> getPager() {
		// TODO Auto-generated method stub
		return new Pager<Connector>();
	}

	@Override
	public String getCollection() {
		// TODO Auto-generated method stub
		return "connector";
	}

	@Override
	public void otherOperation(HttpServletRequest request,
			Pager<Connector> pager) throws Exception {
		List<Connector> list = pager.getData();
		for (Connector connector : list) {
			connector.setIconUrl(request.getContextPath()
					+ Constants.IMAGE_CONNECTOR_SHOWRES
					+ connector.getIconUrl());
		}
	}

	@Override
	public void otherOperation(HttpServletRequest request, Connector connector)
			throws Exception {
		if(null != connector)
			connector.setIconUrl(request.getContextPath()
				+ Constants.IMAGE_CONNECTOR_SHOWRES + connector.getIconUrl());
	}
}
