package com.tibco.ma.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
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

import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.App;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.ScaleStaticsValue;
import com.tibco.ma.model.Schedule;
import com.tibco.ma.service.BaseService;
import com.tibco.ma.service.OrderDispatcher;
import com.tibco.ma.service.ScaleService;
import com.tibco.ma.service.ScheduleService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping("ma/1/schedule")
public class ScheduleController extends BaseController<Schedule> {
	private static Logger log = LoggerFactory
			.getLogger(ScheduleController.class);

	@Resource
	private ScheduleService service;

	@Resource
	private ScaleService scaleService;

	@Resource
	private ConfigInfo configInfo;

	@Resource
	private OrderDispatcher orderDispatcher;

	/**
	 * find all schedule by appId
	 * 
	 * @param appId
	 * @return
	 */
	@ApiOperation(value = "get all schedule by id", notes = "get all shedule by id")
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public ResponseEntity<?> all(
			@ApiParam(value = "appId") @RequestParam String appId) {
		if (StringUtil.isEmpty(appId))
			return ResponseUtils.fail("AppId is null");
		log.info(appId);

		Query query = new Query();
		query.addCriteria(Criteria.where("app").is(new App(appId)));
		try {
			List<Schedule> list = service.find(query, Schedule.class);
			return ResponseUtils.successWithValues(list);
		} catch (Exception e) {
			log.error("query schedule error : " + e);
			return ResponseUtils.fail("Query schedule error : "
					+ e.getMessage());
		}
	}

	/**
	 * find schedule by id
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "get one schedule by id", notes = "get one shedule by id")
	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ResponseEntity<?> get(
			@ApiParam(value = "schedule id") @RequestParam String id) {
		if (StringUtil.isEmpty(id))
			return ResponseUtils.fail("Schedule id is null");

		try {
			Schedule schedule = service.findById(new ObjectId(id),
					Schedule.class);
			return ResponseUtils.successWithValue(schedule);
		} catch (Exception e) {
			log.error("query schedule error : " + e);
			return ResponseUtils.fail("Query schedule error : "
					+ e.getMessage());
		}
	}

	/**
	 * save schedule
	 * 
	 * @param json
	 * @return
	 */
	@ApiOperation(value = "save schedule", notes = "save schedule")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(
			@ApiParam(value = "save schedule param") @RequestBody net.sf.json.JSONObject json) {
		if (!json.has("appId") || StringUtil.isEmpty(json.getString("appId")))
			return ResponseUtils.fail("AppId is null");

		if (json.has("id") && StringUtil.notEmpty(json.getString("id"))) {

			Document update = new Document();

			try {
				Schedule schedule = service.findById(
						new ObjectId(json.getString("id")), Schedule.class);
				if (schedule == null)
					return ResponseUtils.fail("Id is incorrect");

				if (!json.has("name")
						|| StringUtil.isEmpty(json.getString("name")))
					return ResponseUtils.fail("Name is null");

				if (json.has("rule") && json.getJSONObject("rule") != null) {
					net.sf.json.JSONObject rule = json.getJSONObject("rule");
					update.append("rule", rule);
				} else {
					return ResponseUtils.fail("Rule is null");
				}

				if (json.has("functionIds") && json.get("functionIds") != null) {
					update.append("functionIds", json.get("functionIds"));
				} else {
					return ResponseUtils.fail("Function is null");
				}

				update.append("status", 1);

				service.updateById(getCollection(), json.getString("id"),
						update);
				
				 eval(json.getString("id"), "start");
//				 return ResponseUtils.successWithValue(result);
				return ResponseUtils.success();
			} catch (Exception e) {
				log.error("error" + e);
				return ResponseUtils.fail("Error" + e.getMessage());
			}
		} else {
			if (!json.has("name") || StringUtil.isEmpty(json.getString("name")))
				return ResponseUtils.fail("Name is null");

			Query query = new Query();
			query.addCriteria(Criteria.where("app").is(
					new App(json.getString("appId"))));
			query.addCriteria(Criteria.where("name").is(json.getString("name")));

			try {
				Schedule schedule = service.findOne(query, Schedule.class);
				if (schedule != null)
					return ResponseUtils.fail("The name has exist");
				else {
					schedule = new Schedule();
					schedule.setName(json.getString("name"));
					schedule.setApp(new App(json.getString("appId")));
					// schedule.setScaleName(orderDispatcher.getDispatchedcompId());
					schedule.setStatus(0);
					service.save(schedule);
					
					//eval(json.getString("id"), "start");
					return ResponseUtils.successWithValue(schedule);
				}
			} catch (Exception e) {
				log.error("error" + e);
				return ResponseUtils.fail("Error" + e.getMessage());
			}
		}
	}

	public String eval(String jobId, String type) {

		RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
				.setSocketTimeout(1000 * 60 * 3)
				.setConnectTimeout(1000 * 60 * 3).build();

		CloseableHttpClient client = HttpClients
				.custom()
				.setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
				.setKeepAliveStrategy(
						DefaultConnectionKeepAliveStrategy.INSTANCE).build();

		HttpPost post = new HttpPost(configInfo.getNodejsScheduleUrl()
				+ type + "/" + jobId);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// for (Iterator<?> iterator = json.keys(); iterator.hasNext();) {
		// String key = iterator.next().toString();
		// params.add(new BasicNameValuePair(key, json.getString(key)));
		// }

		String result = null;
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(params, "UTF-8");
			post.setEntity(entity);

			HttpResponse httpResponse = client.execute(post);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				result = EntityUtils.toString(httpEntity);
			}

			client.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			result = e.getMessage().toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = e.getMessage().toString();
		} catch (IOException e) {
			e.printStackTrace();
			result = e.getMessage().toString();
		}
		return result;
	}

	@ApiOperation(value = "reset", notes = "reset")
	@RequestMapping(value = "reset", method = RequestMethod.POST)
	public ResponseEntity<?> resetJobScaleName(
			@ApiParam(value = "json") @RequestBody String input) {

		try {
			log.info(input);
			String scaleName = input.split("=")[1];
			log.info(scaleName);

			Query query = new Query();
			query.addCriteria(Criteria.where("name").is(scaleName));
			Update update = Update.update("status", 0);
			// reset server status
			scaleService.update(query, update, ScaleStaticsValue.class);
			// reset order dispatcher
			orderDispatcher.init();

			query = new Query();
			query.addCriteria(Criteria.where("scaleName").is(scaleName));
			// find all down job
			List<Schedule> list = service.find(query, Schedule.class);

			for (Schedule schedule : list) {
				System.out.println(schedule.getName());
				update = Update.update("scaleName",
						orderDispatcher.getDispatchedcompId());
				// reallocation server
				service.update(new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(schedule.getId()))), update,
						Schedule.class);
				// execute down job
				// eval(schedule.getId());
			}

			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("reset schedule list error : " + e);
			return ResponseUtils.fail("Reset schedule list error : " + e);
		}
	}

	@ApiOperation(value = "stop schedule", notes = "stop schedule")
	@RequestMapping(value = "stop", method = RequestMethod.POST)
	public ResponseEntity<?> stop(
			@ApiParam(value = "schedule id") @RequestBody net.sf.json.JSONObject json) {
		if (!json.has("id") || StringUtil.isEmpty(json.getString("id")))
			return ResponseUtils.fail("Schedule id is null!");

		String id = json.getString("id");
		Query query = new Query();
		query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(id));

		try {
			Update update = Update.update("status", 0);

			service.update(query, update, Schedule.class);
			
			String ret = eval(id, "stop");

			return ResponseUtils.successWithValue(ret);
		} catch (Exception e) {
			log.error("stop schedule error : " + e);
			return ResponseUtils
					.fail("Stop schedule error : " + e.getMessage());
		}
	}

	@ApiOperation(value = "delete schedule", notes = "delete schedule")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(
			@ApiParam(value = "schedule id") @RequestBody net.sf.json.JSONObject json) {
		if (!json.has("id") || StringUtil.isEmpty(json.getString("id")))
			return ResponseUtils.fail("Schedule id is null!");
		try {
			String id = json.getString("id");
			Schedule schedule = service.findById(new ObjectId(id),
					Schedule.class);
			if (schedule.getStatus() == 1) {
				return ResponseUtils.alert("The schedule is running!");
			}

			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(id));

			service.delete(query, Schedule.class);

			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("delete schedule error : " + e);
			return ResponseUtils.fail("Delete schedule error : "
					+ e.getMessage());
		}
	}

	@Override
	public BaseService<Schedule> getService() {
		// TODO Auto-generated method stub
		return service;
	}

	@Override
	public Query getQuery(JSONObject json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<Schedule> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pager<Schedule> getPager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCollection() {
		// TODO Auto-generated method stub
		return "schedule";
	}

}
