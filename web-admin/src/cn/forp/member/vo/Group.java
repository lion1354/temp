package cn.forp.member.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class Group implements Serializable
{
	private static final long serialVersionUID = -2985290527552190858L;

	private Integer groupid;
	private String groupName;
	private String groupDesc;
	private Date createDate;
	private Date modifyDate;
	private Integer deleteFlag;
	private Set<User> user = new HashSet<User>();
	private Set<Role> role = new HashSet<Role>();

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getGroupDesc()
	{
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc)
	{
		this.groupDesc = groupDesc;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public Integer getDeleteFlag()
	{
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag)
	{
		this.deleteFlag = deleteFlag;
	}

	public Set<User> getUser()
	{
		return user;
	}

	public void setUser(Set<User> user)
	{
		this.user = user;
	}

	public Date getModifyDate()
	{
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate)
	{
		this.modifyDate = modifyDate;
	}

	public Set<Role> getRole()
	{
		return role;
	}

	public void setRole(Set<Role> role)
	{
		this.role = role;
	}

	public Integer getGroupid()
	{
		return groupid;
	}

	public void setGroupid(Integer groupid)
	{
		this.groupid = groupid;
	}

}
