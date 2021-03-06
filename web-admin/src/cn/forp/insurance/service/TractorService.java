/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.insurance.service;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.controller.IForm;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.core.vo.PageSort;
import cn.forp.framework.platform.vo.SimpleObject;
import cn.forp.framework.platform.vo.User;
import cn.forp.insurance.vo.Constants;
import cn.forp.insurance.vo.Tractor;

/**
 * 拖拉机保单管理Service
 */
@Service
public class TractorService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(TractorService.class);

	/**
	 * 添加
	 *
	 * 
	 * @param tractor
	 *            保单信息
	 */
	public long create(Tractor tractor) throws Exception
	{
		long tractorId = insertIntoTable(tractor, null, null);
		lg.info("新建拖拉机保单信息：{}[{}]", tractor.getOwner(), tractorId);
		return tractorId;
	}

	public Tractor searchByInsuranceTractorId(String insuranceTractorId) throws Exception
	{
		String sql = "select * from Sys_Tractor where InsuranceTractorId=?";
		List<Tractor> list = findByList(sql, Tractor.class, insuranceTractorId);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}

	/*
	 * 分页查询列表
	 * 
	 * @param user  操作人
	 * @param form  请求参数
	 * @param ps    分页排序信息
	 */
	public Page<Tractor> search(User user, IForm form, PageSort ps) throws Exception
	{
		String sql = "select * from Sys_Tractor form where 1=1";
		List<Object> params = new ArrayList<>();
		String querryFormid = form.get("querryFormid");
		String querryFormOwner = form.get("querryFormOwner");
		String querryFormAddress = form.get("querryFormAddress");
		String querryFormOwnerTel = form.get("querryFormOwnerTel");
		String querryFormCarNum = form.get("querryFormCarNum");
		String querryFormFactoryNum = form.get("querryFormFactoryNum");
		String querryFormEigineNum = form.get("querryFormEigineNum");
		String querryFormJiJiaNum = form.get("querryFormJiJiaNum");
		String carType = form.get("carType");
		String projectTypeList = form.get("projectTypeList");
		String formType = form.get("formType");
		String chargingStatus = form.get("chargingStatus");
		String formYear = form.get("formYear");
		String diquSelected = form.get("diquSelected");
		String xianSelected = form.get("xianSelected");
		String chargingType = form.get("chargingType");
		// String orderType = form.get("orderType");

		lg.debug("searchType carType ：{}", carType);
		if (StringUtils.isNotBlank(querryFormid))
		{
			sql += " and form.insuranceTractorId like ?";
			params.add("%" + querryFormid + "%");
		}
		if (StringUtils.isNotBlank(querryFormOwner))
		{
			sql += " and form.owner like ?";
			params.add("%" + querryFormOwner + "%");
		}
		if (StringUtils.isNotBlank(querryFormAddress))
		{
			sql += " and form.address like ?";
			params.add("%" + querryFormAddress + "%");
		}
		if (StringUtils.isNotBlank(querryFormOwnerTel))
		{
			sql += " and form.telNum = ?";
			params.add(querryFormOwnerTel);
		}
		if (StringUtils.isNotBlank(querryFormCarNum))
		{
			sql += " and form.carNum like ?";
			params.add("%" + querryFormCarNum + "%");
		}
		if (StringUtils.isNotBlank(querryFormFactoryNum))
		{
			sql += " and form.factoryNum like ?";
			params.add("%" + querryFormFactoryNum + "%");

		}
		if (StringUtils.isNotBlank(querryFormEigineNum))
		{
			sql += " and form.engineNum like ?";
			params.add("%" + querryFormEigineNum + "%");
		}
		if (StringUtils.isNotBlank(querryFormJiJiaNum))
		{
			sql += " and form.jijiaNum like ?";
			params.add("%" + querryFormJiJiaNum + "%");
		}
		if (StringUtils.isNotBlank(carType))
		{
			sql += " and form.type = ?";
			params.add(carType);
		}
		else
		{
			sql += " and form.type = 0";
		}
		if (StringUtils.isNotBlank(projectTypeList))
		{
			List<String> projectTypeArrays = Arrays.asList(projectTypeList.split(Constants.SEPARATOR));
			if (!projectTypeArrays.contains("5"))
			{

				List<Integer> notInQuerry = new ArrayList<Integer>();
				notInQuerry.add(0);
				notInQuerry.add(1);
				notInQuerry.add(2);
				notInQuerry.add(3);
				notInQuerry.add(4);
				notInQuerry.add(6);
				for (String i : projectTypeArrays)
				{
					if ("0".equals(i))
					{
						sql += " and form.jishenHuiFeiMoney > 0";
					}
					else if ("1".equals(i))
					{
						sql += " and form.driverHuiFeiMoney > 0";
					}
					else if ("2".equals(i))
					{
						sql += " and form.thridHuiFeiMoney > 0";
					}
					else if ("3".equals(i))
					{
						sql += " and form.thridPeopleCombineHuiFeiMoney > 0";
					}
					else if ("4".equals(i))
					{
						sql += " and form.combineHuiFeiMoney > 0";
					}
					else if ("6".equals(i))
					{
						sql += " and form._350CombineHuifei > 0";
					}
					notInQuerry.remove(Integer.valueOf(i));
				}
				for (Integer i : notInQuerry)
				{
					if (i == 0)
					{
						sql += " and form.jishenHuiFeiMoney = 0";
					}
					else if (i == 1)
					{
						sql += " and form.driverHuiFeiMoney = 0";
					}
					else if (i == 2)
					{
						sql += " and form.thridHuiFeiMoney = 0";
					}
					else if (i == 3)
					{
						sql += " and form.thridPeopleCombineHuiFeiMoney = 0";
					}
					else if (i == 4)
					{
						sql += " and form.combineHuiFeiMoney = 0";
					}
					else if (i == 6)
					{
						sql += " and form._350CombineHuifei = 0";
					}
				}
			}
		}

		if (StringUtils.isNotBlank(formType) && !formType.equals("-1"))
		{
			sql += " and form.formStatus = ?";
			params.add(formType);
		}
		if (StringUtils.isNotBlank(chargingStatus) && !chargingStatus.equals("A"))
		{
			sql += " and form.charging_status = ?";
			params.add(chargingStatus);
		}
		if (StringUtils.isNotBlank(formYear) && !formYear.equals("-1"))
		{
			sql += " and form.qiandanYear = ?";
			params.add(formYear);
		}
		if (StringUtils.isNotBlank(diquSelected) && !diquSelected.equals("0"))
		{
			if (StringUtils.isNotBlank(xianSelected) && !xianSelected.equals("0"))
			{
				sql += " and form.xianId = ?";
				params.add(xianSelected);
			}
			if (StringUtils.isNotBlank(xianSelected) && xianSelected.equals("0"))
			{
				sql += " and form.xianId in (select xianId from sys_xian where areaId=?)";
				params.add(diquSelected);
			}
		}
		if (StringUtils.isBlank(diquSelected))
		{
			diquSelected = user.getItem1();
			xianSelected = user.getItem2();
			if (!diquSelected.equals("0"))
			{
				if (StringUtils.isNotBlank(xianSelected) && !xianSelected.equals("0"))
				{
					sql += " and form.xianId = ?";
					params.add(xianSelected);
				}
				if (StringUtils.isNotBlank(xianSelected) && xianSelected.equals("0"))
				{
					sql += " and form.areaId = ?";
					params.add(diquSelected);
				}
			}
		}
		if (StringUtils.isNotBlank(chargingType) && !chargingType.equals("-1"))
		{
			sql += " and form.insuranceTractorId in (select cd.insurance_id from Charge_Detail as cd where cd.charging_type = ?)";
			params.add(chargingType);
		}
		/*
		 * if (StringUtils.isNotBlank(orderType)) { if ("0".equals(orderType)) { sql += " order by form.qiandanDate"; }
		 * else if ("1".equals(orderType)) { sql += " order by form.insuranceTractorId"; } else if
		 * ("2".equals(orderType)) { sql += " order by form.address"; } else if ("3".equals(orderType)) { sql +=
		 * " order by form.factoryNum"; } else if ("4".equals(orderType)) { sql += " order by form.qiandanPerson"; } }
		 */
		lg.debug("SQL：{}", sql);
		Page<Tractor> page = findByPage(sql, params.toArray(new Object[0]), Tractor.class, ps);
		List<Tractor> list = page.getRows();
		if (list != null && list.size() > 0)
		{
			// 加载PC端账号名称Cache
			Map<String, String> pcQianDanRenCache = new HashMap<>();
			SqlRowSet rs = jdbc.queryForRowSet("select ID, UserName from Forp_User");
			while (rs.next())
			{
				pcQianDanRenCache.put(rs.getString("ID"), rs.getString("UserName"));
			}

			// 加载移动端账号名称Cache
			Map<String, String> mobileQianDanRenCache = new HashMap<>();
			rs = jdbc.queryForRowSet("select ID, UserName from Sys_Member_Info");
			while (rs.next())
			{
				mobileQianDanRenCache.put(rs.getString("ID"), rs.getString("UserName"));
			}

			for (Tractor tractor : list)
			{
				Integer xianId = tractor.getXianId();
				SimpleObject ob = getRegionById(xianId).get(0);
				tractor.setXianName(ob.getName());

				// Mobile端的签单人名称转义
				if (1 == tractor.getQiandanPersonType())
				{
					// 移动端会员
					if (mobileQianDanRenCache.containsKey(tractor.getQiandanPerson()))
						tractor.setQiandanPerson(mobileQianDanRenCache.get(tractor.getQiandanPerson()));
				}
				else
				{
					// PC端管理员
					if (pcQianDanRenCache.containsKey(tractor.getQiandanPerson()))
						tractor.setQiandanPerson(pcQianDanRenCache.get(tractor.getQiandanPerson()));
				}
			}
		}

		return page;
	}

	private List<SimpleObject> getRegionById(Integer id) throws Exception
	{
		List<Object> params = new ArrayList<>();
		params.add(id);
		return findByList(
				"select XianID as ID, XianName as Name, AreaID as Remark from Sys_Xian where XianID = ? order by AreaID, XianID",
				params.toArray(new Object[0]), SimpleObject.class);
	}

	/**
	 * 修改
	 *
	 * @param tractor
	 *            保单信息
	 * @param includeFields
	 *            更新的属性列表
	 */
	public void update(Tractor tractor, String[] includeFields) throws Exception
	{
		lg.debug("修改保单：" + tractor.getInsuranceTractorId());
		updateTable(tractor, includeFields, null, "insuranceTractorId", tractor.getInsuranceTractorId());
	}

	public List<SimpleObject> getYear() throws Exception
	{
		String sql = "select distinct qiandanYear as Id, qiandanYear as Name from Sys_Tractor order by qiandanYear asc";
		return findByList(sql, SimpleObject.class);
	}

	public List<Tractor> reportInsuranceSearch(String insuranceTractorId, String telNum) throws Exception
	{
		String sql = "select * from Sys_Tractor form where 1=1 and insuranceEnd >= ?";
		List<Object> params = new ArrayList<>();
		params.add(new Date());
		if (StringUtils.isNotBlank(insuranceTractorId))
		{
			sql += " and form.insuranceTractorId like ?";
			params.add("%" + insuranceTractorId + "%");
		}
		if (StringUtils.isNotBlank(telNum))
		{
			sql += " and form.telNum = ?";
			params.add(telNum);
		}
		lg.debug("SQL：{}", sql);
		List<Tractor> list = findByList(sql, params.toArray(new Object[0]), Tractor.class);
		return list;
	}

}