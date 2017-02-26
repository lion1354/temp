package com.popular.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popular.dao.ClientUserDao;
import com.popular.dao.LoginInformationDao;
import com.popular.exception.DBException;
import com.popular.model.ClientUser;
import com.popular.model.LoginInformation;
import com.popular.rest.api.UserController.loginUser;
import com.popular.service.ClientUserService;

@Service
public class ClientUserServiceImpl implements ClientUserService {
	private final static Logger log = LoggerFactory.getLogger(ClientUserServiceImpl.class);
	@Autowired
	private ClientUserDao clientUserDao;
	
	@Autowired
	private LoginInformationDao loginfoDao;

	@Override
	public ClientUser getClientUserByPhoneNumber(String phoneNumber) throws DBException {
		return clientUserDao.getClientUserByPhoneNumber(phoneNumber);
	}

	@Override
	public ClientUser getClientUserByCode(String code) throws DBException {
		return clientUserDao.getClientUserByCode(code);
	}

	@Override
	public List<ClientUser> getClientUserByIds(List<Integer> ids) throws DBException {
		return clientUserDao.getClientUserByIds(ids);
	}

	@Override
	public ClientUser getClientUserById(Integer id) throws DBException {
		return clientUserDao.getClientUserById(id);
	}

	@Override
	public int add(ClientUser clientUser) throws DBException {
		return clientUserDao.add(clientUser);
	}

	@Override
	public int update(ClientUser clientUser) throws DBException {
		return clientUserDao.update(clientUser);
	}

	@Override
	public int deleteById(Integer id) throws DBException {
		return clientUserDao.deleteById(id);
	}

	@Override
	public ClientUser getClientUserByPhoneAndPassword(loginUser user) throws DBException {
		return clientUserDao.getClientUserByPhoneAndPassword(user);
	}

	/**
	 * 用戶註冊，保存信息
	 * 
	 *  Aidan
	 */
	@Override
	public void register(ClientUser clientUser, String phoneNumber, String deviceUniqueKey) throws DBException {
		log.debug(phoneNumber + "," + deviceUniqueKey);
		add(clientUser);
		clientUser = getClientUserByPhoneNumber(phoneNumber);
		LoginInformation info = new LoginInformation(clientUser.getId(), deviceUniqueKey, new Date());
		loginfoDao.add(info);
	}

	@Override
	public List<ClientUser> getClientUserByMap(Map<String, Object> params) throws DBException {
		return clientUserDao.getClientUserByMap(params);
	}
}
