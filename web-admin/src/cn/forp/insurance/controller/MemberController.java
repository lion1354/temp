package cn.forp.insurance.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.forp.framework.core.controller.BaseController;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.platform.vo.User;
import cn.forp.insurance.service.MemberService;
import cn.forp.insurance.vo.Member;

/**
 * 会员管理Controller
 *
 * @author Apple
 */
@RestController
public class MemberController extends BaseController
{
	/**
	 * Logger
	 */
	private static final Logger lg = LoggerFactory.getLogger(MemberController.class);
	/**
	 * Service
	 */
	@Autowired
	protected MemberService service = null;

	/**
	 * 分页查询
	 *
	 * @param contentQuery
	 *            模糊搜索内容（账号(手机号码)，真实姓名，身份证号，企业名称, 法人姓名, 组织机构码, 统一社会信用码）
	 * @param typeQuery
	 *            用户类型
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/member/members", method = RequestMethod.POST)
	public Page<Member> search(@RequestParam("contentQuery") String contentQuery,
			@RequestParam("typeQuery") String typeQuery, HttpServletRequest req) throws Exception
	{
		return service.search(getSessionUser(req), contentQuery,
				(StringUtils.isBlank(typeQuery) ? -1 : Integer.parseInt(typeQuery)), getPageSort(req));
	}

	/**
	 * 新建/修改
	 *
	 * @param id
	 *            用户ID
	 * @param member
	 *            会员信息
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/member/member/{id}", method = RequestMethod.POST)
	public String save(@PathVariable Long id, Member member, HttpServletRequest req) throws Exception
	{
		member.setId(id);
		lg.debug("会员账号：{}[{}]", member.getMobilePhone(), id);

		return -1 == id ? create(member, req) : update(member, req);
	}

	/**
	 * 新建
	 *
	 * @param member
	 *            会员信息
	 * @param req
	 *            Request请求参数
	 */
	private String create(Member member, HttpServletRequest req) throws Exception
	{
		User su = getSessionUser(req);

		service.create(su, member);
		// 操作日志
		service.writeSystemLog(su, req, "添加新会员", "会员姓名：" + member.getUserName() + "\r\n登录账号：" + member.getMobilePhone(),
				null);

		return success("OK");
	}

	/**
	 * 修改
	 *
	 * @param member
	 *            会员信息
	 * @param req
	 *            Request请求参数
	 */
	public String update(Member member, HttpServletRequest req) throws Exception
	{
		User su = getSessionUser(req);

		service.update(su, member);
		// 操作日志
		service.writeSystemLog(su, req, "修改会员信息",
				"会员姓名：" + member.getUserName() + "\r\n登录账号：" + member.getMobilePhone(), null);

		return success("OK");
	}

	/**
	 * 删除
	 * 
	 * @param id
	 *            会员ID
	 * @param name
	 *            会员账号
	 * @param req
	 *            Request请求参数
	 */
	@RequestMapping(path = "/member/member/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable Long id, @RequestParam("mobilePhone") String mobilePhone, HttpServletRequest req)
			throws Exception
	{
		service.delete(id);
		// 操作日志
		service.writeSystemLog(getSessionUser(req), req, "删除会员", "会员账号：" + mobilePhone, null);

		return success("OK");
	}

}