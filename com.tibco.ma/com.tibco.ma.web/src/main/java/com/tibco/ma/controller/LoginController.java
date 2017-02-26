package com.tibco.ma.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.MD5Util;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.log.Log;
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
public class LoginController {
	private static Logger log = LoggerFactory.getLogger(LoginController.class);

	@Resource
	private AdminUserService userService;

	@Resource
	private ConfigInfo configInfo;

	protected String getUserName() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		log.info(userDetails.toString());
		String username = userDetails.getUsername();
		return username;
	}

	@Log(operate = "user login", modelName = "security")
	@ApiOperation(value = "login", notes = "login")
	@RequestMapping("login/dispatcher")
	public String dispatcher(HttpServletRequest request) {
		log.info(getUserName());
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(getUserName()));

		AdminUser user = null;
		try {
			user = userService.findOne(query, AdminUser.class);
			HttpSession session = request.getSession();
			session.setAttribute("logined", "Yes");
			session.setMaxInactiveInterval(600);

			if (StringUtil.notEmptyAndEqual(user.getType(), "free")) {
				return "redirect:/#/apps";
			} else if (StringUtil.notEmptyAndEqual(user.getType(), "fee")) {
				return "fee";
			} else {
				return "redirect:/#/systemManagement/home";
			}
		} catch (Exception e) {
			log.error("{}", e);
			return "redirect:/";
		}
	}

	@ApiOperation(value = "check time out", notes = "check time out")
	@RequestMapping(value = "checktimeout", method = RequestMethod.GET)
	public ResponseEntity<?> check(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("logined") == null) {
				return ResponseUtils.successWithValue("true");
			}
			return ResponseUtils.successWithValue("false");
		} catch (Exception e) {
			log.error("session time out", e);
			return ResponseUtils.successWithValue("true");
		}
	}
	
	@ApiOperation(value = "clear session when log out", notes = "clear session when log out")
	@RequestMapping(value = "clearSession", method = RequestMethod.GET)
	public ResponseEntity<?> clearSession(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			session.setAttribute("logined", null);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("clear session : ", e);
			return ResponseUtils.fail("Clear session : " + e.getMessage());
		}
	}

	/**
	 * login check
	 * 
	 * @param json
	 * @return
	 */

	@ApiOperation(value = "check login", notes = "check login")
	@RequestMapping(value = "checklogin", method = RequestMethod.POST)
	public ResponseEntity<?> checkLogin(
			@ApiParam(value = "json") @RequestBody JSONObject json) {
		String username = null;
		String password = null;
		if (json.has("username"))
			username = json.getString("username");
		else
			return ResponseUtils.fail("Username is null");
		if (json.has("password"))
			password = json.getString("password");
		else
			return ResponseUtils.fail("Password is null");
		password = MD5Util.convertMD5Password(password);
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username.toLowerCase())).addCriteria(
				Criteria.where("password").is(password));
		try {
			AdminUser user = userService.findOne(query, AdminUser.class);
			if (user != null)
				return ResponseUtils.success();
			else
				return ResponseUtils.fail("Username or password is incorrect");
		} catch (Exception e) {
			log.error("login check error:", e);
			return ResponseUtils.fail("Username or password is incorrect");
		}
	}

	@ApiOperation(value = "send reset password mail", notes = "send reset password mail")
	@RequestMapping(value = "site/forgetPWD", method = RequestMethod.POST)
	public ResponseEntity<?> sendResetPWDMail(
			@ApiParam(value = "json") @RequestBody JSONObject json,
			HttpServletRequest request) {
		String email = null;
		if (json.has("email"))
			email = json.getString("email");
		else
			return ResponseUtils.fail("Email is null");
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));

		AdminUser user = null;
		try {
			user = userService.findOne(query, AdminUser.class);
			if (user == null)
				return ResponseUtils.fail("Email is incorrect");

		} catch (Exception e) {
			log.error("email error:", e);
			return ResponseUtils.fail("Email is incorrect:" + e.getMessage());
		}

		Long time = System.currentTimeMillis();

		String forgotPWDCode = MD5Util.encode2hex((String) json.get("email")
				+ time.toString());

		Update update = Update.update("siteForgotPWDTime", time).set(
				"siteForgotPWDCode", forgotPWDCode);
		try {
			userService.update(query, update, AdminUser.class);
		} catch (Exception e) {
			log.error("update user massage error:", e);
			return ResponseUtils.fail("Update user massage error:"
					+ e.getMessage());
		}

		String baseUri = configInfo.getEmailServerPath()
				+ configInfo.getEmailContextPath();

		String text = user.getUsername() + ",<br><br>";
		text += "A request to reset your password has been made. If you did not make this request, simply ignore this email. If you did make this request just click the link below:";
		text += "<br><br><a href=" + baseUri
				+ "/#/resetpassword?code="
				+ forgotPWDCode + "&email=" + email + ">reset your password</a><br><br>";
		text += "This link will expire in 10 minutes. If the above URL does not work try copying and pasting it into your browser. If you continue to have problems please feel free to contact us.";
		String subject = "Reset your mobilePlatform password instructions";

		try {
			MailUtils.instance().send(email, text, subject);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("send mail error:", e);
			return ResponseUtils.fail("Send mail error: " + e.getMessage());
		}
	}

	@ApiOperation(value = "reset password", notes = "reset password")
	@RequestMapping(value = "resetPWD", method = RequestMethod.POST)
	public ResponseEntity<?> resetPWD(
			@ApiParam(value = "json") @RequestBody JSONObject json) {
		String newPWD = null;
		if (json.has("newPassword"))
			newPWD = json.getString("newPassword");
		else {
			return ResponseUtils.fail("NewPassword is null");
		}
		String reNewPWD = null;
		if (json.has("confirmPassword"))
			reNewPWD = json.getString("confirmPassword");
		else {
			return ResponseUtils.fail("ConfirmPassword is null");
		}

		AdminUser user = null;
		if (json.has("email")) {
			String email = json.getString("email");

			Query query = new Query();
			query.addCriteria(Criteria.where("email").is(email));
			try {
				user = userService.findOne(query, AdminUser.class);
			} catch (Exception e) {
				log.error("email error:", e);
				return ResponseUtils.fail("Email is incorrect");
			}

			if ((user.getSiteForgotPWDTime() + 60 * 10 * 1000) < System
					.currentTimeMillis()) {
				return ResponseUtils.fail("Your request has timed out");
			}

			String code = null;
			if (json.has("code"))
				code = json.getString("code");
			else {
				return ResponseUtils.fail("Code is null");
			}

			if (!StringUtil.notEmptyAndEqual(user.getSiteForgotPWDCode(), code)) {
				return ResponseUtils.fail("Your request is invalid");
			}

			if (!newPWD.equals(reNewPWD)) {
				return ResponseUtils
						.fail("Two times the password is different");
			}

			Update update = Update.update("password",
					MD5Util.convertMD5Password(newPWD));
			try {
				userService.update(query, update, AdminUser.class);
				return ResponseUtils.success();
			} catch (Exception e) {
				log.error("reset password error:", e);
				return ResponseUtils.fail("Reset password error :"
						+ e.getMessage());
			}
		} else {
			return ResponseUtils.fail("Email is null");
		}

	}
}
