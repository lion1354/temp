/**
 * Copyright © 2015, HuaYu iStudy Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.thread;

import cn.forp.framework.core.thread.BaseTaskEventThread;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

/**
 * 邮件发送后台服务
 *
 * @author		Bruce
 * @version	2012-08-31 10:54:26
 */
public class Mail extends BaseTaskEventThread<cn.forp.framework.platform.vo.Mail>
{
    /**
     * Logger for this class
     */
	private static final Logger logger = Logger.getLogger(Mail.class);
	/**
	 * Mail发送类
	 */
	private cn.forp.framework.core.util.Mail mail = null;
	/**
	 * 域配置参数
	 */
	private static JSONObject domainProfile = null;

	/**
	 * @param name
	 */
	public Mail(String name) throws Exception
	{
		super(name);
//
//		// 备份参数取Domain=1的企业
//		domainProfile = FORP.getCachedObject(FORP.CACHE_DOMAIN_PROFILE + 1, null);
//
//		mail = new cn.forp.framework.core.util.Mail(domainProfile.getJSONObject("globalParams").getString("11"), domainProfile.getJSONObject("globalParams").getString("10"),
//				domainProfile.getJSONObject("globalParams").getString("12"), domainProfile.getJSONObject("globalParams").getString("13"));
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
	public void doWork(cn.forp.framework.platform.vo.Mail task) throws Exception
	{
//		// 开发模式下面不真正发送SMS
//		if ("dev".equalsIgnoreCase(FORP.STATUS))
//		{
//			logger.info("模拟发送邮件：" + task.getSubject() + "\r\n" + task.getBody());
//			return;
//		}
//
//		logger.debug("发送邮件：" + task.getSubject() + "\r\n" + task.getBody());
//		// 尝试发送邮件
//		Exception ex = null;
//		while (task.getRetryTimes() <= domainProfile.getJSONObject("globalParams").getIntValue("14"))
//		{
//			try
//			{
//				mail.sendMail(task.getMailAddress(), null, task.getSubject(), task.getBody(), null);
//				ex = null;
//				logger.info("邮件“" + task.getSubject() + "”发送成功！");
//			}
//			catch (Exception e)
//			{
//				ex = e;
//				task.setRetryTimes(task.getRetryTimes() + 1);
//				logger.warn("邮件“" + task.getSubject() + "”第" + task.getRetryTimes() + "次尝试发送邮件失败：" + ex.toString());
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
//			((SystemService) FORP.SPRING_CONTEXT.getBean("platformSystemService")).writeWarningLog(1, 2, result.toString());
//		}
	}
}