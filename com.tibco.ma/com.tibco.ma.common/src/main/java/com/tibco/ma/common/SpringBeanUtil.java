package com.tibco.ma.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * @author aidan
 *
 * 2015/5/29
 *
 */
@Component("springBeanUtil")
public final class SpringBeanUtil implements ApplicationContextAware {

	private static ApplicationContext ctx;

	public static Object getBean(String id) {
		if (ctx == null) {
			throw new NullPointerException("ApplicationContext is null");
//			ctx = new ClassPathXmlApplicationContext(
//					"classpath:spring/applicationContext.xml");
		}
		return ctx.getBean(id);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ctx = applicationContext;
	}

}
