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
 * 系统日志类
 *
 * @author	Bruce
 * @version	2012-3-23 14:41:31
 */
@DBTable(name="Forp_Log")
public class Log implements Serializable
{
	@DBColumn(isPrimaryKey=true)
	private Long id;
	@DBColumn(name="FK_DomainID")
	private Long domainId;
	@DBColumn(name="FK_ModuleID")
	private Long moduleId;
	private String moduleName;
	@DBColumn(name="FK_UserID")
	private Long userId;
	private String userName;
	private String loginName;
	private Date createDate;
	private String ipAddress;
	private String machineName;
	private String content;
	private String parameters;
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
	 * @return the ipAddress
	 */
	public String getIpAddress()
	{
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the machineName
	 */
	public String getMachineName()
	{
		return machineName;
	}

	/**
	 * @param machineName the machineName to set
	 */
	public void setMachineName(String machineName)
	{
		this.machineName = machineName;
	}

	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * @return the params
	 */
	public String getParameters()
	{
		return parameters;
	}

	/**
	 * @param parameters		the params to set
	 */
	public void setParameters(String parameters)
	{
		this.parameters = parameters;
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
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

	/**
	 * @return the loginName
	 */
	public String getLoginName()
	{
		return loginName;
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
	 * @return the moduleId
	 */
	public Long getModuleId()
	{
		return moduleId;
	}

	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(Long moduleId)
	{
		this.moduleId = moduleId;
	}

	/**
	 * @return the moduleName
	 */
	public String getModuleName()
	{
		return moduleName;
	}

	/**
	 * @param moduleName the moduleName to set
	 */
	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}

	/**
	 * @return the domainId
	 */
	public Long getDomainId()
	{
		return domainId;
	}

	/**
	 * @param domainId the domainId
	 */
	public void setDomainId(Long domainId)
	{
		this.domainId = domainId;
	}
}