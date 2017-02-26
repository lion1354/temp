/**
 * Copyright © 2015, HuaYu iStudy Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.tag;

import cn.forp.framework.core.FORP;
import cn.forp.framework.core.tag.BaseBodyTag;
import cn.forp.framework.platform.service.UserService;
import cn.forp.framework.platform.vo.User;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;

/**
 * 细粒度权限控制Tag
 *
 * @author		Bruce
 * @version	2014年7月18日 下午4:59:19
 */
public class PrivilegeTag extends BaseBodyTag
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PrivilegeTag.class);
	/**
	 * 菜单编码
	 */
	private String menu;
	/**
	 * 权限名称
	 */
	private String sn;

	/**
	 * 标签解析开始
	 */
	public int doStartTag() throws JspException
	{
		try
		{
			User user = (User) session.getAttribute(FORP.SESSION_USER);
			UserService service = (UserService) FORP.SPRING_CONTEXT.getBean("platformUserService");
			if (service.hasPrivilege(user.getId(), menu, sn))
				return EVAL_BODY_INCLUDE;
		}
		catch (Exception e)
		{
			logger.error("细粒度权限检查Tag错误:", e);
		}

		return SKIP_BODY;
	};

	/* (non-Javadoc)
	 * @see com.chinasoftware.core.tag.BaseBodyTag#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}

	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn)
	{
		this.sn = sn;
	}

	/**
	 * @param menu the menu to set
	 */
	public void setMenu(String menu)
	{
		this.menu = menu;
	}
}