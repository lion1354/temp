/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.controller;

import cn.forp.framework.core.FORP;
import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.controller.IForm;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.platform.service.RoleService;
import cn.forp.framework.platform.service.UserService;
import cn.forp.framework.platform.vo.Role;
import cn.forp.framework.platform.vo.SimpleObject;
import cn.forp.framework.platform.vo.User;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户管理Controller
 *
 * @author  Bruce
 * @version 2016-8-12 16:55
 */
@RestController
public class UserController extends BaseController
{
	/**
	 * Logger
	 */
	private static final Logger lg = LoggerFactory.getLogger(UserController.class);
	/**
	 * Service
	 */
	@Autowired
	protected UserService service = null;

	/**
	 * 获取初始化数据
	 *
	 * @param req   Request请求参数
	 */
	@RequestMapping(path="/platform/user/init")
	public JSONObject init(HttpServletRequest req) throws Exception
	{
		JSONObject json = new JSONObject();

		// 角色
		List<Role> roles = FORP.SPRING_CONTEXT.getBean(RoleService.class).search(getSessionUser(req));
		// 省市
		List<SimpleObject> provinces = service.getAllProvince();
		// 区县
		List<SimpleObject> regions = service.getAllRegion();

		json.put("roles", roles);
		json.put("provinces", provinces);
		json.put("regions", regions);

		return json;
	}

	/**
	 * 分页查询
	 *
	 * @param content   模糊搜索内容（用户姓名，登录账号，部门名称，角色名称）
	 * @param state     状态
	 * @param req       Request请求参数
	 */
	@RequestMapping(path="/platform/user", method=RequestMethod.POST)
	public Page<User> search(@RequestParam("content") String content, @RequestParam("state") String state, HttpServletRequest req) throws Exception
	{
		return service.search(getSessionUser(req), content, (StringUtils.isBlank(state) ? -1 : Integer.parseInt(state)), getPageSort(req));
	}

	/**
	 * 新建/修改
	 *
	 * @param id	    用户ID
	 * @param user    用户信息
	 * @param deptId	部门信息
	 * @param req	    Request请求参数
	 */
	@RequestMapping(path="/platform/user/{id}", method=RequestMethod.POST)
	public String save(@PathVariable Long id, User user, @RequestParam("deptId") String deptId, HttpServletRequest req) throws Exception
	{
		user.setId(id);
		user.setDeptId(Long.valueOf(deptId.split(",")[0]));
		lg.debug("用户：{}[{}]", user.getUserName(), id);

		return -1 == id ? create(user, req) : update(user, req);
	}

	/**
	 * 新建
	 *
	 * @param user	用户信息
	 * @param req   Request请求参数
	 */
	private String create(User user, HttpServletRequest req) throws Exception
	{
		User su = getSessionUser(req);

		service.create(su, user);
		// 操作日志
		service.writeSystemLog(su, req, "添加新员工", "员工姓名：" + user.getUserName() + "\r\n登录账号：" + user.getLoginName(), null);

		return success("OK");
	}

	/**
	 * 修改
	 *
	 * @param user	用户信息
	 * @param req	Request请求参数
	 */
	public String update(User user, HttpServletRequest req) throws Exception
	{
		User su = getSessionUser(req);

		service.update(su, user);
		// 操作日志
		service.writeSystemLog(su, req, "修改员工信息", "员工姓名：" + user.getUserName() + "\r\n登录账号：" + user.getLoginName(), null);

		return success("OK");
	}

	/**
	 * 启用/停用
	 *
	 * @param userIds	员工编号列表
	 * @param newStatus 新状态
	 * @param req	    Request请求参数
	 */
	@RequestMapping(path="/platform/user/status")
	public String changeStatus(@RequestParam("ids") String userIds, @RequestParam("newStatus") int newStatus, HttpServletRequest req) throws Exception
	{
		lg.debug("User ids：{}", userIds);

		service.changeStatus(userIds, newStatus);
		// 操作日志
		service.writeSystemLog(getSessionUser(req), req, (1 == newStatus ? "启用" : "停用") + "员工账号", "员工ID：" + userIds, null);

		return success("OK");
	}

	/**
	 * 查询指定角色的用户列表
	 *
	 * @param req   Request请求参数
	 */
	@RequestMapping(path="/platform/user/find-by-role-id/{roleId}")
	public List<User> searchByRoleId(HttpServletRequest req) throws Exception
	{
		IForm form = getFormWrapper(req);

		Long domainId;
		// 没有传递domainId参数时，默认获取User对象的属性
		if (StringUtils.isBlank(form.get("domainId")))
			domainId = ((User) getSessionUser(req)).getDomainId();
		else
			// 传递domainId参数时，直接取传递的参数值（会员网站登录后Session中不是User对象，会报ClassCast异常）
			domainId = form.getLong("domainId");

		// 是否过滤系统管理员
		boolean filterAdministrator = true;
		if (StringUtils.isNotBlank(form.get("filterAdministrator")) && "false".equalsIgnoreCase(form.get("filterAdministrator")))
			filterAdministrator = false;

		return service.searchByRoleId(domainId, form.getLong("roleId"), filterAdministrator);
	}

	/**
	 * 查询指定角色名称的用户列表
	 *
	 * @param req   Request请求参数
	 */
	@RequestMapping(path="/platform/user/find-by-role-name")
	public List<User> searchByRoleName(HttpServletRequest req) throws Exception
	{
		IForm form = getFormWrapper(req);

		Long domainId;
		// 没有传递domainId参数时，默认获取User对象的属性
		if (StringUtils.isBlank(form.get("domainId")))
			domainId = ((User) getSessionUser(req)).getDomainId();
		else
			// 传递domainId参数时，直接取传递的参数值（会员网站登录后Session中不是User对象，会报ClassCast异常）
			domainId = form.getLong("domainId");

		// 是否过滤系统管理员
		boolean filterAdministrator = true;
		if (StringUtils.isNotBlank(form.get("filterAdministrator")) && "false".equalsIgnoreCase(form.get("filterAdministrator")))
			filterAdministrator = false;

		return service.searchByRoleName(domainId, form.get("roleName"), filterAdministrator);
	}
}