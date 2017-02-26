package com.tibco.ma.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.model.Progress;
import com.wordnik.swagger.annotations.ApiOperation;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@Controller
@RequestMapping("/ma/1/fileStatus")
public class ProgressController {
	@ApiOperation(value = "upfile progress", notes = "upfile progress")
	@RequestMapping(value = "/upfile/progress", method = RequestMethod.POST)
	public ResponseEntity<?> initCreateInfo(HttpServletRequest request) {
		Progress status = (Progress) request.getSession().getAttribute("upload_Progress");
		if (status == null) {
			return null;
		}
		return ResponseUtils.successWithValue(status);
	}

}
