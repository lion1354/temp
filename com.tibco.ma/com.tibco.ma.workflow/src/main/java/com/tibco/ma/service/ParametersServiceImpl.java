package com.tibco.ma.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.ParametersDao;
import com.tibco.ma.model.Parameters;

@Service("parametersService")
public class ParametersServiceImpl extends BaseServiceImpl<Parameters>
		implements ParametersService {

	@Resource
	private ParametersDao dao;

	@Override
	public BaseDao<Parameters> getDao() {
		return dao;
	}

}
