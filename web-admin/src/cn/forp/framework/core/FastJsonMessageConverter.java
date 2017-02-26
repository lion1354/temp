/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

/**
 * FastJson for Spring MVC Message Converter
 *
 * @author	LiangLei
 * @version	2016年8月23日 上午9:58:25
 */
public class FastJsonMessageConverter extends FastJsonHttpMessageConverter
{
	@Override
  protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException
  {
    JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    // JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat);
    super.writeInternal(obj, outputMessage);
  }
}