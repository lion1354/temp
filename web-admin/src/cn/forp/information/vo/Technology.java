package cn.forp.information.vo;

import java.io.Serializable;
import java.util.Date;

import cn.forp.framework.core.rowmapper.DBColumn;
import cn.forp.framework.core.rowmapper.DBTable;

/**
 * 新技术
 *
 * @author Apple
 * @version 2017-2-18 12:14:43
 */
@DBTable(name = "Sys_Technology")
public class Technology implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7445035195678662110L;

	private Long id;
	private Integer category;// 技术信息类别 '0拖拉机，1收割机，2农业机械'
	private String title;// 信息标题
	private String source;// 来源
	private String docId; // 文档ID
	@DBColumn(name="")
	private String info;// 信息详情介绍
	private String imageId;// 照片MongoDB文档编号
	private Date createDate;// 创建日期
	private String createUserName;// 创建人
	private Date lastModifyDate;// 修改日期(发布时间)
	private String lastModifyUserName;// 修改人
	private Long lastModifyUserId = -1L; // 修改人ID

	/**
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return the category
	 */
	public Integer getCategory()
	{
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(Integer category)
	{
		this.category = category;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the source
	 */
	public String getSource()
	{
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source)
	{
		this.source = source;
	}

	/**
	 * @return the info
	 */
	public String getInfo()
	{
		return info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(String info)
	{
		this.info = info;
	}

	/**
	 * @return the imageId
	 */
	public String getImageId()
	{
		return imageId;
	}

	/**
	 * @param imageId
	 *            the imageId to set
	 */
	public void setImageId(String imageId)
	{
		this.imageId = imageId;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate()
	{
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName()
	{
		return createUserName;
	}

	/**
	 * @param createUserName
	 *            the createUserName to set
	 */
	public void setCreateUserName(String createUserName)
	{
		this.createUserName = createUserName;
	}

	/**
	 * @return the lastModifyDate
	 */
	public Date getLastModifyDate()
	{
		return lastModifyDate;
	}

	/**
	 * @param lastModifyDate
	 *            the lastModifyDate to set
	 */
	public void setLastModifyDate(Date lastModifyDate)
	{
		this.lastModifyDate = lastModifyDate;
	}

	/**
	 * @return the lastModifyUserName
	 */
	public String getLastModifyUserName()
	{
		return lastModifyUserName;
	}

	/**
	 * @param lastModifyUserName
	 *            the lastModifyUserName to set
	 */
	public void setLastModifyUserName(String lastModifyUserName)
	{
		this.lastModifyUserName = lastModifyUserName;
	}

	/**
	 * @return the lastModifyUserId
	 */
	public Long getLastModifyUserId()
	{
		return lastModifyUserId;
	}

	/**
	 * @param lastModifyUserId
	 *            the lastModifyUserId to set
	 */
	public void setLastModifyUserId(Long lastModifyUserId)
	{
		this.lastModifyUserId = lastModifyUserId;
	}

	public String getDocId()
	{
		return docId;
	}

	public void setDocId(String docId)
	{
		this.docId = docId;
	}
}
