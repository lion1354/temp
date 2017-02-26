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
 * 角色类
 *
 * @author	Bruce
 * @version	2012-3-23 14:39:04
 */
@DBTable(name="Forp_Role")
public class Role implements Serializable
{
	@DBColumn(isPrimaryKey=true)
	private Long id;
	@DBColumn(name="FK_DomainID")
	private Long domainId;
	private String name;
	private Integer level = 2;
	private Integer isInited = 2;
	private Date createDate;
	@DBColumn(name="FK_UserID")
	private Long createUserId;
	private String createUserName;
	private Date lastModifyDate;
	@DBColumn(name="LastModifyUserID")
	private Long lastModifyUserId;
	private String lastModifyUserName;
	private String description;
	private String remark;

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
	 * @return the createDate
	 */
	// @JsonFormat(pattern = FORP.PATTERN_DATE_TIME, timezone = "GMT+8")
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
	// @JsonFormat(pattern = FORP.PATTERN_DATE_TIME, timezone = "GMT+8")
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
	 * @return the level
	 */
	public Integer getLevel()
	{
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(Integer level)
	{
		this.level = level;
	}
	/**
	 * @return the isInited
	 */
	public Integer getIsInited()
	{
		return isInited;
	}
	/**
	 * @param isInited the isInited to set
	 */
	public void setIsInited(Integer isInited)
	{
		this.isInited = isInited;
	}
	/**
	 * @return the userId
	 */
	public Long getCreateUserId()
	{
		return createUserId;
	}
	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(Long createUserId)
	{
		this.createUserId = createUserId;
	}
	/**
	 * @return the createUserName
	 */
	public String getCreateUserName()
	{
		return createUserName;
	}
	/**
	 * @param createUserName the createUserName to set
	 */
	public void setCreateUserName(String createUserName)
	{
		this.createUserName = createUserName;
	}
	/**
	 * @return the lastModifyUserId
	 */
	public Long getLastModifyUserId()
	{
		return lastModifyUserId;
	}
	/**
	 * @param lastModifyUserId the lastModifyUserId to set
	 */
	public void setLastModifyUserId(Long lastModifyUserId)
	{
		this.lastModifyUserId = lastModifyUserId;
	}
	/**
	 * @return the lastModifyUserName
	 */
	public String getLastModifyUserName()
	{
		return lastModifyUserName;
	}
	/**
	 * @param lastModifyUserName the lastModifyUserName to set
	 */
	public void setLastModifyUserName(String lastModifyUserName)
	{
		this.lastModifyUserName = lastModifyUserName;
	}
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
}