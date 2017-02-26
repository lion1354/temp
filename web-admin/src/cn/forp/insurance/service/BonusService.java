package cn.forp.insurance.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.platform.vo.SimpleObject;

@Service
public class BonusService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(BonusService.class);

	/**
	 * 获取所有省市列表
	 */
	public List<SimpleObject> getAllProvince() throws Exception
	{
		return findByList("select AreaID as ID, AreaName as Name from Sys_Area order by AreaID asc",
				SimpleObject.class);
	}

	/**
	 * 获取所有区县列表
	 */
	public List<SimpleObject> getAllRegion() throws Exception
	{
		return findByList(
				"select XianID as ID, XianName as Name, AreaID as Remark, Bonus as Value from Sys_Xian order by XianID",
				SimpleObject.class);
	}

	/**
	 * 更新sys_xian中的bonus
	 * 
	 * @param id
	 * @param bonus
	 */
	public void updateBonus(Long id, String bonus) throws Exception
	{
		String sql = "update sys_xian set bonus=? where xianId=?";
		lg.debug("Update SQL：{}", sql);
		jdbc.update(sql, bonus, id);
	}
}
