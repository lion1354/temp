/**
 * Copyright © 2017, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.information.vo;

import java.io.Serializable;
import java.util.Date;

import cn.forp.framework.core.rowmapper.DBTable;

/**
 * 合作社展台类
 *
 * @author Apple
 * @version 2017-2-18 12:14:43
 */
@DBTable(name = "Sys_Cooperative")
public class Cooperative implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4958135630161239969L;

	private Long id;
	private String name;// 服务站名称
	private String contactPerson;// 负责人姓名
	private String mobilePhone;// 联系电话
	private String address;// 地址
	private String longitude;// 经度
	private String latitude;// 纬度
	private Integer status = 1;// 状态 0 - 禁用,1 - 启用
	private String imageId;// 照片MongoDB文档编号
	private Date createDate;// 创建日期
	private String createUserName;// 创建人
	private Date lastModifyDate;// 修改日期
	private String lastModifyUserName;// 修改人
	private Long lastModifyUserId = -1L;// 修改人ID
	private String remark;// 简介

	/**
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the contactPerson
	 */
	public String getContactPerson()
	{
		return contactPerson;
	}

	/**
	 * @param contactPerson
	 *            the contactPerson to set
	 */
	public void setContactPerson(String contactPerson)
	{
		this.contactPerson = contactPerson;
	}

	/**
	 * @return the mobilePhone
	 */
	public String getMobilePhone()
	{
		return mobilePhone;
	}

	/**
	 * @param mobilePhone
	 *            the mobilePhone to set
	 */
	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}

	/**
	 * @return the address
	 */
	public String getAddress()
	{
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address)
	{
		this.address = address;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude()
	{
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude()
	{
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status)
	{
		this.status = status;
	}

	/**
	 * @return the imageId
	 */
	public String getImageId()
	{
		return imageId;
	}

	/**
	 * @param imageId
	 *            the imageId to set
	 */
	public void setImageId(String imageId)
	{
		this.imageId = imageId;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate()
	{
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName()
	{
		return createUserName;
	}

	/**
	 * @param createUserName
	 *            the createUserName to set
	 */
	public void setCreateUserName(String createUserName)
	{
		this.createUserName = createUserName;
	}

	/**
	 * @return the lastModifyDate
	 */
	public Date getLastModifyDate()
	{
		return lastModifyDate;
	}

	/**
	 * @param lastModifyDate
	 *            the lastModifyDate to set
	 */
	public void setLastModifyDate(Date lastModifyDate)
	{
		this.lastModifyDate = lastModifyDate;
	}

	/**
	 * @return the lastModifyUserName
	 */
	public String getLastModifyUserName()
	{
		return lastModifyUserName;
	}

	/**
	 * @param lastModifyUserName
	 *            the lastModifyUserName to set
	 */
	public void setLastModifyUserName(String lastModifyUserName)
	{
		this.lastModifyUserName = lastModifyUserName;
	}

	/**
	 * @return the lastModifyUserId
	 */
	public Long getLastModifyUserId()
	{
		return lastModifyUserId;
	}

	/**
	 * @param lastModifyUserId
	 *            the lastModifyUserId to set
	 */
	public void setLastModifyUserId(Long lastModifyUserId)
	{
		this.lastModifyUserId = lastModifyUserId;
	}

	/**
	 * @return the remark
	 */
	public String getRemark()
	{
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark)
	{
		this.remark = remark;
	}

}