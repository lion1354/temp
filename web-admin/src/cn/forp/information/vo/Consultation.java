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
 * 通用资讯（监理站，厂商）类
 *
 * @author  Bruce
 * @version 2017-2-18 12:14:43
 */
@DBTable(name = "Sys_Consultation")
public class Consultation implements Serializable
{
	private static final long serialVersionUID = -3184750090545268388L;

	private Long id;
	private String name;// 名称
	private String mobilePhone;// 电话
	private String address;// 地址
	private String longitude;// 经度
	private String latitude;// 纬度
	private Integer status = 1;// 状态 0 - 禁用,1 - 启用
	private String imageId;// 照片MongoDB文档编号
	private Integer isMachineMerchant = 0;  // 是否农机经销商
	private Integer isPartsMerchant = 0;    // 是否配件经销商
	private Integer isCareMerchant = 0;     // 是否修理厂
	private Date endDate;// 入驻到期结束日期
	private String weight;// 商户排序权重
	private String formType = "0";// 表单类型 0 监理站(联系协会),1 商户
	private Date createDate;// 创建日期
	private String createUserName;// 创建人
	private Date lastModifyDate;// 修改日期
	private String lastModifyUserName;// 修改人
	private Long lastModifyUserId = -1L;// 修改人ID
	private String remark;// 简介

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getMobilePhone()
	{
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getLongitude()
	{
		return longitude;
	}

	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}

	public String getLatitude()
	{
		return latitude;
	}

	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getImageId()
	{
		return imageId;
	}

	public void setImageId(String imageId)
	{
		this.imageId = imageId;
	}

	public Integer getIsMachineMerchant()
	{
		return isMachineMerchant;
	}

	public void setIsMachineMerchant(Integer isMachineMerchant)
	{
		this.isMachineMerchant = isMachineMerchant;
	}

	public Integer getIsPartsMerchant()
	{
		return isPartsMerchant;
	}

	public void setIsPartsMerchant(Integer isPartsMerchant)
	{
		this.isPartsMerchant = isPartsMerchant;
	}

	public Integer getIsCareMerchant()
	{
		return isCareMerchant;
	}

	public void setIsCareMerchant(Integer isCareMerchant)
	{
		this.isCareMerchant = isCareMerchant;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public String getWeight()
	{
		return weight;
	}

	public void setWeight(String weight)
	{
		this.weight = weight;
	}

	public String getFormType()
	{
		return formType;
	}

	public void setFormType(String formType)
	{
		this.formType = formType;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public String getCreateUserName()
	{
		return createUserName;
	}

	public void setCreateUserName(String createUserName)
	{
		this.createUserName = createUserName;
	}

	public Date getLastModifyDate()
	{
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate)
	{
		this.lastModifyDate = lastModifyDate;
	}

	public String getLastModifyUserName()
	{
		return lastModifyUserName;
	}

	public void setLastModifyUserName(String lastModifyUserName)
	{
		this.lastModifyUserName = lastModifyUserName;
	}

	public Long getLastModifyUserId()
	{
		return lastModifyUserId;
	}

	public void setLastModifyUserId(Long lastModifyUserId)
	{
		this.lastModifyUserId = lastModifyUserId;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}
}