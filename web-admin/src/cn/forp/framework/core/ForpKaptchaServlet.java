/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core;

import com.google.code.kaptcha.servlet.KaptchaServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Forp Google验证码类（修改为支持Cluster实时同步功能）
 *
 * @author	LiangLei
 * @version	2016年7月28日 下午5:33:21
 */
public class ForpKaptchaServlet extends KaptchaServlet
{
	/* 
	 * 重新doGet方法 更新session时通过setAttribute("__changed__", null)方式同步ClusterSession
	 * @see com.google.code.kaptcha.servlet.KaptchaServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		super.doGet(req, resp);
		req.getSession().setAttribute("__changed__",null);
	}
}