/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.insurance.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	/**
	 * 分页查询列表
	 * 
	 * @param user
	 *            操作人
	 * @param state
	 *            保单状态
	 * @param ps
	 *            分页排序信息
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
		String orderType = form.get("orderType");

		lg.debug("searchType carType ：{}", carType);
		if (StringUtils.isNoneBlank(querryFormid))
		{
			sql += " and form.insuranceTractorId like ?";
			params.add("%" + querryFormid + "%");
		}
		if (StringUtils.isNoneBlank(querryFormOwner))
		{
			sql += " and form.owner like ?";
			params.add("%" + querryFormOwner + "%");
		}
		if (StringUtils.isNoneBlank(querryFormAddress))
		{
			sql += " and form.address like ?";
			params.add("%" + querryFormAddress + "%");
		}
		if (StringUtils.isNoneBlank(querryFormOwnerTel))
		{
			sql += " and form.telNum = ?";
			params.add(querryFormOwnerTel);
		}
		if (StringUtils.isNoneBlank(querryFormCarNum))
		{
			sql += " and form.carNum like ?";
			params.add("%" + querryFormCarNum + "%");
		}
		if (StringUtils.isNoneBlank(querryFormFactoryNum))
		{
			sql += " and form.factoryNum like ?";
			params.add("%" + querryFormFactoryNum + "%");

		}
		if (StringUtils.isNoneBlank(querryFormEigineNum))
		{
			sql += " and form.engineNum like ?";
			params.add("%" + querryFormEigineNum + "%");
		}
		if (StringUtils.isNoneBlank(querryFormJiJiaNum))
		{
			sql += " and form.jijiaNum like ?";
			params.add("%" + querryFormJiJiaNum + "%");
		}
		if (StringUtils.isNoneBlank(carType))
		{
			sql += " and form.type = ?";
			params.add(carType);
		}
		else
		{
			sql += " and form.type = 0";
		}
		if (StringUtils.isNoneBlank(projectTypeList))
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

		if (StringUtils.isNoneBlank(formType) && !formType.equals("-1"))
		{
			sql += " and form.formStatus = ?";
			params.add(formType);
		}
		if (StringUtils.isNoneBlank(chargingStatus) && !chargingStatus.equals("A"))
		{
			sql += " and form.charging_status = ?";
			params.add(chargingStatus);
		}
		if (StringUtils.isNoneBlank(formYear) && !formYear.equals("-1"))
		{
			sql += " and form.qiandanYear = ?";
			params.add(formYear);
		}
		if (StringUtils.isNoneBlank(diquSelected) && !diquSelected.equals("0"))
		{
			if (StringUtils.isNoneBlank(xianSelected) && !xianSelected.equals("0"))
			{
				sql += " and form.xianId = ?";
				params.add(xianSelected);
			}
			if (StringUtils.isNoneBlank(xianSelected) && xianSelected.equals("0"))
			{
				sql += " and form.areaId = ?";
				params.add(diquSelected);
			}
		}
		if (StringUtils.isBlank(diquSelected))
		{
			diquSelected = user.getItem1();
			xianSelected = user.getItem2();
			if (!diquSelected.equals("0"))
			{
				if (StringUtils.isNoneBlank(xianSelected) && !xianSelected.equals("0"))
				{
					sql += " and form.xianId = ?";
					params.add(xianSelected);
				}
				if (StringUtils.isNoneBlank(xianSelected) && xianSelected.equals("0"))
				{
					sql += " and form.areaId = ?";
					params.add(diquSelected);
				}
			}
		}
		if (StringUtils.isNoneBlank(chargingType) && !chargingType.equals("-1"))
		{
			sql += " and form.insuranceTractorId in (select cd.insurance_id from Charge_Detail as cd where cd.charging_type = ?)";
			params.add(chargingType);
		}

		if (StringUtils.isNoneBlank(orderType))
		{
			if ("0".equals(orderType))
			{
				sql += " order by form.qiandanDate";
			}
			else if ("1".equals(orderType))
			{
				sql += " order by form.insuranceTractorId";
			}
			else if ("2".equals(orderType))
			{
				sql += " order by form.address";
			}
			else if ("3".equals(orderType))
			{
				sql += " order by form.factoryNum";
			}
			else if ("4".equals(orderType))
			{
				sql += " order by form.qiandanPerson";
			}
		}
		lg.debug("SQL：{}", sql);
		Page<Tractor> page = findByPage(sql, params.toArray(new Object[0]), Tractor.class, ps);
		List<Tractor> list = page.getRows();
		if (list != null && list.size() > 0)
		{
			for (Tractor tractor : list)
			{
				Integer xianId = tractor.getXianId();
				SimpleObject ob = getRegionById(xianId).get(0);
				tractor.setXianName(ob.getName());
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
		String sql = "select * from Sys_Tractor form where 1=1";
		List<Object> params = new ArrayList<>();
		if (StringUtils.isNoneBlank(insuranceTractorId))
		{
			sql += " and form.insuranceTractorId like ?";
			params.add("%" + insuranceTractorId + "%");
		}
		if (StringUtils.isNoneBlank(telNum))
		{
			sql += " and form.telNum = ?";
			params.add(telNum);
		}
		lg.debug("SQL：{}", sql);
		List<Tractor> list = findByList(sql, params.toArray(new Object[0]), Tractor.class);
		return list;
	}

}