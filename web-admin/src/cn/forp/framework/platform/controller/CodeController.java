/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.controller;

import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.platform.service.CodeService;
import cn.forp.framework.platform.vo.SystemCode;
import cn.forp.framework.platform.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 通用编码Controller
 *
 * @author  Bruce
 * @version 2016-8-25 18:09
 */
@RestController
public class CodeController extends BaseController
{
	/**
	 * Log4j logger
	 */
	private final static Logger lg = LoggerFactory.getLogger(CodeController.class);
	/**
	 * Service
	 */
	@Autowired
	protected CodeService service = null;

	/**
	 * 分页查询
	 *
	 * @param category  编码类别
	 * @param req       Request请求参数
	 */
	@RequestMapping(path="/platform/setting/code/{category}", method= RequestMethod.GET)
	public Page<SystemCode> search(@PathVariable Long category, HttpServletRequest req) throws Exception
	{
		return service.search(getSessionUser(req), category, getPageSort(req));
	}

	/**
	 * 查询所有列表
	 *
	 * @param category  编码类别
	 * @param req       Request请求参数
	 */
	@RequestMapping(path="/platform/setting/code/{category}", method= RequestMethod.OPTIONS)
	public List<SystemCode> getAll(@PathVariable Long category, HttpServletRequest req) throws Exception
	{
		return service.getAll(getSessionUser(req), category);
	}

	/**
	 * 新建/修改
	 *
	 * @param id	          ID
	 * @param category	    类别ID
	 * @param code	        编码信息
	 * @param req	          Request请求参数
	 */
	@RequestMapping(path="/platform/setting/code/{category}/{id}", method=RequestMethod.POST)
	public String save(@PathVariable Long id, @PathVariable Long category, SystemCode code,
	                   HttpServletRequest req) throws Exception
	{
		code.setId(id);
		code.setCategoryId(category);

		lg.debug("{}：{}[{}]", code.getCategoryName(), code.getName(), id);

		return -1 == id ? create(code, req) : update(code, req);
	}

	/**
	 * 新建
	 *
	 * @param code	编码信息
	 * @param req		Request请求参数
	 */
	private String create(SystemCode code, HttpServletRequest req) throws Exception
	{
		lg.debug("新建{}：{}", code.getCategoryName(), code.getName());

		User su = getSessionUser(req);
		service.create(su, code);
		// 操作日志
		service.writeSystemLog(su, req, "新建" + code.getCategoryName(), code.getCategoryName() + "：" + code.getName(), null);

		return success("OK");
	}

	/**
	 * 修改
	 *
	 * @param code	编码信息
	 * @param req		Request请求参数
	 */
	private String update(SystemCode code, HttpServletRequest req) throws Exception
	{
		lg.debug("修改{}：{}", code.getCategoryName(), code.getName());

		User su = getSessionUser(req);
		service.update(su, code);
		// 操作日志
		service.writeSystemLog(su, req, "修改" + code.getCategoryName(), code.getCategoryName() + "：" + code.getName(), null);

		return success("OK");
	}

	/**
	 * 删除（引用检测靠业务模块的主外键约束来完成）
	 *
	 * @param id	          ID
	 * @param categoryName	类别名称
	 * @param name	        编码名称
	 * @param req		        Request请求参数
	 */
	@RequestMapping(path="/platform/setting/code/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable Long id, @RequestParam String categoryName, @RequestParam String name,
	                     HttpServletRequest req) throws Exception
	{
		service.delete(id);
		// 操作日志
		service.writeSystemLog(getSessionUser(req), req, "删除" + categoryName, categoryName + "：" + name, null);

		return success("OK");
	}
}