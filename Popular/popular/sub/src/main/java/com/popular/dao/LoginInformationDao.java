package com.popular.dao;

import org.springframework.stereotype.Repository;

import com.popular.exception.DBException;
import com.popular.model.LoginInformation;

/**
 * 
 * @author Aidan
 *
 *         2016/7/26
 */
@Repository
public interface LoginInformationDao {

	int add(LoginInformation info) throws DBException;

	LoginInformation getLoginfoByUserIdAndDevice(LoginInformation info) throws DBException;

	LoginInformation getLoginfoByUserId(Integer userId) throws DBException;

	int updateDeviceByUserId(LoginInformation info) throws DBException;

	LoginInformation getLoginfoByDevice(String deviceUniqueKey) throws DBException;
}
