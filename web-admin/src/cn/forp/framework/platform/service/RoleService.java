/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.BusinessException;
import cn.forp.framework.core.FORP;
import cn.forp.framework.core.util.Redis;
import cn.forp.framework.core.vo.Menu;
import cn.forp.framework.core.vo.MenuPrivilege;
import cn.forp.framework.platform.vo.MenuJQTreeNode;
import cn.forp.framework.platform.vo.MenuTreeNode;
import cn.forp.framework.platform.vo.Role;
import cn.forp.framework.platform.vo.User;
import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import java.sql.Timestamp;
import java.util.*;

/**
 * 角色管理Service
 *
 * @author	Bruce
 * @version	2012-05-04 11:55:18
 */
@Service
public class RoleService extends BaseService
{
  /**
   * Logger
   */
  private static final Logger lg = LoggerFactory.getLogger(RoleService.class);

	/**
	 * 查询
	 * 
	 * @param user  操作人
	 * @throws Exception
	 */
	public List<Role> search(User user) throws Exception
	{
		return findByList("select * from Forp_Role where FK_DomainID=? order by ID asc", Role.class, user.getDomainId());
	}

	/**
	 * 添加
	 * 
	 * @param user	操作人
	 * @param role	角色
	 * @throws Exception
	 */
	public long create(User user, Role role) throws Exception
	{
		// 1 重名检查
		if (isFieldDuplicate("Forp_Role", "Name", role.getName(), "FK_DomainId=" + user.getDomainId()))
			throw new BusinessException("角色“" + role.getName() + "”已存在，请检查您的输入！");

		// 2 属性设置
		role.setDomainId(user.getDomainId());
		role.setCreateUserId(user.getId());
		role.setCreateUserName(user.getUserName());
		role.setCreateDate(new Timestamp(System.currentTimeMillis()));
		
		// 3 持久化
		long id = insertIntoTable(role, null, new String[]{"lastModifyDate", "lastModifyUserId", "lastModifyUserName"});
		return id;
	}

	/**
	 * 修改
	 * 
	 * @param user			操作人
	 * @param role			角色
	 * 
	 * @throws Exception
	 */
	public void update(User user, Role role) throws Exception
	{
		// 1 重名检查
		if (isFieldDuplicate("Forp_Role", "Name", role.getName(), "FK_DomainId=" + user.getDomainId() + " and ID<>" + role.getId()))
			throw new BusinessException("角色“" + role.getName() + "”已存在，请检查您的输入！");

		// 2 属性设置
		role.setDomainId(user.getDomainId());
		role.setLastModifyUserId(user.getId());
		role.setLastModifyUserName(user.getUserName());
		role.setLastModifyDate(new Timestamp(System.currentTimeMillis()));

		// 3 持久化		
		updateTable(role, new String[]{"name", "description", "remark", "lastModifyDate", "lastModifyUserId", "lastModifyUserName"});
	}

	/**
	 * 删除
	 * 
	 * @param id						角色编号
	 * @throws Exception
	 */
	public void delete(Long id) throws Exception
	{
		try
		{
			jdbc.update("delete from Forp_Role where ID=?", id);
			lg.info("删除角色：{}", id);

			// 同步用户权限缓冲
			synchronizeUserPopedomCache(id);			
		}
		catch (Exception e)
		{
			throw new BusinessException("删除失败：该角色正在使用中！");
		}
	}

	//=================================================================
	//		角色权限
	//=================================================================	

	/**
	 * 同步该角色的用户权限缓冲
	 * 
	 * @param roleId
	 */
	private void synchronizeUserPopedomCache(Long roleId) throws Exception
	{
		List<Long> users = new ArrayList<Long>();
		SqlRowSet rs = jdbc.queryForRowSet("select * from Forp_UserRole where FK_RoleID=?", new Object[]{roleId});
		while (rs.next())
		{
			users.add(rs.getLong("FK_UserID"));
		}

		loadUserPopedomCache(users.toArray(new Long[0]));
	}
	
	public List<MenuJQTreeNode> getJQTreeRolePermission(User user, Long roleId) throws Exception
	{
		List<MenuJQTreeNode> rows = new ArrayList<MenuJQTreeNode>();
	
		int count = 0;
		String sql = null;
		SqlRowSet rs = null;
		loadMenuPrivligesCache(user.getDomainId());
		
		
		
		
		Map<Long, Map<String, Integer>> mCache = new HashMap<Long, Map<String, Integer>>();
		// 单个菜单权限Cache：key - 权限SN，value - 权限值
		Map<String, Integer> mpCache = null;
		sql = "select FK_MenuID, SN, Value from V_Forp_RoleMenu_Privilege where FK_RoleID=?";
		rs = jdbc.queryForRowSet(sql, new Object[]{roleId});
		count = 0;
		while (rs.next())
		{
			if (mCache.containsKey(rs.getLong("FK_MenuID")))
				mpCache = mCache.get(rs.getLong("FK_MenuID"));
			else
			{
				mpCache = new HashMap<String, Integer>();
				mCache.put(rs.getLong("FK_MenuID"), mpCache);
			}

			mpCache.put(rs.getString("SN"), rs.getInt("Value"));
			count++;
		}
		lg.info("加载角色模块细粒度权限缓存：{}", count);
		// 3 查询该角色的权限列表
	
		MenuJQTreeNode node;
		Map<String, MenuJQTreeNode> menuTreeCache = new HashMap<String, MenuJQTreeNode>();
		sql = "select FK_MenuID, MenuName, NodeNo, ParentNodeNo, MenuIcon from V_Forp_RoleMenu where FK_RoleID=? order by NodeNo asc";
		rs = jdbc.queryForRowSet(sql, new Object[]{roleId});
		// 按照Jquery TreeTable结构组织数据结构
		while (rs.next())
		{
			//读取节点
			node = new MenuJQTreeNode();
			node.setId(rs.getString("NodeNo"));
			node.setName(rs.getString("MenuIcon")+"&nbsp;&nbsp;"+rs.getString("MenuName"));
			node.setpId(rs.getString("ParentNodeNo"));
			menuTreeCache.put(node.getId(), node);
			rows.add(node);			
		}

		return  rows;
	}
	
	
	
	
	/**
	 * 查询指定角色的权限
	 * 
	 * @param user			操作人
	 * @param roleId		角色编号
	 * @throws Exception
	 */
	public List<MenuTreeNode> getRolePermission(User user, Long roleId) throws Exception
	{
		List<MenuTreeNode> rows = new ArrayList<MenuTreeNode>();

		JedisPool jp = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");
		int count = 0;
		String sql = null;
		SqlRowSet rs = null;

		// 1 所有模块细粒度权限
		loadMenuPrivligesCache(user.getDomainId());

		// 2 缓冲当前角色已分配的细粒度权限列表
		// 菜单已分配的细粒度权限Cache：key - 菜单ID，value - 细粒度权限列表（key - 权限SN，value - 权限值）
		Map<Long, Map<String, Integer>> mCache = new HashMap<Long, Map<String, Integer>>();
		// 单个菜单权限Cache：key - 权限SN，value - 权限值
		Map<String, Integer> mpCache = null;
		sql = "select FK_MenuID, SN, Value from V_Forp_RoleMenu_Privilege where FK_RoleID=?";
		rs = jdbc.queryForRowSet(sql, new Object[]{roleId});
		count = 0;
		while (rs.next())
		{
			if (mCache.containsKey(rs.getLong("FK_MenuID")))
				mpCache = mCache.get(rs.getLong("FK_MenuID"));
			else
			{
				mpCache = new HashMap<String, Integer>();
				mCache.put(rs.getLong("FK_MenuID"), mpCache);
			}

			mpCache.put(rs.getString("SN"), rs.getInt("Value"));
			count++;
		}
		lg.info("加载角色模块细粒度权限缓存：{}", count);

		// 3 查询该角色的权限列表
		MenuPrivilege rmp;
		List<MenuPrivilege> menuPrivileges;
		MenuTreeNode node, parentNode;
		Map<String, MenuTreeNode> menuTreeCache = new HashMap<String, MenuTreeNode>();
		sql = "select FK_MenuID, MenuName, NodeNo, ParentNodeNo, MenuIcon from V_Forp_RoleMenu where FK_RoleID=? order by NodeNo asc";
		rs = jdbc.queryForRowSet(sql, new Object[]{roleId});
		// 按照Extjs Tree结构组织数据结构
		while (rs.next())
		{
			node = new MenuTreeNode();
			node.setId(rs.getString("FK_MenuID") + "," + rs.getString("NodeNo") + "," + rs.getString("ParentNodeNo"));
			node.setText(rs.getString("MenuName"));
			node.setIcon(rs.getString("MenuIcon"));

			// 3.1 缓冲该节点，方便子节点追加
			menuTreeCache.put(rs.getString("NodeNo"), node);

			// 3.2 一级节点
			if ("-1".equals(rs.getString("ParentNodeNo")))
				rows.add(node);
			else
			{
				// 查找父节点，加为子节点
				parentNode = menuTreeCache.get(rs.getString("ParentNodeNo"));
				if (null != parentNode)
				{
					parentNode.setLeaf(false);
					parentNode.getChildren().add(node);					
				}
			}

			// 3.3 细粒度权限
			if (Redis.isCached(FORP.CACHE_MENU_PRIVILEGE + rs.getString("FK_MenuID"), jp))
			{
				// 菜单标准细粒度权限
				menuPrivileges = JSON.parseArray(Redis.getString(FORP.CACHE_MENU_PRIVILEGE + rs.getString("FK_MenuID"), jp), MenuPrivilege.class);
				for (MenuPrivilege p : menuPrivileges)
				{
					rmp = (MenuPrivilege) BeanUtils.cloneBean(p);
					// 角色菜单已定义的细粒度权限列表（单个菜单中使用权限SN替代ID进行查找）
					if (mCache.containsKey(rs.getLong("FK_MenuID")) && mCache.get(rs.getLong("FK_MenuID")).containsKey(p.getSn()))
					{
						rmp.setValue(mCache.get(rs.getLong("FK_MenuID")).get(p.getSn()));
						lg.debug("细粒度权限：{}-->{}", p.getValue(), rmp.getValue());
					}

					// 逐个添加细粒度权限
					node.getPrivileges().add(rmp);
				}
			}
		}

		return rows;
	}

	/**
	 * 查询指定角色的权限
	 * 
	 * @param user			操作人
	 * @param roleId		角色编号
	 * @throws Exception
	 */
	public List<MenuTreeNode> getRolePermissionByModify(User user, long roleId) throws Exception
	{
		List<MenuTreeNode> rows = new ArrayList<MenuTreeNode>();

		JedisPool jp = (JedisPool) FORP.SPRING_CONTEXT.getBean("defaultPool");
		// 1 查询该角色已有的权限列表
		Map<Long, Boolean> myMenuCache = new HashMap<Long, Boolean>();
		SqlRowSet rs = jdbc.queryForRowSet("select * from Forp_RoleMenu where FK_RoleID=?", new Object[]{roleId});
		while (rs.next())
		{
			myMenuCache.put(rs.getLong("FK_MenuID"), true);
		}

		// 2 查询当前域所有菜单列表
		List<Menu> menus = loadMenuCache(user.getDomainId());
		// 按照NodeNo升序排列
		Collections.sort(menus, new BeanComparator<Menu>("nodeNo"));

		// 3 所有模块细粒度权限
		// 3.1 缓冲当前角色已分配的细粒度权限列表
		// 菜单已分配的细粒度权限Cache：key - 菜单ID，value - 细粒度权限列表（key - 权限SN，value - 权限值）
		Map<Long, Map<String, Integer>> mCache = new HashMap<Long, Map<String, Integer>>();
		// 单个菜单权限Cache：key - 权限SN，value - 权限值
		Map<String, Integer> mpCache = null;
		String sql = "select FK_MenuID, SN, Value from V_Forp_RoleMenu_Privilege where FK_RoleID=?";
		rs = jdbc.queryForRowSet(sql, new Object[]{roleId});
		int count = 0;
		while (rs.next())
		{
			if (mCache.containsKey(rs.getLong("FK_MenuID")))
				mpCache = mCache.get(rs.getLong("FK_MenuID"));
			else
			{
				mpCache = new HashMap<String, Integer>();
				mCache.put(rs.getLong("FK_MenuID"), mpCache);
			}

			mpCache.put(rs.getString("SN"), rs.getInt("Value"));
			count++;
		}
		lg.info("加载角色模块细粒度权限缓存：{}", count);

		// 4 按照Extjs Tree结构组织数据结构
		MenuPrivilege rmp;
		List<MenuPrivilege> menuPrivileges;
		Map<String, MenuTreeNode> menuTreeCache = new HashMap<>();
//		rs = jdbc.queryForRowSet("Select * From Forp_Menu Order By NodeNo ASC");
		MenuTreeNode node, parentNode;
		for (Menu menu : menus)
		{
			node = new MenuTreeNode();
			node.setId(menu.getId() + "," + menu.getNodeNo());
			node.setText(menu.getName());
			node.setIcon(menu.getIcon());

			// 是否选中
			if (myMenuCache.containsKey(menu.getId()))
				node.setChecked(true);
			else
				node.setChecked(false);

			// 缓冲该节点，方便子节点追加
			menuTreeCache.put(menu.getNodeNo(), node);

			// 一级节点
			if ("-1".equals(menu.getParentNodeNo()))
				rows.add(node);
			else
			{
				// 查找父节点，加为子节点
				parentNode = menuTreeCache.get(menu.getParentNodeNo());
				parentNode.setLeaf(false);
				parentNode.getChildren().add(node);
			}

			// 细粒度权限
			if (Redis.isCached(FORP.CACHE_MENU_PRIVILEGE + menu.getId(), jp))
			{
				// 菜单标准细粒度权限
				menuPrivileges = JSON.parseArray(Redis.getString(FORP.CACHE_MENU_PRIVILEGE + menu.getId(), jp), MenuPrivilege.class);
				for (MenuPrivilege p : menuPrivileges)
				{
					rmp = (MenuPrivilege) BeanUtils.cloneBean(p);
					// 角色菜单已定义的细粒度权限列表（单个菜单中使用权限SN替代ID进行查找）
					if (mCache.containsKey(menu.getId()) && mCache.get(menu.getId()).containsKey(p.getSn()))
					{
						rmp.setValue(mCache.get(menu.getId()).get(p.getSn()));
						lg.debug("细粒度权限：{}-->{}", p.getValue(), rmp.getValue());
					}

					// 逐个添加细粒度权限
					node.getPrivileges().add(rmp);
				}
			}
		}

		return rows;
	}
	
	/**
	 * 修改角色权限
	 * 
	 * @param id							角色ID
	 * @param menus					模块列表（分隔符号","）
	 * @param menuPrivileges		模块细粒度权限列表（行分隔符号"；"，值分隔符号“,”）
	 * @param user						操作人
	 */
	public void saveRolePermission(Long id, String menus, String menuPrivileges, User user) throws Exception
	{
		// 1 清除老权限（模块 + 细粒度权限）
		String sql = "delete from Forp_RoleMenu where FK_RoleID=?";
		jdbc.update(sql, id);

		// 2 插入新权限
		sql = "insert into Forp_RoleMenu (FK_RoleID, FK_MenuID) values(?, ?)";
		List<Object[]> batchArgs = new ArrayList<>();
		String[] menuIds = menus.split(",");
		lg.info("角色新权限个数：{}", menuIds.length);
		for (String m : menuIds)
		{
			batchArgs.add(new Object[]{id, new Long(m)});
		}
		jdbc.batchUpdate(sql, batchArgs);

		// 3 插入细粒度权限
		if (menuPrivileges.trim().length() > 0)
		{
			String[] row;
			batchArgs.clear();
			sql = "insert into Forp_RoleMenu_Privilege (FK_RoleMenuID, FK_MenuPrivilegeID, Value) values((Select ID From Forp_RoleMenu Where FK_RoleID=? And FK_MenuID=?), ?, ?)";
			String[] mps = menuPrivileges.split(";");
			for (String r : mps)
			{
				row = r.split(",");
				batchArgs.add(new Object[]{id, new Long(row[0]), new Long(row[1]), new Integer(row[2])});
			}
			jdbc.batchUpdate(sql, batchArgs);
		}

		// 4 更新角色修改信息
		jdbc.update("update Forp_Role set LastModifyDate=?, LastModifyUserID=?, lastModifyUserName=? where ID=?",
				new Timestamp(System.currentTimeMillis()), user.getId(), user.getUserName(), id);

		// 5 同步用户权限缓冲
		synchronizeUserPopedomCache(id);
	}
}