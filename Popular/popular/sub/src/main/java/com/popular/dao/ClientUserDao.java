package com.popular.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.popular.exception.DBException;
import com.popular.model.ClientUser;
import com.popular.rest.api.UserController.loginUser;

@Repository
public interface ClientUserDao {

	ClientUser getClientUserByPhoneNumber(String phoneNumber) throws DBException;

	ClientUser getClientUserByCode(String code) throws DBException;

	List<ClientUser> getClientUserByIds(List<Integer> ids) throws DBException;

	ClientUser getClientUserById(Integer id) throws DBException;

	int add(ClientUser clientUser) throws DBException;

	int update(ClientUser clientUser) throws DBException;

	int deleteById(Integer id) throws DBException;

	ClientUser getClientUserByPhoneAndPassword(loginUser user) throws DBException;

	List<ClientUser> getClientUserByMap(Map<String, Object> params) throws DBException;
}
