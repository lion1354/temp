package com.tibco.ma.controller;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.tibco.ma.common.Csv2Data;
import com.tibco.ma.common.ReadTxtData;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.AnalyticsService;
import com.tibco.ma.service.BaseService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping("ma/1/analytics")
public class AnalyticsController extends BaseController<Object> {

	@Resource
	private AnalyticsService analyticsService;

	private static final Logger log = LoggerFactory
			.getLogger(AnalyticsController.class);

	@Override
	public BaseService<Object> getService() {
		// TODO Auto-generated method stub
		return analyticsService;
	}

	@Override
	public Query getQuery(JSONObject json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<Object> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pager<Object> getPager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCollection() {
		// TODO Auto-generated method stub
		return null;
	}

	@ApiOperation(value = "page stay time", notes = "page stay time")
	@RequestMapping(value = "/pagesStayTime", method = RequestMethod.GET)
	public ResponseEntity<?> getPagesStayTime(
			@ApiParam(value = "app id") @RequestParam("appId") String appId)
			throws Exception {
		try {
			Aggregation agg = newAggregation(match(where("appId").is(appId)),
					group("pageid").sum("duration").as("value"),
					sort(DESC, "value"), limit(5));
			List<Object> pageStayData = analyticsService.doAggretationFunc(agg,
					"user_page_analytics", Object.class);
			return ResponseUtils.successWithValues(pageStayData);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@ApiOperation(value = "query click count", notes = "query click count")
	@RequestMapping(value = "/queryClickCount", method = RequestMethod.GET)
	public ResponseEntity<?> getClickActionCount(
			@ApiParam(value = "app id") @RequestParam("appId") String appId)
			throws Exception {
		try {
			Aggregation agg = newAggregation(match(where("appId").is(appId)),
					group("actionid").count().as("value"), sort(DESC, "value"),
					limit(5));
			List<Object> pageStayData = analyticsService.doAggretationFunc(agg,
					"user_action_analytics", Object.class);
			return ResponseUtils.successWithValues(pageStayData);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@ApiOperation(value = "query active user per hour", notes = "query active user per hour")
	@RequestMapping(value = "/queryActiveUserPerPour", method = RequestMethod.GET)
	public ResponseEntity<?> getActivePerHour(
			@ApiParam(value = "app id") @RequestParam("appId") String appId)
			throws Exception {
		try {
			// TODO
			ProjectionOperation operation = Aggregation.project()
					.and("edatetime").extractHour().as("name");
			Aggregation agg = newAggregation(match(where("appId").is(appId)),
					operation, group("name").count().as("value"),
					sort(ASC, "_id"));
			List<Object> activeUserPerHour_1 = analyticsService
					.doAggretationFunc(agg, "user_action_analytics",
							Object.class);
			operation = Aggregation.project().and("enterdatetime")
					.extractHour().as("name");
			agg = newAggregation(match(where("appId").is(appId)), operation,
					group("name").count().as("value"), sort(DESC, "value"));
			List<Object> activeUserPerHour_2 = analyticsService
					.doAggretationFunc(agg, "user_page_analytics", Object.class);
			List<Object> result = AnalyticsController.mergeResult(
					activeUserPerHour_1, activeUserPerHour_2);
			return ResponseUtils.successWithValues(result);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@ApiOperation(value = "query url open times", notes = "query url open times")
	@RequestMapping(value = "/queryUrlOpenTimes", method = RequestMethod.GET)
	public ResponseEntity<?> getUrlOpenTimes(
			@ApiParam(value = "app id") @RequestParam("appId") String appId)
			throws Exception {
		try {
			Aggregation agg = newAggregation(match(where("appId").is(appId)),
					match(where("url").regex(Pattern.compile("^http{1}.*"))),
					group("url").count().as("value"), sort(DESC, "value"),
					limit(5));
			List<Object> result = analyticsService.doAggretationFunc(agg,
					"user_action_analytics", Object.class);
			return ResponseUtils.successWithValues(result);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "query count number install on different device", notes = "query count number install on different device")
	@RequestMapping(value = "/installsOnDevices", method = RequestMethod.GET)
	public ResponseEntity<?> queryCountNumberInstallsOnDifferentDevice(
			@ApiParam(value = "app id") @RequestParam("appId") String appId)
			throws Exception {
		try {
			Aggregation agg = newAggregation(match(where("appId").is(appId)),
					group().sum("daily_device_installs").as("value"),
					sort(DESC, "value"));
			List<Object> androidResult = analyticsService.doAggretationFunc(
					agg, "android_device_installs", Object.class);
			agg = newAggregation(
					match(where("appId").is(appId)),
					match(where("client_agent").regex(Pattern.compile("^7.*"))),
					match(where("device").regex(Pattern.compile("^i.*"))),
					group().count().as("value"), sort(DESC, "value"));
			List<Object> iosResult = analyticsService.doAggretationFunc(agg,
					"devices", Object.class);
			if (androidResult.size() <= 0 && iosResult.size() <= 0) {
				return ResponseUtils.successWithValues(new ArrayList());
			}
			if (androidResult.size() <= 0 || iosResult.size() <= 0) {
				Map<String, Object> initAndroidResult = new HashMap<String, Object>();
				initAndroidResult.put("_id", null);
				initAndroidResult.put("value", 0);
				androidResult = new ArrayList<Object>();
				androidResult.add(initAndroidResult);
			}
			((Map) androidResult.get(0)).put("_id", "Android");
			if (iosResult.size() <= 0 || iosResult == null) {
				Map<String, Object> initIOSResult = new HashMap<String, Object>();
				initIOSResult.put("_id", null);
				initIOSResult.put("value", 0);
				iosResult = new ArrayList<Object>();
				iosResult.add(initIOSResult);
			}
			((Map) iosResult.get(0)).put("_id", "IOS");
			List<Object> result = new ArrayList<Object>();
			result.add(androidResult.get(0));
			result.add(iosResult.get(0));
			return ResponseUtils.successWithValues(result);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@ApiOperation(value = "get install on android device", notes = "get install on android device")
	@RequestMapping(value = "/installsOnDifferentAndroidDevice", method = RequestMethod.GET)
	public ResponseEntity<?> getInstallsOnAndroidDevice(
			@ApiParam(value = "app id") @RequestParam("appId") String appId)
			throws Exception {
		try {
			Aggregation agg = newAggregation(match(where("appId").is(appId)),
					group("device").count().as("value"), sort(DESC, "value"),
					limit(5));
			List<Object> result = analyticsService.doAggretationFunc(agg,
					"android_device_installs", Object.class);
			return ResponseUtils.successWithValues(result);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@ApiOperation(value = "get install on different android version", notes = "get install on different android version")
	@RequestMapping(value = "/installsOnDifferentAndroidOSVersion", method = RequestMethod.GET)
	public ResponseEntity<?> getInstallsOnDifferentAndroidOSVersion(
			@ApiParam(value = "app id") @RequestParam("appId") String appId)
			throws Exception {
		try {
			Aggregation agg = newAggregation(match(where("appId").is(appId)),
					match(where("os_version").regex(Pattern.compile("^A.*"))),
					group("os_version").count().as("value"),
					sort(DESC, "value"));
			List<Object> result = analyticsService.doAggretationFunc(agg,
					"android_os_version_installs", Object.class);
			return ResponseUtils.successWithValues(result);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@ApiOperation(value = "get install on different ios device", notes = "get install on different ios device")
	@RequestMapping(value = "/installsOnDifferentIOSDevice", method = RequestMethod.GET)
	public ResponseEntity<?> getInstallsOnDifferentIOSDevice(
			@ApiParam(value = "app id") @RequestParam("appId") String appId)
			throws Exception {
		try {
			Aggregation agg = newAggregation(
					match(where("appId").is(appId)),
					match(where("client_agent").regex(Pattern.compile("^7.*"))),
					match(where("device").regex(Pattern.compile("^i.*"))),
					group("device").count().as("value"), sort(DESC, "value"));
			List<Object> iosResult = analyticsService.doAggretationFunc(agg,
					"devices", Object.class);
			return ResponseUtils.successWithValues(iosResult);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@ApiOperation(value = "get install on different ios version", notes = "get install on different ios version")
	@RequestMapping(value = "/installsOnDifferentIOSOSVersion", method = RequestMethod.GET)
	public ResponseEntity<?> getInstallsOnDifferentIOSOSDevice(
			@ApiParam(value = "app id") @RequestParam("appId") String appId)
			throws Exception {
		try {
			Aggregation agg = newAggregation(match(where("appId").is(appId)),
					group("version").count().as("value"), sort(DESC, "value"));
			List<Object> iosResult = analyticsService.doAggretationFunc(agg,
					"sale_report_ios", Object.class);
			return ResponseUtils.successWithValues(iosResult);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@ApiOperation(value = "get active user location", notes = "get active user location")
	@RequestMapping(value = "/queryActiveUserLocation", method = RequestMethod.GET)
	public ResponseEntity<?> getActiveUserLocation(
			@ApiParam(value = "app id") @RequestParam("appId") String appId)
			throws Exception {
		try {
			Aggregation agg = newAggregation(match(where("appId").is(appId)),
					match(where("longitude").ne(null)),
					match(where("longitude").ne("")),
					group("longitude", "lautitude").count().as("value"),
					sort(DESC, "value"));
			List<Object> iosResult = analyticsService.doAggretationFunc(agg,
					"user_action_analytics", Object.class);
			return ResponseUtils.successWithValues(iosResult);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	// add by Daniel 08/20/2015
	@ApiOperation(value = "upload data", notes = "add data from csv file")
	@RequestMapping(value = "/uploadData", method = RequestMethod.POST)
	public ResponseEntity<?> uploadData(HttpServletRequest request)
			throws IOException {
		CommonsMultipartResolver commonsMultipartResolver = new CustomMultipartResolver(
				request.getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile multipartFile = multiRequest.getFile(iter.next());
				if (multipartFile != null) {
					InputStream inputStream = multipartFile.getInputStream();
					String fileName = multipartFile.getOriginalFilename();
					File tmpFile = File.createTempFile(fileName, ".tmp");
					String path = tmpFile.getPath();
					String appID = request.getParameter("appId");
					// Judging file format
					String prefix = fileName.substring(fileName
							.lastIndexOf(".") + 1);
					// System.out.println(prefix);
					if (prefix.equals("gz")) {
						inputStream = new GZIPInputStream(inputStream);
					}
					FileUtils.copyInputStreamToFile(inputStream, tmpFile);
					// System.out.println(tmpFile.getName());
					if (prefix.equals("csv")) {
						Csv2Data csv2data = new Csv2Data();
						List data = csv2data.change2data(path, appID);
						String collectionName = fileName.substring(12,
								(fileName.length() - 4));
						// System.out.println(collectionName);
						// System.out.println(appID);
						try {
							analyticsService.insertMany(collectionName, data);
						} catch (Exception e) {
							log.error("{}", e);
							return ResponseUtils.fail(e.getMessage(),
									HttpStatus.FOUND);
						}
					}
					if (prefix.equals("txt") || prefix.equals("gz")) {
						ReadTxtData readtxtdata = new ReadTxtData();
						List data = readtxtdata.readTXT(path, appID);
						String collectionName = "sale_report_ios";
						try {
							analyticsService.insertMany(collectionName, data);
						} catch (Exception e) {
							log.error("{}", e);
							return ResponseUtils.fail(e.getMessage(),
									HttpStatus.FOUND);
						}
					}
					// else {
					// return ResponseUtils.fail("File format is not correct");
					// }
					tmpFile.delete();
				}
			}
		}
		return ResponseUtils.successWithStatus(HttpStatus.OK);
	}

	// Common function :merge two result list to one
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<Object> mergeResult(List<Object> list1, List<Object> list2) {
		List<Object> result = new ArrayList<Object>();
		for (int i = 0; i < list1.size(); i++) {
			for (int j = 0; j < list2.size(); j++) {
				if (Integer.parseInt(((Map) list1.get(i)).get(MongoDBConstants.DOCUMENT_ID).toString()) == Integer
						.parseInt(((Map) list2.get(j)).get(MongoDBConstants.DOCUMENT_ID).toString())) {
					Map data = new HashMap();
					int hour = (Integer.parseInt(((Map) list1.get(i)).get(MongoDBConstants.DOCUMENT_ID).toString()) + 8) % 24;
					data.put(MongoDBConstants.DOCUMENT_ID, hour + "");
					data.put("value", Integer.parseInt(((Map) list1.get(i)).get("value").toString())
							+ Integer.parseInt(((Map) list2.get(j)).get("value").toString()));
					result.add(data);
				}
			}
		}
		return result;
	}

}
