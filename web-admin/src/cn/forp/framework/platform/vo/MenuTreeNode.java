/**
 * Copyright © 2015, HuaYu iStudy Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.vo;

import cn.forp.framework.core.vo.MenuPrivilege;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单Tree节点类
 *
 * @author	Bruce
 * @version	2012-05-04 13:50:51
 */
public class MenuTreeNode implements Serializable
{
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 名称
	 */
	private String text;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * 是否展开
	 */
	private Boolean expanded = true;
	/**
	 * 是否选中
	 */
	private Boolean checked = null;
	/**
	 * 叶子节点
	 */
	private Boolean leaf = true;
	/**
	 * 角色菜单权限：TODO 检查调用的地方，建议改为json字符串形式
	 */
	private List<MenuPrivilege> privileges = new ArrayList<>();
	/**
	 * 子菜单
	 */
	private List<MenuTreeNode> children = new ArrayList<>();

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
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
	}
	/**
	 * @return the expanded
	 */
	public Boolean getExpanded()
	{
		return expanded;
	}
	/**
	 * @param expanded the expanded to set
	 */
	public void setExpanded(Boolean expanded)
	{
		this.expanded = expanded;
	}
	/**
	 * @return the checked
	 */
	public Boolean getChecked()
	{
		return checked;
	}
	/**
	 * @param checked the checked to set
	 */
	public void setChecked(Boolean checked)
	{
		this.checked = checked;
	}
	/**
	 * @return the children
	 */
	public List<MenuTreeNode> getChildren()
	{
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<MenuTreeNode> children)
	{
		this.children = children;
	}
	/**
	 * @return the leaf
	 */
	public Boolean getLeaf()
	{
		return leaf;
	}
	/**
	 * @param leaf the leaf to set
	 */
	public void setLeaf(Boolean leaf)
	{
		this.leaf = leaf;
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
	 * @return the privileges
	 */
	public List<MenuPrivilege> getPrivileges()
	{
		return privileges;
	}
	/**
	 * @param privileges the privileges to set
	 */
	public void setPrivileges(List<MenuPrivilege> privileges)
	{
		this.privileges = privileges;
	}
}