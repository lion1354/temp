/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.controller;

import cn.forp.framework.core.FORP;
import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.controller.IForm;
import cn.forp.framework.core.util.Redis;
import cn.forp.framework.core.vo.Message;
import cn.forp.framework.platform.service.SystemService;
import cn.forp.framework.platform.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 * 默认Controller
 *
 * @author	Bruce
 * @version	2016-03-30 17:40:19
 */
@RestController
public class IndexController extends BaseController
{
  /**
   * Logger
   */
  private static final Logger lg = LoggerFactory.getLogger(IndexController.class);

  /**
   * 首页
   */
  @RequestMapping("/")
  public ModelAndView index(HttpServletRequest req) throws Exception
  {
		lg.info("首页跳转！");
    return new ModelAndView("/index.html");
  }

  /**
   * home跳转
   */
  @RequestMapping(path="/home", method=RequestMethod.GET)
  public ModelAndView home(@CookieValue("layout") String layout, HttpServletRequest req) throws Exception
  {
		return new ModelAndView("/home-" + layout + ".jsp");
  }

	/**
	 * 欢迎页面
	 */
	@RequestMapping("/welcome")
	public ModelAndView welcome(HttpServletRequest req) throws Exception
	{
		return new ModelAndView("/welcome.html");
	}

  /**
   * 错误页面跳转 - 401
   */
  @RequestMapping("/platform/error/401")
  public ModelAndView error401(HttpServletRequest req) throws Exception
  {
    Message msg = new Message("由于您长时间没有操作，系统需要重新验证您的账号安全。<br/>请重新登录系统！");
    msg.setType(3);
    req.setAttribute("msg", msg);
    req.setAttribute("actionURL", "/");

	  return new ModelAndView("/common/message.jsp");
  }

  /**
   * 错误页面跳转 - 404
   */
  @RequestMapping("/platform/error/404")
  public ModelAndView error404() throws Exception
  {
	  return new ModelAndView("/common/404.jsp");
  }

  /**
   * 错误页面跳转 - 500
   */
  @RequestMapping("/platform/error/500")
  public ModelAndView error500(HttpServletRequest req) throws Exception
  {
    Message msg = new Message("系统内部错误，请稍后重试！如果问题依然存在请及时联系系统管理员！");
    msg.setType(3);
    req.setAttribute("msg", msg);
		// req.setAttribute("actionURL", "");

	  return new ModelAndView("/common/message.jsp");
  }

	//=================================================================
	//		登录/退出系统
	//=================================================================	

  /**
   * 登录系统
   *
   * @param req   请求参数
   */
  @RequestMapping(path = "/platform/logon", method = RequestMethod.POST )
  public String logon(HttpServletRequest req) throws Exception
  {
    IForm form = getFormWrapper(req);
    SystemService service = FORP.SPRING_CONTEXT.getBean(SystemService.class);
		User user = service.logon(form.get("userName"), form.get("passwd"));

		if (null == user)
		{
			lg.warn("“{}”尝试登录系统失败！", form.get("userName"));
			return error("登录失败：无效的账号信息，请检查您的输入！");
		}
		else
		{
			// Session缓冲User
			req.getSession().setAttribute(FORP.SESSION_USER, user);
			// 操作日志
			service.writeSystemLog(user, req, "登录系统", null, null);

			lg.info("“{}”成功登录系统！", user.getUserName());
			// logger.debug(JSON.toJSONString(user, HuaYuIStudy.FAST_JSON_CONFIG));
			return "{\"success\": true, \"userId\": " + user.getId() + ", \"domainId\": " + user.getDomainId() + ", \"userHeadImg\": \"" + user.getHeadImg() + "\"}";
		}
  }

  /**
   * 退出系统
   *
   * @param id    用户ID
   * @param page	跳转页面（默认为空，跳转至/路由。不同的系统定义自己的page跳转【/admin】）
   * @param req	  请求参数
   */
  @RequestMapping("/platform/logout/{id}/{page}")
  public ModelAndView logout(@PathVariable Long id, @PathVariable String page, HttpServletRequest req) throws Exception
  {
    lg.info("退出系统：userId[{}], page-[{}]", id, page);

		// 清除用户权限缓冲
		User user = getSessionUser(req);
		if (null != user)
		{
			// 操作日志
			SystemService service = FORP.SPRING_CONTEXT.getBean(SystemService.class);
			service.writeSystemLog(user, req, "退出系统", null, null);
			Redis.delete(FORP.CACHE_USER_PERMISSION + user.getId(), null);
		}

		req.getSession().removeAttribute(FORP.SESSION_USER);
		req.getSession().invalidate();

		if (page.equals("default"))	// 默认首页
			return new ModelAndView("redirect:/");
		else	// 子系统首页
			return new ModelAndView("redirect:/" + page);
  }

	//=================================================================
	//		测试方法
	//=================================================================	

  /**
   * 保存
   *
   * @param id	id
   * @param user	用户信息
   */
  @RequestMapping(path = "/save/{id}", method = RequestMethod.POST)
  public String save(@PathVariable Long id, User user) throws Exception
  {
	  return "{\"success\": true}";
  }

  /**
   * Servlet 3.0 文件上传
   *
   * @param name  名称
   * @param file  上传文件
   */
  @RequestMapping(path = "/upload", method = RequestMethod.POST)
  public String upload(@RequestParam("name") String name, @RequestParam("file") Part file)
  {
		// InputStream inputStream = file.getInputStream();
	  // store bytes from uploaded file somewhere

	  return success("");
  }

  /**
   * json序列化测试
   *
   * @param id    id
   */
  @RequestMapping(path = "/user/{id}")
  public User user(@PathVariable Long id) throws Exception
  {
    User u = new User();
    u.setId(id);
    u.setUserName("哇哈哈哈");

	  return u;
  }
}