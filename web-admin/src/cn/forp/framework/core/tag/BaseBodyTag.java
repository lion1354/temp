/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * 自定义BodyTag的基类
 *
 * @author	Bruce
 * @version	2012-3-19 09:19:21
 */
public abstract class BaseBodyTag extends BodyTagSupport 
{
	protected HttpServletRequest request = null;
	protected HttpServletResponse response = null;
	protected HttpSession session = null;
	protected PageContext pageContext = null;

	protected JspWriter page = null;

//	/**
//	 * 标签开始事件
//	 */
//	public abstract int doStartTag() throws JspException;
	/**
	 * 标签结束事件
	 */
	public abstract int doEndTag() throws JspException;
//	/**
//	 * 标签体初始化事件
//	 */
//	public abstract void doInitBody() throws JspException;
//	/**
//	 * 标签体结束事件
//	 */
//	public abstract int doAfterBody() throws JspException;
//	

	/**
	 * 上下文环境回调方法
	 *
	 * @param pc    页面上下文
	 */
	public void setPageContext(PageContext pc)
	{
		super.setPageContext(pc);

		if(null != pc)
		{
			pageContext = pc;
			request  = (HttpServletRequest) pc.getRequest();
			response = (HttpServletResponse) pc.getResponse();
			page 	 = pc.getOut();
			session  = pc.getSession();
			
		}	
	}
}