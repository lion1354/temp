/**
 * Copyright © 2016, AnDong
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform;

import cn.forp.framework.core.FORP;
import cn.forp.framework.core.thread.BaseThread;
import cn.forp.framework.core.util.QiNiu;
import cn.forp.framework.platform.service.SystemService;
import com.alibaba.media.MediaConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.io.FileUtils;
import org.apache.oro.text.regex.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.List;

/**
 * 系统初始化类
 *
 * @author	Bruce
 * @version	2016-3-31 14:32:23
 */
public class SystemInit extends HttpServlet
{
    /**
     * Logger
     */
    private static final Logger lg = LoggerFactory.getLogger(SystemInit.class);
	/**
	 * system-config.xml
	 */
	private Configuration systemConfigXML;
	/**
	 * spring.xml
	 */
	private Configuration springXML;

	/**
	 * 系统初始化
	 */
	public SystemInit(){}

	/**
	 * 系统初始化
	 */
	public void init() throws ServletException
	{
		lg.info("初始化系统运行参数......");
		try
		{
			// Spring Context
			FORP.SPRING_CONTEXT = WebApplicationContextUtils.getWebApplicationContext(getServletContext());

			// Web应用发布路径
			FORP.WEB_APP_PATH = getServletContext().getRealPath("/");
			if (FORP.WEB_APP_PATH.endsWith("\\"))
			{
				FORP.WEB_APP_PATH = FORP.WEB_APP_PATH.substring(0, FORP.WEB_APP_PATH.length() - 1);
			}
			lg.info("Web应用磁盘路径：{}", FORP.WEB_APP_PATH);

			// 初始化XML配置参数
			Configurations cfg = new Configurations();
			systemConfigXML = cfg.xml(FORP.WEB_APP_PATH + "/WEB-INF/system-config.xml");
			springXML = cfg.xml(FORP.WEB_APP_PATH + "/WEB-INF/spring.xml");
			initXMLParameter();

//			// 顽兔平台
//			if ("alibaba".equalsIgnoreCase(FORP.ATTACHMENT_ENGINE))
//				initAliBaBa();
//
//			// 七牛云存储平台
//			if ("qiniu".equalsIgnoreCase(FORP.ATTACHMENT_ENGINE))
				initQiNiu();

			SystemService service = FORP.SPRING_CONTEXT.getBean(SystemService.class);
			service.loadDomainProfileCache(1L);

			initThread();

			// 清理临时文件夹文件
			FileUtils.cleanDirectory(new File(FORP.WEB_APP_PATH + "/disk-file/temp"));
		}
		catch (Exception e)
		{
			lg.error("系统配置参数加载失败：", e);
		}
	}

	/**
	 * 初始化XML系统参数
	 */
	private void initXMLParameter() throws Exception
	{
		// 1 system-config.xml - 系统参数
		FORP.STATUS = systemConfigXML.getString("status").trim();
		lg.info("运行状态：{}", FORP.STATUS);
		FORP.ATTACHMENT_ENGINE = systemConfigXML.getString("attachment-engine").trim();
		lg.info("附件存储引擎：{}", FORP.ATTACHMENT_ENGINE);

		// 2 spring.xml - 数据库配置
		Pattern pt = null;
		String url = springXML.getString("bean.property(0)[@value]").toLowerCase();
		if (url.contains("mysql"))
		{
			// jdbc:mysql://192.168.1.100:3306/NewStarEdu?zeroDateTimeBehavior=convertToNull
			FORP.DB_TYPE = "mysql";
			pt = new Perl5Compiler().compile("jdbc:mysql://([^:]+):(\\d+)/([^\\?]+)");

		}
		else
			if (url.contains("oracle"))
			{
				// jdbc:oracle:thin:@192.168.100.151:1521:FORP
				FORP.DB_TYPE = "oracle";
				pt = new Perl5Compiler().compile("jdbc:oracle:thin:@([^:]+):(\\d+):(\\w+)");
			}
			else
				FORP.DB_TYPE = "unknown";

		PatternMatcher pm = new Perl5Matcher();
		if (pm.contains(url, pt))
		{
			MatchResult mr = pm.getMatch();
			FORP.DB_IP = mr.group(1);
			FORP.DB_PORT = mr.group(2);
			FORP.DB_NAME = mr.group(3);
		}

		FORP.DB_USER_NAME = springXML.getString("bean.property(1)[@value]");
		FORP.DB_PASSWORD = springXML.getString("bean.property(2)[@value]");
		lg.info("数据库：{}[{}]", FORP.DB_NAME, FORP.DB_TYPE);
		lg.debug("{}:{}/{}", FORP.DB_IP, FORP.DB_PORT, FORP.DB_USER_NAME);
		lg.info("初始化XML配置参数[OK]");
	}

	/**
	 * 初始化阿里巴巴参数
	 */
	private void initAliBaBa() throws Exception
	{
		FORP.ALIBABA_MEDIA_CFG = new MediaConfiguration();
		FORP.ALIBABA_MEDIA_CFG.setAk(systemConfigXML.getString("alibaba-media.ak"));
		FORP.ALIBABA_MEDIA_CFG.setSk(systemConfigXML.getString("alibaba-media.sk"));
		FORP.ALIBABA_MEDIA_CFG.setNamespace(systemConfigXML.getString("alibaba-media.name-space"));
		lg.info("阿里百川顽兔服务参数初始化[OK]");
	}

	/**
	 * 初始化七牛云存储参数
	 */
	private void initQiNiu() throws Exception
	{
		QiNiu.init(systemConfigXML.getString("qiniu.ak"), systemConfigXML.getString("qiniu.sk"),
				systemConfigXML.getString("qiniu.bucket-name"), systemConfigXML.getString("qiniu.domain-name"));
		lg.info("七牛云存储服务参数初始化[OK]");
	}

	/**
	 * 初始化后台Thread服务
	 */
	private void initThread() throws Exception
	{
		List<Object> threads = systemConfigXML.getList("threads.thread");

		if (null != threads && threads.size() > 0)
		{
			BaseThread service;
			String clazz, name, start;
			for (int i = 0; i < threads.size(); i++)
			{
				try
				{
					clazz = (String) threads.get(i);
					name = systemConfigXML.getString("threads.thread(" + i + ")[@name]");
					start = systemConfigXML.getString("threads.thread(" + i + ")[@start]");

					service = (BaseThread)(Class.forName(clazz.trim()).getConstructor(new Class[]{String.class}).newInstance(name));
					if ("true".equalsIgnoreCase(start.trim()))
						service.start();

					FORP.THREADS.put(clazz, service);
				}
				catch (Exception e)
				{
					lg.error("Thread初始化失败：", e);
				}
			}
		}

		lg.info("初始化后台Threads参数[OK]");
	}

	/**
	 * 系统退出（释放资源）
	 */
    public void destroy()
    {
		try
		{
			// 服务
			for (String s : FORP.THREADS.keySet())
			{
				FORP.THREADS.get(s).stopService();
			}

			// JDBC Drivers
			Enumeration<Driver> drivers = DriverManager.getDrivers();
			Driver d;
	        while (drivers.hasMoreElements())
	        {
	             d = drivers.nextElement();
	             DriverManager.deregisterDriver(d);
	             lg.info("释放JDBC驱动：{}", d);
	        }
		}
		catch (Exception e)
		{
			lg.error("系统退出错误：", e);
		}
    }
}