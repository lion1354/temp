/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页类
 *
 * @author	Bruce
 * @version	2012-3-19 10:20:13
 */
public class Page<T> implements Serializable
{
	private long total = 0;
	private List<T> rows;
	private String remark = "";

	/**
	 * @param rows	The data to set
	 */
	public void setRows(List<T> rows)
	{
		this.rows = rows;
	}
	
	/**
	 * @return the data
	 */
	public List<T> getRows()
	{
		return rows;
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
	 * @return the total
	 */
	public long getTotal()
	{
		return total;
	}


	/**
	 * @param total the total to set
	 */
	public void setTotal(long total)
	{
		this.total = total;
	}
}