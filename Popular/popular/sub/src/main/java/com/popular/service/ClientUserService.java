package com.popular.service;

import java.util.List;
import java.util.Map;

import com.popular.exception.DBException;
import com.popular.model.ClientUser;
import com.popular.rest.api.UserController.loginUser;

public interface ClientUserService {
	ClientUser getClientUserByPhoneNumber(String phoneNumber)
			throws DBException;

	ClientUser getClientUserByCode(String code) throws DBException;

	List<ClientUser> getClientUserByIds(List<Integer> ids) throws DBException;

	ClientUser getClientUserById(Integer id) throws DBException;

	int add(ClientUser clientUser) throws DBException;

	int update(ClientUser clientUser) throws DBException;

	int deleteById(Integer id) throws DBException;
	
	ClientUser getClientUserByPhoneAndPassword(loginUser user) throws DBException;
	
	void register(ClientUser clientUser, String phoneNumber, String deviceUniqueKey) throws DBException;

	List<ClientUser> getClientUserByMap(Map<String, Object> params) throws DBException;
}
