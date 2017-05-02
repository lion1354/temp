/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.insurance.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.forp.framework.core.FORP;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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
 * @author  GuoPing
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
	 * @param user    操作人
	 * @param content 账号(手机号码)，真实姓名，身份证号，企业名称, 法人姓名, 组织机构码, 统一社会信用码
	 * @param type    会员类型
	 * @param ps      分页排序信息
	 */
	public Page<Member> search(User user, String content, int type, PageSort ps) throws Exception
	{
		lg.debug("searchContent：{}", content);

		String sql = "select * from Sys_Member_Info where 1=1";
		List<Object> params = new ArrayList<>();

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
	 * @param sessionUser 操作人
	 * @param member      会员信息
	 */
	public long create(User sessionUser, Member member) throws Exception
	{
		// 重名检查
		if (isFieldDuplicate("Sys_Member_Info", "MobilePhone", member.getMobilePhone(), null))
			throw new BusinessException("账号“" + member.getMobilePhone() + "”已注册，请检查您的输入！");

		member.setPassword(DigestUtils.md5Hex(FORP.MD5_SALT_PREFIX + member.getPassword()));
		member.setCreateDate(new Date());
		member.setCreateUserName(sessionUser.getUserName());

		return insertIntoTable(member, null, new String[] {"lastModifyDate", "lastModifyUserId", "lastModifyUserName"});
	}

	/**
	 * 修改
	 *
	 * @param sessionUser 操作人
	 * @param member      会员信息
	 */
	public void update(User sessionUser, Member member) throws Exception
	{
		// 1 重名检查
		if (isFieldDuplicate("Sys_Member_Info", "MobilePhone", member.getMobilePhone(), "ID<>" + member.getId()))
			throw new BusinessException("账号“" + member.getMobilePhone() + "”已注册，请检查您的输入！");

		// 2 检查密码是否改动
		SqlRowSet rs = jdbc.queryForRowSet("select Password from Sys_Member_Info where ID=?", member.getId());
		rs.next();
		String oldPassword = rs.getString("Password");
		if (oldPassword.equals(member.getPassword()))
			member.setPassword(oldPassword);			// 没有修改密码
		else
			member.setPassword(DigestUtils.md5Hex(FORP.MD5_SALT_PREFIX + member.getPassword()));

		member.setLastModifyDate(new Date());
		member.setLastModifyUserID(sessionUser.getId());
		member.setLastModifyUserName(sessionUser.getUserName());

		updateTable(member, null, new String[] {"createDate", "createUserName"});
		lg.info("修改用户：" + member.getId());
	}

	/**
	 * 删除
	 * 
	 * @param id  会员编号
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