/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.FORP;
import cn.forp.framework.core.util.Redis;
import cn.forp.framework.platform.vo.Parameter;
import cn.forp.framework.platform.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统全局参数管理Service
 *
 * @author	Bruce
 * @version	2016-08-18 11:08:36
 */
@Service
public class ParameterService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(ParameterService.class);

	/**
	 * 查询参数列表
	 * 
	 * @param user	操作人
	 */
	public List<Parameter> search(User user) throws Exception
	{
		String sql = "select * from Forp_Parameter where FK_DomainID=? and Status=? order by ID asc";
		return findByList(sql, Parameter.class, user.getDomainId(), 1);
	}

	/**
	 * 保存并应用系统参数
	 *
	 * @param user	    操作人
	 * @param id		    数据库主键
	 * @param sn		    参数编号
	 * @param newValue	参数值
	 */
	public void saveAndApply(User user, Long id, String sn, String newValue) throws Exception
	{
		lg.info("修改系统参数：{}->{}", sn, newValue);
		// 持久化
		jdbc.update("update Forp_Parameter set Value=? where ID=?", newValue, id);

		// 更新缓存
		Redis.cacheHashMap(FORP.CACHE_DOMAIN_PROFILE + user.getDomainId(), sn, newValue, null);
	}
}