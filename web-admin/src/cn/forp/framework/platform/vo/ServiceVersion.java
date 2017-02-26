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
 * 服务版本类
 *
 * @author	Bruce
 * @version	2014-02-21 15:50:43
 */
@DBTable(name="Forp_ServiceVersion")
public class ServiceVersion implements Serializable
{
	@DBColumn(isPrimaryKey=true)
	private Long id;
	private String name;
	private String sqlFileName;
	private Integer money1;
	private Integer money2;
	private Integer period;
	private Integer state;
	private String remark = "";

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
	 * @return the sqlFileName
	 */
	public String getSqlFileName()
	{
		return sqlFileName;
	}
	/**
	 * @param sqlFileName the sqlFileName to set
	 */
	public void setSqlFileName(String sqlFileName)
	{
		this.sqlFileName = sqlFileName;
	}
	/**
	 * @return the money1
	 */
	public Integer getMoney1()
	{
		return money1;
	}
	/**
	 * @param money1 the money1 to set
	 */
	public void setMoney1(Integer money1)
	{
		this.money1 = money1;
	}
	/**
	 * @return the money2
	 */
	public Integer getMoney2()
	{
		return money2;
	}
	/**
	 * @param money2 the money2 to set
	 */
	public void setMoney2(Integer money2)
	{
		this.money2 = money2;
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
	/**
	 * @return the period
	 */
	public Integer getPeriod()
	{
		return period;
	}
	/**
	 * @param period the period to set
	 */
	public void setPeriod(Integer period)
	{
		this.period = period;
	}
}