/**
 * Copyright © 2017, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.information.service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.BusinessException;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.core.vo.PageSort;
import cn.forp.framework.platform.vo.User;
import cn.forp.information.vo.Consultation;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 监理站管理Service
 *
 * @author	Bruce
 * @version	2017-02-18 12:09:06
 */
@Service
public class StationService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(StationService.class);

	/**
	 * 分页查询
	 * 
	 * @param name    名称
	 * @param state		状态
	 * @param ps		  分页排序信息
	 */
	public Page<Consultation> search(String name, int state, PageSort ps) throws Exception
	{
		String sql = "select * from Sys_Consultation where FormType=?";
		JSONArray params = new JSONArray();
		params.add("0");

		if (StringUtils.isNotBlank(name))
		{
			sql += " and Name like ?";
			params.add("%" + name + "%");
		}

		if (-1 != state)
		{
			sql += " and Status=?";
			params.add(state);
		}
		lg.debug("SQL：{}", sql);

		return findByPage(sql, params.toArray(), Consultation.class, ps);
	}

	/**
	 * 添加
	 *
	 * @param sessionUser   操作人
	 * @param station       监理站
	 */
	public void create(User sessionUser, Consultation station) throws Exception
	{
		// 重名检查
		if (isFieldDuplicate("Sys_Consultation", "Name", station.getName(), "FormType='" + station.getFormType() + "'"))
			throw new BusinessException("监理站“" + station.getName() + "”已登记，请检查您的输入！");

		station.setCreateUserName(sessionUser.getUserName());
		station.setCreateDate(new Timestamp(System.currentTimeMillis()));
		insertIntoTable(station, null, new String[]{"lastModifyDate", "lastModifyUserName", "lastModifyUserId"});
	}

	/**
	 * 修改
	 *
	 * @param sessionUser   操作人
	 * @param station       监理站
	 */
	public void update(User sessionUser, Consultation station) throws Exception
	{
		// 重名检查
		if (isFieldDuplicate("Sys_Consultation", "Name", station.getName(), "ID<>" + station.getId() + " and FormType='" + station.getFormType() + "'"))
			throw new BusinessException("监理站“" + station.getName() + "”已登记，请检查您的输入！");

		station.setLastModifyUserId(sessionUser.getId());
		station.setLastModifyUserName(sessionUser.getUserName());
		station.setLastModifyDate(new Timestamp(System.currentTimeMillis()));
		updateTable(station, null, new String[]{"createDate", "createUserName"});
	}
}