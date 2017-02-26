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
 * 信息发布
 *
 * @author Apple
 * @version 2017-2-18 12:14:43
 */
@DBTable(name = "Sys_Publish_Info")
public class Information implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5684348798105671063L;
	private Long id;
	private Integer category;// 信息类别 0找活干, 1找机器, 2招司机, 3二手交易, 4其他
	private String title;// 标题
	private Integer status;// 信息状态 0待审批, 1审批通过, 2未通过, 3已关闭
	private String mobilePhone;// 电话
	private String imageId;// 照片MongoDB文档编号
	private String info;// 信息详情介绍
	private Date createDate;// 创建日期
	private String createUserName;// 创建人
	private Date lastModifyDate;// 修改日期(发布时间)
	private String lastModifyUserName;// 修改人(发布人)
	private Long lastModifyUserId = -1L; // 修改人ID

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
	 * @return the category
	 */
	public Integer getCategory()
	{
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(Integer category)
	{
		this.category = category;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
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
	 * @return the info
	 */
	public String getInfo()
	{
		return info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(String info)
	{
		this.info = info;
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

}