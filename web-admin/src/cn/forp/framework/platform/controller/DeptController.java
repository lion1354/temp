/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.controller;

import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.platform.service.DeptService;
import cn.forp.framework.platform.vo.Department;
import cn.forp.framework.platform.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 操作日志Controller
 *
 * @author  Bruce
 * @version 2016年7月28日 下午3:19:31
 */
@RestController
public class DeptController extends BaseController
{
	/**
	 * Logger
	 */
	private static final Logger lg = LoggerFactory.getLogger(DeptController.class);
	/**
	 * Service
	 */
	@Autowired
	protected DeptService service = null;

	/**
	 * 查询当前部门节点下的直属部门列表
	 *
	 * @param extTreeNodeId 树节点ID
	 * @param req	        Request请求参数
	 */
//	@RequestMapping("/platform/dept")
//	public List<Department> index(@RequestParam("extTreeNodeId") String extTreeNodeId, HttpServletRequest req) throws Exception
//	{
//		return service.search(getSessionUser(req), extTreeNodeId);
//	}
	
	/**
	 *一起读取所有部门节点
	 *
	 * @param extTreeNodeId 树节点ID
	 * @param req	        Request请求参数
	 */
	@RequestMapping("/platform/dept")
	public List<Department> newIndex(@RequestParam("extTreeNodeId") String extTreeNodeId, HttpServletRequest req) throws Exception
	{
		return service.search(getSessionUser(req), extTreeNodeId);
	}
	
	

	/**
	 * 新建/修改
	 *
	 * @param id	部门ID
	 * @param dept	部门信息
	 * @param req	Request请求参数
	 */
	@RequestMapping(path="/platform/dept/{id}", method= RequestMethod.POST)
	public String save(@PathVariable Long id, Department dept, HttpServletRequest req) throws Exception
	{
		dept.setId(id);
		lg.debug("部门：{}[{}]", dept.getName(), id);

		return -1 == id ? create(dept, req) : update(dept, req);
	}

	/**
	 * 新建
	 *
	 * @param dept  部门信息
	 * @param req   Request请求参数
	 * @return String
	 */
	private String create(Department dept, HttpServletRequest req) throws Exception
	{
		User su = getSessionUser(req);

		service.create(dept, su);
		// 操作日志
		service.writeSystemLog(su, req, "新建部门", "部门名称：" + dept.getName(), null);

		return success("OK");
	}

	/**
	 * 修改
	 *
	 * @param dept  部门信息
	 * @param req   Request请求参数
	 */
	private String update(Department dept, HttpServletRequest req) throws Exception
	{
		User su = getSessionUser(req);

		service.update(dept, su);
		// 操作日志
		service.writeSystemLog(su, req, "修改部门", "部门名称：" + dept.getName(), null);

		return success("OK");
	}

	/**
	 * 删除
	 */
	@RequestMapping(path="/platform/dept/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable Long id, @RequestParam("name") String name, @RequestParam("nodeNo") String nodeNo,
	                     HttpServletRequest req) throws Exception
	{
		User user = getSessionUser(req);

		service.delete(id, nodeNo, user);
		// 操作日志
		service.writeSystemLog(user, req, "删除部门", "部门名称：" + name, null);

		return success("OK");
	}
}