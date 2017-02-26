package cn.forp.member.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Resource implements Serializable
{
	private static final long serialVersionUID = 3020618868068116833L;

	private Integer id;
	private String name;
	private String comment;
	private String url;
	private Integer isMenu;// 0:menu,1:not menu
	private Integer paramid;
	private Date createDate;
	private Date modifyDate;
	private Integer parentid = 9999;
	private Set<Resource> childResources = new HashSet<Resource>();
	// for UI
	private List<Resource> childResourcesList = new ArrayList<Resource>();
	private Set<Role> roles = new HashSet<Role>();

	public Resource()
	{
	}

	public Resource(Integer id, String name, String comment, Date createDate)
	{
		super();
		this.id = id;
		this.name = name;
		this.comment = comment;
		this.createDate = createDate;
	}

	public Resource(String name, String comment, String url, Date createDate)
	{
		this.name = name;
		this.comment = comment;
		this.url = url;
		this.createDate = createDate;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public Integer getId()
	{
		return this.id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public Set<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}

	public Date getModifyDate()
	{
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate)
	{
		this.modifyDate = modifyDate;
	}

	public Integer getIsMenu()
	{
		return isMenu;
	}

	public void setIsMenu(Integer isMenu)
	{
		this.isMenu = isMenu;
	}

	public Integer getParamid()
	{
		return paramid;
	}

	public void setParamid(Integer paramid)
	{
		this.paramid = paramid;
	}

	public Integer getParentid()
	{
		return parentid;
	}

	public void setParentid(Integer parentid)
	{
		this.parentid = parentid;
	}

	public Set<Resource> getChildResources()
	{
		return childResources;
	}

	public void setChildResources(Set<Resource> childResources)
	{
		this.childResources = childResources;
	}

	public List<Resource> getChildResourcesList()
	{
		return childResourcesList;
	}

	public void setChildResourcesList(List<Resource> childResourcesList)
	{
		this.childResourcesList = childResourcesList;
	}

}