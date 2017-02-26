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
 * 域联系人类
 *
 * @author	Bruce
 * @version	2014-04-10 15:19:43
 */
@DBTable(name="Forp_Domain_Contact")
public class DomainContact implements Serializable
{
	@DBColumn(isPrimaryKey=true)
	private Long id;
	@DBColumn(name="FK_DomainID")
	private Long domainId;
	private String name;
	private String position;
	private String phone;
	private String email;
	private String other;
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
	 * @return the position
	 */
	public String getPosition()
	{
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(String position)
	{
		this.position = position;
	}
	/**
	 * @return the phone
	 */
	public String getPhone()
	{
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}
	/**
	 * @return the other
	 */
	public String getOther()
	{
		return other;
	}
	/**
	 * @param other the other to set
	 */
	public void setOther(String other)
	{
		this.other = other;
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