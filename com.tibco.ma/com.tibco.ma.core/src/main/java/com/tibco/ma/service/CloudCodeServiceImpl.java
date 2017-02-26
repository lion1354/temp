package com.tibco.ma.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.CloudCodeDao;
import com.tibco.ma.model.CloudCode;

/**
 * 
 * @author aidan
 *
 * 2015/6/23
 *
 */
@Service
public class CloudCodeServiceImpl extends BaseServiceImpl<CloudCode> implements
		CloudCodeService {

	@Resource
	private CloudCodeDao dao;

	@Override
	public BaseDao<CloudCode> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

}
