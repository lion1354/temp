package com.tibco.ma.service;

import com.tibco.ma.model.ScaleStaticsValue;

public interface ScaleService extends BaseService<ScaleStaticsValue> {
	
	 void updateStatus(String id);
	 
	 ScaleStaticsValue saveValue(ScaleStaticsValue value);

}
