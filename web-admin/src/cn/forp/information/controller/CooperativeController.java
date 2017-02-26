/**
 * Copyright © 2017, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.information.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.platform.vo.User;
import cn.forp.information.service.CooperativeService;
import cn.forp.information.vo.Cooperative;

/**
 * 合作社展台管理Controller
 *
 * @author Apple
 * @version 2017-2-18 12:04
 */
@RestController
public class CooperativeController extends BaseController
{
	/**
	 * Logger
	 */
	private static final Logger lg = LoggerFactory.getLogger(CooperativeController.class);
	/**
	 * Service
	 */
	@Autowired
	protected CooperativeService service = null;

	/**
	 * 分页查询
	 */
	@RequestMapping(path = "/information/cooperative", method = RequestMethod.POST)
	public Page<Cooperative> search(@RequestParam("name") String name,
			@RequestParam("contactPerson") String contactPerson, @RequestParam("status") String status,
			HttpServletRequest req) throws Exception
	{
		return service.search(name, contactPerson, (StringUtils.isBlank(status) ? -1 : Integer.parseInt(status)),
				getPageSort(req));
	}

	/**
	 * 新建/修改
	 */
	@RequestMapping(path = "/information/cooperative/{id}", method = RequestMethod.POST)
	public String save(@PathVariable Long id, Cooperative cooperative, MultipartFile image, HttpServletRequest req)
			throws Exception
	{
		cooperative.setId(id);

		return -1 == id ? create(cooperative, image, req) : update(cooperative, image, req);
	}

	/**
	 * 新建
	 */
	private String create(Cooperative cooperative, MultipartFile image, HttpServletRequest req) throws Exception
	{
		User su = getSessionUser(req);
		service.create(su, cooperative, image);
		// 操作日志
		service.writeSystemLog(su, req, "添加新合作社展台", "合作社展台：" + cooperative.getName(), null);

		return success("OK");
	}

	/**
	 * 修改
	 */
	public String update(Cooperative cooperative, MultipartFile image, HttpServletRequest req) throws Exception
	{
		User su = getSessionUser(req);
		service.update(su, cooperative, image);
		// 操作日志
		service.writeSystemLog(su, req, "修改合作社展台", "合作社展台：" + cooperative.getName(), null);

		return success("OK");
	}
}