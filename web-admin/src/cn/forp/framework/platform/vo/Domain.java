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
 * 域类
 *
 * @author		Bruce
 * @version	2013-5-11 13:38:43
 */
@DBTable(name="Forp_Domain")
public class Domain implements Serializable
{
	@DBColumn(isPrimaryKey=true)
	private Long id;
	@DBColumn(name="FK_UserID")
	private Long createUserId;
	private String province;
	private String city;
	private String region;
	private Integer category;
	private String sn;
	private String name;
	private String address;
	private String userName;
	private String telephone;
	private Date createDate = null;
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
	private Long managerUserId;
	private String remark = "";

	// 非业务字段，仅显示用
	@DBColumn(name="")
	private String createUserName;
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
	 * @return the createUserId
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
	 * @return the province
	 */
	public String getProvince()
	{
		return province;
	}
	/**
	 * @param province the province to set
	 */
	public void setProvince(String province)
	{
		this.province = province;
	}
	/**
	 * @return the city
	 */
	public String getCity()
	{
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city)
	{
		this.city = city;
	}
	/**
	 * @return the region
	 */
	public String getRegion()
	{
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region)
	{
		this.region = region;
	}
	/**
	 * @return the category
	 */
	public Integer getCategory()
	{
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(Integer category)
	{
		this.category = category;
	}
	/**
	 * @return the sn
	 */
	public String getSn()
	{
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn)
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
	 * @return the address
	 */
	public String getAddress()
	{
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address)
	{
		this.address = address;
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
	 * @return the telephone
	 */
	public String getTelephone()
	{
		return telephone;
	}
	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
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
	 * @return the managerUserId
	 */
	public Long getManagerUserId()
	{
		return managerUserId;
	}
	/**
	 * @param managerUserId the managerUserId to set
	 */
	public void setManagerUserId(Long managerUserId)
	{
		this.managerUserId = managerUserId;
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