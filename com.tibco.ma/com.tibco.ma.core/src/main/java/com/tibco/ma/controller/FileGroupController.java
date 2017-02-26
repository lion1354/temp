package com.tibco.ma.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tibco.ma.common.Constants;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
import com.tibco.ma.model.File;
import com.tibco.ma.model.FileGroup;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.core.MaFile;
import com.tibco.ma.model.core.MaImage;
import com.tibco.ma.service.CoreGridFSService;
import com.tibco.ma.service.FileGroupService;
import com.tibco.ma.service.FileService;
import com.tibco.ma.service.SpecificationService;
import com.tibco.ma.service.ValuesService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@Controller
@RequestMapping("ma/1/file")
public class FileGroupController {

	@Autowired
	private FileGroupService fileGroupService;

	@Autowired
	private ValuesService valuesService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CoreGridFSService coreGridFsService;

	@Autowired
	private SpecificationService specificationService;

	private static final Logger log = LoggerFactory
			.getLogger(FileGroupController.class);

	/**
	 * 
	 * @param request
	 *            { id:$"{_id}" entityId:$"{entityId}" colName:$"{colName}"
	 *            owningEntityId:$"{owningEntityId}"
	 *            owningColName:$"{owningColName}" }
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Log(operate = "Upload File", modelName = "Core")
	@ApiOperation(value = "upload file", notes = "upload file")
	@RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
	public ResponseEntity<?> uploadFileByCore(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			FileGroup fileGroup = fileGroupService.saveFileGroup(request,
					coreGridFsService);
			String rootPath = request.getContextPath();
			String type = request.getParameter("uploadType");
			JSONObject jsonObject = new JSONObject();
			if (fileGroup != null) {
				jsonObject.put(MaFile.GROUPID, fileGroup.getId());
				if (StringUtil.notEmpty(fileGroup.getDescription())) {
					jsonObject.put(MongoDBConstants.ENTITY_COMMENT,
							fileGroup.getDescription());
				} else {
					jsonObject.put(MongoDBConstants.ENTITY_COMMENT,
							fileGroup.getGroupName());
				}

				if (fileGroup.getLists() != null
						&& fileGroup.getLists().size() > 0) {
					for (File file : fileGroup.getLists()) {
						file.setFilePath(rootPath
								+ Constants.IMAGE_REST_CORE_SHOWRESOURCE
								+ file.getFilePath());
					}
				}
				if (fileGroup.getLists() != null
						&& fileGroup.getLists().size() > 0) {
					if (StringUtil.isEmpty(type) || !"ImageGroup".equals(type)) {
						jsonObject.put(MaFile.URL, fileGroup.getLists().get(0)
								.getFilePath());
					} else {

						JSONArray array = new JSONArray();
						for (File file : fileGroup.getLists()) {
							array.add(file.getFilePath());
						}
						jsonObject.put(MaImage.URLS, array);
					}
				}
			}
			log.debug("result : {}", jsonObject);
			String id = request.getParameter(MongoDBConstants.DOCUMENT_ID);
			String entityId = request
					.getParameter(MongoDBConstants.VALUES_ENTITYID);
			String colName = request
					.getParameter(MongoDBConstants.COLUMN_COLNAME);
			String appId = request.getParameter(MongoDBConstants.ENTITY_APPID);
			String ownEntityId = request.getParameter("ownEntityId");
			String ownValuesId = request.getParameter("ownValuesId");
			String ownColName = request.getParameter("ownColName");

			if (jsonObject != null) {
				valuesService.save(appId, id, entityId, colName,
						jsonObject.toJSONString(), ownEntityId, ownValuesId,
						ownColName);
			}
			return ResponseUtils.success();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * get image
	 * 
	 * @param groupId
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "get image by group", notes = "upload file")
	@RequestMapping(value = "getImageByGroup", method = RequestMethod.GET)
	public ResponseEntity<?> getImageByCategoryAndGroup(
			@ApiParam(value = "group id") @RequestParam(value = "groupId") String groupId,
			HttpServletRequest request) {
		String rootPath = request.getContextPath();
		try {
			List<File> files = fileService.showByGroupId(groupId, rootPath);
			return ResponseUtils.successWithValues(files);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Log(operate = "Upload File", modelName = "Setting")
	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	public ResponseEntity<?> uploadFile(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			List<File> listFiles = fileService.uploadFile(request,
					coreGridFsService);
			String rootPath = request.getContextPath();
			if (listFiles != null && listFiles.size() > 0) {
				for (File file : listFiles) {
					file.setFilePath(rootPath
							+ Constants.IMAGE_REST_CORE_SHOWRESOURCE
							+ file.getFilePath());
				}
			}
			return ResponseUtils.successWithValues(listFiles);
		} catch (Exception e) {
			log.error("{}", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}
}
