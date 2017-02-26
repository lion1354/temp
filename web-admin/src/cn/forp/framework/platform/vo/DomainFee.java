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
 * 域费用类
 *
 * @author	Bruce
 * @version	2013-5-11 14:56:43
 */
@DBTable(name="Forp_Domain_Fee")
public class DomainFee implements Serializable
{
	@DBColumn(isPrimaryKey=true)
	private Long id;
	@DBColumn(name="FK_DomainID")
	private Long domainId;
	@DBColumn(name="FK_UserID")
	private Long userId;
	@DBColumn(name="FK_ServiceID")
	private Long serviceId;
	private Integer action;
	private Integer money;
	private Date origExpireDate;
	private Integer months;
	private Date createDate;
	private String remark;

	// 非业务字段，仅显示用
	@DBColumn(name="")
	private String domainName;
	@DBColumn(name="")
	private String serviceVersion;
	@DBColumn(name="")
	private String userName;
	@DBColumn(name="")
	private String managerUserName;

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
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	/**
	 * @return the action
	 */
	public Integer getAction()
	{
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(Integer action)
	{
		this.action = action;
	}
	/**
	 * @return the money
	 */
	public Integer getMoney()
	{
		return money;
	}
	/**
	 * @param money the money to set
	 */
	public void setMoney(Integer money)
	{
		this.money = money;
	}
	/**
	 * @return the origExpireDate
	 */
	public Date getOrigExpireDate()
	{
		return origExpireDate;
	}
	/**
	 * @param origExpireDate the origExpireDate to set
	 */
	public void setOrigExpireDate(Date origExpireDate)
	{
		this.origExpireDate = origExpireDate;
	}
	/**
	 * @return the months
	 */
	public Integer getMonths()
	{
		return months;
	}
	/**
	 * @param months the months to set
	 */
	public void setMonths(Integer months)
	{
		this.months = months;
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
	 * @return the domainName
	 */
	public String getDomainName()
	{
		return domainName;
	}
	/**
	 * @param domainName the domainName to set
	 */
	public void setDomainName(String domainName)
	{
		this.domainName = domainName;
	}
	/**
	 * @return the serviceId
	 */
	public Long getServiceId()
	{
		return serviceId;
	}
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(Long serviceId)
	{
		this.serviceId = serviceId;
	}
	/**
	 * @return the serviceVersion
	 */
	public String getServiceVersion()
	{
		return serviceVersion;
	}
	/**
	 * @param serviceVersion the serviceVersion to set
	 */
	public void setServiceVersion(String serviceVersion)
	{
		this.serviceVersion = serviceVersion;
	}
	/**
	 * @return the managerUserName
	 */
	public String getManagerUserName()
	{
		return managerUserName;
	}
	/**
	 * @param managerUserName the managerUserName to set
	 */
	public void setManagerUserName(String managerUserName)
	{
		this.managerUserName = managerUserName;
	}
}