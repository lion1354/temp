/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.alibaba.media.client.MediaClient;
import com.alibaba.media.client.impl.DefaultMediaClient;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.BusinessException;
import cn.forp.framework.core.FORP;
import cn.forp.framework.core.util.MongoDB;
import cn.forp.framework.core.util.Redis;
import cn.forp.framework.core.vo.Page;
import cn.forp.framework.core.vo.PageSort;
import cn.forp.framework.platform.vo.SimpleObject;
import cn.forp.framework.platform.vo.User;

/**
 * 员工管理Service
 *
 * @author	Bruce
 * @version	2016-08-12 17:03:06
 */
@Service
public class UserService extends BaseService
{
	/**
	 * Log4j logger
	 */
	private static Logger lg = LoggerFactory.getLogger(UserService.class);

	/**
	 * 获取所有省市列表
	 */
	public List<SimpleObject> getAllProvince() throws Exception
	{
		return findByList("select AreaID as ID, AreaName as Name from Sys_Area order by AreaID asc", SimpleObject.class);
	}

	/**
	 * 获取所有区县列表
	 */
	public List<SimpleObject> getAllRegion() throws Exception
	{
		return findByList("select XianID as ID, XianName as Name, AreaID as Remark from Sys_Xian order by AreaID, XianID", SimpleObject.class);
	}
	
	/**
	 * 获取所有省市列表
	 */
	public List<SimpleObject> getProvinceById(Integer id) throws Exception
	{
		List<Object> params = new ArrayList<>();
		params.add(id);
		return findByList("select AreaID as ID, AreaName as Name from Sys_Area where AreaID = ? order by AreaID asc", params.toArray(new Object[0]), SimpleObject.class);
	}

	/**
	 * 获取所有区县列表
	 */
	public List<SimpleObject> getRegionById(Integer id) throws Exception
	{
		List<Object> params = new ArrayList<>();
		params.add(id);
		return findByList("select XianID as ID, XianName as Name, AreaID as Remark from Sys_Xian where XianID = ? order by AreaID, XianID", params.toArray(new Object[0]), SimpleObject.class);
	}

	/**
	 * 分页查询列表
	 * 
	 * @param user		操作人
	 * @param content	模糊搜索内容（用户姓名，登录账号，部门名称，角色名称）
	 * @param state		账号状态
	 * @param ps		  分页排序信息
	 */
	public Page<User> search(User user, String content, int state, PageSort ps) throws Exception
	{
		lg.debug("searchContent：{}", content);

		String sql = "select * from V_Forp_User where FK_DomainID=?";
		List<Object> params = new ArrayList<>();
		params.add(user.getDomainId());

		if (StringUtils.isNotBlank(content))
		{
			sql += " and (UserName like ? or LoginName like ? or DeptName like ? or RoleName like ?)";
			params.add("%" + content + "%");
			params.add("%" + content + "%");
			params.add("%" + content + "%");
			params.add("%" + content + "%");
		}

		if (-1 != state)
		{
			sql += " and State=?";
			params.add(state);
		}
		lg.debug("SQL：{}", sql);

		return findByPage(sql, params.toArray(new Object[0]), User.class, ps);
	}

	/**
	 * 添加
	 *
	 * @param sessionUser   操作人
	 * @param user			    用户信息
	 */
	public long create(User sessionUser, User user) throws Exception
	{
		// 1 重名检查
		if (isFieldDuplicate("Forp_User", "LoginName", user.getLoginName(), "FK_DomainId=" + sessionUser.getDomainId()))
			throw new BusinessException("登陆账号“" + user.getLoginName() + "”已存在，请检查您的输入！");

		// 2 属性设置
		user.setDomainId(sessionUser.getDomainId());
		user.setCreateUserId(sessionUser.getId());
		user.setCreateUserName(sessionUser.getUserName());
		user.setCreateDate(new Timestamp(System.currentTimeMillis()));
		// 加密Password
		user.setPassword(DigestUtils.md5Hex(FORP.MD5_SALT_PREFIX + user.getPassword()));


		// 3 更新新头像
		if (!user.getHeadImg().equals("default.png"))
		{
			saveUserAvatar(user, null);
		}

		// 4 保存
		long userId = insertIntoTable(user, null, new String[]{"type", "pageLimit", "lastModifyDate", "lastModifyUserId", "lastModifyUserName"});
		lg.info("新建系统账号：{}[{}]", user.getUserName(), userId);

		// 5 保存角色列表
		if (StringUtils.isNotBlank(user.getRoleId()))
		{
			String[] roleIds = user.getRoleId().split(",");
			String sql = "insert into Forp_UserRole (FK_UserID, FK_RoleID) Values(?, ?)";
			for (String roleId : roleIds)
			{
				jdbc.update(sql, userId, Long.valueOf(roleId));
			}
			lg.info("角色：{}", user.getRoleId());
		}

		return userId;
	}

	/**
	 * 保存用户头像
	 *
	 * @param user          用户信息
	 * @param origFileId    原文件ID
	 */
	public static void saveUserAvatar(User user, String origFileId) throws Exception
	{
		byte[] avatar = Redis.getByteArray(FORP.CACHE_TEMP_AVATAR + user.getHeadImg(), null);
		// 删除临时Cache
		Redis.delete(FORP.CACHE_TEMP_AVATAR + user.getHeadImg(), null);

		// 磁盘文件
		if ("file".equalsIgnoreCase(FORP.ATTACHMENT_ENGINE))
		{
			// 1 删除原文件
			if (StringUtils.isNotBlank(origFileId) && !origFileId.equals("default.png"))
				FileUtils.deleteQuietly(new File(FORP.WEB_APP_PATH + "/disk-file/user-avatar/" + user.getDomainId() + "/" + origFileId));

			// 2 上传新文件
			IOUtils.write(avatar, new FileOutputStream(FORP.WEB_APP_PATH + "/disk-file/user-avatar/" + user.getDomainId() + "/" + user.getHeadImg()));
		}
		else
			if ("mongodb".equalsIgnoreCase(FORP.ATTACHMENT_ENGINE))
			{
				// MongoDB
				DBObject metaData = new BasicDBObject();
				metaData.put("type", "user-avatar");
				String fileId = MongoDB.saveFile(avatar, origFileId, "user-" + user.getId(), "image/png", metaData);
				user.setHeadImg(fileId);
			}
			else
				if ("alibaba".equalsIgnoreCase(FORP.ATTACHMENT_ENGINE))
				{
					// 顽兔平台
					MediaClient client = new DefaultMediaClient(FORP.ALIBABA_MEDIA_CFG);

					// 1 删除原文件
					if (StringUtils.isNotBlank(origFileId))
					{
						client.deleteFile("user-avatar", origFileId);
					}

					// 2 上传新文件
					ByteArrayInputStream bais = new ByteArrayInputStream(avatar);
					client.upload("user-avatar", user.getHeadImg(), bais, avatar.length);

					// 3 清理资源
					IOUtils.closeQuietly(bais);
				}
				else
					throw new BusinessException("无效的附件存储引擎：" + FORP.ATTACHMENT_ENGINE);
	}

	/**
	 * 修改
	 *
	 * @param sessionUser 操作人
	 * @param user			  用户信息
	 */
	public void update(User sessionUser, User user) throws Exception
	{
		// 1 重名检查
		if (isFieldDuplicate("Forp_User", "LoginName", user.getLoginName(), "FK_DomainId=" + sessionUser.getDomainId() + " and ID<>" + user.getId()))
			throw new BusinessException("员工编号“" + user.getLoginName() + "”已存在，请检查您的输入！");

		user.setDomainId(sessionUser.getDomainId());
		user.setLastModifyUserId(sessionUser.getId());
		user.setLastModifyUserName(sessionUser.getUserName());
		user.setLastModifyDate(new Timestamp(System.currentTimeMillis()));

		// 2 检查密码是否改动
		SqlRowSet rs = jdbc.queryForRowSet("select HeadImg, Password from Forp_User where ID=" + user.getId());
		rs.next();
		String origHeadImg = rs.getString("HeadImg");

		String oldPassword = rs.getString("Password");
		if (oldPassword.equals(user.getPassword()))
			user.setPassword(oldPassword);			// 没有修改密码
		else
			user.setPassword(DigestUtils.md5Hex(FORP.MD5_SALT_PREFIX + user.getPassword()));

		// 3 更新新头像
		if (!origHeadImg.equals(user.getHeadImg()))
		{
			saveUserAvatar(user, origHeadImg);
		}

		// 4 保存
		updateTable(user, null, new String[]{"domainId", "type", "pageLimit", "createDate", "createUserId", "createUserName"});
		lg.info("修改用户：" + user.getId());

		// 5 更新角色列表
		jdbc.update("delete from Forp_UserRole where FK_UserID=?", user.getId());
		String[] roleIds = user.getRoleId().split(",");
		String sql = "insert into Forp_UserRole (FK_UserID, FK_RoleID) Values(?, ?)";
		for (String roleId : roleIds)
		{
			jdbc.update(sql, user.getId(), new Long(roleId));
		}
		lg.info("角色：{}", user.getRoleId());
	}

	/**
	 * 启用/停用
	 * 
	 * @param ids		    员工编号列表
	 * @param newStatus	新状态
	 */
	public void changeStatus(String ids, int newStatus) throws Exception
	{
		lg.info("更新员工{}状态为：{}", ids, newStatus);
		jdbc.update("update Forp_User set State=?, LastModifyDate=? where Id in (" + ids + ")", newStatus, new Date());
	}

	/**
	 * 查询指定角色ID的用户列表
	 * 
	 * @param domainId				    域编号
	 * @param roleId				      角色编号
	 * @param filterAdministrator	是否过滤系统管理员
	 */
	public List<User> searchByRoleId(Long domainId, Long roleId, boolean filterAdministrator) throws Exception
	{
		String sql = "select * from V_Forp_User where FK_DomainID=?";
		List<Object> params = new ArrayList<>();
		params.add(domainId);

		// 过滤管理员
		if (filterAdministrator)
			sql += " and Type=2";

		// 角色
		if (-1 != roleId)
		{
			sql += " and ID in (select FK_UserID from Forp_UserRole where FK_RoleID=?)";
			params.add(roleId);
		}

		sql += " order by UserName";
		lg.debug("SQL：{}", sql);

		return findByList(sql, User.class, params.toArray(new Object[0]));
	}

	/**
	 * 查询指定角色名称的用户列表
	 * 
	 * @param domainId				域编号
	 * @param roleName				角色名称
	 * @param filterAdministrator	是否过滤系统管理员
	 */
	public List<User> searchByRoleName(Long domainId, String roleName, boolean filterAdministrator) throws Exception
	{
		String sql = "select * from V_Forp_User where FK_DomainID=?";
		List<Object> params = new ArrayList<>();
		params.add(domainId);

		// 过滤管理员
		if (filterAdministrator)
			sql += " and Type=2";

		// 角色
		if (StringUtils.isNotBlank(roleName))
		{
			
			sql += " and ID in (select ur.FK_UserID from Forp_UserRole ur, Forp_Role r where ur.FK_RoleID=r.ID and r.Name=?)";
			params.add(roleName);
		}

		sql += " order by UserName";
		lg.debug("SQL：{}", sql);

		return findByList(sql, params.toArray(new Object[0]), User.class);
	}

	/**
	 * 检查细粒度权限
	 * 
	 * @param userId						用户ID
	 * @param menuNodeNo			菜单编码
	 * @param sn							权限编码
	 * 
	 * @return boolean
	 */
	public boolean hasPrivilege(Long userId, String menuNodeNo, String sn)
	{
		String sql = "select Value, StandardValue from V_Forp_RoleMenu_Privilege " + 
				"where MenuNodeNo=? and SN=? and FK_RoleID in (select FK_RoleID from Forp_UserRole where FK_UserID=?)";
		SqlRowSet rs = jdbc.queryForRowSet(sql, menuNodeNo, sn, userId);
		if (rs.next())
		{
			// 细粒度权限值 == 角色赋予值
			if (rs.getString("Value").equals(rs.getString("StandardValue")))
				return true;
		}

		return false;
	}
}