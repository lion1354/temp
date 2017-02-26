package cn.forp.insurance.vo;

import java.io.Serializable;
import java.util.Date;

import cn.forp.framework.core.rowmapper.DBColumn;
import cn.forp.framework.core.rowmapper.DBTable;

/**
 * 会员信息
 * 
 * @author Apple
 */
@DBTable(name = "sys_member_info")
public class Member implements Serializable
{
	private static final long serialVersionUID = -1900276934560883069L;

	@DBColumn(isPrimaryKey = true)
	private Long id;
	private String mobilePhone;// 账号(手机号码)
	private String password;// 密码
	private Integer type;// 用户类型 1－个人用户;2－企业用户
	private String userName;// 真实姓名
	private String idNumber;// 身份证号
	private String enterpriseName;// 企业名称
	private String corporateName;// 法人姓名
	private String organizationCode;// 组织机构码
	private String creditCode;// 统一社会信用码
	private Date createDate;// 创建日期
	private String createUserName;// 创建人
	private Date lastModifyDate;// 修改日期
	private String lastModifyUserName;// 修改人
	private Long lastModifyUserID;// 修改人ID
	private String remark;// 备注

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
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return the type
	 */
	public Integer getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Integer type)
	{
		this.type = type;
	}

	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * @param username
	 *            the userName to set
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	/**
	 * @return the idNumber
	 */
	public String getIdNumber()
	{
		return idNumber;
	}

	/**
	 * @param idNumber
	 *            the idNumber to set
	 */
	public void setIdNumber(String idNumber)
	{
		this.idNumber = idNumber;
	}

	/**
	 * @return the enterpriseName
	 */
	public String getEnterpriseName()
	{
		return enterpriseName;
	}

	/**
	 * @param enterpriseName
	 *            the enterpriseName to set
	 */
	public void setEnterpriseName(String enterpriseName)
	{
		this.enterpriseName = enterpriseName;
	}

	/**
	 * @return the corporateName
	 */
	public String getCorporateName()
	{
		return corporateName;
	}

	/**
	 * @param corporateName
	 *            the corporateName to set
	 */
	public void setCorporateName(String corporateName)
	{
		this.corporateName = corporateName;
	}

	/**
	 * @return the organizationCode
	 */
	public String getOrganizationCode()
	{
		return organizationCode;
	}

	/**
	 * @param organizationCode
	 *            the organizationCode to set
	 */
	public void setOrganizationCode(String organizationCode)
	{
		this.organizationCode = organizationCode;
	}

	/**
	 * @return the creditCode
	 */
	public String getCreditCode()
	{
		return creditCode;
	}

	/**
	 * @param creditCode
	 *            the creditCode to set
	 */
	public void setCreditCode(String creditCode)
	{
		this.creditCode = creditCode;
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
	 * @return the lastModifyUserID
	 */
	public Long getLastModifyUserID()
	{
		return lastModifyUserID;
	}

	/**
	 * @param lastModifyUserID
	 *            the lastModifyUserID to set
	 */
	public void setLastModifyUserID(Long lastModifyUserID)
	{
		this.lastModifyUserID = lastModifyUserID;
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