package com.tibco.ma.service;

import java.util.List;

import org.springframework.data.mongodb.core.aggregation.Aggregation;


public interface AnalyticsService extends BaseService<Object>{

	public List<Object> doAggretationFunc(Aggregation agg,String collectionName,Class<Object>entityClass);
	

}
