/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class数据库映射信息类
 *
 * @author	Bruce
 * @version	2016-3-31 14:50:25
 */
public class ClassMapping implements Serializable
{
	/**
	 * Table名称
	 */
	private String tableName;
	/**
	 * Table 主键列名称
	 */
	private String pkColumn = "ID";
	/**
	 * VO 主键属性名称
	 */
	private String pkProperty = "id";
	/**
	 * O/R mapping字段列表
	 * 		key    - 数据库字段名称（大写）
	 * 		value - Bean属性信息
	 */
	private Map<String, DBMappingProperty> fields = new HashMap<>();

	/**
	 * @return the tableName
	 */
	public String getTableName()
	{
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}
	/**
	 * @param columnName		数据库Table列名称
	 * @param p							VO属性信息
	 */
	public void addProperty(String columnName, DBMappingProperty p)
	{
		fields.put(columnName, p);
	}
	/**
	 * 是否有指定列的映射关系
	 * 
	 * @param columnName		数据库字段名称（大写）
	 * @return boolean
	 */
	public boolean hasProperty(String columnName)
	{
		return fields.containsKey(columnName);
	}
	/**
	 * 获取指定列的映射关系信息
	 * 
	 * @param columnName		数据库字段名称（大写）
	 * @return 映射关系
	 */
	public DBMappingProperty getProperty(String columnName)
	{
		return fields.get(columnName);
	}
	/**
	 * 获取所有映射字段信息
	 * 
	 * @return 映射字段对象
	 */
	public Map<String, DBMappingProperty> getAllProperties()
	{
		return fields;
	}
	/**
	 * @return the pkColumn
	 */
	public String getPkColumn()
	{
		return pkColumn;
	}
	/**
	 * @param pkColumn the pkColumn to set
	 */
	public void setPkColumn(String pkColumn)
	{
		this.pkColumn = pkColumn;
	}
	/**
	 * @return the pkProperty
	 */
	public String getPkProperty()
	{
		return pkProperty;
	}
	/**
	 * @param pkProperty the pkProperty to set
	 */
	public void setPkProperty(String pkProperty)
	{
		this.pkProperty = pkProperty;
	}
}