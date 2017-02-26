/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core;

import cn.forp.framework.core.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Exception 
 *
 * @author	Bruce
 * @version	2016年6月8日 下午4:31:00
 */
public class ExceptionHandler implements HandlerExceptionResolver
{
  /**
   * Logger
   */
  private static final Logger lg = LoggerFactory.getLogger(ExceptionHandler.class);

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerExceptionResolver#resolveException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex)
	{
		// 解析错误消息
		String msg;
		if (ex instanceof BusinessException)
		{
			// 业务异常
			msg = ex.getMessage();
		}
		else
		{
			lg.error("系统错误：", ex);
			msg = "您当前的操作失败，请稍后重试！<br/>如果操作仍然没有成功，请及时联系系统管理员。";
		}

		if (BaseController.isAjaxRequest(req))
		{
			// Ajax请求
			msg = msg.replaceAll("\"", "&quot;").replaceAll(":", "：").replaceAll("\r\n", "<br/>").replaceAll("\r", "<br/>").replaceAll("\n", "<br/>");
			msg = StringUtils.replace(msg, "[", "【");
			msg = StringUtils.replace(msg, "]", "】");

			String json = BaseController.error(msg);
			lg.debug("JSON:\r\n{}", json);
			try
			{
				resp.setStatus(200);
				resp.setContentType("application/json;charset=utf-8");
				resp.getWriter().write(json);
				resp.getWriter().flush();
			}
			catch (Exception e)
			{
				lg.error("Response Json回写失败：", e);
			}

			return new ModelAndView();
		}
		else
		{
			// 普通页面请求
			return BaseController.errorView(msg, req);
		}
	}
}