/**
 * Copyright © 2015, HuaYu iStudy Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.vo;

import cn.forp.framework.core.rowmapper.DBColumn;
import cn.forp.framework.core.rowmapper.DBTable;

import java.io.Serializable;

/**
 * 提醒参数
 *
 * @author	Bruce
 * @version	2014-02-27 09:47:35
 */
@DBTable(name="Forp_Remind")
public class Remind implements Serializable
{
	@DBColumn(isPrimaryKey=true)
	private Long id;
	@DBColumn(name="FK_DomainID")
	private Long domainId;
	private Integer sn;
	private String name;
	private Integer days;
	private Integer state;
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
	 * @return the sn
	 */
	public Integer getSn()
	{
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(Integer sn)
	{
		this.sn = sn;
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
	 * @return the days
	 */
	public Integer getDays()
	{
		return days;
	}
	/**
	 * @param days the days to set
	 */
	public void setDays(Integer days)
	{
		this.days = days;
	}
	/**
	 * @return the state
	 */
	public Integer getState()
	{
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(Integer state)
	{
		this.state = state;
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
}