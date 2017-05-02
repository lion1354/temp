/**
 * Copyright © 2017, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.information.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.controller.IForm;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.platform.vo.User;
import cn.forp.information.service.MerchantService;
import cn.forp.information.vo.Consultation;

/**
 * 入驻商户管理Controller
 *
 * @author  Bruce
 * @version 2017-2-18 14:48
 */
@RestController
public class MerchantController extends BaseController
{
	/**
	 * Logger
	 */
	private static final Logger lg = LoggerFactory.getLogger(MerchantController.class);
	/**
	 * Service
	 */
	@Autowired
	protected MerchantService service = null;

	/**
	 * 分页查询
	 *
	 * @param req     Request请求参数
	 */
	@RequestMapping(path="/information/merchant", method=RequestMethod.POST)
	public Page<Consultation> search(HttpServletRequest req) throws Exception
	{
		IForm form = getFormWrapper(req);
		return service.search(form.get("type"), form.get("name"), form.get("fromDate"), form.get("toDate"), getPageSort(req));
	}

	/**
	 * 新建/修改
	 *
	 * @param id	      ID
	 * @param merchant  商户
	 * @param image     照片
	 * @param req	      Request请求参数
	 */
	@RequestMapping(path="/information/merchant/{id}", method=RequestMethod.POST)
	public String save(@PathVariable Long id, Consultation merchant, MultipartFile image, HttpServletRequest req) throws Exception
	{
		merchant.setId(id);
		// 手工构造日期字段
		merchant.setEndDate(DateUtils.parseDate(req.getParameter("endDate1"), "yyyy-MM-dd"));

		return -1 == id ? create(merchant, image, req) : update(merchant, image, req);
	}

	/**
	 * 新建
	 *
	 * @param merchant  商户
	 * @param image     图片
	 * @param req       Request请求参数
	 */
	private String create(Consultation merchant, MultipartFile image, HttpServletRequest req) throws Exception
	{
		lg.info("新建厂商！");
		User su = getSessionUser(req);

		merchant.setFormType("1");
		service.create(su, merchant, image);
		// 操作日志
		service.writeSystemLog(su, req, "添加新商户", "商户：" + merchant.getName(), null);

		return success("OK");
	}

	/**
	 * 修改
	 *
	 * @param merchant  商户
	 * @param image     图片
	 * @param req       Request请求参数
	 */
	private String update(Consultation merchant, MultipartFile image, HttpServletRequest req) throws Exception
	{
		lg.info("修改厂商！");
		User su = getSessionUser(req);

		merchant.setFormType("1");
		service.update(su, merchant, image);
		// 操作日志
		service.writeSystemLog(su, req, "修改商户信息", "商户：" + merchant.getName(), null);

		return success("OK");
	}
}