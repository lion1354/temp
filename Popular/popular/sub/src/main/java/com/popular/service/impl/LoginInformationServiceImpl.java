package com.popular.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popular.dao.LoginInformationDao;
import com.popular.exception.DBException;
import com.popular.model.LoginInformation;
import com.popular.service.LoginInformationService;

/**
 * 
 * @author Aidan
 *
 *         2016/7/26
 */
@Service
public class LoginInformationServiceImpl implements LoginInformationService {

	@Autowired
	private LoginInformationDao dao;

	@Override
	public int add(LoginInformation info) throws DBException {
		return dao.add(info);
	}

	@Override
	public LoginInformation getLoginfoByUserIdAndDevice(LoginInformation info) throws DBException {
		return dao.getLoginfoByUserIdAndDevice(info);
	}

	@Override
	public LoginInformation getLoginfoByUserId(Integer userId) throws DBException {
		return dao.getLoginfoByUserId(userId);
	}

	@Override
	public int updateDeviceByUserId(LoginInformation info) throws DBException {
		return dao.updateDeviceByUserId(info);
	}

	@Override
	public LoginInformation getLoginfoByDevice(String deviceUniqueKey) throws DBException {
		return dao.getLoginfoByDevice(deviceUniqueKey);
	}

}
