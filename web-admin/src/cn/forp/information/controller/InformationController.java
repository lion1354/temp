/**
 * Copyright © 2017, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.information.controller;

import javax.servlet.http.HttpServletRequest;

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
import cn.forp.framework.core.controller.IForm;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.platform.vo.User;
import cn.forp.information.service.InformationService;
import cn.forp.information.vo.Information;

/**
 * 信息管理Controller
 *
 * @author Apple
 * @version 2017-2-18 14:48
 */
@RestController
public class InformationController extends BaseController
{
	/**
	 * Logger
	 */
	private static final Logger lg = LoggerFactory.getLogger(InformationController.class);
	/**
	 * Service
	 */
	@Autowired
	protected InformationService service = null;

	/**
	 * 分页查询
	 * 
	 * @param title
	 * @param category
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "/information/information", method = RequestMethod.POST)
	public Page<Information> search(HttpServletRequest req) throws Exception
	{
		IForm form = getFormWrapper(req);
		return service.search(form.get("title"), form.get("fromDate"), form.get("toDate"), form.get("category"),
				form.get("status"), getPageSort(req));
	}

	/**
	 * 新建/修改
	 */
	@RequestMapping(path = "/information/information/{id}", method = RequestMethod.POST)
	public String save(@PathVariable Long id, Information information, MultipartFile image, HttpServletRequest req)
			throws Exception
	{
		information.setId(id);

		return -1 == id ? create(information, image, req) : update(information, image, req);
	}

	/**
	 * 新建
	 */
	private String create(Information information, MultipartFile image, HttpServletRequest req) throws Exception
	{
		lg.info("新建新消息！");
		User su = getSessionUser(req);
		service.create(su, information, image);
		// 操作日志
		service.writeSystemLog(su, req, "添加新消息", "新消息：" + information.getTitle(), null);

		return success("OK");
	}

	/**
	 * 修改
	 */
	private String update(Information information, MultipartFile image, HttpServletRequest req) throws Exception
	{
		lg.info("修改新消息！");
		User su = getSessionUser(req);
		service.update(su, information, image);
		// 操作日志
		service.writeSystemLog(su, req, "修改新消息信息", "新消息：" + information.getTitle(), null);

		return success("OK");
	}

	@RequestMapping(path = "/information/information/status")
	public String changeStatus(@RequestParam("id") String id, @RequestParam("status") int status,
			HttpServletRequest req) throws Exception
	{
		lg.debug("information id：{}", id);

		service.changeStatus(id, status);
		String statusStr = "审批通过";
		if (2 == status)
		{
			statusStr = "未通过";
		}
		else if (3 == status)
		{
			statusStr = "关闭";
		}
		// 操作日志
		service.writeSystemLog(getSessionUser(req), req, statusStr + " 新消息", "新消息ID：" + id, null);

		return success("OK");
	}
}