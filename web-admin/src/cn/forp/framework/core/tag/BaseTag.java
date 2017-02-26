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
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 没有Body内容Tag基类
 *
 * @author	Bruce
 * @version	2012-3-19 09:19:13
 */
public abstract class BaseTag extends TagSupport
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
//	 * 标签体结束事件
//	 */
//	public abstract int doAfterBody() throws JspException;
	
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