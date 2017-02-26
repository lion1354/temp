/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.controller;

import cn.forp.framework.core.FORP;
import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.util.Redis;
import cn.forp.framework.platform.service.SystemService;
import cn.forp.framework.platform.vo.User;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统控制器
 *
 * @author		Bruce
 * @version	2016年6月7日 下午1:06:46
 */
@RestController
public class SystemController extends BaseController
{
    /**
     * Logger
     */
    private static final Logger lg = LoggerFactory.getLogger(SystemController.class);

	/**
	 * 导航至系统菜单路径
	 *
	 * @param menuId    菜单ID
	 * @param userId    用户ID
	 * @param req       Request请求参数
	 */
	@RequestMapping(path="/platform/menu/{menuId}/{userId}", method=RequestMethod.GET)
	public ModelAndView menu(@PathVariable Long menuId, @PathVariable Long userId, HttpServletRequest req) throws Exception
	{
		lg.debug("菜单跳转{}-{}", menuId, userId);

		User user = getSessionUser(req);
		if (null == user)
			return errorView("由于长时间没有操作，您的登录信息已过期，请重新登录！", req);

		// 记录当前模块编号
		user.setCurrentModuleId(menuId);
		// TODO Cache失效后怎么办？
		JedisPool pool = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");
		JSONArray menus = Redis.getJSONArray(FORP.CACHE_USER_PERMISSION + user.getId(), pool);
		if (null != menus)
		{
			// 检查角色权限
			if (menus.contains(menuId.intValue()))
			{
				JSONObject menu = Redis.getJSONObject(FORP.CACHE_MENU + menuId, pool);
				// 页面跳转
				lg.debug("{}[{}]--->{}", menu.getString("name"), menuId, menu.getString("url"));
				return new ModelAndView(menu.getString("url") + (!menu.getString("url").contains("?") ? "?" : "&") + "menuId=" + menuId + "&userId=" + userId);
			}
			else
				return errorView("未授权的系统功能调用，请检查您的操作！", req);
		}
		else
		{
			// 设置对话框按钮动作URL
			req.setAttribute("actionURL", "/");
			return errorView("您还没有登录系统，无法调用系统功能。请先登录！", req);
		}
	}

	/**
	 * 修改个人参数
	 *
	 * @param user          个人参数信息
	 * @param origPassword  原密码
	 * @param newPassword   新密码
	 * @param req           Request请求参数
	 */
	@RequestMapping(path="/platform/profile", method=RequestMethod.POST)
	public String changeProfile(User user, @RequestParam("origPassword") String origPassword, @RequestParam("newPassword") String newPassword,
			HttpServletRequest req) throws Exception
	{
		// Session缓冲的User对象
		User sessionUser = getSessionUser(req);

		// 1 修改
		user.setId(sessionUser.getId());
		user.setDomainId(sessionUser.getDomainId());
		SystemService service = FORP.SPRING_CONTEXT.getBean(SystemService.class);
		service.changeProfile(user, origPassword, newPassword);

		// 2 同步Session缓冲
		sessionUser.setPageLimit(user.getPageLimit());
		sessionUser.setMobilePhone(user.getMobilePhone());
		sessionUser.setEmail(user.getEmail());
		sessionUser.setHeadImg(user.getHeadImg());
		// 同步Cluster Session属性
		req.getSession().setAttribute("__changed__", null);

		// 3 记录日志
		service.writeSystemLog(sessionUser, req, "修改个人参数信息", null, null);
		return success("OK");
	}
}