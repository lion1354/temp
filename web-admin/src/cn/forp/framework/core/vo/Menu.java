/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.vo;

import cn.forp.framework.core.rowmapper.DBColumn;
import cn.forp.framework.core.rowmapper.DBTable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统菜单类
 *
 * @author	Bruce
 * @version	2012-3-19 10:18:24
 */
@DBTable(name="Forp_Menu")
public class Menu implements Serializable
{
	private Long id;
	@DBColumn(name="FK_DomainID")
	private Long domainId;
	private String name;
	private String nodeNo;
	private String parentNodeNo;
	private Integer orderNo = 1;
	private String icon;
	private String url;
	private Date createDate;
	private Date lastModifyDate;
	private String remark;
	@DBColumn(name="")
	private Integer clickTimes = 0;
	/**
	 * 细粒度菜单权限：TODO 检查调用的地方，建议改为json字符串形式
	 */
	@DBColumn(name="")
	private List<MenuPrivilege> privileges;

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
	 * @return the nodeNo
	 */
	public String getNodeNo()
	{
		return nodeNo;
	}
	/**
	 * @param nodeNo the nodeNo to set
	 */
	public void setNodeNo(String nodeNo)
	{
		this.nodeNo = nodeNo;
	}
	/**
	 * @return the parentNodeNo
	 */
	public String getParentNodeNo()
	{
		return parentNodeNo;
	}
	/**
	 * @param parentNodeNo the parentNodeNo to set
	 */
	public void setParentNodeNo(String parentNodeNo)
	{
		this.parentNodeNo = parentNodeNo;
	}
	/**
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url)
	{
		this.url = url;
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
	 * @return the lastModifyDate
	 */
	public Date getLastModifyDate()
	{
		return lastModifyDate;
	}
	/**
	 * @param lastModifyDate the lastModifyDate to set
	 */
	public void setLastModifyDate(Date lastModifyDate)
	{
		this.lastModifyDate = lastModifyDate;
	}
	/**
	 * @return the remark
	 */
	public String getRemark()
	{
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	/**
	 * @return the orderNo
	 */
	public Integer getOrderNo()
	{
		return orderNo;
	}
	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(Integer orderNo)
	{
		this.orderNo = orderNo;
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
	 * @return the clickTimes
	 */
	public Integer getClickTimes()
	{
		return clickTimes;
	}
	/**
	 * @param clickTimes the clickTimes to set
	 */
	public void setClickTimes(Integer clickTimes)
	{
		this.clickTimes = clickTimes;
	}
	/**
	 * @return the domainId
	 */
	public Long getDomainId()
	{
		return domainId;
	}
	/**
	 * @param domainId the domainId to set
	 */
	public void setDomainId(Long domainId)
	{
		this.domainId = domainId;
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