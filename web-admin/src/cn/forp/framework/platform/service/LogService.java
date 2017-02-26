/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.FORP;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.core.vo.PageSort;
import cn.forp.framework.platform.vo.Log;
import cn.forp.framework.platform.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *操作日志Service
 *
 * @author	Bruce
 * @version	2016年7月28日 下午3:29:52
 */
@Service
public class LogService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(LogService.class);

	/**
	 * 分页查询日志列表
	 * 
	 * @param user					操作人
	 * @param fromDate			开始日期
	 * @param toDate				结束日期
	 * @param content				日志内容
	 * @param ps      			分页排序信息
	 */
	public Page<Log> search(User user, String fromDate, String toDate, String content, PageSort ps) throws Exception
	{
		String sql = "select * from V_Forp_Log where DomainID=?";
		List<Object> params = new ArrayList<>();
		params.add(user.getDomainId());

		// 开始日期
		if (StringUtils.isNotBlank(fromDate))
		{
			sql += " and CreateDate>=?";
			params.add(DateUtils.parseDate(fromDate + " 00:00:00", FORP.PATTERN_DATE_TIME));
		}

		// 结束日期
		if (StringUtils.isNotBlank(toDate))
		{
			sql += " and CreateDate<=?";
			params.add(DateUtils.parseDate(toDate + " 23:59:59", FORP.PATTERN_DATE_TIME));
		}

		// 日志内容
		if (StringUtils.isNotBlank(content))
		{
			sql += " and (Content Like ? or Parameters Like ?)";
			params.add("%" + content + "%");
			params.add("%" + content + "%");
		}

		lg.debug("SQL：{}", sql);
		return findByPage(sql, params.toArray(new Object[0]), Log.class, ps);
	}
}