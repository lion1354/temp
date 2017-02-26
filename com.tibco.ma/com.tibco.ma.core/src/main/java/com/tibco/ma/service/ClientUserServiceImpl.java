package com.tibco.ma.service;

import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tibco.ma.common.ConfigInfo;
import com.tibco.ma.common.Constants;
import com.tibco.ma.common.MD5Util;
import com.tibco.ma.common.MaException;
import com.tibco.ma.common.ResponseErrorCode;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.mail.MailUtils;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.ClientUserDao;
import com.tibco.ma.model.App;
import com.tibco.ma.model.ClientUser;
import com.tibco.ma.model.MongoDBConstants;

@Service
public class ClientUserServiceImpl extends BaseServiceImpl<ClientUser>
		implements ClientUserService {

	private static Logger log = LoggerFactory
			.getLogger(ClientUserServiceImpl.class);

	@Resource
	private ClientUserDao dao;

	@Override
	public BaseDao<ClientUser> getDao() {
		return dao;
	}

	@Resource
	private ConfigInfo configInfo;
	
	@Resource
	private EntityService entityService;
	
	@Resource
	private ValuesService valuesService;
	
	@Resource
	private AppService appService;

	// for normal user registration
	@Override
	public ClientUser register(String email, String password, String appKey,
			HttpServletRequest request) throws MaException {
		email = email.toLowerCase();
		// create login by replacing @ and .com from the String
		String login = email.split("@")[0];
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(1000);
		login = login + "" + Integer.toString(randomInt);
		// Consider first name to be the email id
		String firstName = email.split("@")[0];
		// consider last name to be the domain of the email
		String lastName = '@' + email.split("@")[1];
		return registerUser(email, password, login, firstName, lastName, "",
				appKey, request);
	}

	// for facebook user registration
	@Override
	public ClientUser register(String email, String password, String fname,
			String lname, String source, String appKey,
			HttpServletRequest request) throws MaException {
		email = email.toLowerCase();
		// create login by getting part of email before @
		String login = email.split("@")[0];
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(1000);
		login = login + "" + Integer.toString(randomInt);
		String firstName = null, lastName = null;
		// Consider first name to be the email id
		if (fname.equals("")) {
			firstName = email.split("@")[0];
		} else {
			firstName = fname;
		}
		// consider last name to be the domain of the email
		if (lname.equals("")) {
			lastName = email.split("@")[1];
			lastName = lastName.replace(".", "");
		} else {
			lastName = lname;
		}
		return registerUser(email, password, login, firstName, lastName,
				source, appKey, request);
	}

	private ClientUser registerUser(String email, String password,
			String login, String firstName, String lastName, String source,
			String appKey, HttpServletRequest request) throws MaException {
		ClientUser user = null;
		ClientUser userInDB = getUserByEmailAndApp(email, appKey);

		// if account does not exist for the user
		if (userInDB == null) {
			log.info("Account does not exist");

			user = new ClientUser();
			user.setUsername(firstName);
			user.setPassword(MD5Util.convertMD5Password(password));
			user.setApp(new App(appKey));
			user.setEmail(email);
			user.setFirstname(firstName);
			user.setLastname(lastName);
			user.setLoyaltyAccountExist(0);
			user.setShopperId(0);
			if (StringUtil.notEmptyAndEqual(source, "facebook")) {
				user.setAccountType(2);
				user.setState("1");
			} else if (StringUtil.notEmptyAndEqual(source, "twitter")) {
				user.setAccountType(3);
				user.setState("1");
			} else {
				user.setAccountType(1);
				user.setState("0");
			}
			long time = System.currentTimeMillis();
			user.setCreateDateTime(time);
			user.setUpdateDateTime(time);
			user.setLastAccessed(time);
			user.setRegisterTime(time);
			user.setCode(MD5Util.encode2hex(email + "_" + appKey + "_" + time));
			dao.save(user);
			
//			if(StringUtil.notEmptyAndEqual(user.getState(), "1")){
				try {
					initUserInfo(user);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("init user info error : " + e);
				}
//			}

			try {
				String baseUri = configInfo.getEmailServerPath() + "/"
						+ request.getContextPath()
						+ configInfo.getEmailActivateUri();
				String text = Constants.EMAIL_SERVER_TEXT + "<a href='"
						+ baseUri + "?code=" + user.getCode()
						+ "'>Activate Account</a>";
				String subject = "Your " +appService.findById(new ObjectId(appKey), App.class).getName()+ " Account Activation!";
				MailUtils.instance().send(email, text, subject);
			} catch (Exception e) {
				log.error("send email error:", e);
			}

		} else {
			if (userInDB.getAccountType() == 2) {
				log.info(email
						+ " is used by an Facebook account, please login with Facebook!");
				throw new MaException(
						ResponseErrorCode.EXISTING_FACEBOOK_EMAIL,
						email
								+ " is used by an Facebook account, please login with Facebook!");
			} else if (userInDB.getAccountType() == 3) {
				log.info(email
						+ " is used by a Twitter account, please login with Twitter!");
				throw new MaException(
						ResponseErrorCode.EXISTING_TWITTER_EMAIL,
						email
								+ " is used by a Twitter account, please login with Twitter!");
			} else {
				
				if(StringUtil.notEmptyAndEqual(userInDB.getState(), "1")) {
					log.info(email
							+ " is used by an existing account, please login directly!");
					throw new MaException(
							ResponseErrorCode.EXISTING_EMAIL,
							email
									+ " is used by an existing account, please login directly!");
				} else {
					log.info(email
							+ " is used by an register account, but not activate, please activate!");
					throw new MaException(
							ResponseErrorCode.EXISTING_EMAIL,
							email
									+ " is used by an register account, but not activate, please activate!");
				}
			}
		}

		log.info("registerUser called end!");
		return user;
	}

	public ClientUser getUserByEmailAndApp(String email, String appKey) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));
		query.addCriteria(Criteria.where("app").is(new App(appKey)));
		ClientUser user = dao.findOne(query, ClientUser.class);
		return user;
	}

	@Override
	public ClientUser getUserByToken(String authtoken) {
		Query query = new Query(Criteria.where("token").is(authtoken));
		return dao.findOne(query, ClientUser.class);
	}

	@Override
	public ClientUser login(String email, String password, String appKey)
			throws MaException {
		log.info("login with email: " + email);

		try {
			email = email.toLowerCase();
			ClientUser userInDB = getUserByEmailAndApp(email, appKey);

			if (userInDB != null) {
				if (StringUtil.notEmptyAndEqual(userInDB.getPassword(),
						MD5Util.convertMD5Password(password))) {
					if (StringUtil.notEmptyAndEqual(userInDB.getState(), "1")) {
						Long time = System.currentTimeMillis();
						Query query = new Query();
						query.addCriteria(Criteria.where("email").is(email));
						query.addCriteria(Criteria.where("app").is(
								new App(appKey)));
						if (userInDB.getLoginCount() == null) {
							userInDB.setLoginCount(0);
						}
						Update update = Update
								.update("lastAccessed", time)
								.set("loginCount", userInDB.getLoginCount() + 1);
						dao.update(query, update, ClientUser.class);
					} else {
						throw new MaException(
								"you had not activated your Account! please login your email and activate your Account!");
					}
				} else {
					throw new MaException(
							"email or password is wrong! please try again!");
				}
				log.info("login completed!");
				return userInDB;
			} else {
				throw new MaException(ResponseErrorCode.INVALID_EMAIL,
						"user with this email doesn't exist!");
			}
		} catch (Exception e) {
			throw new MaException(ResponseErrorCode.INTERNAL_ERROR,
					e.getMessage());
		}
	}
	
	@Override
	public void initUserInfo(ClientUser user) throws Exception {
		Document userSystem = entityService.getEntityByClassName(user.getApp().getId(), MongoDBConstants.SYSTEM_USER_COLLECTION_NAME);
		initValue(userSystem, user, "email", user.getEmail());
		initValue(userSystem, user, "username", user.getUsername());
		initValue(userSystem, user, "password", user.getPassword());
		initValue(userSystem, user, "uniqueKey", user.getId());
		initValue(userSystem, user, "isActivate", (user.getState().equals("0") ? "false" : "true"));
	}

	private void initValue(Document entity, ClientUser user, String colName, String colValue) throws Exception {
		if (entity != null) {
			Document document = new Document(MongoDBConstants.VALUES_ENTITYID, entity.getString(MongoDBConstants.DOCUMENT_ID)).append("email", user.getEmail());
			String valuesCollectionName =  valuesService.getValuesCollectionName(user.getApp().getId(), MongoDBConstants.SYSTEM_USER_COLLECTION_NAME);
			Document value = valuesService.getOne(valuesCollectionName, document);
			Document col = new Document();
			col.append(colName, colValue);
			if (value != null) {
				valuesService.updateById(valuesCollectionName, value.getString(MongoDBConstants.DOCUMENT_ID), col);
			} else {
				valuesService.save(user.getApp().getId(), entity.getString(MongoDBConstants.DOCUMENT_ID), MongoDBConstants.SYSTEM_USER_COLLECTION_NAME, col);
			}
		}
	}
}
