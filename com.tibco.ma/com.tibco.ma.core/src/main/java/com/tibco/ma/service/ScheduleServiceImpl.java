package com.tibco.ma.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.ScheduleDao;
import com.tibco.ma.model.Schedule;

/**
 * 
 * @author aidan
 *
 * 2015/6/23
 *
 */
@Service
public class ScheduleServiceImpl extends BaseServiceImpl<Schedule> implements
		ScheduleService {
	
	@Resource
	private ScheduleDao dao;

	@Override
	public BaseDao<Schedule> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

}
