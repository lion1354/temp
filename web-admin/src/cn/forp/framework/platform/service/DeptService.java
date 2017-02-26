/*
  Copyright © 2016, Forp Co., LTD

  All Rights Reserved.
 */
package cn.forp.framework.platform.service;

import cn.forp.framework.core.BaseService;
import cn.forp.framework.core.BusinessException;
import cn.forp.framework.platform.vo.Department;
import cn.forp.framework.platform.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 部门管理Service
 *
 * @author	Bruce
 * @version	2012-3-23 15:27:36
 */
@Service
public class DeptService extends BaseService
{
	/**
	 * Log4j lg
	 */
	private static Logger lg = LoggerFactory.getLogger(DeptService.class);

	/**
	 * 查询当前部门节点下的直属部门列表
	 *
	 * @param user          操作人
	 * @param treeNodeId    Tree节点编号（格式：id,nodeNo）
	 * @return List<Department>
	 */
//	public List<Department> search(User user, String treeNodeId) throws Exception
//	{
//		// 节点编号
//		String nodeNo = treeNodeId.split(",")[1];
//
//		// 一次性检索所有数据
//		String sql = "select * from Forp_Dept where FK_DomainID=? order by ID";
//		List<Department> rows = findByList(sql, Department.class, user.getDomainId());
////		List<Department> rows = jdbc.query(sql, new Object[]{user.getDomainId()}, new ADBeanPropertyRowMapper<Department>(Department.class));
//
//		// 内存中过滤
//		Department r;
//		// 所有下级节点缓冲
//		Map<String, Boolean> secondNodeCache = new HashMap<>();
//		for (Iterator<Department> itr = rows.iterator(); itr.hasNext();)
//		{
//			r = itr.next();
//
//			if (!nodeNo.equals(r.getParentNodeNo()))
//			{
//				itr.remove();
//				secondNodeCache.put(r.getParentNodeNo(), true);
//			}
//		}
//
//		// 2 标识节点是否有子节点
//		for (Department d : rows)
//		{
//			if (!secondNodeCache.containsKey(d.getNodeNo()))
//				d.setLeaf(true);
//		}
//
//		return rows;
//	}
	/**
	 * 一次读取所有节点
	 *
	 */
	public List<Department> search(User user, String treeNodeId) throws Exception
	{
		String nodeNo = treeNodeId.split(",")[1];
		String sql = "select * from Forp_Dept where FK_DomainID=? order by ID";
		List<Department> rows = findByList(sql, Department.class, user.getDomainId());
		Department r;
		// 所有下级节点缓冲
				Map<String, Boolean> secondNodeCache = new HashMap<>();
				for (Iterator<Department> itr = rows.iterator(); itr.hasNext();)
				{
					r = itr.next();
					if (!nodeNo.equals(r.getParentNodeNo()))
					{
						//itr.remove();
						secondNodeCache.put(r.getParentNodeNo(), true);
					}
				}

				// 2 标识节点是否有子节点
				for (Department d : rows)
				{
					if (!secondNodeCache.containsKey(d.getNodeNo()))
						d.setLeaf(true);
				}
		return rows;
	}
	
	
	/**
	 * 添加
	 * 
	 * @param dept  部门信息
	 * @param user  操作人
	 */
	public void create(Department dept, User user) throws Exception
	{
		// 1 重名检查
		if (isFieldDuplicate("Forp_Dept", "Name", dept.getName(), "FK_DomainId=" + user.getDomainId() + " And ParentNodeNo='" + dept.getParentNodeNo() + "'"))
			throw new BusinessException("部门“" + dept.getName() + "”已存在，请检查您的输入！");

		// 2 属性设置
		dept.setDomainId(user.getDomainId());
		dept.setCreateUserId(user.getId());
		dept.setCreateUserName(user.getUserName());
		dept.setCreateDate(new Timestamp(System.currentTimeMillis()));
		// 当前节点编号
		dept.setNodeNo(getMaxTreeNodeCode("Forp_Dept", dept.getParentNodeNo(), 3, "FK_DomainId=" + dept.getDomainId()));

		// 3 持久化
		insertIntoTable(dept, null, new String[]{"lastModifyDate", "lastModifyUserId", "lastModifyUserName"});
	}

	/**
	 * 修改
	 *
	 * @param dept  部门信息
	 * @param user  操作人
	 */
	public void update(Department dept, User user) throws Exception
	{
		// 1 重名检查
		if (isFieldDuplicate("Forp_Dept", "Name", dept.getName(), "FK_DomainId=" + user.getDomainId() + " and ID<>" + dept.getId() + " and ParentNodeNo='" + dept.getParentNodeNo() + "'"))
			throw new BusinessException("部门“" + dept.getName() + "”已存在，请检查您的输入！");

		// 2 属性设置
		dept.setDomainId(user.getDomainId());
		dept.setLastModifyUserId(user.getId());
		dept.setLastModifyUserName(user.getUserName());
		dept.setLastModifyDate(new Timestamp(System.currentTimeMillis()));

		updateTable(dept, new String[]{"name", "remark", "lastModifyDate", "lastModifyUserId", "lastModifyUserName"}, null);
	}

	/**
	 * 删除
	 * 
	 * @param id		  部门编号
	 * @param nodeNo	节点编号
	 * @param user		操作人
	 */
	public void delete(long id, String nodeNo, User user) throws Exception
	{
		lg.debug("删除部门：{}", id);

		// 检测引用
		if (isUsedByOtherTable("Forp_User", "FK_DeptID", id, null))
			throw new BusinessException("该部门正在使用中，无法删除！");

		// 是否有下级部门
		SqlRowSet rs = jdbc.queryForRowSet("select Count(*) as TC from Forp_Dept where FK_DomainId=? and ParentNodeNo=?",
				user.getDomainId(), nodeNo);
		rs.next();
		if (rs.getLong("TC") > 0)
			throw new BusinessException("该部门下设有子部门，无法删除！");

		jdbc.update("delete from Forp_Dept where ID=?", id);
		lg.info("删除部门：{}", id);
	}

	//	/**
//	 * 查询当前部门节点下的直属子节点
//	 *
//	 * @param user          操作人
//	 * @param treeNodeId	Tree节点编号（格式：id,nodeNo）
//	 * @throws Exception
//	 */
//	public List<TreeNode> getSonTreeNode(User user, String treeNodeId) throws Exception
//	{
//		String nodeNo = null;
//		if (StringUtils.isBlank(treeNodeId))
//		{
//			nodeNo = "001";
//			treeNodeId = "-1,001";
//		}
//		else
//			nodeNo = treeNodeId.split(",")[1];
//
//		String sql = "Select * From Forp_Dept Where FK_DomainID=? Order By " + convertToGBKOrderBy("Name", "ASC");
//		SqlRowSet  rs = getJdbcTemplate().queryForRowSet(sql, new Object[]{user.getDomainId()});
//
//		// 1 查找直接叶子节点
//		List<TreeNode> rows = new ArrayList<TreeNode>();
//		TreeNode n = null;
//		// 所有二级节点缓冲
//		Map<String, Boolean> secondNodeCache = new HashMap<String, Boolean>();
//		while (rs.next())
//		{
//			// 直属节点
//			if (nodeNo.equals(rs.getString("ParentNodeNo")))
//			{
//				n = new TreeNode();
//				n.setId(rs.getString("ID") + "," + rs.getString("NodeNo"));
//				n.setpId(treeNodeId);
//				n.setName(rs.getString("Name"));
//
//				rows.add(n);
//			}
//			else
//			{
//				secondNodeCache.put(rs.getString("ParentNodeNo"), true);
//				lg.debug("二级子节点：" + rs.getString("Name"));
//			}
//		}
//
//		// 2 标识节点是否有子节点
//		for (TreeNode node : rows)
//		{
//			if (secondNodeCache.containsKey(node.getId().split(",")[1]))
//				node.setIsParent(true);
//		}
//
//		return rows;
//	}
//
//	/**
//	 * 加载指定节点下的所有子节点列表（按照OrderNo+Name升序排列）
//	 *
//	 * @param user						操作人
//	 * @param treeNodeId			Ext Tree节点编号（格式：id,nodeNo,parentNodeNo）
//	 * @throws Exception
//	 */
//	public List<TreeNode> getAllTreeNodes(User user, String treeNodeId) throws Exception
//	{
//		// 1 原始记录
//		String sql = "Select * From Forp_Dept Where FK_DomainID=? And ParentNodeNo Like ?";
//		List<Department> depts = getJdbcTemplate().query(sql, new Object[]{user.getDomainId(), treeNodeId.split(",")[1] + "%"},
//				new ADBeanPropertyRowMapper<Department>(Department.class));
//
//		// 2 重新按照orderNo+name排序
//		// 2.1 排序规则
//		ComparatorChain cc = new ComparatorChain();
//		cc.addComparator(new BeanComparator("orderNo"));
//		cc.addComparator(new BeanComparator("name", Collator.getInstance(java.util.Locale.CHINA)));
//
//		// 2.2 排序
//		List<Department> rows = new ArrayList<Department>();
//		reOrderTreeNodes(depts, rows, treeNodeId.split(",")[1], cc);
//		lg.debug("原始部门：" + depts.size() + "，重新排序后部门：" + rows.size());
//
//		// 3 转换为ExtTreeNode对象列表
//		List<TreeNode> result = new ArrayList<TreeNode>();
//		TreeNode n = null;
//		for (Department d : rows)
//		{
//			n = new TreeNode();
//			n.setId(d.getId() + "," + d.getNodeNo() + "," + d.getParentNodeNo());
//			n.setName(d.getName());
//
//			result.add(n);
//		}
//		lg.debug("Ext部门：" + result.size());
//
//		return result;
//	}
}