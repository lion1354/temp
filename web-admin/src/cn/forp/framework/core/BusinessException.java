/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core;

/**
 * 业务异常类（系统只设置一个业务异常类，不同业务异常的提示信息通过构造的参数进行区别，前台页面直接使用该参数内容作为提示信息）
 *
 * @author	Bruce
 * @version	2016-3-31 14:37:30
 */
public class BusinessException extends Exception
{
	/**
	 * 默认构造函数
	 */
	public BusinessException()
	{
		super();
	}
	
	/**
	 * @param message
	 */
	public BusinessException(String message)
	{
		super(message);
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public BusinessException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	/**
	 * @param cause
	 */
	public BusinessException(Throwable cause)
	{
		super(cause);
	}
}