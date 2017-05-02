package cn.forp.insurance.controller;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import cn.forp.framework.core.FORP;
import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.controller.IForm;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.platform.service.UserService;
import cn.forp.framework.platform.vo.User;
import cn.forp.insurance.service.AccidentService;
import cn.forp.insurance.service.TractorService;
import cn.forp.insurance.vo.Accident;
import cn.forp.insurance.vo.Constants;
import cn.forp.insurance.vo.Tractor;

@RestController
public class AccidentController extends BaseController
{
	/**
	 * Logger
	 */
	private static final Logger lg = LoggerFactory.getLogger(AccidentController.class);
	/**
	 * Service
	 */
	@Autowired
	protected UserService userService = null;
	@Autowired
	private AccidentService accidentService = null;
	@Autowired
	private TractorService tractorService = null;

	// =================================================================
	// 报案
	// =================================================================

	/**
	 * 新建/修改 报案
	 */
	@RequestMapping(path = "/accident/report", method = RequestMethod.POST)
	public ModelAndView saveReport(Accident accident, HttpServletRequest request) throws Exception
	{
		boolean editMode = request.getParameter("editMode") == null ? false : Boolean.parseBoolean(request.getParameter("editMode"));
		String status = String.valueOf(accident.getStatus().toString());
		lg.debug("事故信息类型：报案 {}", status);
		request.setAttribute("editMode", false);

		createOrUpdateReport(accident, getSessionUser(request), editMode, request);
		String view = "insurance" + Constants.SEPARATOR + "accident" + Constants.SEPARATOR + "add_report";

		request.setAttribute("view", view);
		return new ModelAndView("/insurance/result.jsp");
	}

	/**
	 * Report
	 *
	 * @param accident
	 * @param user
	 * @param editMode
	 * @param request
	 * @throws Exception
	 */
	private void createOrUpdateReport(Accident accident, User user, boolean editMode, HttpServletRequest request) throws Exception
	{
		String dangerTimeStr = request.getParameter("dangerTimeStr");
		if (StringUtils.isNotBlank(dangerTimeStr))
		{
			accident.setDangerTime(DateUtils.parseDate(dangerTimeStr, FORP.PATTERN_DATE_TIME));
		}

		String situation = "";
		if (0 != accident.getSituation1().intValue())
		{
			situation += "," + accident.getSituation1();
		}
		if (0 != accident.getSituation2().intValue())
		{
			situation += "," + accident.getSituation2();
		}
		if (0 != accident.getSituation3().intValue())
		{
			situation += "," + accident.getSituation3();
		}
		if (0 != accident.getSituation4().intValue())
		{
			situation += "," + accident.getSituation4();
		}
		if (0 != accident.getSituation5().intValue())
		{
			situation += "," + accident.getSituation5();
		}
		if (StringUtils.isNotBlank(situation))
		{
			situation = situation.replaceFirst(",", "");
			accident.setSituation(situation);
		}
		accident.setReportTime(new Date());
		accident.setCreateUserName(user.getUserName());
		long diff = accident.getReportTime().getTime() - accident.getDangerTime().getTime();
		int hours = (int) (diff / (1000 * 60 * 60));
		if ((!accident.getMemberName().equals(accident.getCaseName())) || hours > 8)
		{
			accident.setUnusual(1);
		}
		else
		{
			accident.setUnusual(0);
		}
		accident.setLastModifyDate(new Date());
		accident.setLastModifyUserName(user.getUserName());
		if (!editMode)
		{
			try
			{
				Tractor tractor = tractorService.searchByInsuranceTractorId(accident.getInsuranceId());
				Integer regionId = tractor.getXianId();
				String provinceId = userService.getRegionById(regionId).get(0).getRemark();
				accident.setProvinceId(provinceId);
				accident.setRegionId(String.valueOf(regionId));
				accident.setAccidentId("-1");
				createReport(accident, request);
			}
			catch (Exception e)
			{
				lg.error("报案信息新建失败，请检查页面中是否有非法字符：", e);
				throw new Exception("报案信息新建失败，请检查页面中是否有非法字符");
			}
		}
		else
		{
			try
			{
				updateReport(accident, request);
			}
			catch (Exception e)
			{
				lg.error("报案信息更新失败，请检查页面中是否有非法字符：", e);
				throw new Exception("报案信息更新失败，请检查页面中是否有非法字符");
			}
		}
	}

	/**
	 * 新建Report
	 */
	private void createReport(Accident accident, HttpServletRequest request) throws Exception
	{
		User user = getSessionUser(request);
		accidentService.create(accident, null, null);
		// 操作日志
		userService.writeSystemLog(user, request, "添加报案信息", "操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), null);
	}

	/**
	 * 修改Report
	 */
	private void updateReport(Accident accident, HttpServletRequest request) throws Exception
	{
		User user = getSessionUser(request);
		String[] includeFields = { "caseName", "caseTel", "reason", "responsibility", "accident", "situation", "driverName", "unusual" };
		accidentService.update(accident, includeFields, null);
		// 操作日志
		userService.writeSystemLog(user, request, "修改报案信息", "操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), null);
	}

	// =================================================================
	// 查勘
	// =================================================================

	/**
	 * 查勘 - 新建/保存
	 *
	 * @param id
	 *            ID
	 * @param survey
	 *            查勘信息
	 * @param request
	 *            Request请求参数
	 * @param zhengti
	 *            事故现场整体照片(1-4张)
	 * @param jubu
	 *            事故现场局部照片(1-20张)
	 * @param xingshi
	 *            行驶证照片(0-1张)
	 * @param jiashi
	 *            驾驶证照片(0-1张)
	 * @param jipai
	 *            机牌合影照片(0-1张)
	 * @param jijia
	 *            农机机架号照片(0-1张)
	 * @param shangzhe
	 *            受伤者照片(0-1张)
	 * @param buwei
	 *            伤者部位照片(0-5张)
	 */
	@ResponseBody
	@RequestMapping(path = "/accident/survey/{id}", method = RequestMethod.POST)
	public Object saveSurvey(@PathVariable long id, Accident survey, HttpServletRequest request, MultipartFile[] zhengti, MultipartFile[] jubu, MultipartFile xingshi,
			MultipartFile jiashi, MultipartFile jipai, MultipartFile jijia, MultipartFile shangzhe, MultipartFile[] buwei) throws Exception
	{
		// boolean editMode = request.getParameter("editMode") == null ? false :
		// Boolean.parseBoolean(request.getParameter("editMode"));
		// String status = String.valueOf(survey.getStatus().toString());
		// lg.debug("事故信息类型：查勘 {}", status);
		// request.setAttribute("editMode", false);

		if (-1 == id)
		{
			createSurvey(survey, getSessionUser(request), request, zhengti, jubu, xingshi, jiashi, jipai, jijia, shangzhe, buwei);
		}
		else
		{
			updateSurvey(survey, getSessionUser(request), request, zhengti, jubu, xingshi, jiashi, jipai, jijia, shangzhe, buwei);
		}

		return success("OK");
	}

	/**
	 * 查勘 - 新建
	 */
	private void createSurvey(Accident survey, User user, HttpServletRequest request, MultipartFile[] zhengti, MultipartFile[] jubu, MultipartFile xingshi, MultipartFile jiashi,
			MultipartFile jipai, MultipartFile jijia, MultipartFile shangzhe, MultipartFile[] buwei) throws Exception
	{
		survey.setCreateUserName(user.getUserName());
		survey.setLastModifyDate(new Date());
		survey.setLastModifyUserName(user.getUserName());

		Accident report = accidentService.searchById(survey.getAccidentId());
		survey.setCaseName(report.getCaseName());
		survey.setMemberName(report.getMemberName());
		survey.setReason(report.getReason());
		survey.setResponsibility(report.getResponsibility());
		survey.setAccident(report.getAccident());
		survey.setSituation(report.getSituation());
		survey.setProvinceId(report.getProvinceId());
		survey.setRegionId(report.getRegionId());
		survey.setReportTime(report.getReportTime());
		survey.setInsuranceId(report.getInsuranceId());

		// 保存查勘信息
		accidentService.createSurvey(user, survey, zhengti, jubu, xingshi, jiashi, jipai, jijia, shangzhe, buwei);
		// 操作日志
		accidentService.writeSystemLog(user, request, "添加查勘信息", "报案单号：" + survey.getAccidentId() + "，联系电话：" + survey.getCaseTel() + "，车牌号码：" + survey.getCarNum(), null);
	}

	/**
	 * 查勘 - 编辑跳转
	 *
	 * @param id
	 *            查勘ID
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/accident/survey/{id}", method = RequestMethod.GET)
	public ModelAndView editSurvey(@PathVariable long id, HttpServletRequest req) throws Exception
	{
		lg.debug("修改查勘跳转");
		Accident survey = accidentService.getSurvey(id);
		req.setAttribute("r", survey);

		return new ModelAndView("/insurance/accident/edit-survey.jsp");
	}

	/**
	 * 查勘 - 查看跳转
	 *
	 * @param id
	 *            查勘ID
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/accident/view-survey/{id}", method = RequestMethod.GET)
	public ModelAndView viewSurvey(@PathVariable long id, HttpServletRequest req) throws Exception
	{
		lg.debug("查看查勘跳转");
		Accident survey = accidentService.getSurvey(id);
		req.setAttribute("r", survey);

		return new ModelAndView("/insurance/accident/view-survey.jsp");
	}

	/**
	 * 查勘 - 修改
	 */
	private void updateSurvey(Accident survey, User user, HttpServletRequest request, MultipartFile[] zhengti, MultipartFile[] jubu, MultipartFile xingshi, MultipartFile jiashi,
			MultipartFile jipai, MultipartFile jijia, MultipartFile shangzhe, MultipartFile[] buwei) throws Exception
	{
		survey.setLastModifyDate(new Date());
		survey.setLastModifyUserName(user.getUserName());
		accidentService.updateSurvey(user, survey, request.getParameter("deletedImages"), zhengti, jubu, xingshi, jiashi, jipai, jijia, shangzhe, buwei);
		// 操作日志
		userService.writeSystemLog(user, request, "修改查勘信息", "报案单号：" + survey.getAccidentId() + "，联系电话：" + survey.getCaseTel() + "，车牌号码：" + survey.getCarNum(), null);
	}

	// =================================================================
	// 理赔
	// =================================================================

	/**
	 * 新建/修改 理赔
	 */
	@RequestMapping(path = "/accident/settlement/{id}", method = RequestMethod.POST)
	public Object saveSettlement(@PathVariable long id, Accident settlement, HttpServletRequest request, MultipartFile[] sunshi, MultipartFile[] weixiu, MultipartFile[] zeren,
			MultipartFile[] panjue, MultipartFile[] zhenduan, MultipartFile[] fapiao, MultipartFile[] yongyao) throws Exception
	{
		if (-1 == id)
		{
			createSettlement(settlement, getSessionUser(request), request, sunshi, weixiu, zeren, panjue, zhenduan, fapiao, yongyao);
		}
		else
		{
			updateSettlement(settlement, getSessionUser(request), request, sunshi, weixiu, zeren, panjue, zhenduan, fapiao, yongyao);
		}

		return success("OK");
	}

	/**
	 * 新建Settlement
	 */
	private void createSettlement(Accident settlement, User user, HttpServletRequest request, MultipartFile[] sunshi, MultipartFile[] weixiu, MultipartFile[] zeren,
			MultipartFile[] panjue, MultipartFile[] zhenduan, MultipartFile[] fapiao, MultipartFile[] yongyao) throws Exception
	{
		settlement.setCreateUserName(user.getUserName());
		settlement.setLastModifyDate(new Date());
		settlement.setLastModifyUserName(user.getUserName());

		Accident survey = accidentService.searchById(settlement.getAccidentId());
		settlement.setCaseName(survey.getCaseName());
		settlement.setMemberName(survey.getMemberName());
		settlement.setReason(survey.getReason());
		settlement.setResponsibility(survey.getResponsibility());
		settlement.setAccident(survey.getAccident());
		settlement.setSituation(survey.getSituation());
		settlement.setProvinceId(survey.getProvinceId());
		settlement.setRegionId(survey.getRegionId());
		settlement.setReportTime(survey.getReportTime());
		settlement.setInsuranceId(survey.getInsuranceId());

		accidentService.createSettlement(user, settlement, sunshi, weixiu, zeren, panjue, zhenduan, fapiao, yongyao);
		// 操作日志
		accidentService.writeSystemLog(user, request, "添加理赔信息", "查勘单号：" + survey.getAccidentId() + "，联系电话：" + survey.getCaseTel() + "，车牌号码：" + survey.getCarNum(), null);
	}

	/**
	 * 报案 - 编辑跳转
	 *
	 * @param id
	 *            查勘ID
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/accident/settlement/{id}", method = RequestMethod.GET)
	public ModelAndView editSettlement(@PathVariable long id, HttpServletRequest req) throws Exception
	{
		lg.debug("修改报案跳转");
		Accident settlement = accidentService.getSettlement(id);
		req.setAttribute("r", settlement);

		return new ModelAndView("/insurance/accident/edit-settlement.jsp");
	}

	/**
	 * 报案 - 查看跳转
	 *
	 * @param id
	 *            查勘ID
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/accident/view-settlement/{id}", method = RequestMethod.GET)
	public ModelAndView viewSettlement(@PathVariable long id, HttpServletRequest req) throws Exception
	{
		lg.debug("查看报案跳转");
		Accident settlement = accidentService.getSettlement(id);
		req.setAttribute("r", settlement);

		return new ModelAndView("/insurance/accident/view-settlement.jsp");
	}

	/**
	 * 修改Settlement
	 */
	private void updateSettlement(Accident settlement, User user, HttpServletRequest request, MultipartFile[] sunshi, MultipartFile[] weixiu, MultipartFile[] zeren,
			MultipartFile[] panjue, MultipartFile[] zhenduan, MultipartFile[] fapiao, MultipartFile[] yongyao) throws Exception
	{
		settlement.setLastModifyDate(new Date());
		settlement.setLastModifyUserName(user.getUserName());
		accidentService.updateSettlement(user, settlement, request.getParameter("deletedImages"), sunshi, weixiu, zeren, panjue, zhenduan, fapiao, yongyao);
		// 操作日志
		userService.writeSystemLog(user, request, "修改报案信息", "报案单号：" + settlement.getAccidentId() + "，联系电话：" + settlement.getCaseTel() + "，车牌号码：" + settlement.getCarNum(), null);
	}

	@RequestMapping(path = "/accident/get-accident", method = RequestMethod.POST)
	public List<Accident> reportInsuranceSearch(HttpServletRequest req) throws Exception
	{
		String accidentId = req.getParameter("accidentId");
		String insuranceTel = req.getParameter("insuranceTel");
		String status = req.getParameter("status");
		String approvalStatus = req.getParameter("approvalStatus");
		return accidentService.searchAccident(accidentId, insuranceTel, status, approvalStatus);
	}

	/**
	 * 分页查询
	 *
	 * @param status
	 *            状态
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/insurance/accident/report-query/{status}", method = RequestMethod.POST)
	public Page<Accident> searchReport(@PathVariable String status, HttpServletRequest req) throws Exception
	{
		IForm form = getFormWrapper(req);
		return accidentService.searchReport(getSessionUser(req), form, status, getPageSort(req));
	}

	@RequestMapping(path = "/insurance/accident/survey-query/{status}", method = RequestMethod.POST)
	public Page<Accident> searchSurvey(@PathVariable String status, HttpServletRequest req) throws Exception
	{
		IForm form = getFormWrapper(req);
		return accidentService.searchSurvey(getSessionUser(req), form, status, getPageSort(req));
	}

	@RequestMapping(path = "/insurance/accident/settlement-query/{status}", method = RequestMethod.POST)
	public Page<Accident> searchSettlement(@PathVariable String status, HttpServletRequest req) throws Exception
	{
		IForm form = getFormWrapper(req);
		return accidentService.searchSettlement(getSessionUser(req), form, status, getPageSort(req));
	}

	@RequestMapping("/accident/show/{id}/{status}/{edit}")
	public ModelAndView showAccident(@PathVariable String id, @PathVariable String status, @PathVariable String edit, HttpServletRequest req) throws Exception
	{
		req.setAttribute("editMode", true);
		req.setAttribute("id", id);
		req.setAttribute("status", status);

		String view = "/insurance/accident/add_report.jsp";
		if ("false".equals(edit))
		{
			view = "/insurance/accident/view_report.jsp";
		}
		return new ModelAndView(view);
	}

	@RequestMapping(path = "/accident/approve/{id}/{status}")
	public ModelAndView approve(@PathVariable String id, @PathVariable String status, HttpServletRequest request) throws Exception
	{
		User user = getSessionUser(request);
		updateAccidentApprovalStatus(id, 2, null);
		// 操作日志
		userService.writeSystemLog(user, request, "审查通过案件", "操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), "案件单号是：" + id);
		String view = "/insurance/query/report_query.jsp";
		if ("2".equals(status))
		{
			view = "/insurance/query/survey_query.jsp";
		}
		else
		{
			if ("3".equals(status))
			{
				view = "/insurance/query/settlement_query.jsp";
			}
		}
		return new ModelAndView(view);
	}

	@RequestMapping(path = "/accident/disapprove/{id}")
	public String disapprove(@PathVariable String id, HttpServletRequest request) throws Exception
	{
		String approvalRemark = request.getParameter("approvalRemark");
		User user = getSessionUser(request);
		updateAccidentApprovalStatus(id, 1, approvalRemark);
		// 操作日志
		userService.writeSystemLog(user, request, "审查不通过案件", "操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), "案件单号是：" + id);
		return success("OK");
	}

	@RequestMapping(path = "/accident/drop/{id}")
	public String drop(@PathVariable String id, HttpServletRequest request) throws Exception
	{
		String approvalRemark = request.getParameter("approvalRemark");
		User user = getSessionUser(request);
		updateAccidentApprovalStatus(id, 3, approvalRemark);
		// 操作日志
		userService.writeSystemLog(user, request, "审查关闭案件", "操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), "案件单号是：" + id);
		return success("OK");
	}

	@RequestMapping(path = "/accident/pay/{id}")
	public String pay(@PathVariable String id, HttpServletRequest request) throws Exception
	{
		String damageMoney = request.getParameter("damageMoney");
		String paymentMoney = request.getParameter("paymentMoney");
		String swiftNumber = request.getParameter("swiftNumber");
		User user = getSessionUser(request);
		updateAccidentPayStatus(id, damageMoney, paymentMoney, swiftNumber);
		// 操作日志
		userService.writeSystemLog(user, request, "已赔付（审查通过）", "操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), "案件单号是：" + id);
		return success("OK");
	}

	private void updateAccidentApprovalStatus(String id, Integer approvalStatus, String approvalRemark) throws Exception
	{
		Accident accident = new Accident();
		accident.setId(Long.valueOf(id));
		accident.setApprovalStatus(approvalStatus);
		if (StringUtils.isNotBlank(approvalRemark))
		{
			accident.setApprovalRemark(approvalRemark);
			String[] includeFields = { "approvalStatus", "approvalRemark" };
			accidentService.update(accident, includeFields);
		}
		else
		{
			String[] includeFields = { "approvalStatus" };
			accidentService.update(accident, includeFields);
		}

	}

	private void updateAccidentPayStatus(String id, String damageMoney, String paymentMoney, String swiftNumber) throws Exception
	{
		Accident accident = new Accident();
		accident.setId(Long.valueOf(id));
		accident.setApprovalStatus(4);
		accident.setDamageMoney(damageMoney);
		accident.setPaymentMoney(paymentMoney);
		accident.setSwiftNumber(swiftNumber);
		String[] includeFields = { "approvalStatus", "damageMoney", "paymentMoney", "swiftNumber" };
		accidentService.update(accident, includeFields);
	}

	/**
	 * 获取历史保单记录
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(path = "/accident/get-report", method = RequestMethod.POST)
	public void getHisInfo(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out;
		JSONObject json = new JSONObject();

		String id = request.getParameter("id");
		Accident report = accidentService.searchById(id);

		json.put("insuranceId", report.getInsuranceId());
		json.put("caseName", report.getCaseName());
		json.put("caseTel", report.getCaseTel());
		json.put("memberName", report.getMemberName());
		json.put("machineryType", report.getMachineryType());
		json.put("machineryTypeSelect", report.getMachineryType());
		json.put("carNum", report.getCarNum());
		Date reportTime = report.getReportTime();
		if (reportTime != null)
		{
			json.put("dangerTime", DateFormatUtils.format(report.getReportTime(), FORP.PATTERN_DATE_TIME));
		}
		json.put("dangerAddress", report.getDangerAddress());
		json.put("dangerLongitude", report.getDangerLongitude());
		json.put("dangerLatitude", report.getDangerLatitude());
		json.put("reason", report.getReason());
		json.put("responsibility", report.getResponsibility());
		json.put("accident", report.getAccident());
		String situation = report.getSituation();
		if (StringUtils.isNoneBlank(situation))
		{
			if (situation.indexOf("1") > -1)
				json.put("situation1", 1);
			if (situation.indexOf("2") > -1)
				json.put("situation2", 2);
			if (situation.indexOf("3") > -1)
				json.put("situation3", 3);
			if (situation.indexOf("4") > -1)
				json.put("situation4", 4);
			if (situation.indexOf("5") > -1)
				json.put("situation5", 5);
		}
		json.put("driverName", report.getDriverName());
		json.put("reportAddress", report.getReportAddress());
		json.put("reportLongitude", report.getReportLongitude());
		json.put("reportLatitude", report.getReportLatitude());
		json.put("unusual", report.getUnusual());
		json.put("approvalStatus", report.getApprovalStatus());
		try
		{
			out = response.getWriter();
			out.print(json);
			out.flush();
			out.close();
		}
		catch (IOException e)
		{
			lg.error("获得报案信息错误getReport ：", e);
		}
	}
}
