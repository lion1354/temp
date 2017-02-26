package com.tibco.ma.controller.rest.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tibco.ma.common.CodecUtil;
import com.tibco.ma.common.MaException;
import com.tibco.ma.common.ResponseErrorCode;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.model.ClientUser;
import com.tibco.ma.service.ClientUserService;

@Service
public abstract class AbstractThirdPartyLoginRestController {
	private static Logger log = LoggerFactory
			.getLogger(AbstractThirdPartyLoginRestController.class);

	@Resource
	private ClientUserService userService;
	
	/**
	 * Generate the user need login. This object's email, firstName and lastName
	 * must be set.
	 * 
	 * @return
	 * @throws GSEException
	 */
	public abstract ClientUser genUser() throws MaException;

	/**
	 * The third party's name, such as "facebook", "twitter".<br>
	 * This value will be used as source property in TIBBR registration.
	 * 
	 * @return
	 */
	public abstract String thirdpartName();

	public ResponseEntity<?> doLogin(String appKey, HttpServletRequest request)
			throws MaException {
		ClientUser user = null;
		try {
			ClientUser u = genUser();
			String email = u.getEmail(), id = u.getId(), firstName = u
					.getFirstname(), lastName = u.getLastname();
			String password = CodecUtil.genPassword(email, id);

			ClientUser userInDB = userService.getUserByEmailAndApp(email,
					appKey);
			// user with the email doesn't exist, create one
			if (userInDB == null) {
				log.info("user doesn't exist, registering " + thirdpartName()
						+ " User .. ");
				user = userService.register(email, password, firstName,
						lastName, thirdpartName(), appKey, request);
				if (user != null) {
					log.info("register success, logging in...");
					user = userService.login(email, password, appKey);
					log.info("login ret -> " + user.getLoginCount());
				}
			} else {
				if (userInDB.getAccountType() == 2
						|| userInDB.getAccountType() == 3) {
					// user exists and tibbr account exist
					user = userService.login(email, password, appKey);
					log.info("login ret: " + +user.getLoginCount());
				} else {
					log.info(email
							+ " is used by an existing account, please login directly!");
					return ResponseUtils
							.fail(ResponseErrorCode.EXISTING_EMAIL.value(),
									email
											+ " is used by an existing account, please login directly!",
									HttpStatus.OK);
				}
			}
			return ResponseUtils.successWithValue(user);
		} catch (MaException e) {
			log.error("AbstractThirdPartyLogin AppException: ", e);
			return ResponseUtils.fail(e.getErrorCode(), e.getMessage());
		} catch (Exception e) {
			log.error("AbstractThirdPartyLogin Exception: ", e);
			throw new MaException(e);
		}
	}
}
