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
 * 系统编码Class
 *
 * @author	Bruce
 * @version	2012-08-29 11:53:04
 */
@DBTable(name="Forp_Code")
public class SystemCode implements Serializable
{
	@DBColumn(isPrimaryKey=true)
	private Long id;
	@DBColumn(name="FK_DomainID")
	private Long domainId;
	@DBColumn(name="FK_CategoryId")
	private Long categoryId;
	private String name;
	private Integer status;
	private String item1;
	private String item2;
	private String item3;
	private String item4;
	private String item5;
	private String item6;
	private String item7;
	private String item8;
	private String item9;
	private String item10;
	private Date createDate;
	private Date lastModifyDate;
	private String remark;

	/* 业务展示字段 */
	@DBColumn(name = "")
	private String categoryName;

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
	 * @return the categoryId
	 */
	public Long getCategoryId()
	{
		return categoryId;
	}
	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(Long categoryId)
	{
		this.categoryId = categoryId;
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
	 * @return the status
	 */
	public Integer getStatus()
	{
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status)
	{
		this.status = status;
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
	 * @return the item1
	 */
	public String getItem1()
	{
		return item1;
	}
	/**
	 * @param item1 the item1 to set
	 */
	public void setItem1(String item1)
	{
		this.item1 = item1;
	}
	/**
	 * @return the item2
	 */
	public String getItem2()
	{
		return item2;
	}
	/**
	 * @param item2 the item2 to set
	 */
	public void setItem2(String item2)
	{
		this.item2 = item2;
	}
	/**
	 * @return the item3
	 */
	public String getItem3()
	{
		return item3;
	}
	/**
	 * @param item3 the item3 to set
	 */
	public void setItem3(String item3)
	{
		this.item3 = item3;
	}
	/**
	 * @return the item4
	 */
	public String getItem4()
	{
		return item4;
	}
	/**
	 * @param item4 the item4 to set
	 */
	public void setItem4(String item4)
	{
		this.item4 = item4;
	}
	/**
	 * @return the item5
	 */
	public String getItem5()
	{
		return item5;
	}
	/**
	 * @param item5 the item5 to set
	 */
	public void setItem5(String item5)
	{
		this.item5 = item5;
	}
	/**
	 * @return the item6
	 */
	public String getItem6()
	{
		return item6;
	}
	/**
	 * @param item6 the item6 to set
	 */
	public void setItem6(String item6)
	{
		this.item6 = item6;
	}
	/**
	 * @return the item7
	 */
	public String getItem7()
	{
		return item7;
	}
	/**
	 * @param item7 the item7 to set
	 */
	public void setItem7(String item7)
	{
		this.item7 = item7;
	}
	/**
	 * @return the item8
	 */
	public String getItem8()
	{
		return item8;
	}
	/**
	 * @param item8 the item8 to set
	 */
	public void setItem8(String item8)
	{
		this.item8 = item8;
	}
	/**
	 * @return the item9
	 */
	public String getItem9()
	{
		return item9;
	}
	/**
	 * @param item9 the item9 to set
	 */
	public void setItem9(String item9)
	{
		this.item9 = item9;
	}
	/**
	 * @return the item10
	 */
	public String getItem10()
	{
		return item10;
	}
	/**
	 * @param item10 the item10 to set
	 */
	public void setItem10(String item10)
	{
		this.item10 = item10;
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

	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}
}