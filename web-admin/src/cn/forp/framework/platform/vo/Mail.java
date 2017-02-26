/**
 * Copyright © 2015, HuaYu iStudy Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.vo;

import java.io.Serializable;

/**
 * 邮件类
 *
 * @author	Bruce
 * @version	2012-3-23 14:39:48
 */
public class Mail implements Serializable
{
	private String subject;
	private String mailAddress;
	private String body;
	private int retryTimes = 0;

	/**
	 * @return the subject
	 */
	public String getSubject()
	{
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	/**
	 * @return the mailAddress
	 */
	public String getMailAddress()
	{
		return mailAddress;
	}
	/**
	 * @param mailAddress the mailAddress to set
	 */
	public void setMailAddress(String mailAddress)
	{
		this.mailAddress = mailAddress;
	}
	/**
	 * @return the body
	 */
	public String getBody()
	{
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body)
	{
		this.body = body;
	}
	/**
	 * @return the retryTimes
	 */
	public int getRetryTimes()
	{
		return retryTimes;
	}
	/**
	 * @param retryTimes the retryTimes to set
	 */
	public void setRetryTimes(int retryTimes)
	{
		this.retryTimes = retryTimes;
	}
}