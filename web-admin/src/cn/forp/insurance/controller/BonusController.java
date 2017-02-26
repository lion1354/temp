package cn.forp.insurance.controller;

import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.platform.service.UserService;
import cn.forp.framework.platform.vo.SimpleObject;
import cn.forp.framework.platform.vo.User;
import cn.forp.insurance.service.BonusService;

/**
 * 积分抵扣管理Controller
 *
 * @author Apple
 * @version 2016-8-12 16:55
 */
@RestController
public class BonusController extends BaseController
{
	/**
	 * Logger
	 */
	private static final Logger lg = LoggerFactory.getLogger(BonusController.class);
	/**
	 * Service
	 */
	@Autowired
	protected UserService userService = null;
	@Autowired
	protected BonusService bonusService = null;

	@RequestMapping(path = "/bonus/get-region")
	public JSONArray getRegion(HttpServletRequest req) throws Exception
	{
		JSONArray array = new JSONArray();

		// 省市
		List<SimpleObject> provinces = bonusService.getAllProvince();
		// 区县
		List<SimpleObject> regions = bonusService.getAllRegion();
		if (provinces != null && provinces.size() > 0)
		{
			DecimalFormat df = new DecimalFormat("000");
			for (int i = 0; i < provinces.size(); i++)
			{
				SimpleObject ob = provinces.get(i);
				JSONObject json = new JSONObject();
				json.put("id", ob.getId());
				json.put("leaf", true);
				json.put("name", ob.getName());
				json.put("nodeNo", "0".equals(ob.getId()) ? "001" : "001" + df.format(i));
				json.put("parentNodeNo", "0".equals(ob.getId()) ? "-1" : "001");
				json.put("bonus", "");
				array.add(json);
			}
		}

		if (regions != null && regions.size() > 0)
		{
			DecimalFormat df = new DecimalFormat("000");
			for (int i = 0; i < regions.size(); i++)
			{
				SimpleObject ob = regions.get(i);
				JSONObject json = new JSONObject();
				json.put("id", ob.getId());
				json.put("leaf", false);
				json.put("name", ob.getName());
				json.put("nodeNo",
						"001" + df.format(Integer.parseInt(ob.getRemark())) + df.format(Integer.parseInt(ob.getId())));
				json.put("parentNodeNo", "001" + df.format(Integer.parseInt(ob.getRemark())));
				json.put("bonus", ob.getValue());
				array.add(json);
			}
		}
		return array;
	}

	/**
	 * 修改
	 *
	 * @param id
	 *            区县ID
	 * @param bunus
	 *            积分抵扣金额比例
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/bonus/bonus/{id}", method = RequestMethod.POST)
	public String save(@PathVariable Long id, HttpServletRequest req) throws Exception
	{
		User user = getSessionUser(req);
		String bonus = req.getParameter("bonus");
		bonusService.updateBonus(id, bonus);
		// 操作日志
		userService.writeSystemLog(user, req, "修改积分抵扣金额比例",
				"操作人姓名：" + user.getUserName() + "\r\n操作账号：" + user.getLoginName(), null);
		return success("OK");
	}
}
