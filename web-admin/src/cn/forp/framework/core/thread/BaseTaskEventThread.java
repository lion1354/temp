/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于任务事件触发的Thread基类（多Worker）
 *
 * @author	Bruce
 * @version	2012-08-30 14:32:25
 */
public abstract class BaseTaskEventThread<T> extends BaseThread
{
	/**
	 * Logger for this class
	 */
	private static final Logger lg = LoggerFactory.getLogger(BaseTaskEventThread.class);
	/**
	 * 结束运行标志
	 */
	private static boolean exitNow = false;
	/**
	 * 任务列表资源锁
	 */
	private static final ReentrantLock Lock = new ReentrantLock();
	/**
	 * 任务列表资源锁Condition
	 */
	private static final Condition HasTask = Lock.newCondition();
	/**
	 * 发送任务列表
	 */
	private List<T> Tasks = new ArrayList<>();
//	/**
//	 * Worker工人列表
//	 */
//	private List<Worker> workers = new ArrayList<>();

	/**
	 * 构造函数
	 * 
	 * @param name			服务名称
	 */
	public BaseTaskEventThread(String name)
	{
		super(name);

		// 启动Workers
		short workers = getWorkers();
		Worker worker;
		for (int i = 0; i < workers; i++)
		{
			worker = new Worker("Worker-" + String.valueOf((i + 1)));
			worker.start();

//		this.workers.add(worker);
		}
	}

	/**
	 * 停止Service
	 */
	public void stopService()
	{
		exitNow = true;

		// 唤醒处于wait状态的线程，准备退出
		Lock.lock();
		try
		{
			// 唤醒Workers
			HasTask.signalAll();
			// 唤醒主线程
			synchronized(this)
			{
				this.notify();
			}
		}
		finally
		{
			Lock.unlock();
		}
	}

	/**
	 * 添加新任务
	 * 
	 * @param task		新任务
	 */
	public void addTask(T task)
	{
		Lock.lock();
		try
		{
			Tasks.add(task);
			lg.info("接收到1个新任务");

			// 发送通知
			HasTask.signalAll();
		}
		finally
		{
			Lock.unlock();
		}
	}

	/**
	 * 批量添加新发送任务
	 * 
	 * @param tasks   任务列表
	 */
	public void addTask(List<T> tasks)
	{
		Lock.lock();
		try
		{
			Tasks.addAll(tasks);
			lg.info("接收到{}个新任务", tasks.size());

			// 发送通知
			HasTask.signalAll();
		}
		finally
		{
			Lock.unlock();
		}
	}

	/**
	 * 获取发送任务
	 */
	public T getTask()
	{
		Lock.lock();

		try
		{	
			while (0 == Tasks.size())
			{
				// 检测退出标志
				if (exitNow)
					return null;

				lg.info("等待新工作任务！");
				HasTask.await();
			}

			// 获取任务
			T task = Tasks.get(0);
			Tasks.remove(0);

			return task;
		}
		catch (Exception e)
		{
			lg.error("工作任务获取错误：", e);
		}
		finally
		{
			Lock.unlock();
		}

		return null;
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
				synchronized(this)
				{
					this.wait();
				}
			}
			catch (Exception e)
			{
				lg.error("调度线程运行错误：", e);
			}
		}

		lg.info("后台服务【{}】结束运行！", getName());
	}

	//=================================================================
	//		抽象方法
	//=================================================================

	/**
	 * 获取工作线程个数
	 * 
	 * @return 工作线程数量
	 */
	public abstract short getWorkers();

	/**
	 * 执行工作任务
	 */
	public abstract void doWork(T t) throws Exception;

	//=================================================================
	//		Worker内部类
	//=================================================================

	/**
	 * Worker类
	 *
	 * @author	Bruce
	 * @version	2012-08-30 14:32:25
	 */
	class Worker extends Thread
	{
		/**
		 * 构造函数
		 * 
		 * @param name  工作者名称
		 */
		public Worker(String name)
		{
			this.setName(name);
			lg.info("{}启动开始工作！", getName());
		}

		/**
		 * 启动线程
		 */
		public void run()
		{
			while (!exitNow)
			{
				try
				{
					T task = getTask();	
					if (null != task)
						doWork(task);
				}
				catch (Exception e)
				{
					lg.error("任务执行失败：", e);
				}
			}

			lg.info("{}结束工作！", getName());
		}
	}
}