package cn.forp.member.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Role implements Serializable
{
	private static final long serialVersionUID = -6375965935980121349L;

	private Integer roleid;
	private String roleName;
	private String roleDesc;
	private Date createDate;
	private Date modifyDate;
	private Integer deleteFlag;
	private Set<Group> group;
	private Set<User> user;
	private Set<Resource> resources = new HashSet<Resource>();

	public Role()
	{
	}

	public Role(String name, String desc, Date createDate)
	{
		super();
		this.roleName = name;
		this.roleDesc = desc;
		this.createDate = createDate;
	}

	public Integer getRoleid()
	{
		return roleid;
	}

	public void setRoleid(Integer roleid)
	{
		this.roleid = roleid;
	}

	public String getRoleName()
	{
		return roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public String getRoleDesc()
	{
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc)
	{
		this.roleDesc = roleDesc;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public Date getModifyDate()
	{
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate)
	{
		this.modifyDate = modifyDate;
	}

	public Integer getDeleteFlag()
	{
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag)
	{
		this.deleteFlag = deleteFlag;
	}

	public Set<Group> getGroup()
	{
		return group;
	}

	public void setGroup(Set<Group> group)
	{
		this.group = group;
	}

	public Set<User> getUser()
	{
		return user;
	}

	public void setUser(Set<User> user)
	{
		this.user = user;
	}

	public Set<Resource> getResources()
	{
		return resources;
	}

	public void setResources(Set<Resource> resources)
	{
		this.resources = resources;
	}

}
