/**
 * Copyright © 2015, HuaYu iStudy Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.vo;

import cn.forp.framework.core.rowmapper.DBColumn;
import cn.forp.framework.core.rowmapper.DBTable;

import java.io.Serializable;
import java.util.Date;

/**
 * 桌面快捷项类
 *
 * @author	Bruce
 * @version	2012-07-11 11:10:51
 */
@DBTable(name="Desktop_Shortcut")
public class Shortcut implements Serializable
{
	@DBColumn(isPrimaryKey=true)
	private Long id;
	@DBColumn(name="FK_UserID")
	private Long userId;
	@DBColumn(name="FK_MenuID")
	private Long menuId;
	private String menuName;
	@DBColumn(name="null")
	private String icon;
	private Date createDate;

	/**
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
	/**
	 * @return the userId
	 */
	public Long getUserId()
	{
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
	/**
	 * @return the menuId
	 */
	public Long getMenuId()
	{
		return menuId;
	}
	/**
	 * @param menuId the menuId to set
	 */
	public void setMenuId(Long menuId)
	{
		this.menuId = menuId;
	}
	/**
	 * @return the menuName
	 */
	public String getMenuName()
	{
		return menuName;
	}
	/**
	 * @param menuName the menuName to set
	 */
	public void setMenuName(String menuName)
	{
		this.menuName = menuName;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate()
	{
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
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
}