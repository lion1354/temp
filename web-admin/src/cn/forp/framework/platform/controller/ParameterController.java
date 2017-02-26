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
import cn.forp.framework.platform.service.ParameterService;
import cn.forp.framework.platform.vo.Parameter;
import cn.forp.framework.platform.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统参数Controller
 *
 * @author  Bruce
 * @version 2016-8-18 11:02
 */
@RestController
public class ParameterController extends BaseController
{
	/**
	 * Log4j logger
	 */
	private final static Logger lg = LoggerFactory.getLogger(ParameterController.class);
	/**
	 * Service
	 */
	@Autowired
	protected ParameterService service = null;

	/**
	 * 列表查询
	 *
	 * @param req	Request请求参数
	 */
	@RequestMapping(path="/platform/setting/parameter", method= RequestMethod.GET)
	public List<Parameter> search(HttpServletRequest req) throws Exception
	{
		return service.search(getSessionUser(req));
	}

	/**
	 * 保存并应用
	 *
	 * @param id  参数ID
	 * @param req	Request请求参数
	 */
	@RequestMapping(path="/platform/setting/parameter/{id}", method= RequestMethod.POST)
	public String save(@PathVariable Long id, HttpServletRequest req) throws Exception
	{
		lg.debug("修改系统参数");
		User user = getSessionUser(req);
		IForm form = getFormWrapper(req);

		String origValue = Redis.getHashMap(FORP.CACHE_DOMAIN_PROFILE + user.getDomainId(), null, form.get("sn")).get(0);
		service.saveAndApply(user, id, form.get("sn"), form.get("value"));
		// 操作日志
		service.writeSystemLog(user, req, "修改系统参数", "参数名称：" + form.get("name") + "\r\n参数值：" +
				form.get("value") + " [原值" + origValue + "]", null);

		return success("OK");
	}
}