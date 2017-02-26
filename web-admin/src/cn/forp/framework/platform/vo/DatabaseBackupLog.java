/**
 * Copyright © 2015, HuaYu iStudy Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.vo;

import cn.forp.framework.core.rowmapper.DBColumn;
import cn.forp.framework.core.rowmapper.DBTable;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据库备份日志类
 *
 * @author		Bruce
 * @version	2012-3-23 14:45:55
 */
@DBTable(name="Forp_DBBackup")
public class DatabaseBackupLog implements Serializable
{
	@DBColumn(isPrimaryKey=true)
	private Long id;
	private Date createDate;
	private String fileName;
	private Long fileSize;
	private Date beginTime;
	private Date endTime;
	private Integer result;
	private String remark;

	/**
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
	/**
	 * @return the createDate
	 */
	// @JsonFormat(pattern = FORP.PATTERN_DATE_TIME, timezone = "GMT+8")
	public Date getCreateDate()
	{
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	/**
	 * @return the fileSize
	 */
	public Long getFileSize()
	{
		return fileSize;
	}
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(Long fileSize)
	{
		this.fileSize = fileSize;
	}
	/**
	 * @return the beginTime
	 */
	// @JsonFormat(pattern = FORP.PATTERN_DATE_TIME, timezone = "GMT+8")
	public Date getBeginTime()
	{
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(Date beginTime)
	{
		this.beginTime = beginTime;
	}
	/**
	 * @return the endTime
	 */
	// @JsonFormat(pattern = FORP.PATTERN_DATE_TIME, timezone = "GMT+8")
	public Date getEndTime()
	{
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	/**
	 * @return the result
	 */
	public Integer getResult()
	{
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(Integer result)
	{
		this.result = result;
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
}