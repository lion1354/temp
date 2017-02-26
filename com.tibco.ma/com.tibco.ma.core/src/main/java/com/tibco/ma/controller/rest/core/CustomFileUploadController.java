package com.tibco.ma.controller.rest.core;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.Constants;
import com.tibco.ma.common.ResponseErrorCode;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.model.File;
import com.tibco.ma.model.FileGroup;
import com.tibco.ma.service.CoreGridFSService;
import com.tibco.ma.service.FileGroupService;
import com.tibco.ma.service.FileService;
import com.wordnik.swagger.annotations.ApiOperation;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@RestController
@RequestMapping(value = "/1/file")
public class CustomFileUploadController {

	@Autowired
	private FileService fileService;

	@Autowired
	private FileGroupService fileGroupService;

	@Autowired
	private CoreGridFSService coreGridFsService;
	
	@Autowired
	private ConfigInfo configInfo;

	private static Logger log = LoggerFactory
			.getLogger(CustomFileUploadController.class);

	@ApiOperation(value = "filegroup upload", notes = "filegroup upload")
	@RequestMapping(value = "/fileGroup/upload", method = RequestMethod.POST)
	public ResponseEntity<?> fileGroupUpload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {

		FileGroup fileGroup = fileGroupService.saveFileGroup(request,
				coreGridFsService);
		String rootPath = request.getContextPath();
		String type = request.getParameter("uploadType");
		JSONObject jsonObject = new JSONObject();
		if (fileGroup != null) {
			jsonObject.put("groupId", fileGroup.getId());
			if (StringUtil.notEmpty(fileGroup.getDescription())) {
				jsonObject.put("comment", fileGroup.getDescription());
			} else {
				jsonObject.put("comment", fileGroup.getGroupName());
			}

			if (fileGroup.getLists() != null && fileGroup.getLists().size() > 0) {
				for (File file : fileGroup.getLists()) {
					file.setFilePath(configInfo.getEmailServerPath()+rootPath + Constants.IMAGE_REST_CORE_SHOWRESOURCE
							+ file.getFilePath());
				}
			}
			if (StringUtil.isEmpty(type) || !"ImageGroup".equals(type)) {
				if (fileGroup.getLists() != null
						&& fileGroup.getLists().size() > 0) {
					jsonObject.put("url",fileGroup.getLists().get(0)
							.getFilePath());
				}
			}
		}
		return ResponseUtils.successWithValue(jsonObject);
		} catch (Exception e) {
			log.error("upload file group  error : " + e);
			return ResponseUtils
					.fail(ResponseErrorCode.UPLOAD_FILE_ERROR.value(),
							"Upload filegroup error : " + e.getMessage(),
							HttpStatus.OK);
		}
	}

	@ApiOperation(value = "file upload", notes = "file upload")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<?> fileUpload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
		List<File> listFiles = fileService.uploadFile(request,
				coreGridFsService);
		String rootPath = request.getContextPath();
		if (listFiles != null && listFiles.size() > 0) {
			for (File file : listFiles) {
				file.setFilePath(rootPath + Constants.IMAGE_REST_CORE_SHOWRESOURCE
						+ file.getFilePath());
			}
		}
		return ResponseUtils.successWithValues(listFiles);
		} catch (Exception e) {
			log.error("upload file   error : " + e);
			return ResponseUtils
					.fail(ResponseErrorCode.UPLOAD_FILE_ERROR.value(),
					"Upload file error : " + e.getMessage(),
							HttpStatus.OK);
		}
	}

}
