/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.vo;

import java.io.Serializable;
import java.util.List;

/**
 * ExtJS组件的树节点类
 *
 * @author	Bruce
 * @version	2012-3-19 10:17:43
 */
public class TreeNode implements Serializable
{
	/**
	 * 节点编号
	 */
	private String id;
	/**
	 * 节点名称
	 */
	private String name;
	/**
	 * 是否叶子节点
	 */
	private Boolean isParent = false;
	/**
	 * 图标（绝对路径）
	 */
	private String icon = null;
	/**
	 * 父节点ID
	 */
	private String pId = null;
//	/**
//	 * 是否已展开
//	 */
//	private Boolean expanded = false;
//	/**
//	 * 是否显示复选框（true - 选中；false - 不选中）
//	 */
//	private Boolean checked = null;
	/**
	 * 子节点列表
	 */
	private List<TreeNode> children = null;

	/**
	 * @return the id
	 */
 	public String getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return the children
	 */
	public List<TreeNode> getChildren()
	{
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<TreeNode> children)
	{
		this.children = children;
	}
	/**
	 * @return the isParent
	 */
	public Boolean getIsParent()
	{
		return isParent;
	}
	/**
	 * @param isParent the isParent to set
	 */
	public void setIsParent(Boolean isParent)
	{
		this.isParent = isParent;
	}
	/**
	 * @return the icon
	 */
	public String getIcon()
	{
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon)
	{
		this.icon = icon;
	}
	/**
	 * @return the pId
	 */
	public String getpId()
	{
		return pId;
	}
	/**
	 * @param pId the pId to set
	 */
	public void setpId(String pId)
	{
		this.pId = pId;
	}
}