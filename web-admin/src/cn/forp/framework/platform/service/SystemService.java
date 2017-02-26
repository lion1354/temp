/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.BusinessException;
import cn.forp.framework.core.FORP;
import cn.forp.framework.platform.vo.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统管理Service
 *
 * @author		Bruce
 * @version	2016-4-1 15:55:23
 */
@Service
public class SystemService extends BaseService
{
  /**
   * Logger
   */
  private static final Logger lg = LoggerFactory.getLogger(SystemService.class);

	/**
	 * 登陆系统
	 * 
	 * @param loginName		登录名称
	 * @param password		密码
	 */
	public User logon(String loginName, String password) throws Exception
	{
		User user = null;
		lg.debug("用户尝试登陆：{}", loginName);

		// 1 账号有效性
		String sql = "select u.*, dept.Name As DeptName, dept.NodeNo As DeptNodeNo from Forp_User u, Forp_Dept dept " +
						"where u.FK_DeptID=dept.ID and u.FK_DomainID=? and u.LoginName=? and u.Password=? and u.State=1";
		List<User> rows = findByList(sql, User.class, 1, loginName, password);

		if (1 == rows.size())
		{
			user = rows.get(0);
			user.setRoleId("");
			user.setRoleName("");

			// 获取角色列表
			int roleLevel = 100;
			sql = "select ur.FK_RoleID, r.Name, r.Level from Forp_UserRole ur, Forp_Role r where ur.FK_RoleID=r.ID and ur.FK_UserID=?";
			SqlRowSet rs = jdbc.queryForRowSet(sql, user.getId());
			while (rs.next())
			{
				roleLevel = Math.min(roleLevel, rs.getInt("Level"));
				user.setRoleId(user.getRoleId() + ("".equals(user.getRoleId()) ? "" : ",") + rs.getString("FK_RoleID"));
				user.setRoleName(user.getRoleName() + ("".equals(user.getRoleName()) ? "" : ",") + rs.getString("Name"));
			}
			user.setRoleLevel(roleLevel);

			lg.debug("用户角色：{}/{}/{}", user.getRoleLevel(), user.getRoleId(), user.getRoleName());
		}

		if (null != user)
		{
			// 加载用户权限Cache
			loadUserPopedomCache(new Long[]{user.getId()});
			// 加载域全局参数Cache
			loadDomainProfileCache(user.getDomainId());
			// 加载域菜单Cache
			loadMenuCache(user.getDomainId());
		}

		return user;
	}

	/**
	 * 修改个人参数
	 * 
	 * @param user          个人参数
	 * @param origPassword  原密码
	 * @param newPassword   新密码
	 */
	public void changeProfile(User user, String origPassword, String newPassword) throws Exception
	{
		// 用户原信息：密码，头像
		SqlRowSet rs = jdbc.queryForRowSet("select HeadImg, Password, FK_DomainID from Forp_User where ID=?", user.getId());
		rs.next();
		String origPasswd = rs.getString("Password");
		String origHeadImg = rs.getString("HeadImg");
		// String domainId = rs.getString("FK_DomainID");

		// 保存头像
		if (!origHeadImg.equals(user.getHeadImg()))
		{
			UserService.saveUserAvatar(user, origHeadImg);
		}

		String sql = "update Forp_User set PageLimit=?";
		List<Object> params = new ArrayList<>();
		params.add(user.getPageLimit());

		// 密码
		if (StringUtils.isNotBlank(origPassword))
		{
			String passwd = DigestUtils.md5Hex(FORP.MD5_SALT_PREFIX + origPassword);
			if  (passwd.equals(origPasswd))
			{
				sql += ", Password=?";
				params.add(DigestUtils.md5Hex(FORP.MD5_SALT_PREFIX + newPassword));
				lg.info("修改个人密码！");
			}
			else
				throw new BusinessException("无效的原密码，请检查您的输入！");
		}

		// 手机号码
		if (StringUtils.isNotBlank(user.getMobilePhone()))
		{
			sql += ", MobilePhone=?";
			params.add(user.getMobilePhone());
		}

		// Email
		if (StringUtils.isNotBlank(user.getEmail()))
		{
			sql += ", EMail=?";
			params.add(user.getEmail());
		}

		// 头像
		if (!origHeadImg.equals(user.getHeadImg()))
		{
			sql += ", HeadImg=?";
			params.add(user.getHeadImg());
		}

		sql += " where ID=?";
		params.add(user.getId());
		lg.debug("SQL:{}", sql);
		jdbc.update(sql, params.toArray(new Object[0]));
	}

//	//=================================================================
//	//		警告日志
//	//=================================================================
//
//	/**
//	 * 添加告警日志内容
//	 *
//	 * @param category	日志类别：1 - 邮件；2 - 短信。
//	 * @param level		级别：1 - 警告；2 - 错误。
//	 * @param content   日志内容
//	 */
//	public void addWarningLog(int category, int level, String content)
//	{
//		jdbc.update("insert into Forp_WarnLog (Category, LogLevel, Content, CreateDate) values(?, ?, ?, ?)",
//				category, level, content, new Date());
//	}
}