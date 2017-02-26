/**
 * Copyright © 2015, HuaYu iStudy Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.thread;

import cn.forp.framework.core.thread.BaseTaskEventThread;
import cn.forp.framework.platform.thread.smsprovider.ISMSProvider;
import org.apache.log4j.Logger;

/**
 * 短信发送后台服务
 *
 * @author		Bruce
 * @version	2012-08-31 11:30:31
 */
public class SMS extends BaseTaskEventThread<cn.forp.framework.platform.vo.SMS>
{
    /**
     * Logger for this class
     */
	private static final Logger logger = Logger.getLogger(SMS.class);
	/**
	 * 短信Provider
	 */
	private ISMSProvider sp = null;

	/**
	 * @param name
	 */
	public SMS(String name)
	{
		super(name);

//		// 加载默认的短信提供商
//		try
//		{
//			XML xml = new XML(FORP.WEB_APP_PATH + "/WEB-INF/system-config.xml");
//			String className = xml.getElement("sms-provider").getAttributeValue("class").trim();
//			sp = (ISMSProvider) Class.forName(className).newInstance();
//
//			logger.info("短信Provider“" + xml.getElement("sms-provider").getAttributeValue("name") + "”初始化成功！");
//		}
//		catch (Exception e)
//		{
//			logger.error("短信Provider加载失败：", e);
//		}
	}

	/* (non-Javadoc)
	 * @see com.chinasoftware.core.thread.BaseTaskEventThread#getWorkers()
	 */
	@Override
	public short getWorkers()
	{
		// 默认2个【TODO 后期改为系统全局参数】
		return 2;
	}

	/* (non-Javadoc)
	 * @see com.chinasoftware.core.thread.BaseTaskEventThread#doWork(java.lang.Object)
	 */
	@Override
	public void doWork(cn.forp.framework.platform.vo.SMS task) throws Exception
	{
//		// 开发模式下面不真正发送SMS
//		if ("dev".equalsIgnoreCase(FORP.STATUS))
//		{
//			logger.info("模拟发送" + task.getMobile() + "的短信：\r\n" + task.getContent());
//			return;
//		}
//
//		// 尝试发送
//		Exception ex = null;
//		SystemService service = (SystemService) FORP.SPRING_CONTEXT.getBean("platformSystemService");
//		// 使用域的短信账号参数
//		JSONObject dp = FORP.getCachedObject(FORP.CACHE_DOMAIN_PROFILE + task.getDomainId(), null);
//		int retryTimes = dp.getJSONObject("globalParams").getIntValue("24");
//		logger.debug("SMS.retryTimes-" + task.getRetryTimes() + "，Global.retryTimes-" + retryTimes);
//		while (task.getRetryTimes() <= retryTimes)
//		{
//			try
//			{
//				logger.debug("发送短信：\r\n" + task.getContent());
//				sp.send(dp, task);
//
//				ex = null;
//				logger.info(task.getMobile() + "的短信发送成功！");
//			}
//			catch (Exception e)
//			{
//				ex = e;
//				task.setRetryTimes(task.getRetryTimes() + 1);
//				logger.warn(task.getMobile() + "的短信第" + task.getRetryTimes() + "次尝试发送失败：" + ex.toString());
//
//				try
//				{
//					sleep(5 * 1000);
//				}
//				catch (Exception e1){}
//			}
//
//			if (null == ex)
//				break;
//		}
//
//		// 记录告警日志
//		if (null != ex)
//		{
//			Writer result = new StringWriter();
//			PrintWriter pw = new PrintWriter(result);
//			ex.printStackTrace(pw);
//
//			service.writeWarningLog(2, 2, result.toString());
//		}
	}
}