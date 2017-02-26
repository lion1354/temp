package com.tibco.ma.service;

import javax.servlet.http.HttpServletRequest;

import com.tibco.ma.common.MaException;
import com.tibco.ma.model.ClientUser;

public interface ClientUserService extends BaseService<ClientUser> {

	ClientUser register(String email, String password, String appKey, HttpServletRequest request) throws MaException;

	ClientUser getUserByToken(String authtoken);

	public ClientUser register(String email, String password, String fname,
			String lname, String source, String appKey, HttpServletRequest request) throws MaException;

	ClientUser login(String email, String password, String appKey) throws MaException;
	
	ClientUser getUserByEmailAndApp(String email, String appKey);
	
	void initUserInfo(ClientUser user) throws Exception; 
}
