/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.thread;

/**
 * Thread基类
 *
 * @author	Bruce
 * @version	2012-08-30 14:29:52
 */
public abstract class BaseThread extends Thread
{
	/**
	 * 构造方法
	 */
	BaseThread(String name)
	{
		this.setName(name);
	}

	/**
	 * 停止Service
	 */
	public abstract void stopService();
}
