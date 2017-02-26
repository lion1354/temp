package com.tibco.ma.controller;

import java.util.Iterator;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.mongodb.gridfs.GridFSDBFile;
import com.tibco.ma.common.ImageUtils;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.common.pns.PNHelper;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.App;
import com.tibco.ma.model.Credential;
import com.tibco.ma.service.BaseService;
import com.tibco.ma.service.CredentialService;
import com.tibco.ma.service.ResourceGridFSService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping("ma/1/credential")
public class CredentialController extends BaseController<Credential> {

	private static final Logger log = LoggerFactory
			.getLogger(CredentialController.class);

	@Resource
	private CredentialService service;
	@Resource
	private ResourceGridFSService resourceGridFSService;

	@Log(operate = "Get credential by id", modelName = "Notification")
	@ApiOperation(value = "get one credential", notes = "get one credential")
	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ResponseEntity<?> getOne(
			@ApiParam(value = "app id") @RequestParam("id") String id)
			throws Exception {
		try {
			Query queryCred = new Query();
			if (StringUtil.notEmpty(id)) {
				queryCred.addCriteria(Criteria.where("app").is(new App(id)));
			} else {
				ResponseUtils.fail("APPID can't be null");
			}
			List<Credential> listCred = service.find(queryCred,
					Credential.class);
			if (listCred.size() > 0) {
				return ResponseUtils.successWithValue(listCred.get(0));
			} else {
				return ResponseUtils.success();
			}
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}
	}

	@Log(operate = "Delete Credential", modelName = "Notification")
	@ApiOperation(value = "delete credential", notes = "delete credential")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ResponseEntity<?> delete(
			HttpServletRequest request,
			@ApiParam(value = "credential id") @RequestParam("id") String credentialId) {
		try {
			if (StringUtil.isEmpty(credentialId)) {
				throw new Exception("id is necessary");
			}

			Credential credential = service.findById(
					new ObjectId(credentialId), Credential.class);
			if (credential.getPath() != null) {
				Query queryByName = new Query(Criteria.where("filename").is(
						credential.getPath()));
				resourceGridFSService.deleteSFDBFile(queryByName);
			}
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(
					new ObjectId(credentialId)));
			service.delete(query, Credential.class);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage(), HttpStatus.FOUND);
		}

	}

	@Log(operate = "Save Credential", modelName = "Notification")
	@ApiOperation(value = "save credential", notes = "save credential")
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public ResponseEntity<?> upload(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Credential credential = new Credential();
			packToObj(request, credential);

			Credential oldCredential = null;
			if (StringUtil.notEmpty(credential.getId())) {
				oldCredential = service.findById(
						new ObjectId(credential.getId()), Credential.class);
				credential.setPath(oldCredential.getPath());
			}

			Boolean isProduction = null;
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
					request.getServletContext());
			if (commonsMultipartResolver.isMultipart(request)) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						isProduction = PNHelper
								.isProduction(file.getInputStream(),
										credential.getPassword());
						credential.setIsProduction(isProduction);

						if (oldCredential != null) {
							Query query = new Query(Criteria.where("filename")
									.is(oldCredential.getPath()));
							resourceGridFSService.deleteSFDBFile(query);
						}

						Query query1 = new Query(Criteria.where("filename").is(
								file.getOriginalFilename()));
						GridFSDBFile fsdbFile = resourceGridFSService
								.querySFDBFileOne(query1);
						String newFileName = null;
						if (fsdbFile != null) {
							newFileName = ImageUtils.createResName(
									"Credential", file.getOriginalFilename());
						} else {
							newFileName = file.getOriginalFilename();
						}
						resourceGridFSService.save(file.getInputStream(),
								newFileName, null);
						credential.setPath(newFileName);
					}
				}
			}
			if (isProduction == null && credential.getPassword() != null) {
				Query queryByFileName = new Query(Criteria.where("filename")
						.is(credential.getPath()));
				GridFSDBFile creFile = resourceGridFSService
						.querySFDBFileOne(queryByFileName);
				isProduction = PNHelper.isProduction(creFile.getInputStream(),
						credential.getPassword());
				credential.setIsProduction(isProduction);
			}
			Credential cred = service.saveCred(credential);
			return ResponseUtils.successWithValue(cred);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	private void packToObj(HttpServletRequest request, Credential credential) {
		String id = request.getParameter("id");
		if (StringUtil.notEmpty(id)) {
			credential.setId(id);
		}
		String appID = request.getParameter("appID");
		if (StringUtil.notEmpty(appID)) {
			credential.setApp(new App(appID));
		}
		String path = request.getParameter("path");
		if (StringUtil.notEmpty(path)) {
			credential.setPath(path);
		}
		String password = request.getParameter("password");
		if (StringUtil.notEmpty(password)) {
			credential.setPassword(password);
		}
		String apiKey = request.getParameter("apiKey");
		if (StringUtil.notEmpty(apiKey)) {
			credential.setApiKey(apiKey);
		}
		String projectNumber = request.getParameter("projectNumber");
		if (StringUtil.notEmpty(projectNumber)) {
			credential.setProjectNumber(projectNumber);
		}
	}

	@Override
	public BaseService<Credential> getService() {
		return service;
	}

	@Override
	public Query getQuery(JSONObject json) {
		Query query = new Query();
		return query;
	}

	@Override
	public Class<Credential> getEntityClass() {
		return Credential.class;
	}

	@Override
	public Pager<Credential> getPager() {
		Pager<Credential> pager = new Pager<Credential>();
		pager.setPagesize(20);
		return pager;
	}

	@Override
	public String getCollection() {
		return "credential";
	}

}
