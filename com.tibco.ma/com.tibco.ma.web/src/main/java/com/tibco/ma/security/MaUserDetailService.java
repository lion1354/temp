package com.tibco.ma.security;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tibco.ma.model.AdminUser;
import com.tibco.ma.service.AdminUserService;

public class MaUserDetailService implements UserDetailsService {
	private static final Logger log = LoggerFactory
			.getLogger(MaUserDetailService.class);
	@Resource
	private AdminUserService userService;

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {

		AdminUser user = null;
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("username").is(username.toLowerCase()));
			user = userService.findOne(query, AdminUser.class);

			if (user == null) {
				throw new UsernameNotFoundException("the user is not exist!");
			}

			// TODO update user login info here
			userService.updateUserLoginInfo(user);

			log.info("user " + user.getUsername() + " logging in ... ");
			return new MaUserDetails(user);
		} catch (Exception e) {
			throw new UsernameNotFoundException(username + " not found!", e);
		}

	}

}
