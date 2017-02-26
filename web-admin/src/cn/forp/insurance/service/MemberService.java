/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.insurance.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.BusinessException;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.core.vo.PageSort;
import cn.forp.framework.platform.vo.User;
import cn.forp.insurance.vo.Member;

/**
 * 会员管理Service
 *
 * @author
 */
@Service
public class MemberService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(MemberService.class);

	/**
	 * 分页查询列表
	 * 
	 * @param user
	 *            操作人
	 * @param content
	 *            账号(手机号码)，真实姓名，身份证号，企业名称, 法人姓名, 组织机构码, 统一社会信用码
	 * @param type
	 *            会员类型
	 * @param ps
	 *            分页排序信息
	 */
	public Page<Member> search(User user, String content, int type, PageSort ps) throws Exception
	{
		lg.debug("searchContent：{}", content);

		String sql = "select * from Sys_Member_Info where 1=1";
		List<Object> params = new ArrayList<Object>();

		if (StringUtils.isNotBlank(content))
		{
			sql += " and (MobilePhone like ? or UserName like ? or IdNumber like ? or EnterpriseName like ? or CorporateName like ? or OrganizationCode like ? or CreditCode like ?)";
			params.add("%" + content + "%");
			params.add("%" + content + "%");
			params.add("%" + content + "%");
			params.add("%" + content + "%");
			params.add("%" + content + "%");
			params.add("%" + content + "%");
			params.add("%" + content + "%");
		}

		if (-1 != type)
		{
			sql += " and Type=?";
			params.add(type);
		}
		lg.debug("SQL：{}", sql);

		return findByPage(sql, params.toArray(new Object[0]), Member.class, ps);
	}

	/**
	 * 添加
	 *
	 * @param sessionUser
	 *            操作人
	 * @param user
	 *            会员信息
	 */
	public long create(User sessionUser, Member member) throws Exception
	{
		member.setCreateDate(new Date());
		member.setCreateUserName(sessionUser.getUserName());

		long userId = insertIntoTable(member, null,
				new String[] { "lastModifyDate", "lastModifyUserId", "lastModifyUserName" });

		return userId;
	}

	/**
	 * 修改
	 *
	 * @param sessionUser
	 *            操作人
	 * @param user
	 *            会员信息
	 */
	public void update(User sessionUser, Member member) throws Exception
	{
		member.setLastModifyDate(new Date());
		member.setLastModifyUserID(sessionUser.getId());
		member.setLastModifyUserName(sessionUser.getUserName());

		updateTable(member, null, new String[] { "createDate", "createUserName" });
		lg.info("修改用户：" + member.getId());
	}

	/**
	 * 删除
	 * 
	 * @param id
	 *            会员编号
	 * @throws Exception
	 */
	public void delete(Long id) throws Exception
	{
		try
		{
			jdbc.update("delete from Sys_Member_Info where ID=?", id);
			lg.info("删除会员：{}", id);
		}
		catch (Exception e)
		{
			throw new BusinessException("删除失败：该会员正在使用中！");
		}
	}

}