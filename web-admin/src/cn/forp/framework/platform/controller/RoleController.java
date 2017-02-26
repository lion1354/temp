/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.controller;

import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.controller.IForm;
import cn.forp.framework.platform.service.RoleService;
import cn.forp.framework.platform.vo.MenuJQTreeNode;
import cn.forp.framework.platform.vo.MenuTreeNode;
import cn.forp.framework.platform.vo.Role;
import cn.forp.framework.platform.vo.User;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 角色控制器
 *
 * @author		Bruce
 * @version	2016年6月7日 下午5:52:53
 */
@RestController
public class RoleController extends BaseController
{
  /**
   * Logger
   */
  private static final Logger lg = LoggerFactory.getLogger(RoleController.class);
  /**
   * 角色Service
   */
  @Autowired
  protected RoleService service = null;

	/**
	 * 列表查询
	 * 
	 * @param req		Request请求参数
	 */
  @RequestMapping(path="/platform/role", method=RequestMethod.POST)
	public List<Role> search(HttpServletRequest req) throws Exception
	{
		return service.search(getSessionUser(req));
	}

	/**
	 * 新建/修改
	 * 
	 * @param id	  角色ID
	 * @param role	角色信息
	 * @param req	  Request请求参数
	 */
	@RequestMapping(path="/platform/role/{id}", method=RequestMethod.POST)
	public String save(@PathVariable Long id, Role role, HttpServletRequest req) throws Exception
	{
		role.setId(id);
		lg.debug("角色：{}[{}]", role.getName(), id);

		return -1 == id ? create(role, req) : update(role, req);
	}

	/**
	 * 新建
	 * 
	 * @param role	角色信息
	 * @param req		Request请求参数
	 */
	private String create(Role role, HttpServletRequest req) throws Exception
	{
		lg.debug("新建角色：{}", role.getName());

		User su = getSessionUser(req);
		long id = service.create(su, role);
		// 操作日志
		service.writeSystemLog(su, req, "新建角色", "角色名称：" + role.getName(), null);

		return "{\"success\": true, \"id\": " + id + "}";
	}

	/**
	 * 修改
	 * 
	 * @param role	角色信息
	 * @param req		Request请求参数
	 */
	private String update(Role role, HttpServletRequest req) throws Exception
	{
		lg.debug("修改角色：{}", role.getName());

		User su = getSessionUser(req);
		service.update(su, role);
		// 操作日志
		service.writeSystemLog(su, req, "修改角色", "角色名称：" + role.getName(), null);

		return success("OK");
	}

	/**
	 * 删除
	 * 
	 * @param id		角色ID
	 * @param name	角色名称
	 * @param req		Request请求参数
	 */
	@RequestMapping(path="/platform/role/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable Long id, @RequestParam("name") String name, HttpServletRequest req) throws Exception
	{
		service.delete(id);
		// 操作日志
		service.writeSystemLog(getSessionUser(req), req, "删除角色", "角色名称：" + name, null);

		return success("OK");
	}

	//=================================================================
	//		角色权限
	//=================================================================	

	/**
	 * 查询指定角色的权限
	 */
	@RequestMapping(path="/platform/role/permission")
	public List<MenuTreeNode> getRolePermission(@RequestParam("roleId") Long roleId, HttpServletRequest req) throws Exception
	{
		return service.getRolePermission(getSessionUser(req), roleId);
	}
	
	@RequestMapping(path="/platform/role/jqTreePermission")
	public List<MenuJQTreeNode> getoleJQTreePermission(@RequestParam("roleId") Long roleId, HttpServletRequest req) throws Exception
	{
		//return null;
		return service.getJQTreeRolePermission(getSessionUser(req), roleId);
	}
	/**
	 * 查询指定角色的权限（修改用 - 自己的权限复选框选中）
	 */
	@RequestMapping(path="/platform/role/permission/modify", produces = "text/json; charset=utf-8")
	public String getRolePermissionByModify(@RequestParam("roleId") Long roleId, HttpServletRequest req) throws Exception
	{
		List<MenuTreeNode> rows = service.getRolePermissionByModify(getSessionUser(req), roleId);
		return JSON.toJSONString(rows).replaceAll("\"children\":\\[\\],", "");
	}

	/**
	 * 修改角色权限
	 */
	@RequestMapping(path="/platform/role/permission/{id}", method=RequestMethod.POST)
	public String saveRolePermission(@PathVariable Long id, HttpServletRequest req) throws Exception
	{
		IForm form = getFormWrapper(req);
		User user = getSessionUser(req);
		service.saveRolePermission(id, form.get("menus"), form.get("menuPrivileges"), user);
		// 操作日志
		service.writeSystemLog(user, req, "修改角色权限", "角色名称：" + form.get("name"), null);

		return success("OK");
	}
}