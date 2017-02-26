/**
 * Copyright © 2015, HuaYu iStudy Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.vo;

import cn.forp.framework.core.rowmapper.DBColumn;
import cn.forp.framework.core.rowmapper.DBTable;

import java.io.Serializable;

/**
 * 提醒接收人
 *
 * @author	Bruce
 * @version	2014-02-27 16:32:35
 */
@DBTable(name="Forp_Remind_Member")
public class RemindMember implements Serializable
{
	@DBColumn(isPrimaryKey=true)
	private Long id;
	@DBColumn(name="FK_RemindID")
	private Long remindId;
	@DBColumn(name="FK_UserID")
	private Long userId;
	@DBColumn(name="")
	private String userName;

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
	 * @return the remindId
	 */
	public Long getRemindId()
	{
		return remindId;
	}
	/**
	 * @param remindId the remindId to set
	 */
	public void setRemindId(Long remindId)
	{
		this.remindId = remindId;
	}
	/**
	 * @return the userId
	 */
	public Long getUserId()
	{
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
}