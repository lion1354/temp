/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.vo;

import java.io.Serializable;

/**
 * 角色菜单权限类
 *
 * @author	Bruce
 * @version	2014-07-24 11:19:51
 */
public class MenuPrivilege implements Serializable
{
	private Long id;
//	private Long menuId;
	private String name;
	private String sn;
	/**
	 * 细粒度权限值
	 */
	private Integer standardValue;
	/**
	 * 赋予的细粒度权限值
	 */
	private Integer value = -1;
	private Integer uiType;

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
	 * @return the sn
	 */
	public String getSn()
	{
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn)
	{
		this.sn = sn;
	}
	/**
	 * @return the standardValue
	 */
	public Integer getStandardValue()
	{
		return standardValue;
	}
	/**
	 * @param standardValue the standardValue to set
	 */
	public void setStandardValue(Integer standardValue)
	{
		this.standardValue = standardValue;
	}
	/**
	 * @return the value
	 */
	public Integer getValue()
	{
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Integer value)
	{
		this.value = value;
	}
	/**
	 * @return the uiType
	 */
	public Integer getUiType()
	{
		return uiType;
	}
	/**
	 * @param uiType the uiType to set
	 */
	public void setUiType(Integer uiType)
	{
		this.uiType = uiType;
	}
//	/**
//	 * @return the menuId
//	 */
//	public Long getMenuId()
//	{
//		return menuId;
//	}
//	/**
//	 * @param menuId the menuId to set
//	 */
//	public void setMenuId(Long menuId)
//	{
//		this.menuId = menuId;
//	}
}