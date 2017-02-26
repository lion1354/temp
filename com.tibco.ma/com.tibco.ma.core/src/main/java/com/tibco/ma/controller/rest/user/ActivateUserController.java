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
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.mail.MailUtils;
import com.tibco.ma.model.App;
import com.tibco.ma.model.ClientUser;
import com.tibco.ma.service.AppService;
import com.tibco.ma.service.ClientUserService;
import com.tibco.ma.service.EntityService;
import com.tibco.ma.service.ValuesService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller("ActivateUserController")
@RequestMapping("/1/user")
public class ActivateUserController {
	private static Logger log = LoggerFactory
			.getLogger(ActivateUserController.class);

	@Resource
	private ClientUserService clientUserService;

	@Resource
	private ConfigInfo configInfo;

	@Resource
	private ValuesService valuesService;

	@Resource
	private EntityService entityService;

	@Resource
	private AppService appService;

	@ApiOperation(value = "activate", notes = "activate")
	@RequestMapping(value = "/activate", method = RequestMethod.GET)
	public ModelAndView activate(
			@ApiParam(value = "code", required = true) @RequestParam(required = true, value = "code") String code,
			HttpServletRequest request) {
		long t = System.currentTimeMillis();
		boolean activation = false;
		String email = null;
		String appName = "";
		String iconUrl = "";
		Map<String, String> map = new HashMap<String, String>();
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("code").is(code));
			ClientUser user = clientUserService
					.findOne(query, ClientUser.class);
			
			App app = appService.findById(new ObjectId(user.getApp().getId()),
					App.class);
			appName += app.getName();
			iconUrl += app.getIconUrl();
			map.put("appName", appName);
			map.put("url", iconUrl);
			
			// check is activate already
			if (StringUtil.notEmptyAndEqual(user.getState(), "1")) {
				log.info("user had activated already.");
				return new ModelAndView("redirect:"
						+ configInfo.getEmailServerPath()
						+ configInfo.getEmailContextPath()
						+ "/app/views/fanzone/activateSuccess.jsp", map);
			}

			email = user.getEmail();
			Update update = Update.update("state", "1");
			clientUserService.update(query, update, ClientUser.class);
			user = clientUserService.findOne(query, ClientUser.class);
			clientUserService.initUserInfo(user);
			activation = true;

		} catch (Exception ex) {
			log.error("Active user '" + code + "' error!");
			activation = false;
		}

		log.info("Active user '" + code + "' spend: " + (System.currentTimeMillis() - t) + " ms. Result is: "
				+ activation);
		if (activation) {
			log.info("activate user password OK.");
			String success = "Congratulations, your " + appName
					+ " account has been officially activated!";
			try {
				String subject = "Your " + appName
						+ " Account Activate Success!";
				MailUtils.instance().send(email, success, subject);
			} catch (Exception e) {
				log.error("send activate success mail error:", e);
			}
			return new ModelAndView("redirect:"
					+ configInfo.getEmailServerPath()
					+ configInfo.getEmailContextPath()
					+ "/app/views/fanzone/activateSuccess.jsp", map);

		} else {
			log.info("activate user password Failure.");
			String failure = "<div>Sorry, Service unavailable ...<br /><br />Please try again later</div>";
			try {
				String subject = "Your " + appName
						+ " Account Activate Failure!";
				MailUtils.instance().send(email, failure, subject);
			} catch (Exception e) {
				log.error("send activate failure mail error:", e);
			}
			return new ModelAndView("redirect:"
					+ configInfo.getEmailServerPath()
					+ configInfo.getEmailContextPath()
					+ "/app/views/fanzone/failure.html");
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
