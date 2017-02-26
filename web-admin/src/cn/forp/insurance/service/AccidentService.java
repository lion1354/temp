/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.insurance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.forp.framework.core.BaseService;
import cn.forp.insurance.vo.Accident;

/**
 * 事故信息管理Service
 */
@Service
public class AccidentService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(AccidentService.class);

	/**
	 * 添加
	 */
	public long create(Accident accident, String[] includeFields, String[] excludeFields) throws Exception
	{
		long accidentId = insertIntoTable(accident, includeFields, excludeFields);
		lg.info("新建事故信息信息：{}[{}]", accident.getInsuranceId(), accidentId);
		return accidentId;
	}

	/**
	 * 修改
	 */
	public void update(Accident accident, String[] includeFields, String[] excludeFields) throws Exception
	{
		lg.debug("修改事故信息：" + accident.getId());
		updateTable(accident, includeFields, excludeFields);
	}

}