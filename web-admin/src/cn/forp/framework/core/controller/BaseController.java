/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.controller;

import cn.forp.framework.core.BusinessException;
import cn.forp.framework.core.FORP;
import cn.forp.framework.core.vo.Message;
import cn.forp.framework.core.vo.PageSort;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Controller基类
 *
 * @author	Bruce
 * @version	2016-4-1 14:04:41
 */
public abstract class BaseController
{
  /**
   * Logger
   */
	private static final Logger lg = LoggerFactory.getLogger(BaseController.class);

	//=================================================================
	//							公用方法
	//=================================================================

	/**
	 * 打印所有请求中的参数和值（调试用）
	 */
	protected void printRequestParameters(HttpServletRequest req)
	{
		lg.debug("------------------ request请求参数 ------------------");
		Enumeration<String> params = req.getParameterNames();
		String name;
		while (params.hasMoreElements())
		{
			name = params.nextElement();
			lg.debug("{}-->{}", name, req.getParameter(name));
		}
		lg.debug("------------------ request请求参数 ------------------");
	}

	/**
	 * 获取表单请求Wrapper
	 * 
	 * @param req   Request请求参数
	 */
	protected IForm getFormWrapper(HttpServletRequest req)
	{
		return new FormImpl(req.getParameterMap());
	}

	/**
	 * 使用反射机制，实现表单数据向bo对象相应属性值的自动复制（对于需要拆分、名称不匹配等特殊功能的操作，必须手工完成）。
	 *
	 * @param req		Request请求参数
	 * @param clazz vo对象类
	 */
	public <T> T mapping(HttpServletRequest req, Class<T> clazz) throws Exception
	{
		T obj = clazz.newInstance();
		BeanUtils.populate(obj, req.getParameterMap());

		return obj;
	}

	/**
	 * 获取当前登陆用户信息
	 * 
	 * @return 登陆用户对象
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getSessionUser(HttpServletRequest req)
	{
		return (T) req.getSession().getAttribute(FORP.SESSION_USER);
	}

	/**
	 * 检查客户是否已登陆
	 *
	 * @param req   Request请求参数
	 */
	protected void checkSessionUser(HttpServletRequest req) throws Exception
	{
		if (null == req.getSession() || null == req.getSession().getAttribute(FORP.SESSION_USER))
			throw new BusinessException("您还没有登录系统，请先登录！");
	}

	/**
	 * 解析分页和排序信息
	 * 
	 * @return 分页和排序对象
	 */
	protected PageSort getPageSort(HttpServletRequest req)
	{
		PageSort ps = new PageSort();
		// 分页信息
		if (StringUtils.isNotBlank(req.getParameter(PageSort.PAGE_NUMBER)))
			ps.setPageNumber(Long.parseLong(req.getParameter(PageSort.PAGE_NUMBER)));

		if (StringUtils.isNotBlank(req.getParameter(PageSort.PAGE_SIZE)))
			ps.setPageSize(Integer.parseInt(req.getParameter(PageSort.PAGE_SIZE)));

		// 排序信息
		ps.setSortName(req.getParameter(PageSort.SORT_NAME));
		ps.setSortOrder(req.getParameter(PageSort.SORT_ORDER));

		return ps;
	}

	/**
	 * 判断是否是Ajax请求
	 * 
	 * @param req       Request请求参数
	 * @return boolean
	 */
	public static boolean isAjaxRequest(HttpServletRequest req)
	{
		// TODO 检查Header中的“Powered-By”信息
		return StringUtils.isNotBlank(req.getHeader("X-Requested-With"));
	}

//	/**
//	 * 获取回应的ContentType
//	 * 
//	 * @return ContentType
//	 */
//	private String getResponseContentType()
//	{
//		// 普通Ajax
//		String contentType = "application/json;charset=UTF-8";
//
//		// 文件上传
//		if (ServletFileUpload.isMultipartContent(request))
//			contentType = "text/html;charset=utf-8";
//
//		return contentType;
//	}
//
//	/**
//	 * 检查客户是否已登陆
//	 * 
//	 * @throws Exception
//	 */
//	public void checkLogon() throws Exception
//	{
//		if (null == session || null == session.getAttribute(BaseSystemParams.SESSION_USER))
//			throw new BusinessException("您还没有登录系统，请先登录！");
//	}

	//=================================================================
	//							信息提示
	//=================================================================

	/**
	 * 操作成功
	 * 
	 * @param msg		信息内容
	 */
	public String success(String msg)
	{
		return "{\"success\": true, \"msg\": \"" + msg + "\"}";
	}

	/**
	 * 操作失败
	 * 
	 * @param msg		信息内容
	 */
	public static String error(String msg)
	{
		return "{\"success\": false, \"msg\": \"" + msg + "\"}";
	}

	/**
	 * 返回操作成功View页面
	 * 
	 * @param msg		信息内容
	 */
	public ModelAndView successView(String msg, HttpServletRequest req)
	{
		Message message = new Message(msg);
		message.setType(1);
		req.setAttribute("msg", message);

		return new ModelAndView("redirect:/common/message.jsp");
	}

	/**
	 * 返回操作失败View页面
	 * 
	 * @param msg		信息内容
	 */
	public static ModelAndView errorView(String msg, HttpServletRequest req)
	{
		Message message = new Message(msg);
		message.setType(2);
		req.setAttribute("msg", message);

		return new ModelAndView("redirect:/common/message.jsp");
	}
}