/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.service;

import cn.forp.framework.core.BaseService;

/**
 * 模块管理Service
 *
 * @author		Bruce
 * @version	2012-06-01 15:00:36
 */
public class ModuleService extends BaseService
{
//	/**
//	 * Log4j logger
//	 */
//	private static Logger logger = Logger.getLogger(ModuleService.class);
//	/**
//	 * ID编号列资源锁
//	 */
//	private static final ReentrantLock ID_Lock = new ReentrantLock();
//
//	/**
//	 * 查询当前模块节点下的直属子节点
//	 * 
//	 * @param treeNodeId			Ext Tree节点编号（格式：id,nodeNo）
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unchecked")
//	public List<TreeNode> getSonTreeNode(User user, String treeNodeId) throws Exception
//	{
//		List<Menu> menus = new ArrayList<Menu>();
//		// 从Cache过滤当前节点下的子节点列表
//		Menu m = null;
//		String nodeNo = treeNodeId.split(",")[1];
//		for (Long key : (List<Long>) HuaYuIStudy.MENU_CACHE.getKeys())
//		{
//			m = (Menu) (HuaYuIStudy.MENU_CACHE.get(key).getObjectValue());
//			if (user.getDomainId().longValue() == m.getDomainId().longValue() && m.getParentNodeNo().equals(nodeNo))
//				menus.add(m);
//		}
//
//		// 按照NodeNo升序排列
//		Collections.sort(menus, new BeanComparator("nodeNo"));
//
//		// 转换对象类型
//		TreeNode n = null;
//		List<TreeNode> rows = new ArrayList<TreeNode>();
//		for (Menu menu : menus)
//		{
//			n = new TreeNode();
//			n.setId(menu.getId() + "," + menu.getNodeNo());
//			n.setIcon(HuaYuIStudy.WEB_APP_CONTEXT + "/image/menu/" + menu.getIcon());
//			n.setName(menu.getName());
//
//			rows.add(n);
//		}
//
//		return rows;
//	}
//
//	/**
//	 * 查询当前模块节点下的直属模块列表
//	 * 
//	 * @param parentNodeNo			父节点编号
//	 * @throws Exception
//	 */
//	@SuppressWarnings({"unchecked"})
//	public List<Menu> search(User user, String parentNodeNo) throws Exception
//	{
//		List<Menu> rows = new ArrayList<Menu>();
//
//		// 从Cache过滤当前节点下的子节点列表
//		Menu m = null;
//		for (Long key : (List<Long>) HuaYuIStudy.MENU_CACHE.getKeys())
//		{
//			m = (Menu) (HuaYuIStudy.MENU_CACHE.get(key).getObjectValue());
//			if (user.getDomainId().longValue() == m.getDomainId().longValue() && m.getParentNodeNo().equals(parentNodeNo))
//				rows.add(m);
//		}
//
//		// 按照NodeNo升序排列
//		Collections.sort(rows, new BeanComparator("nodeNo"));
//		return rows;
//	}
//
//	/**
//	 * 添加
//	 * 
//	 * @param m						模块信息
//	 * @throws Exception
//	 */
//	public void create(Menu m) throws Exception
//	{
//		// 重名检查
//		if (isFieldDuplicate("Forp_Menu", "Name", m.getName(), "FK_DomainId=" + m.getDomainId() + " And ParentNodeNo='" + m.getParentNodeNo() + "'"))
//			throw new BusinessException("模块名称“" + m.getName() + "”已存在，请检查您的输入！");
//
//		// 生成主键
//		long id = 0;
//		ID_Lock.lock();
//		try
//		{
//			SqlRowSet rs = getJdbcTemplate().queryForRowSet("Select Max(ID) As ID From Forp_Menu");
//			if (rs.next())
//				id = rs.getLong("ID");
//
//			id++;
//			m.setId(id);
//
//			// 当前节点编号
//			m.setNodeNo(getMaxTreeNodeCode("Forp_Menu", m.getParentNodeNo(), 3, "FK_DomainId=" + m.getDomainId()));
//			m.setCreateDate(new Timestamp(System.currentTimeMillis()));
//			// 保存（通用的save方法不会保存id主键字段值）
//			getJdbcTemplate().update("Insert Into Forp_Menu (ID, FK_DomainId, Name, NodeNo, ParentNodeNo, OrderNo, Icon, URL, CreateDate, Remark)" +
//					" Values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{id, m.getDomainId(), m.getName(), m.getNodeNo(), m.getParentNodeNo(), m.getOrderNo(), m.getIcon(),
//					m.getUrl(), new Date(), m.getRemark()});
//
//			// 同步Cache
//			HuaYuIStudy.MENU_CACHE.put(new Element(m.getId(), m));
//			logger.info("同步MENU_CACHE：+[" + m.getId() + "/" + HuaYuIStudy.MENU_CACHE.getSize() + "]");
//		}
//		finally
//		{
//			ID_Lock.unlock();
//		}
//		
//		logger.info("新建模块：" + m.getName() + "[" + id + "]");
//	}
//
//	/**
//	 * 修改
//	 * 
//	 * @param m					模块信息
//	 * @throws Exception
//	 */
//	public void update(Menu m) throws Exception
//	{
//		logger.info("修改模块：" + m.getId());
//
//		// 重名检查
//		String where = "FK_DomainId=" + m.getDomainId() + " And ID<>" + m.getId() + " And ParentNodeNo='" + m.getParentNodeNo() + "'";
//		if (isFieldDuplicate("Forp_Menu", "Name", m.getName(), where))
//			throw new BusinessException("模块名称“" + m.getName() + "”已存在，请检查您的输入！");
//
//		m.setLastModifyDate(new Timestamp(System.currentTimeMillis()));
//		updateTable(m, null, new String[]{"domainId", "nodeNo", "parentNodeNo", "createDate"});
//		// 同步Cache
//		Menu cache = (Menu) HuaYuIStudy.MENU_CACHE.get(m.getId()).getObjectValue();
//		cache.setName(m.getName());
//		cache.setIcon(m.getIcon());
//		cache.setUrl(m.getUrl());
//		cache.setRemark(m.getRemark());
//		logger.info("同步MENU_CACHE：*[" + m.getId() + "]");
//	}
//
//	/**
//	 * 删除
//	 * 
//	 * @param id						模块编号
//	 * @param nodeNo			节点编号
//	 * @throws Exception
//	 */
//	public void delete(Long id, String nodeNo) throws Exception
//	{
//		logger.debug("删除模块：" + id);
//
//		// 是否有下级模块
//		SqlRowSet rs = getJdbcTemplate().queryForRowSet("Select Count(*) As TC From Forp_Menu Where ParentNodeNo=?", new Object[]{nodeNo});
//		rs.next();
//		if (rs.getLong("TC") > 0)
//			throw new BusinessException("该模块下设有子模块，无法删除！");
//
//		getJdbcTemplate().update("Delete From Forp_Menu Where ID=?", new Object[]{id});
//		// 同步Cache
//		HuaYuIStudy.MENU_CACHE.remove(id);
//		logger.info("同步MENU_CACHE：-[" + id + "/" + HuaYuIStudy.MENU_CACHE.getSize() + "]");
//
//		logger.info("删除模块：" + id);
//	}
}