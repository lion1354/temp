/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.vo;

import java.io.Serializable;

/**
 * 系统信息类
 *
 * @author	Bruce
 * @version	2012-3-19 上午10:18:59
 */
public class Message implements Serializable
{
	/**
	 * 信息类型：
	 * 1－information
	 * 2－warning
	 * 3－error
	 */
	private int type = 1;
	/**
	 * 信息内容
	 */
	private String content = "";
	
	/**
	 * 构造方法
	 *  
	 * @param str	信息内容
	 */
	public Message(String str)
	{
		content = str;
	}	
	/**
	 * @return Returns the type.
	 */
	public int getType()
	{
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type)
	{
		this.type = type;
	}
	/**
	 * @return Returns the content.
	 */
	public String getContent()
	{
		return content;
	}
}
