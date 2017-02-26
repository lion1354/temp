package com.tibco.ma.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.Constants;
import com.tibco.ma.common.MD5Util;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.mail.MailUtils;
import com.tibco.ma.model.AdminUser;
import com.tibco.ma.service.AdminUserService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 *
 *
 */
@Controller
@RequestMapping("register")
public class RegisterController {

	private static Logger log = LoggerFactory
			.getLogger(RegisterController.class);

	@Resource
	private AdminUserService userService;

	@Resource
	private ConfigInfo configInfo;

	@RequestMapping("")
	public ResponseEntity<?> register(
			@RequestBody(required = true) JSONObject json,
			HttpServletRequest request) throws Exception {
		if (!json.containsKey("username") || !json.containsKey("password")) {
			return ResponseUtils.alert("Username or password is null");
		} else if (!json.containsKey("email")) {
			return ResponseUtils.alert("Email is null");
		} else {
			AdminUser user = new AdminUser();
			user.setUsername(json.get("username").toString().toLowerCase());
			user.setPassword(MD5Util.convertMD5Password((String) json
					.get("password")));
			user.setEmail((String) json.get("email"));
			boolean flag = userService.exists(user);
			if (flag) {
				return ResponseUtils.alert("User had existed!");
			} else {
				if (userService.findOne(
						new Query(Criteria.where("email").is(user.getEmail())),
						AdminUser.class) != null)
					return ResponseUtils.alert("The email has been used!");
				// send register email required info --start
				// update by Wade 2015/4/22
				user.setState("0");
				user.setRegisterTime(System.currentTimeMillis());
				user.setActiveCode(MD5Util.encode2hex((String) json
						.get("email")));
				// send register email required info --end
				user.setType("free");
				userService.register(user);

				String baseUri = configInfo.getEmailServerPath()
						+ configInfo.getEmailContextPath();
				String text = Constants.EMAIL_SERVER_TEXT + "<a href='"
						+ baseUri + "/register/activate/adminuser?activeCode="
						+ user.getActiveCode() + "&email=" + user.getEmail()
						+ "'>Activate Account</a>";
				String subject = "Your MobilePlatform Account Activation";
				MailUtils.instance().send((String) json.get("email"), text,
						subject);
				return ResponseUtils.success();
			}
		}
	}

	/**
	 * add by Wade 2015/4/22
	 * 
	 * @param email
	 * @param activeCode
	 * @return
	 */
	@ApiOperation(value = "activate user", notes = "activate user")
	@RequestMapping("activate/adminuser")
	public ModelAndView activeUser(
			@ApiParam(value = "email") @RequestParam("email") String email,
			@ApiParam(value = "active code") @RequestParam("activeCode") String activeCode) {
		ModelAndView mav = new ModelAndView();
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email.trim()));
		AdminUser user = null;
		try {
			user = userService.findOne(query, AdminUser.class);
			if (user != null) {
				if (user.getState().equals("0")) {
					// TODO
					if (user.getRegisterTime()
							+ Long.parseLong(configInfo.getEmailServerTimeout()) > System
								.currentTimeMillis()) {
						if (user.getActiveCode().equals(activeCode)) {
							query = new Query();
							query.addCriteria(Criteria.where("email").is(
									email.trim()));
							Update update = Update.update("state", "1");
							userService.update(query, update, AdminUser.class);
							mav.setViewName("redirect:/#/activate");
						} else {
							mav.setViewName("redirect:/#/login?errorMessage=The active code is error!");
						}
					} else {
						mav.setViewName("redirect:/#/login?errorMessage=The email has been out of time!");
					}
				} else {
					mav.setViewName("redirect:/#/login?errorMessage=The email has been activated,please login!");
				}
			} else {
				mav.setViewName("redirect:/#/login?errorMessage=You haven't register the email!");
			}
		} catch (Exception e) {
			log.error("{}", e);
			mav.setViewName("redirect:/#/login?errorMessage=" + e.getMessage());
		}
		return mav;
	}
}
