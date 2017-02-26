/**
 * Copyright © 2015, HuaYu iStudy Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.platform.thread.smsprovider;

import cn.forp.framework.platform.vo.SMS;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

/**
 * 西安圣保利短信Provider
 *
 * @author		Bruce
 * @version	2014-04-24 15:07:19
 */
public class XiAnShengBaoLi implements ISMSProvider
{
    /**
     * Logger for this class
     */
	private static final Logger logger = Logger.getLogger(XiAnShengBaoLi.class);

	/**
	 * 构造方法
	 */
	public XiAnShengBaoLi() {}

	/* (non-Javadoc)
	 * @see com.chinasoftware.platform.thread.smsprovider.ISMSProvider#send(com.chinasoftware.platform.IDomainProfile)
	 */
	@Override
	public void send(JSONObject dp, SMS sms) throws Exception
	{
		// logger.debug("发送短信：\r\n" + sms.getContent());
//
//		// 发送参数
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair(dp.getJSONObject("globalParams").getString("21"), sms.getMobile()));
//		params.add(new BasicNameValuePair(dp.getJSONObject("globalParams").getString("22"), sms.getContent() + dp.getJSONObject("globalParams").getString("25")));
//		// 发送回应字节数组
//		byte[] result = Http.post(dp.getJSONObject("globalParams").getString("20"), params, dp.getJSONObject("globalParams").getString("23"));
//
//		// 发送回应XML内容
//		XML response = new XML(new String(result, dp.getJSONObject("globalParams").getString("23")).trim().getBytes(dp.getJSONObject("globalParams").getString("23")));
//		String returnCode = response.getElement("error").getValue();
//		logger.debug("短信发送结果：" + returnCode);
//		if (!"0".equals(returnCode))
//			throw new Exception("短信发送失败：" + returnCode + '/' + response.getElement("message").getValue());
	}
}