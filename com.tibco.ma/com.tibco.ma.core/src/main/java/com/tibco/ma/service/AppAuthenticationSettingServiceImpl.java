package com.tibco.ma.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tibco.ma.dao.AppAuthenticationSettingDao;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.model.AppAuthenticationSetting;

/**
 * 
 * @author aidan
 *
 * 2015/7/3
 *
 */
@Service
public class AppAuthenticationSettingServiceImpl extends
		BaseServiceImpl<AppAuthenticationSetting> implements
		AppAuthenticationSettingService {
	
	@Resource
	private AppAuthenticationSettingDao dao;

	@Override
	public BaseDao<AppAuthenticationSetting> getDao() {
		return dao;
	}


}
