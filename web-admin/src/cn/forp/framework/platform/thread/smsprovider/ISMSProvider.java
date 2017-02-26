/**
 * Copyright © 2015, HuaYu iStudy Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.thread.smsprovider;

import cn.forp.framework.platform.vo.SMS;
import com.alibaba.fastjson.JSONObject;

/**
 * 短信Provider接口
 *
 * @author		Bruce
 * @version	2014-04-24 14:53:39
 */
public interface ISMSProvider
{
	/**
	 * 发送短信
	 * 
	 * @param dp		域参数
	 * @param sms		短信内容
	 * 
	 * @throws Exception
	 */
	public void send(JSONObject dp, SMS sms) throws Exception;
}