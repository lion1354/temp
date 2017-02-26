/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core;

import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.vo.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Session超时拦截器
 *
 * @author	Bruce
 * @version	2016年6月8日 16:18:15
 */
public class SessionInterceptor extends HandlerInterceptorAdapter
{
  /**
   * Logger
   */
  private static final Logger lg = LoggerFactory.getLogger(SessionInterceptor.class);

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception
	{
		checkGlobalContext(req);

		// 检查Session超时标记
		if (null == req.getSession() || null == req.getSession().getAttribute(FORP.SESSION_USER))
		{
			handleSessionTimeout(req, resp);
			return false;
		}
		else
			return true;
	}

	/**
	 * 检查全局Context变量
	 * 
	 * @param req	请求对象
	 */
	private void checkGlobalContext(HttpServletRequest req)
	{
    	// WEB_APP_CONTEXT变量
		if (null == FORP.WEB_APP_CONTEXT)
		{
			FORP.WEB_APP_CONTEXT = req.getContextPath();
			lg.info("初始化WEB_APP_CONTEXT：{}", FORP.WEB_APP_CONTEXT);
		}

		// WEB_APP_CONTEXT + ROOT变量
		if (null == req.getServletContext().getAttribute("ROOT"))
		{
			req.getServletContext().setAttribute("ROOT", FORP.WEB_APP_CONTEXT);
			lg.info("初始化ROOT：{}", FORP.WEB_APP_CONTEXT);
		}
	}

	/**
	 * Session超时处理
	 * 
	 * @param req	请求对象
	 * @param resp	回应对象
	 */
	private void handleSessionTimeout(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		lg.warn("Session已超时，重定向至登陆页面：{}", req.getRequestURL());
		lg.debug("Referer：{}", req.getHeader("Referer"));

		if (BaseController.isAjaxRequest(req))
		{
			// Ajax请求：401
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else
		{
			// 普通请求
			Message message = new Message("您长时间没有操作，需要重新验证您的账号安全。<br/>请重新登录系统！");
			message.setType(3);
			req.setAttribute("msg", message);
			// 设置对话框按钮动作URL
			req.setAttribute("actionURL", "/");
			req.getRequestDispatcher("/WEB-INF/jsp/common/message.jsp").forward(req, resp);
		}
	}
}