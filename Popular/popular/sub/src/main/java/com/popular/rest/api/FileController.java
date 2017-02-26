package com.popular.rest.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.popular.common.util.StringUtils;
import com.popular.model.FileType;
import com.popular.responseutil.ResponseUtils;
import com.popular.service.FileService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Api(value = "fileUpload")
@RestController
@RequestMapping("/1/file")
public class FileController {
	private static Logger log = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private FileService fileService;

	@ApiOperation(value = "upload files", notes = "upload files", httpMethod = "POST", consumes = "multipart/form-data", produces = "application/json")
	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<?> upload(HttpServletRequest request) {
		try {
			String userId = request.getParameter("userId");
			if(StringUtils.isEmpty(userId))
				return ResponseUtils.fail("UserId is necessary!");
			String type = request.getParameter("fileType");
			if(StringUtils.isEmpty(type))
				return ResponseUtils.fail("File type is necessary!");
			FileType fileType = FileType.valueOf(type);
			List<String> fileList = fileService.saveFile(request, userId, fileType);
			return ResponseUtils.successWithValue(fileList);
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	@ApiOperation(value = "delete files", notes = "delete files", httpMethod = "DELETE", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(
			@ApiParam(name = "filePaths", required = true) @RequestBody(required = true) String filePaths) {
		try {
			JSONObject json = JSONObject.fromObject(filePaths);
			JSONArray jsonArray = json.getJSONArray("filePaths");
			for (int i = 0; i < jsonArray.size(); i++) {
				String path = jsonArray.getString(i);
				fileService.deleteFile(path);
			}
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}
}
