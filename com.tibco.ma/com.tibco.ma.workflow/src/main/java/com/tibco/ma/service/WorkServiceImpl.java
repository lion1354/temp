package com.tibco.ma.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.WorkDao;
import com.tibco.ma.model.Work;

@Service
public class WorkServiceImpl extends BaseServiceImpl<Work> implements
		WorkService {

	@Resource
	private WorkDao dao;

	@Override
	public BaseDao<Work> getDao() {
		return dao;
	}

}
