/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.vo;

import java.io.Serializable;

/**
 * 分页和排序类
 *
 * @author	Bruce
 * @version	2015-09-09 16:26:02
 */
public class PageSort implements Serializable
{
	public final static String PAGE_NUMBER = "pageNumber";
	public final static String PAGE_SIZE = "pageSize";
	public final static String SORT_NAME = "sortName";
	public final static String SORT_ORDER = "sortOrder";

	// 当前页号
	private long pageNumber = 1;
	// 每页记录数：-1 - 不分页，查询所有记录
	private int pageSize = 20;
	// 排序字段名称
	private String sortName;
	// 排序方向
	private String sortOrder;

	/**
	 * @return the pageNumber
	 */
	public long getPageNumber()
	{
		return pageNumber;
	}
	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(long pageNumber)
	{
		this.pageNumber = pageNumber;
	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize()
	{
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}
	/**
	 * @return the sortName
	 */
	public String getSortName()
	{
		return sortName;
	}
	/**
	 * @param sortName the sortName to set
	 */
	public void setSortName(String sortName)
	{
		this.sortName = sortName;
	}
	/**
	 * @return the sortOrder
	 */
	public String getSortOrder()
	{
		return sortOrder;
	}
	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(String sortOrder)
	{
		this.sortOrder = sortOrder;
	}
}