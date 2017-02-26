package com.tibco.ma.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.mongodb.gridfs.GridFSDBFile;
import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.Constants;
import com.tibco.ma.common.ImageUtils;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.pns.PNHelper;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.App;
import com.tibco.ma.model.Credential;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.PNTask;
import com.tibco.ma.service.AppService;
import com.tibco.ma.service.BaseService;
import com.tibco.ma.service.CredentialService;
import com.tibco.ma.service.NotificationService;
import com.tibco.ma.service.ResourceGridFSService;
import com.tibco.ma.service.ValuesService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping("ma/1/notification")
public class NotificationController extends BaseController<PNTask> {

	private static final Logger log = LoggerFactory
			.getLogger(NotificationController.class);

	@Resource
	private NotificationService service;
	@Resource
	private AppService appservice;
	@Resource
	private CredentialService credentialService;
	@Resource
	private ResourceGridFSService resourceGridFSService;
	@Resource
	private ConfigInfo configInfo;
	@Resource
	private ValuesService valuesService;

	@Log(operate = "Get notification by id", modelName = "Notification")
	@ApiOperation(value = "get notification", notes = "get notification")
	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ResponseEntity<?> getOne(
			@ApiParam(value = "id") @RequestParam("id") String id)
			throws Exception {
		try {
			PNTask pntask = service.findById(new ObjectId(id), PNTask.class);
			return ResponseUtils.successWithValue(pntask);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Log(operate = "Delete Notification", modelName = "Notification")
	@ApiOperation(value = "delete notification", notes = "delete notification")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(
			@ApiParam(value = "json") @RequestBody JSONObject json) {
		try {
			if (json == null) {
				throw new Exception("id is necessary");
			}
			ArrayList<String> id = (ArrayList<String>) json.get("id");

			for (String s : id) {
				PNTask pnTask = service.findById(new ObjectId(s), PNTask.class);
				String preview = pnTask.getPreview();
				if (StringUtil.notEmpty(preview)) {
					if (preview.contains(Constants.IMAGE_NOTIFICATION_SHOWRES)) {
						String[] ids = preview.split("=");
						resourceGridFSService.deleteSFDBFile(new Query(Criteria
								.where(MongoDBConstants.DOCUMENT_ID).is(
										new ObjectId(ids[ids.length - 1]))));
					}
				}
			}

			Query query = new Query();
			query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).in(
					id));
			service.delete(query, PNTask.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}

	}

	@SuppressWarnings("unchecked")
	@Log(operate = "Save and Send Notification", modelName = "Notification")
	@ApiOperation(value = "save and send notification", notes = "save and send notification")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<?> save(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String appID = request.getParameter("appID");
			if (StringUtil.isEmpty(appID)) {
				return ResponseUtils.fail("AppID can't be null");
			}

			Query queryCred = new Query();
			if (StringUtil.notEmpty(appID)) {
				queryCred.addCriteria(Criteria.where("app").is(new App(appID)));
			}
			Credential credential = credentialService.findOne(queryCred,
					Credential.class);
			if (credential == null) {
				return ResponseUtils
						.fail("APP Credential is null, please add Credential!");
			}

			PNTask pnTask = packToObj(request);

			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
					request.getServletContext());
			if (commonsMultipartResolver.isMultipart(request)) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						String myFileName = file.getOriginalFilename();
						String fileName = ImageUtils.createResName("Image",
								myFileName);
						String imgUrl = resourceGridFSService.save(
								file.getInputStream(), fileName, null);
						String preview = getResUrl(request, imgUrl);
						pnTask.setPreview(preview);
					}
				}
			}

			String osSend = pnTask.getDeviceOS();
			if (StringUtil.notEmpty(osSend)) {
				osSend = osSend.toLowerCase();
			}
			String previewUrl = null;
			if (StringUtil.notEmpty(pnTask.getPreview())) {
				previewUrl = getShowUrl(pnTask.getPreview());
			}
			if ("android".equals(osSend) || "all".equals(osSend)) {
				if (StringUtil.isEmpty(credential.getApiKey())) {
					return ResponseUtils
							.fail("The Andriod Credential is null, please add Credential");
				}
				Document query = new Document();
				query.append("appId", appID);
				query.append("os", "android");
				String valuesCollectionName = valuesService
						.getValuesCollectionName(appID,
								MongoDBConstants.SYSTEM_DEVICE_COLLECTION_NAME);
				List<Document> list = valuesService.query(valuesCollectionName,
						query, null);
				List<String> regList = new ArrayList<String>();
				for (Document document : list) {
					String deviceToken = document.getString("deviceToken");
					if (StringUtil.notEmpty(deviceToken)) {
						regList.add(deviceToken);
					}
				}
				if (regList.size() > 0) {
					PNHelper.sendGCM(pnTask.getContent(), pnTask.getUrl(),
							previewUrl, null, pnTask.getMsg_type() + "",
							credential.getApiKey(), regList);
				}
			}

			if ("ios".equals(osSend) || "all".equals(osSend)) {
				if (StringUtil.isEmpty(credential.getPath())) {
					return ResponseUtils
							.fail("The Apple Credential is null, please add Credential");
				}
				Document query = new Document();
				query.append("appId", appID);
				query.append("os", "ios");
				String valuesCollectionName = valuesService
						.getValuesCollectionName(appID,
								MongoDBConstants.SYSTEM_DEVICE_COLLECTION_NAME);
				List<Document> list = valuesService.query(valuesCollectionName,
						query, null);
				List<String> regList = new ArrayList<String>();
				for (Document document : list) {
					String deviceToken = document.getString("deviceToken");
					if (StringUtil.notEmpty(deviceToken)) {
						regList.add(deviceToken);
					}
				}
				if (regList.size() > 0) {
					Query queryFS = new Query(Criteria.where("filename").is(
							credential.getPath()));
					GridFSDBFile fsdbFile = resourceGridFSService
							.querySFDBFileOne(queryFS);
					PNHelper.sendAPNS(regList, fsdbFile.getInputStream(),
							credential.getPassword(),
							credential.getIsProduction(), pnTask.getContent(),
							null, false);
				}
			}

			service.save(pnTask);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/*
	 * @Log(operate = "Save and Send Notification", modelName = "Notification")
	 * 
	 * @RequestMapping(value = "save", method = RequestMethod.POST) public
	 * ResponseEntity<?> save(HttpServletRequest request,@RequestBody JSONObject
	 * json) { try {
	 * if((!json.containsKey("appID"))||json.get("appID")==null||StringUtil.
	 * isEmpty((String)json.get("appID"))){ return ResponseUtils.fail(
	 * "AppID can't be null"); }
	 * 
	 * Query queryCred = new Query();
	 * if(StringUtil.notEmpty(json.get("appID").toString())){
	 * queryCred.addCriteria(Criteria.where("app").is( new
	 * App(json.get("appID").toString()))); } List<Credential> listCred =
	 * credentialService.find(queryCred, Credential.class);
	 * if(listCred.size()==0||(StringUtil.isEmpty(listCred.get(0).getApiKey())&&
	 * StringUtil.isEmpty(listCred.get(0).getPath()))){ return
	 * ResponseUtils.fail("APP Credential can't be null"); }
	 * 
	 * PNTask pnTask = service.save(json); String url =
	 * getResUrl(request,pnTask.getId());
	 * 
	 * Query query = new Query();
	 * if(StringUtil.notEmpty(pnTask.getApp().getId())){
	 * query.addCriteria(Criteria.where("app").is( new
	 * App(pnTask.getApp().getId()))); } List<Device> list =
	 * deviceservice.find(query, Device.class); List<String> regList = new
	 * ArrayList<String>(); if(list.size()>0){ for (Device d : list) {
	 * regList.add(d.getDevice_token()); }
	 * PNHelper.sendMessage(pnTask.getContent(), url,
	 * pnTask.getRemoteImageUrl(), pnTask.getMsg_type()+"",
	 * listCred.get(0).getApiKey(), regList); } return ResponseUtils.success();
	 * } catch (Exception e) { log.error("{}", e); return
	 * ResponseUtils.fail(e.getMessage()); } }
	 * 
	 * @Log(operate = "Send Notification", modelName = "Notification")
	 * 
	 * @RequestMapping(value = "send", method = RequestMethod.GET) public
	 * ResponseEntity<?> send(HttpServletRequest request,@RequestParam("id")
	 * String id) throws Exception { try { PNTask pnTask = service.findById(new
	 * ObjectId(id), PNTask.class); String url = getResUrl(request, id); Query
	 * query = new Query(); query.addCriteria(Criteria.where("app").is( new
	 * App(pnTask.getApp().getId()))); List<Device> list =
	 * deviceservice.find(query, Device.class);
	 * 
	 * Query queryCred = new Query();
	 * if(StringUtil.notEmpty(pnTask.getApp().getId())){
	 * queryCred.addCriteria(Criteria.where("app").is( new
	 * App(pnTask.getApp().getId()))); } List<Credential> listCred =
	 * credentialService.find(queryCred, Credential.class);
	 * if(listCred.size()==0||(StringUtil.isEmpty(listCred.get(0).getApiKey())&&
	 * StringUtil.isEmpty(listCred.get(0).getPath()))){ return
	 * ResponseUtils.fail("APP Credential can't be null"); }
	 * 
	 * List<String> regList = new ArrayList<String>(); for (Device d : list) {
	 * regList.add(d.getDevice_token()); }
	 * PNHelper.sendMessage(pnTask.getContent(), url,
	 * pnTask.getRemoteImageUrl(), pnTask.getMsg_type()+"",
	 * listCred.get(0).getApiKey(), regList); return ResponseUtils.success(); }
	 * catch (Exception e) { log.error("{}", e); return
	 * ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND); } }
	 */

	private String getResUrl(HttpServletRequest request, String id)
			throws Exception {
		String url = request.getContextPath()
				+ Constants.IMAGE_NOTIFICATION_SHOWRES + id;
		return url;
	}

	private String getShowUrl(String path) throws Exception {
		String url = configInfo.getEmailServerPath() + path;
		return url;
	}

	@Override
	public BaseService<PNTask> getService() {
		return service;
	}

	@Override
	public Query getQuery(JSONObject json) {
		Query query = new Query();
		if (json != null && json.containsKey("appID")) {
			query.addCriteria(Criteria.where("app").is(
					new App((String) json.get("appID"))));
		}
		return query;
	}

	@Override
	public Class<PNTask> getEntityClass() {
		return PNTask.class;
	}

	@Override
	public Pager<PNTask> getPager() {
		Pager<PNTask> pager = new Pager<PNTask>();
		pager.setPagesize(20);
		return pager;
	}

	@Override
	public String getCollection() {
		return "notification";
	}

	private PNTask packToObj(HttpServletRequest request) {
		PNTask pnTask = new PNTask();
		if (StringUtil.notEmpty(request.getParameter("id"))) {
			pnTask.setId(request.getParameter("id"));
		}
		if (StringUtil.notEmpty(request.getParameter("appID"))) {
			pnTask.setApp(new App(request.getParameter("appID")));
		}
		// if (StringUtil.notEmpty(request.getParameter("preview"))) {
		// pnTask.setPreview(request.getParameter("preview"));
		// }
		if (StringUtil.notEmpty(request.getParameter("content"))) {
			pnTask.setContent(request.getParameter("content"));
		}
		if (StringUtil.notEmpty(request.getParameter("msg_type"))) {
			pnTask.setMsg_type(Integer.parseInt(request
					.getParameter("msg_type")));
		}
		if (StringUtil.notEmpty(request.getParameter("url"))) {
			pnTask.setUrl(request.getParameter("url"));
		}
		if (StringUtil.notEmpty(request.getParameter("deviceOS"))) {
			pnTask.setDeviceOS(request.getParameter("deviceOS"));
		}
		if (StringUtil.notEmpty(request.getParameter("status"))) {
			pnTask.setStatus(Integer.parseInt(request.getParameter("status")));
		}
		return pnTask;
	}

}
