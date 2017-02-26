package com.tibco.ma.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import com.tibco.ma.dao.AnalyticsDao;
import com.tibco.ma.dao.BaseDao;

@Service
public class AnalyticsServiceImpl extends BaseServiceImpl<Object> implements AnalyticsService  
{
	private static Logger log = LoggerFactory
			.getLogger(AnalyticsServiceImpl.class);


	@Resource
	private AnalyticsDao analyticsDao;

	
	@Override
	public List doAggretationFunc(Aggregation agg, String collectionName,
			Class entityClass) {
		return analyticsDao.doAggregationWork(agg, collectionName, entityClass);
	}

	@Override
	public BaseDao<Object> getDao() {
		// TODO Auto-generated method stub
		return analyticsDao;
	}



}
