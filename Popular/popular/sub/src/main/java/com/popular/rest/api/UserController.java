package com.popular.rest.api;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.popular.common.util.MD5Utils;
import com.popular.common.util.StringUtils;
import com.popular.model.ClientUser;
import com.popular.model.LoginInformation;
import com.popular.responseutil.ResponseUtils;
import com.popular.service.CityService;
import com.popular.service.ClientUserService;
import com.popular.service.LoginInformationService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.sf.json.JSONObject;

/**
 * 
 * @author Aidan
 *
 * @date 2016/7/21
 *
 */
@Api(value = "user")
@RestController
@RequestMapping("/1/user")
public class UserController {
	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	CityService cityService;

	@Autowired
	private ClientUserService userService;

	@Autowired
	private LoginInformationService loginInformationService;
	
	/**
	 * 注册
	 * @param deviceUniqueKey
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "new user register", notes = "new user register", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@RequestHeader(value = "client_id", required = true) String deviceUniqueKey,
			@ApiParam(name = "user", required = true) @RequestBody(required = true) String user) {
		if (StringUtils.isEmpty(deviceUniqueKey))
			return ResponseUtils.fail("The device unique key is necessary!");
		try {
			if (!JSONObject.fromObject(user).containsKey("phoneNumber"))
				return ResponseUtils.fail("The phone number is necessary!");
			String phone = JSONObject.fromObject(user).getString("phoneNumber");
			ClientUser clientUser = userService.getClientUserByPhoneNumber(phone);

			if (clientUser != null)
				return ResponseUtils.fail("The user had register!");

			LoginInformation info = loginInformationService.getLoginfoByDevice(deviceUniqueKey);
			if (info != null)
				return ResponseUtils.fail("The device had register!");

			clientUser = new ClientUser(JSONObject.fromObject(user));
			userService.register(clientUser, phone, deviceUniqueKey);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("server error", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 登录
	 * @param deviceUniqueKey
	 * @param loginUser
	 * @return
	 */
	@ApiOperation(value = "user login", notes = "user login", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestHeader(value = "client_id", required = true) String deviceUniqueKey,
			@ApiParam(name = "loginUser", required = true) @RequestBody(required = true) String loginUser) {

		log.debug("deviceUniqueKey : " + deviceUniqueKey);
		if (StringUtils.isEmpty(deviceUniqueKey))
			return ResponseUtils.fail("The device unique key is necessary!");
		try {
			JSONObject jsonUser = JSONObject.fromObject(loginUser);
			if (!jsonUser.containsKey("phoneNumber"))
				return ResponseUtils.fail("Phone number is necessary!");
			ClientUser user = userService.getClientUserByPhoneAndPassword(new loginUser(jsonUser));
			if (user == null) {
				return ResponseUtils.fail("Phone number or password error!");
			}

			LoginInformation info = loginInformationService.getLoginfoByUserId(user.getId());

			if (info != null && StringUtils.notEmptyAndEqual(info.getDeviceUniqueKey(), deviceUniqueKey)) {
				return ResponseUtils.successWithValue(user);
			}

			if (info != null && !StringUtils.notEmptyAndEqual(info.getDeviceUniqueKey(), deviceUniqueKey)) {
				info.setDeviceUniqueKey(deviceUniqueKey);
				loginInformationService.updateDeviceByUserId(info);
			}

			return ResponseUtils.successWithValue(user);
		} catch (Exception e) {
			log.error("error: ", e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 忘记密码
	 * @param phoneAndPassword
	 * @return
	 */
	@ApiOperation(value = "forgot password", notes = "forgot password", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public ResponseEntity<?> forgotPassword(
			@ApiParam(name = "phoneAndPassword", required = true) @RequestBody(required = true) String phoneAndPassword) {
		try {
			JSONObject jsonUser = JSONObject.fromObject(phoneAndPassword);
			if (!jsonUser.containsKey("phoneNumber"))
				return ResponseUtils.fail("Phone number is necessary!");
			String phoneNumber = jsonUser.getString("phoneNumber");
			ClientUser user = userService.getClientUserByPhoneNumber(phoneNumber);
			if (user == null)
				return ResponseUtils.fail("Phone number is not correct!");
			if (!jsonUser.containsKey("password"))
				return ResponseUtils.fail("New password is necessary!");
			user.setPassword(MD5Utils.md5Encode(jsonUser.getString("password")));
			userService.update(user);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 修改密码
	 * @param phoneAndPassword
	 * @return
	 */
	@ApiOperation(value = "reset password", notes = "reset password", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public ResponseEntity<?> resetPassword(
			@ApiParam(name = "phoneAndPassword", required = true) @RequestBody(required = true) String phoneAndPassword) {
		try {
			JSONObject jsonUser = JSONObject.fromObject(phoneAndPassword);
			if (!jsonUser.containsKey("phoneNumber"))
				return ResponseUtils.fail("Phone number is necessary!");
			if (!jsonUser.containsKey("oldPassword"))
				return ResponseUtils.fail("Old password is necessary!");
			String phoneNumber = jsonUser.getString("phoneNumber");
			String oldPassword = jsonUser.getString("oldPassword");
			ClientUser user = userService
					.getClientUserByPhoneAndPassword(new loginUser(phoneNumber, MD5Utils.md5Encode(oldPassword)));
			if (user == null)
				return ResponseUtils.fail("Phone number or old password is not correct!");
			if (!jsonUser.containsKey("newPassword"))
				return ResponseUtils.fail("New password is necessary!");
			user.setPassword(MD5Utils.md5Encode(jsonUser.getString("newPassword")));
			userService.update(user);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 查询用户
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "get user by id", notes = "get user by id", httpMethod = "GET", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserById(@ApiParam(name = "id", required = true) @PathVariable(value = "id") int id) {
		try {
			ClientUser user = userService.getClientUserById(id);
			if(user == null)
				return ResponseUtils.fail("id is not correct!");
			return ResponseUtils.successWithValue(user);
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "update user info", notes = "update user info", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<?> updateUserInfo(
			@ApiParam(name = "user", required = true) @RequestBody(required = true) String user) {
		try {
			ClientUser userinfo = new ClientUser(JSONObject.fromObject(user));
			if (userinfo.getId() == null)
				return ResponseUtils.fail("User id is necessary!");
			userinfo.setPassword(null);
			userinfo.setSexualOrientation(null);
			userinfo.setUpdateTime(new Date());
			userService.update(new ClientUser(JSONObject.fromObject(user)));
			userinfo = userService.getClientUserById(userinfo.getId());
			return ResponseUtils.successWithValue(userinfo);
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}
	
	public class loginUser {
		private String phoneNumber;
		private String password;

		public loginUser() {
			super();
		}

		public loginUser(JSONObject user) {
			super();
			this.phoneNumber = user.containsKey("phoneNumber") ? user.getString("phoneNumber") : null;
			this.password = user.containsKey("password") ? MD5Utils.md5Encode(user.getString("password")) : null;
		}

		public loginUser(String phoneNumber, String password) {
			super();
			this.phoneNumber = phoneNumber;
			this.password = password;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}

}
