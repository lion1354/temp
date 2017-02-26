/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能描述
 *
 * @author		LiangLei
 * @version		2016年8月30日 上午10:26:04
 */
public class MenuJQTreeNode
{
	/**
	 * 权限名称
	 */
	private String name; 
	
	private String pId;
	private String id;
	
	/**
	 * 细粒度权限
	 */
	private List<String> td=new ArrayList<String>();

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
	 * @return the td
	 */
	public List<String> getTd()
	{
		return td;
	}

	/**
	 * @param td the td to set
	 */
	public void setTd(List<String> td)
	{
		this.td = td;
	}

	/**
	 * @return the pId
	 */
	public String getpId()
	{
		return pId;
	}

	/**
	 * @param pId the pId to set
	 */
	public void setpId(String pId)
	{
		this.pId = pId;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	
	
	
	
}
