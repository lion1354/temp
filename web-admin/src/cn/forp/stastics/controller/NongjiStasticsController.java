/**
 * Copyright © 2017, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.stastics.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.controller.IForm;
import cn.forp.framework.platform.vo.User;
import cn.forp.stastics.service.StasticsService;
import cn.forp.stastics.vo.NongJiStastics;

/**
 * 农机统计Controller
 *
 * @author  Apple
 * @version 2017-5-1 12:04
 */
@RestController
public class NongjiStasticsController extends BaseController
{
	@Autowired
	protected StasticsService service;

	@RequestMapping("/stastics/nongji")
	public ModelAndView getStasticsResult(HttpServletRequest req) throws Exception
	{
		IForm form = getFormWrapper(req);
		String year = form.get("year");
		String beginDate = form.get("beginDate");
		String endDate = form.get("endDate");
		User user = getSessionUser(req);
		// int provinceById = StringUtils.isBlank(user.getItem1()) ? 0 : Integer.parseInt(user.getItem1());
		int regionsId = StringUtils.isBlank(user.getItem2()) ? 0 : Integer.parseInt(user.getItem2());

		List<NongJiStastics> list = service.getNongjiStastics(year, beginDate, endDate, regionsId);

		req.setAttribute("year", year);
		req.setAttribute("beginDate", beginDate);
		req.setAttribute("endDate", endDate);
		req.setAttribute("list", list);

		return new ModelAndView("/insurance/statistics/nongji_statistics.jsp");
	}
}
