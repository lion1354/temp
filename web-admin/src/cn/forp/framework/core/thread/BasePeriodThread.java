/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 周期性执行的Thread基类（多Worker）
 *
 * @author	Bruce
 * @version	2012-08-30 14:32:25
 */
public abstract class BasePeriodThread extends BaseThread
{
	/**
	 * Logger for this class
	 */
	private static final Logger lg = LoggerFactory.getLogger(BasePeriodThread.class);
	/**
	 * 结束运行标志
	 */
	private static boolean exitNow = false;
	/**
	 * 轮询间隔时间（单位：秒）
	 */
	private long waitSeconds = 60;

	/**
	 * 构造函数
	 * 
	 * @param name					服务名称
	 */
	public BasePeriodThread(String name)
	{
		super(name);
		waitSeconds = getWaitSeconds();
	}

	/* (non-Javadoc)
	 * @see com.chinasoftware.core.thread.BaseThread#stopService()
	 */
	@Override
	public void stopService()
	{
		exitNow = true;
		// 唤醒线程
		synchronized(this)
		{
			this.notify();
		}
	}

	/**
	 * 启动线程
	 */
	public void run()
	{
		lg.info("后台服务【{}】启动就绪！", getName());

		while (!exitNow)
		{
			try
			{
				// 开始执行动作
				long beginTime = System.currentTimeMillis();
				doWork();
				lg.debug("【{}】任务执行时间：{} ms", getName(), System.currentTimeMillis() - beginTime);

				// 休眠
				synchronized(this)
				{
					this.wait(waitSeconds * 1000);
				}
			}
			catch (Exception e)
			{
				lg.error("后台服务【" + getName() + "】运行错误：", e);

				try
				{
					// 休眠1分钟
					synchronized(this)
					{
						this.wait(60 * 1000);
					}
				}
				catch (Exception ex){}
			}
		}

		lg.info("后台服务【{}】结束运行！", getName());
	}

	//=================================================================
	//		抽象方法
	//=================================================================

	/**
	 * 获取轮询间隔时间（单位：秒）
	 * 
	 * @return 间隔时间
	 */
	public abstract long getWaitSeconds();

	/**
	 * 执行定期动作
	 * TODO 添加同步锁，保证只有一个Tomcat实例会运行
	 */
	public abstract void doWork() throws Exception;
}