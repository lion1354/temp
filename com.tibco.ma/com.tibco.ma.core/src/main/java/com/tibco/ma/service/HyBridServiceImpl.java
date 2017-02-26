package com.tibco.ma.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.CloudCodeDao;
import com.tibco.ma.dao.HyBridDao;
import com.tibco.ma.model.CloudCode;
import com.tibco.ma.model.HyBrid;

/**
 * 
 * @author aidan
 *
 * 2015/6/23
 *
 */
@Service
public class HyBridServiceImpl extends BaseServiceImpl<HyBrid> implements
HyBridService {

	@Resource
	private HyBridDao dao;

	@Override
	public BaseDao<HyBrid> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

}
