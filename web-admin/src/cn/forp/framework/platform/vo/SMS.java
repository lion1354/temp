/**
 * Copyright © 2015, HuaYu iStudy Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.vo;

import java.io.Serializable;

/**
 * 短信类
 *
 * @author	Bruce
 * @version	2012-3-23 15:42:35
 */
public class SMS implements Serializable
{
	private Long domainId;
	private Long businessId;
	private String mobile;
	private String content;
	private int retryTimes = 0;

	/**
	 * @return the mobile
	 */
	public String getMobile()
	{
		return mobile;
	}
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
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
	/**
	 * @return the domainId
	 */
	public Long getDomainId()
	{
		return domainId;
	}
	/**
	 * @param domainId the domainId to set
	 */
	public void setDomainId(Long domainId)
	{
		this.domainId = domainId;
	}
	/**
	 * @return the businessId
	 */
	public Long getBusinessId()
	{
		return businessId;
	}
	/**
	 * @param businessId the businessId to set
	 */
	public void setBusinessId(Long businessId)
	{
		this.businessId = businessId;
	}
}