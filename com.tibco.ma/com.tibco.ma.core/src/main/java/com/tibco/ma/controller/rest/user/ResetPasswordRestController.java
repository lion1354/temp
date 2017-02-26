package com.tibco.ma.controller.rest.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.MD5Util;
import com.tibco.ma.common.MaException;
import com.tibco.ma.common.ResponseErrorCode;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.mail.MailUtils;
import com.tibco.ma.model.App;
import com.tibco.ma.model.ClientUser;
import com.tibco.ma.service.AppService;
import com.tibco.ma.service.ClientUserService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * 
 * @author aidan
 * 
 * 
 */
@RestController
@RequestMapping("/1/user")
public class ResetPasswordRestController {
	private static Logger log = LoggerFactory
			.getLogger(ResetPasswordRestController.class);

	@Resource
	private ClientUserService clientUserService;

	@Resource
	private ConfigInfo configInfo;

	@Resource
	private AppService appService;

	@ApiOperation(value = "reset password", notes = "reset password", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/resetpwd", method = RequestMethod.POST)
	public ResponseEntity<?> getJSONPwd(
			@ApiParam(value = "app id", required = true) @RequestHeader(value = "X-MA-Application-Id", required = true) String appKey,
			@ApiParam(value = "rest_api_key", required = true) @RequestHeader(value = "X-MA-REST-API-Key", required = true) String rest_api_key,
			@ApiParam(value = "email password") @RequestBody String email,
			HttpServletRequest request) throws MaException {
		JSONObject json;
		String emailID = null;
		try {
			json = JSONObject.fromObject(email);
			emailID = (String) json.getString("email");

			resetpwd(emailID, appKey, request);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("reset password Exception: ", e);
			return ResponseUtils.fail(
					ResponseErrorCode.RESET_PWD_ERROR.value(), e.getMessage(),
					HttpStatus.OK);
		}
	}

	private void resetpwd(String email, String appKey,
			HttpServletRequest request) {
		try {
			if (StringUtil.isEmpty(email))
				throw new MaException("email is necessary!");
			Query query = new Query();
			query.addCriteria(Criteria.where("email").is(email));
			query.addCriteria(Criteria.where("app").is(new App(appKey)));
			ClientUser user = clientUserService
					.findOne(query, ClientUser.class);
			if (user == null) {
				throw new Exception("Email is wrong!");
			} else {
				Long time = System.currentTimeMillis();
				String password_reset_code = MD5Util.encode2hex(email + "_"
						+ appKey + "_" + time);
				Update update = Update.update("lastResetPwdTime", time);
				update.set("passwordResetCode", password_reset_code);
				clientUserService.update(query, update, ClientUser.class);

				String appName = appService.findById(new ObjectId(appKey),
						App.class).getName();
				String text = "We've received a request to reset your "
						+ appName + " account password. <br>To continue, "
						+ "please click below.<br>" + "<a href='"
						+ configInfo.getEmailServerPath()
						+ request.getContextPath()
						+ configInfo.getEmailResetPWDUri() + "?id="
						+ user.getId() + "&password_reset_code="
						+ password_reset_code + "'>Click to Reset Password</a>";
				String subject = "Your " + appName + " Account Reset Password!";
				MailUtils.instance().send(user.getEmail(), text, subject);
			}
		} catch (Exception e) {
			log.error("reset password error:", e);
		}
	}
}
