package com.tibco.ma.controller.rest.user;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.MD5Util;
import com.tibco.ma.common.MaException;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.mail.MailUtils;
import com.tibco.ma.model.App;
import com.tibco.ma.model.ClientUser;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.AppService;
import com.tibco.ma.service.ClientUserService;
import com.tibco.ma.service.EntityService;
import com.tibco.ma.service.ValuesService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller("ChangeUserPasswordController")
@RequestMapping("/1/user")
public class ChangeUserPasswordController {
	private static Logger log = LoggerFactory
			.getLogger(ChangeUserPasswordController.class);

	@Resource
	private ClientUserService clientUserService;

	@Resource
	private ConfigInfo configInfo;

	@Resource
	private EntityService entityService;

	@Resource
	private ValuesService valuesService;

	@Resource
	private AppService appService;

	@ApiOperation(value = "change password", notes = "change password")
	@RequestMapping(value = "/changepwd", method = RequestMethod.GET)
	public ModelAndView changepwd(
			@ApiParam(value = "id", required = false) @RequestParam(required = false, value = "id") String id,
			@ApiParam(value = "password", required = false) @RequestParam(required = false, value = "pwd") String pwd,
			@ApiParam(value = "password_reset_code", required = false) @RequestParam(required = false, value = "password_reset_code") String password_reset_code,
			HttpServletRequest request) {

		String appName = "";
		String iconUrl = "";
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtil.notEmpty(id) && StringUtil.notEmpty(password_reset_code)
				&& StringUtil.isEmpty(pwd)) {
			log.info("forawrd to resetpwd html.");
			try {
				ClientUser user = clientUserService.findById(new ObjectId(id),
						ClientUser.class);
				App app = appService.findById(new ObjectId(user.getApp()
						.getId()), App.class);
				appName = app.getName();
				iconUrl = app.getIconUrl();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("redirect:"
					+ configInfo.getEmailServerPath()
					+ configInfo.getEmailContextPath()
					+ "/app/views/fanzone/resetpwd.html?id=" + id
					+ "&password_reset_code=" + password_reset_code
					+ "&appName=" + appName + "&url=" + iconUrl);
		}
		log.info("change user password userId: " + id
				+ ", password_reset_code: " + password_reset_code
				+ ", password: " + pwd);
		boolean passwordReset = false;

		try {
			changePassword(id, pwd, password_reset_code, map);
			passwordReset = true;
		} catch (Exception ex) {
			log.error("change password error:", ex);
			passwordReset = false;
		}

		if (passwordReset) {
			log.info("chagne user password OK.");
			String success = "<div>Your " + map.get("appName")
					+ " account password has been reset!</div>";
			try {
				String subject = "Your " + map.get("appName")
						+ " Account Reset Password Success!";
				ClientUser user = clientUserService.findById(new ObjectId(id),
						ClientUser.class);
				MailUtils.instance().send(user.getEmail(), success, subject);
			} catch (Exception e) {
				log.error("send mail error:", e);
			}

			return new ModelAndView("redirect:"
					+ configInfo.getEmailServerPath()
					+ configInfo.getEmailContextPath()
					+ "/app/views/fanzone/resetPWDSuccess.jsp", map);
		} else {
			log.info("chagne user password fails.");
			String failure = "<div>Sorry, Service unavailable ...<br /><br />Please try again later</div>";
			try {
				String subject = "Your " + map.get("appName")
						+ " Account Reset Password Failure!";
				ClientUser user = clientUserService.findById(new ObjectId(id),
						ClientUser.class);
				MailUtils.instance().send(user.getEmail(), failure, subject);
			} catch (Exception e) {
				log.error("send mail error:", e);
			}
			return new ModelAndView("redirect:"
					+ configInfo.getEmailServerPath()
					+ configInfo.getEmailContextPath()
					+ "/app/views/fanzone/failure.html");
		}
	}

	private void changePassword(String id, String pwd,
			String password_reset_code, Map<String, String> map)
			throws Exception {
		if (StringUtil.isEmpty(id))
			throw new MaException("Id is necessary!");
		ClientUser user = clientUserService.findById(new ObjectId(id),
				ClientUser.class);
		if (user == null) {
			throw new MaException("User not exist!");
		} else {

			App app = appService.findById(new ObjectId(user.getApp().getId()),
					App.class);
			map.put("appName", app.getName());
			map.put("url", app.getIconUrl());

			if (user.getLastResetPwdTime()
					+ Long.parseLong(configInfo.getEmailServerTimeout()) > System
						.currentTimeMillis()) {
				if (StringUtil.notEmptyAndEqual(user.getPasswordResetCode(),
						password_reset_code)) {
					if (StringUtil.notEmpty(pwd)) {
						Query query = new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
								new ObjectId(id)));
						Update update = Update.update("password",
								MD5Util.convertMD5Password(pwd)).set(
								"updateDateTime", System.currentTimeMillis());
						clientUserService.update(query, update,
								ClientUser.class);

						user = clientUserService.findById(new ObjectId(id),
								ClientUser.class);
						clientUserService.initUserInfo(user);
					} else {
						throw new MaException("Pwd is necessary!");
					}
				} else {
					throw new MaException(
							"Your reset password request is invalid");
				}
			} else {
				throw new MaException(
						"Your reset password request is time out!");
			}
		}
	}

//	private void initUserInfo(ClientUser user) throws Exception {
//		Query query = new Query();
//		query.addCriteria(Criteria.where("app").is(
//				new App(user.getApp().getId())));
//		query.addCriteria(Criteria.where("className").is("_User"));
//		Entity entity = entityService.findOne(query, Entity.class);
//		initValue(entity, user, "email", user.getEmail());
//		initValue(entity, user, "username", user.getUsername());
//		initValue(entity, user, "password", user.getPassword());
//		initValue(entity, user, "uniqueKey", user.getId());
//		initValue(entity, user, "isActivate",
//				(user.getState().equals("0") ? "false" : "true"));
//	}
//
//	private void initValue(Entity entity, ClientUser user, String colName,
//			String colValue) throws Exception {
//		if (entity != null) {
//			Document document = new Document("entityId", new ObjectId(
//					entity.getId())).append("email", user.getEmail());
//			Document value = valuesService.getOne("values", document);
//			if (value != null) {
//				valuesService.save("values", value.getObjectId("_id")
//						.toString(), entity.getId(), colName, colValue, null,
//						null);
//			} else {
//				valuesService.save("values", null, entity.getId(), colName,
//						colValue, null, null);
//			}
//		}
//	}
}
