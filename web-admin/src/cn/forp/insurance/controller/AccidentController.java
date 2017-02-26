package cn.forp.insurance.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.forp.framework.core.FORP;
import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.platform.service.UserService;
import cn.forp.framework.platform.vo.User;
import cn.forp.insurance.service.AccidentService;
import cn.forp.insurance.vo.Accident;
import cn.forp.insurance.vo.Constants;

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

	/**
	 * 新建/修改
	 */
	@RequestMapping(path = "/accident/accident", method = RequestMethod.POST)
	public ModelAndView save(Accident accident, HttpServletRequest request) throws Exception
	{
		boolean editMode = request.getParameter("editMode") == null ? false
				: Boolean.parseBoolean(request.getParameter("editMode"));
		String status = String.valueOf(accident.getStatus().toString());
		lg.debug("事故信息类型：{}", status);
		User user = getSessionUser(request);

		String dangerTimeStr = request.getParameter("dangerTimeStr");
		if (StringUtils.isNoneBlank(dangerTimeStr))
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
		accident.setLastModifyDate(new Date());
		accident.setLastModifyUserName(user.getUserName());
		if (!editMode)
		{
			try
			{
				create(accident, request);
			}
			catch (Exception e)
			{
				lg.error("事故信息新建失败，请检查页面中是否有非法字符：", e);
				throw new Exception("事故信息新建失败，请检查页面中是否有非法字符");
			}
		}
		else
		{
			try
			{
				update(accident, request);
			}
			catch (Exception e)
			{
				lg.error("事故信息更新失败，请检查页面中是否有非法字符：", e);
				throw new Exception("事故信息更新失败，请检查页面中是否有非法字符");
			}
		}
		request.setAttribute("editMode", false);
		String view = "insurance" + Constants.SEPARATOR + "accident" + Constants.SEPARATOR + "add_report";
		if ("2".equals(status))
		{
			view = "insurance" + Constants.SEPARATOR + "accident" + Constants.SEPARATOR + "add_survey";
		}
		else if ("3".equals(status))
		{
			view = "insurance" + Constants.SEPARATOR + "accident" + Constants.SEPARATOR + "add_settlement";
		}
		request.setAttribute("view", view);
		return new ModelAndView("/insurance/result.jsp");

	}

	/**
	 * 新建
	 */
	private String create(Accident accident, HttpServletRequest request) throws Exception
	{
		User user = getSessionUser(request);
		accidentService.create(accident, null, null);
		// 操作日志
		userService.writeSystemLog(user, request, "添加事故信息",
				"操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), null);

		return success("OK");
	}

	/**
	 * 修改
	 */
	private String update(Accident accident, HttpServletRequest request) throws Exception
	{
		User user = getSessionUser(request);
		int status = accident.getStatus();
		String[] includeFields = null;
		if (status == 1)
		{
			String[] includeFieldsReport = { "caseName", "caseTel", "reason", "responsibility", "accident", "situation",
					"driverName" };
			includeFields = includeFieldsReport;
		}
		accidentService.update(accident, includeFields, null);
		// 操作日志
		userService.writeSystemLog(user, request, "修改事故信息",
				"操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), null);

		return success("OK");
	}
}
