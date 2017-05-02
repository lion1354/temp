/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.insurance.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import cn.forp.framework.core.FORP;
import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.controller.IForm;
import cn.forp.framework.core.util.Redis;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.platform.service.UserService;
import cn.forp.framework.platform.vo.SimpleObject;
import cn.forp.framework.platform.vo.User;
import cn.forp.insurance.service.TractorService;
import cn.forp.insurance.vo.Constants;
import cn.forp.insurance.vo.Tractor;
import redis.clients.jedis.Jedis;

/**
 * 拖拉机保单管理Controller
 *
 * @author Apple
 * @version 2016-8-12 16:55
 */
@RestController
public class TractorController extends BaseController
{
	/**
	 * Logger
	 */
	private static final Logger lg = LoggerFactory.getLogger(TractorController.class);
	/**
	 * Service
	 */
	@Autowired
	protected UserService userService = null;
	@Autowired
	private TractorService tractorService = null;

	/**
	 * 新建/修改
	 *
	 * @param id
	 *            拖拉机保单ID
	 * @param user
	 *            用户信息
	 * @param deptId
	 *            部门信息
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/insurance/tractor", method = RequestMethod.POST)
	public ModelAndView save(Tractor tractor, HttpServletRequest request) throws Exception
	{
		// printRequestParameters(request);
		String insuranceType = request.getParameter("insuranceType");
		tractor.setType(Integer.valueOf(insuranceType));
		lg.debug("保单类型：{}", insuranceType);
		tractor.setBuyDate(getDate(request.getParameter("buyDateStr")));
		tractor.setRegDate(getDate(request.getParameter("regDateStr")));
		tractor.setInsuranceStart(getDate(request.getParameter("insuranceStartStr")));
		tractor.setInsuranceEnd(getDate(request.getParameter("insuranceEndStr")));
		if (tractor.getHuBaoZuHeZongBaoFei() == null)
		{
			tractor.setHuBaoZuHeZongBaoFei(Double.valueOf(0));
		}
		if (tractor.getJiShenSunShiBaoFei() == null)
		{
			tractor.setJiShenSunShiBaoFei(Double.valueOf(0));
		}
		else
		{
			String jishensunshi = request.getParameter("jishensunshi");
			if (StringUtils.isNotBlank(jishensunshi))
			{
				tractor.setJiShenSunShiBaoFei(Double.valueOf(request.getParameter("jishensunshi")));
			}
		}
		if (tractor.getJiaShiRenYiWaiShangHaiBaoFei() == null)
		{
			tractor.setJiaShiRenYiWaiShangHaiBaoFei(Double.valueOf(0));
		}
		if (tractor.getDiSanZheZeRenBaoFei() == null)
		{
			tractor.setDiSanZheZeRenBaoFei(Double.valueOf(0));
		}
		if (tractor.getNongJiHuoTuoCheBaoFei() == null)
		{
			tractor.setNongJiHuoTuoCheBaoFei(Double.valueOf(0));
		}
		if (tractor.getBoLiDanDuPoSuiBaoFei() == null)
		{
			tractor.setBoLiDanDuPoSuiBaoFei(Double.valueOf(0));
		}
		if (tractor.getJiJuTuoLuoSunShiBaoFei() == null)
		{
			tractor.setJiJuTuoLuoSunShiBaoFei(Double.valueOf(0));
		}
		if (tractor.getYunZhuanYiWaiShangHaiBaoFei() == null)
		{
			tractor.setYunZhuanYiWaiShangHaiBaoFei(Double.valueOf(0));
		}
		if (tractor.getWeiXiuBaoYangZuoYeBaoFei() == null)
		{
			tractor.setWeiXiuBaoYangZuoYeBaoFei(Double.valueOf(0));
		}
		if (tractor.getBuJiMianBuLvBaoFei() == null)
		{
			tractor.setBuJiMianBuLvBaoFei(Double.valueOf(0));
		}
		if (tractor.getDuZhuZuoYeRenYuanShangHaiBaoFei() == null)
		{
			tractor.setDuZhuZuoYeRenYuanShangHaiBaoFei(Double.valueOf(0));
		}
		if (tractor.getZiRanSunShiBaoFei() == null)
		{
			tractor.setZiRanSunShiBaoFei(Double.valueOf(0));
		}
		if (tractor.getZhuangYunSunShiBaoFei() == null)
		{
			tractor.setZhuangYunSunShiBaoFei(Double.valueOf(0));
		}
		if (tractor.getFeiShiGuBuJianSunShiBaoFei() == null)
		{
			tractor.setFeiShiGuBuJianSunShiBaoFei(Double.valueOf(0));
		}
		if (tractor.getDriverMaintenHuiFeiMoney() == null)
		{
			tractor.setDriverMaintenHuiFeiMoney(Double.valueOf(0));
		}
		if (tractor.getDriverOperateHuiFeiMoney() == null)
		{
			tractor.setDriverOperateHuiFeiMoney(Double.valueOf(0));
		}
		if (tractor.getTotalDikouMoney() == null)
		{
			tractor.setTotalDikouMoney(Double.valueOf(0));
		}
		addOrUpdateRecord(tractor, request);
		request.setAttribute("editMode", false);
		String view = "insurance" + Constants.SEPARATOR + "policy" + Constants.SEPARATOR + "add_tractor";
		if ("1".equals(insuranceType))
		{
			view = "insurance" + Constants.SEPARATOR + "policy" + Constants.SEPARATOR + "add_sgj";
		}
		else if ("2".equals(insuranceType))
		{
			view = "insurance" + Constants.SEPARATOR + "policy" + Constants.SEPARATOR + "add_nongji";
		}
		request.setAttribute("view", view);
		return new ModelAndView("/insurance/result.jsp");

	}

	@SuppressWarnings("deprecation")
	private void addOrUpdateRecord(Tractor tractor, HttpServletRequest request) throws Exception
	{
		User user = getSessionUser(request);
		boolean editMode = request.getParameter("editMode") == null ? false
				: Boolean.parseBoolean(request.getParameter("editMode"));
		if (tractor.getQiandanDate() != null)
		{
			tractor.setQiandanYear(dateToString(tractor.getQiandanDate()).substring(0, 4));
		}
		else
		{
			tractor.setQiandanDate(new Date());
			tractor.setQiandanYear(String.valueOf(new Date().getYear() + 1900));
		}

		if (tractor.getQiandanPerson() == null)
		{
			tractor.setQiandanPerson(user.getUserName());
		}
		tractor.setQiandanPersonType(0);
		if (tractor.getType() != 2)
			tractor.setDriverHuiFeiMoney(tractor.getDriverMaintenHuiFeiMoney() + tractor.getDriverOperateHuiFeiMoney());

		tractor.setSubmitDate(new Date());
		String xianId = user.getItem2();
		tractor.setXianId(StringUtils.isBlank(xianId) ? 0 : Integer.parseInt(xianId));
		if (!editMode)
		{
			// 同步保单号生成过程，避免并发情况下的单号重复问题
			Jedis lock = null;
			try
			{
				lock = Redis.lock("order-sn", 30);
				tractor.setInsuranceTractorId(String.valueOf(System.currentTimeMillis()));
			}
			finally
			{
				Redis.unlock(lock, "order-sn");
			}

			try
			{
				tractor.setFormStatus(0);
				tractor.setChargingStatus("N");
				create(tractor, request);
			}
			catch (Exception e)
			{
				lg.error("保单新建失败，请检查保单编号是否重复，或者页面中是否有非法字符：", e);
				throw new Exception("保单新建失败，请检查保单编号是否重复，或者页面中是否有非法字符");
			}
			finally
			{
				request.setAttribute("flag", 1);
			}
		}
		else
		{
			try
			{
				update(tractor, request);
			}
			catch (Exception e)
			{
				lg.error("保单更新失败，请检查保单编号是否存在，或者页面中是否有非法字符：", e);
				throw new Exception("保单更新失败，请检查保单编号是否存在，或者页面中是否有非法字符");
			}
			finally
			{
				request.setAttribute("flag", 0);
			}
		}
	}

	/**
	 * 新建
	 *
	 * @param tractor
	 *            拖拉机保单信息
	 * @param req
	 *            Request请求参数
	 */
	private void create(Tractor tractor, HttpServletRequest request) throws Exception
	{
		User user = getSessionUser(request);

		tractorService.create(tractor);
		// 操作日志
		userService.writeSystemLog(user, request, "添加保单信息",
				"操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), null);
	}

	/**
	 * 修改
	 *
	 * @param user
	 *            拖拉机保单信息
	 * @param req
	 *            Request请求参数
	 */
	private void update(Tractor tractor, HttpServletRequest request) throws Exception
	{
		User user = getSessionUser(request);
		String[] includeFields = { "owner", "idCard", "address", "telNum", "carNum", "factoryNum", "engineNum",
				"jijiaNum", "broughtPrice", "buyDate", "regDate", "secondDirverName", "secondDriverLience",
				"huBaoZuHeZongBaoFei", "jiShenSunShiBaoFei", "jiaShiRenYiWaiShangHaiBaoFei", "diSanZheZeRenBaoFei",
				"nongJiHuoTuoCheBaoFei", "boLiDanDuPoSuiBaoFei", "jiJuTuoLuoSunShiBaoFei",
				"yunZhuanYiWaiShangHaiBaoFei", "weiXiuBaoYangZuoYeBaoFei", "buJiMianBuLvBaoFei",
				"duZhuZuoYeRenYuanShangHaiBaoFei", "ziRanSunShiBaoFei", "zhuangYunSunShiBaoFei",
				"feiShiGuBuJianSunShiBaoFei", "totalDikouMoney", "insuranceStart", "insuranceEnd", "caizhengbutie",
				"qiandanDate", "qiandanDate", "submitDate" };
		tractorService.update(tractor, includeFields);
		// 操作日志
		userService.writeSystemLog(user, request, "修改保单信息",
				"操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), null);
	}

	/**
	 * 获取历史保单记录
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(path = "/insurance/get-tractor", method = RequestMethod.POST)
	public void getHisInfo(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out;
		JSONObject json = new JSONObject();

		String id = request.getParameter("id");
		Tractor tractor = tractorService.searchByInsuranceTractorId(id);
		if (tractor == null)
		{
			json.put("state", "N");
		}
		else
		{
			json.put("state", "Y");
			json.put("type", tractor.getType());// 保单类型 0 拖拉机 1 收割机 2农机
			json.put("owner", tractor.getOwner());// 车主
			json.put("idNum", tractor.getIdCard());// 身份证号码
			json.put("address", tractor.getAddress());// 地址
			json.put("phone", tractor.getTelNum());// 电话号码
			json.put("carNum", tractor.getCarNum());// 号牌号码
			json.put("factoryNum", tractor.getFactoryNum());// 厂牌号码
			json.put("engineNum", tractor.getEngineNum());// 发动机号
			json.put("jijiaNum", tractor.getJijiaNum());// 机架号
			json.put("broughtPrice", tractor.getBroughtPrice());// 新机购置价
			if (tractor.getBuyDate() != null && tractor.getBuyDate().toString().length() >= 10)
			{
				json.put("buyDate", tractor.getBuyDate().toString().substring(0, 10));// 购置日期
			}
			else
			{
				json.put("buyDate", "");// 购置日期
			}
			if (tractor.getRegDate() != null && tractor.getRegDate().toString().length() >= 10)
			{
				json.put("regDate", tractor.getRegDate().toString().substring(0, 10));// 初次登记日期
			}
			else
			{
				json.put("regDate", "");// 初次登记日期
			}

			if (tractor.getInsuranceStart() != null && tractor.getInsuranceStart().toString().length() >= 10)
			{
				json.put("insuranceStart", tractor.getInsuranceStart().toString().substring(0, 10));// 互保期限开始
			}
			else
			{
				json.put("insuranceStart", "");// 互保期限开始
			}
			if (tractor.getInsuranceEnd() != null && tractor.getInsuranceEnd().toString().length() >= 10)
			{
				json.put("insuranceEnd", tractor.getInsuranceEnd().toString().substring(0, 10));// 互保期限结束
			}
			else
			{
				json.put("insuranceEnd", "");// 互保期限结束
			}

			json.put("formDriverName", tractor.getSecondDirverName());// 驾驶操作人姓名
			json.put("formDriverLience", tractor.getSecondDriverLience());// 身份证号码
			json.put("huBaoZuHeZongBaoFei", String.valueOf(tractor.getHuBaoZuHeZongBaoFei()).split("\\.")[0]);// 组合总保费
			json.put("jiShenSunShiBaoFei", String.valueOf(tractor.getJiShenSunShiBaoFei()).split("\\.")[0]);// 机身损失
			json.put("jiaShiRenYiWaiShangHaiBaoFei",
					String.valueOf(tractor.getJiaShiRenYiWaiShangHaiBaoFei()).split("\\.")[0]);// 驾驶人意外伤害
			json.put("diSanZheZeRenBaoFei", String.valueOf(tractor.getDiSanZheZeRenBaoFei()).split("\\.")[0]);// 第三者责任
			json.put("nongJiHuoTuoCheBaoFei", String.valueOf(tractor.getNongJiHuoTuoCheBaoFei()).split("\\.")[0]);// 农机或拖车
			json.put("boLiDanDuPoSuiBaoFei", String.valueOf(tractor.getBoLiDanDuPoSuiBaoFei()).split("\\.")[0]);// 玻璃单独破碎
			json.put("jiJuTuoLuoSunShiBaoFei", String.valueOf(tractor.getJiJuTuoLuoSunShiBaoFei()).split("\\.")[0]);// 机具脱落损失
			json.put("yunZhuanYiWaiShangHaiBaoFei",
					String.valueOf(tractor.getYunZhuanYiWaiShangHaiBaoFei()).split("\\.")[0]);// 运转意外伤害
			json.put("weiXiuBaoYangZuoYeBaoFei", String.valueOf(tractor.getWeiXiuBaoYangZuoYeBaoFei()).split("\\.")[0]);// 维修保养作业
			json.put("buJiMianBuLvBaoFei", String.valueOf(tractor.getBuJiMianBuLvBaoFei()).split("\\.")[0]);// 不计免补率
			json.put("duZhuZuoYeRenYuanShangHaiBaoFei",
					String.valueOf(tractor.getDuZhuZuoYeRenYuanShangHaiBaoFei()).split("\\.")[0]);// 辅助作业人员意外伤害
			json.put("ziRanSunShiBaoFei", String.valueOf(tractor.getZiRanSunShiBaoFei()).split("\\.")[0]);// 自燃损失保费
			json.put("zhuangYunSunShiBaoFei", String.valueOf(tractor.getZhuangYunSunShiBaoFei()).split("\\.")[0]);// 装运损失
			json.put("feiShiGuBuJianSunShiBaoFei",
					String.valueOf(tractor.getFeiShiGuBuJianSunShiBaoFei()).split("\\.")[0]);// 非事故部件损失
			json.put("totalDikouMoney", String.valueOf(tractor.getTotalDikouMoney()).split("\\.")[0]);// 积分抵扣
			json.put("caizhengbutie", tractor.getCaizhengbutie());
		}
		try
		{
			out = response.getWriter();
			out.print(json);
			out.flush();
			out.close();
		}
		catch (IOException e)
		{
			lg.error("获得保单信息错误getHisInfo ：", e);
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param type
	 *            状态
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/insurance/insurance-query", method = RequestMethod.POST)
	public Page<Tractor> search(HttpServletRequest req) throws Exception
	{
		IForm form = getFormWrapper(req);
		return tractorService.search(getSessionUser(req), form, getPageSort(req));
	}

	@RequestMapping("/insurance/show/{id}/{type}/{edit}")
	public ModelAndView showInsurance(@PathVariable String id, @PathVariable String type, @PathVariable String edit,
			HttpServletRequest req) throws Exception
	{
		req.setAttribute("editMode", true);
		req.setAttribute("insuranceTractorId", id);

		// Tractor tractor = tractorService.searchByInsuranceTractorId(id);
		// int type = tractor.getType();

		if (StringUtils.isNotBlank(edit) && Boolean.parseBoolean(edit) == true)
		{
			req.setAttribute("cannotModify", false);
		}
		else
		{
			req.setAttribute("cannotModify", true);
		}
		String view = "/insurance/policy/add_tractor.jsp";
		if ("1".equals(type))
		{
			view = "/insurance/policy/add_sgj.jsp";
		}
		else if ("2".equals(type))
		{
			view = "/insurance/policy/add_nongji.jsp";
		}
		return new ModelAndView(view);
	}

	@RequestMapping(path = "/insurance/approve/{id}")
	public ModelAndView approve(@PathVariable String id, HttpServletRequest request) throws Exception
	{
		User user = getSessionUser(request);
		updateTractorFormStatus(id, 1);
		// 操作日志
		userService.writeSystemLog(user, request, "审查通过保单",
				"操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), null);
		return new ModelAndView("/insurance/query/insurance_query.jsp");
	}

	@RequestMapping(path = "/insurance/disapprove/{id}")
	public ModelAndView disapprove(@PathVariable String id, HttpServletRequest request) throws Exception
	{
		User user = getSessionUser(request);
		updateTractorFormStatus(id, 2);
		// 操作日志
		userService.writeSystemLog(user, request, "表单有错误",
				"操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), null);
		return new ModelAndView("/insurance/query/insurance_query.jsp");
	}

	@RequestMapping(path = "/insurance/drop/{id}")
	public ModelAndView drop(@PathVariable String id, HttpServletRequest request) throws Exception
	{
		User user = getSessionUser(request);
		updateTractorFormStatus(id, 3);
		// 操作日志
		userService.writeSystemLog(user, request, "作废",
				"操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), null);
		return new ModelAndView("/insurance/query/insurance_query.jsp");
	}

	private void updateTractorFormStatus(String id, Integer formStatus) throws Exception
	{
		Tractor tractor = new Tractor();
		tractor.setInsuranceTractorId(id);
		tractor.setFormStatus(formStatus);

		String[] includeFields = { "formStatus" };
		tractorService.update(tractor, includeFields);
	}

	@RequestMapping(path = "/insurance/back/{page}")
	public ModelAndView back(@PathVariable String page, HttpServletRequest request) throws Exception
	{
		if (StringUtils.isNotBlank(page))
		{
			page = page.replace(Constants.SEPARATOR, "/");
		}
		return new ModelAndView("/" + page + ".jsp");
	}

	/**
	 * 获取地理信息
	 *
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/insurance/get-region")
	public JSONObject getRegion(HttpServletRequest req) throws Exception
	{
		JSONObject json = new JSONObject();
		User user = getSessionUser(req);
		int provinceById = StringUtils.isBlank(user.getItem1()) ? 0 : Integer.parseInt(user.getItem1());
		int regionsId = StringUtils.isBlank(user.getItem2()) ? 0 : Integer.parseInt(user.getItem2());
		if (regionsId != 0)
		{
			json.put("provinces", userService.getProvinceById(provinceById));
			json.put("regions", userService.getRegionById(regionsId));
			return json;
		}
		// 省市
		List<SimpleObject> provinces = userService.getAllProvince();
		// 区县
		List<SimpleObject> regions = userService.getAllRegion();
		List<SimpleObject> regionsList = new ArrayList<SimpleObject>();
		int i = 0;
		for (SimpleObject so : regions)
		{
			if (i != Integer.parseInt(so.getRemark()))
			{
				i++;
				SimpleObject newSo = new SimpleObject();
				newSo.setId("0");
				newSo.setName("所有区县");
				newSo.setRemark(so.getRemark());
				regionsList.add(newSo);
			}
			regionsList.add(so);
		}
		regionsList.addAll(regions);
		json.put("provinces", provinces);
		json.put("regions", regionsList);

		return json;
	}

	/**
	 * 获取保单年份
	 *
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/insurance/get-year")
	public JSONObject getYear(HttpServletRequest req) throws Exception
	{
		JSONObject json = new JSONObject();
		List<SimpleObject> years = new ArrayList<SimpleObject>();
		SimpleObject all = new SimpleObject();
		all.setId("-1");
		all.setName("所有年份");
		years.add(all);
		List<SimpleObject> others = tractorService.getYear();
		if (others != null && others.size() > 0)
		{
			years.addAll(others);
		}
		json.put("years", years);

		return json;
	}

	private Date getDate(String date) throws ParseException
	{
		if (StringUtils.isBlank(date))
		{
			return null;
		}
		return DateUtils.parseDate(date, FORP.PATTERN_DATE);
	}

	private String dateToString(Date date)
	{
		if (date == null)
		{
			return "";
		}
		return DateFormatUtils.format(date, FORP.PATTERN_DATE);
	}

	@RequestMapping(path = "/insurance/report-insurance-query", method = RequestMethod.POST)
	public List<Tractor> reportInsuranceSearch(HttpServletRequest req) throws Exception
	{
		String insuranceId = req.getParameter("insuranceId");
		String insuranceTel = req.getParameter("insuranceTel");
		return tractorService.reportInsuranceSearch(insuranceId, insuranceTel);
	}
}