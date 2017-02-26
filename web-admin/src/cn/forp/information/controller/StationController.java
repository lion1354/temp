/**
 * Copyright © 2017, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.information.controller;

import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.platform.vo.User;
import cn.forp.information.service.StationService;
import cn.forp.information.vo.Consultation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 监理站管理Controller
 *
 * @author  Bruce
 * @version 2017-2-18 12:04
 */
@RestController
public class StationController extends BaseController
{
	/**
	 * Logger
	 */
	private static final Logger lg = LoggerFactory.getLogger(StationController.class);
	/**
	 * Service
	 */
	@Autowired
	protected StationService service = null;

	/**
	 * 分页查询
	 *
	 * @param name    名称
	 * @param state   状态
	 * @param req     Request请求参数
	 */
	@RequestMapping(path="/information/station", method=RequestMethod.POST)
	public Page<Consultation> search(@RequestParam("name") String name, @RequestParam("state") String state, HttpServletRequest req) throws Exception
	{
		return service.search(name, (StringUtils.isBlank(state) ? -1 : Integer.parseInt(state)), getPageSort(req));
	}

	/**
	 * 新建/修改
	 *
	 * @param id	    ID
	 * @param station 监理站
	 * @param req	    Request请求参数
	 */
	@RequestMapping(path="/information/station/{id}", method=RequestMethod.POST)
	public String save(@PathVariable Long id, Consultation station, HttpServletRequest req) throws Exception
	{
		station.setId(id);

		return -1 == id ? create(station, req) : update(station, req);
	}

	/**
	 * 新建
	 *
	 * @param station 监理站
	 * @param req     Request请求参数
	 */
	private String create(Consultation station, HttpServletRequest req) throws Exception
	{
		User su = getSessionUser(req);

		station.setFormType("0");
		service.create(su, station);
		// 操作日志
		service.writeSystemLog(su, req, "添加新监理站", "监理站：" + station.getName(), null);

		return success("OK");
	}

	/**
	 * 修改
	 *
	 * @param station 监理站
	 * @param req     Request请求参数
	 */
	public String update(Consultation station, HttpServletRequest req) throws Exception
	{
		User su = getSessionUser(req);

		station.setFormType("0");
		service.update(su, station);
		// 操作日志
		service.writeSystemLog(su, req, "修改监理站信息", "监理站：" + station.getName(), null);

		return success("OK");
	}
}