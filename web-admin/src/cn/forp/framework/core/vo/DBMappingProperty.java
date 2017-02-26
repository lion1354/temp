/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.vo;

import java.io.Serializable;

/**
 * O/R mapping VO属性类
 *
 * @author	Bruce
 * @version	2016-3-31 14:51:02
 */
public class DBMappingProperty implements Serializable
{
	/**
	 * 属性名称
	 */
	private String name;
	/**
	 * 属性字段类型Class
	 */
	private Class<?> type;

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
	 * @return the type
	 */
	public Class<?> getType()
	{
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Class<?> type)
	{
		this.type = type;
	}
}