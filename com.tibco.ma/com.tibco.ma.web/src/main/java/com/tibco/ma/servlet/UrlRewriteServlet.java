package com.tibco.ma.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UrlServlet
 */
@WebServlet(name = "/UrlServlet", urlPatterns = { "/apps/*", "/systemManagement/*", "/account/*", "/index", "/login",
		"/signUp", "/checkEmail", "/activate", "/findpassword", "/resetpassword", "/resetsuccess", "/resetfailed",
		"/resettimeout", "/api-doc", "/documents", "/help", "/error/*" })
public class UrlRewriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UrlRewriteServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uri = request.getRequestURI();
		uri = uri.replace(request.getContextPath(), request.getContextPath() + "/#");
		response.sendRedirect(uri);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
