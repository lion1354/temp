package cn.forp.member.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Area implements Serializable
{

	private static final long serialVersionUID = -8471210283037187830L;

	private Integer areaId;
	private String areaName;
	private Set<User> userSet = new HashSet<User>();
	private List<Xian> xianList = new ArrayList<Xian>();

	public List<Xian> getXianList()
	{
		return xianList;
	}

	public void setXianList(List<Xian> xianList)
	{
		this.xianList = xianList;
	}

	public Area()
	{
	}

	public Integer getAreaId()
	{
		return areaId;
	}

	public void setAreaId(Integer areaId)
	{
		this.areaId = areaId;
	}

	public String getAreaName()
	{
		return areaName;
	}

	public void setAreaName(String areaName)
	{
		this.areaName = areaName;
	}

	public Set<User> getUserSet()
	{
		return userSet;
	}

	public void setUserSet(Set<User> userSet)
	{
		this.userSet = userSet;
	}

}