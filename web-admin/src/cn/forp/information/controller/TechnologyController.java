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
import cn.forp.information.service.TechnologyService;
import cn.forp.information.vo.Technology;

/**
 * 新技术管理Controller
 *
 * @author Apple
 * @version 2017-2-18 14:48
 */
@RestController
public class TechnologyController extends BaseController
{
	/**
	 * Logger
	 */
	private static final Logger lg = LoggerFactory.getLogger(TechnologyController.class);
	/**
	 * Service
	 */
	@Autowired
	protected TechnologyService service = null;

	/**
	 * 分页查询
	 * 
	 * @param title
	 * @param category
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "/information/technology", method = RequestMethod.POST)
	public Page<Technology> search(@RequestParam("title") String title, @RequestParam("category") String category,
			HttpServletRequest req) throws Exception
	{
		return service.search(title, (StringUtils.isBlank(category) ? -1 : Integer.parseInt(category)),
				getPageSort(req));
	}

	/**
	 * 新建/修改
	 *
	 * @param id
	 *            ID
	 * @param technology
	 *            新技术
	 * @param image
	 *            照片
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/information/technology/{id}", method = RequestMethod.POST)
	public String save(@PathVariable Long id, Technology technology, MultipartFile image, HttpServletRequest req)
			throws Exception
	{
		technology.setId(id);

		return -1 == id ? create(technology, image, req) : update(technology, image, req);
	}

	/**
	 * 新建
	 *
	 * @param technology
	 *            新技术
	 * @param image
	 *            图片
	 * @param req
	 *            Request请求参数
	 */
	private String create(Technology technology, MultipartFile image, HttpServletRequest req) throws Exception
	{
		lg.info("新建新技术！");
		User su = getSessionUser(req);
		service.create(su, technology, image);
		// 操作日志
		service.writeSystemLog(su, req, "添加新技术", "新技术：" + technology.getTitle(), null);

		return success("OK");
	}

	/**
	 * 修改
	 *
	 * @param technology
	 *            新技术
	 * @param image
	 *            图片
	 * @param req
	 *            Request请求参数
	 */
	private String update(Technology technology, MultipartFile image, HttpServletRequest req) throws Exception
	{
		lg.info("修改新技术！");
		User su = getSessionUser(req);
		service.update(su, technology, image);
		// 操作日志
		service.writeSystemLog(su, req, "修改新技术信息", "新技术：" + technology.getTitle(), null);

		return success("OK");
	}
}