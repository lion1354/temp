/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串工具类
 *
 * @author	Bruce
 * @version	2016-03-31 14:36:52
 */
public final class StringUtil
{
    /**
     * Logger
     */
	private static final Logger lg = LoggerFactory.getLogger(StringUtil.class);

	private StringUtil() {}

	/**
	 * 
	 */
	/**
	 * 将带格式的普通文本转换为Html格式（\n，空格）
	 * 
	 * @param text	文本内容
	 * @return
	 */
	public static String textToHtml(String text)
	{
		lg.debug("text：{}", text);

		if (StringUtils.isNoneBlank(text))
			return text.replaceAll("\n", "<br/>").replaceAll(" ", "&nbsp;");
		else
			return text;
	}

	/**
	 * 将Html格式转换为带格式的普通文本（<br/>，&nbsp;）
	 */
	public static String htmlToText(String html)
	{
		lg.debug("html：{}", html);

		if (StringUtils.isNoneBlank(html))
			return html.replaceAll("<br/>", "\n").replaceAll("&nbsp;", " ");
		else
			return html;
	}

	/**
	 * 测试方法
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
//		System.out.println(StringEscapeUtils.escapeHtml4("<div>abc<br/>123"));
	}
}