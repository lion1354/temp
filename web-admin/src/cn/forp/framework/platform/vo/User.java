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
 * 系统用户类
 *
 * @author	Bruce
 * @version	2012-3-19 10:21:43
 */
@DBTable(name="Forp_User")
public class User implements Serializable
{
	@DBColumn(isPrimaryKey=true)
	private Long id;
	@DBColumn(name="FK_DomainID")
	private Long domainId;
	@DBColumn(name="FK_DeptID")
	private Long deptId;
	@DBColumn(name="")
	private String deptNodeNo;
	@DBColumn(name="")
	private String deptName;
	private String loginName;
	private String userName;
	private String password;
	private Integer gender;
	private String mobilePhone;
	private String email;
	private String headImg;
	private Date createDate = null;
	@DBColumn(name="FK_UserID")
	private Long createUserId;
	private String createUserName;
	private Date lastModifyDate = null;
	@DBColumn(name="LastModifyUserID")
	private Long lastModifyUserId;
	private String lastModifyUserName;
	private Integer pageLimit = 20;
	private Integer state;
	private Integer type;
	private Integer bindType;
	private String ipOrMAC;
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
	private String remark = "";

	/**
	 * 非业务字段，仅显示用
	 */
	@DBColumn(name="")
	private Long productId;	// 产品ID
	@DBColumn(name="")
	private String productName;	// 产品名称
	@DBColumn(name="")
	private Integer roleLevel;
	@DBColumn(name="")
	private String roleId;
	@DBColumn(name="")
	private String roleName;
	@DBColumn(name="")
	private Date expireDate;
	@DBColumn(name="")
	private String validationCode;
	@DBColumn(name="")
	private Long currentModuleId;	// 最后一次操作的模块ID
	@DBColumn(name="")
	private String currentIPAddress;	// 最后一次操作的IP地址
	@DBColumn(name="")
	private String domainProvince;	// 机构所在省份
	@DBColumn(name="")
	private String domainSn;				// 机构编号

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
	 * @return the roleId
	 */
	public String getRoleId()
	{
		return roleId;
	}
	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId)
	{
		this.roleId = roleId;
	}
	/**
	 * @return the roleName
	 */
	public String getRoleName()
	{
		return roleName;
	}
	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}
	/**
	 * @return the deptId
	 */
	public Long getDeptId()
	{
		return deptId;
	}
	/**
	 * @param deptId the deptId to set
	 */
	public void setDeptId(Long deptId)
	{
		this.deptId = deptId;
	}
	/**
	 * @return the deptName
	 */
	public String getDeptName()
	{
		return deptName;
	}
	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName)
	{
		this.deptName = deptName;
	}
	/**
	 * @return the loginName
	 */
	public String getLoginName()
	{
		return loginName;
	}
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
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
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
	/**
	 * @return the mobilePhone
	 */
	public String getMobilePhone()
	{
		return mobilePhone;
	}
	/**
	 * @param mobilePhone the mobilePhone to set
	 */
	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
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
	 * @return the lastModifyDate
	 */
	// @JsonFormat(pattern = FORP.PATTERN_DATE_TIME, timezone = "GMT+8")
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
	/**
	 * @return the pageLimit
	 */
	public Integer getPageLimit()
	{
		return pageLimit;
	}
	/**
	 * @param pageLimit the pageLimit to set
	 */
	public void setPageLimit(Integer pageLimit)
	{
		this.pageLimit = pageLimit;
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
	 * @return the gender
	 */
	public Integer getGender()
	{
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(Integer gender)
	{
		this.gender = gender;
	}
	/**
	 * @return the headImg
	 */
	public String getHeadImg()
	{
		return headImg;
	}
	/**
	 * @param headImg the headImg to set
	 */
	public void setHeadImg(String headImg)
	{
		this.headImg = headImg;
	}
	/**
	 * @return the type
	 */
	public Integer getType()
	{
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type)
	{
		this.type = type;
	}
	/**
	 * @return the bindType
	 */
	public Integer getBindType()
	{
		return bindType;
	}
	/**
	 * @param bindType the bindType to set
	 */
	public void setBindType(Integer bindType)
	{
		this.bindType = bindType;
	}
	/**
	 * @return the ipOrMAC
	 */
	public String getIpOrMAC()
	{
		return ipOrMAC;
	}
	/**
	 * @param ipOrMAC the ipOrMAC to set
	 */
	public void setIpOrMAC(String ipOrMAC)
	{
		this.ipOrMAC = ipOrMAC;
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
	 * @return the expireDate
	 */
	// @JsonFormat(pattern = FORP.PATTERN_DATE_TIME, timezone = "GMT+8")
	public Date getExpireDate()
	{
		return expireDate;
	}
	/**
	 * @param expireDate the expireDate to set
	 */
	public void setExpireDate(Date expireDate)
	{
		this.expireDate = expireDate;
	}
	/**
	 * @return the validationCode
	 */
	public String getValidationCode()
	{
		return validationCode;
	}
	/**
	 * @param validationCode the validationCode to set
	 */
	public void setValidationCode(String validationCode)
	{
		this.validationCode = validationCode;
	}
	/**
	 * @return the currentModuleId
	 */
	public Long getCurrentModuleId()
	{
		return currentModuleId;
	}
	/**
	 * @param currentModuleId the currentModuleId to set
	 */
	public void setCurrentModuleId(Long currentModuleId)
	{
		this.currentModuleId = currentModuleId;
	}
	/**
	 * @return the deptNodeNo
	 */
	public String getDeptNodeNo()
	{
		return deptNodeNo;
	}
	/**
	 * @param deptNodeNo the deptNodeNo to set
	 */
	public void setDeptNodeNo(String deptNodeNo)
	{
		this.deptNodeNo = deptNodeNo;
	}
	/**
	 * @return the currentIPAddress
	 */
	public String getCurrentIPAddress()
	{
		return currentIPAddress;
	}
	/**
	 * @param currentIPAddress the currentIPAddress to set
	 */
	public void setCurrentIPAddress(String currentIPAddress)
	{
		this.currentIPAddress = currentIPAddress;
	}
	/**
	 * @return the productId
	 */
	public Long getProductId()
	{
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Long productId)
	{
		this.productId = productId;
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
	 * @return the roleLevel
	 */
	public Integer getRoleLevel()
	{
		return roleLevel;
	}
	/**
	 * @param roleLevel the roleLevel to set
	 */
	public void setRoleLevel(Integer roleLevel)
	{
		this.roleLevel = roleLevel;
	}
	/**
	 * @return the userId
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
	 * @return the lastModifyUserId
	 */
	public Long getLastModifyUserId()
	{
		return lastModifyUserId;
	}
	/**
	 * @param lastModifyUserId the lastModifyUserId to set
	 */
	public void setLastModifyUserId(Long lastModifyUserId)
	{
		this.lastModifyUserId = lastModifyUserId;
	}
	/**
	 * @return the lastModifyUserName
	 */
	public String getLastModifyUserName()
	{
		return lastModifyUserName;
	}
	/**
	 * @param lastModifyUserName the lastModifyUserName to set
	 */
	public void setLastModifyUserName(String lastModifyUserName)
	{
		this.lastModifyUserName = lastModifyUserName;
	}
	/**
	 * @return the domainProvinceId
	 */
	public String getDomainProvince()
	{
		return domainProvince;
	}
	/**
	 * @param domainProvince the domainProvince to set
	 */
	public void setDomainProvince(String domainProvince)
	{
		this.domainProvince = domainProvince;
	}
	/**
	 * @return the domainSn
	 */
	public String getDomainSn()
	{
		return domainSn;
	}
	/**
	 * @param domainSn the domainSn to set
	 */
	public void setDomainSn(String domainSn)
	{
		this.domainSn = domainSn;
	}
}