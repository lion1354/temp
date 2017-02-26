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
 * 一信通（中国联通）短信Provider
 *
 * @author		Bruce
 * @version	2014-07-08 14:14:19
 */
public class YiXinTong implements ISMSProvider
{
    /**
     * Logger for this class
     */
	private static final Logger logger = Logger.getLogger(YiXinTong.class);

	/**
	 * 构造方法
	 */
	public YiXinTong() {}

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
//		params.add(new BasicNameValuePair(dp.getJSONObject("globalParams").getString("22"), sms.getContent()));
//		// 发送回应字节数组
//		byte[] result = Http.post(dp.getJSONObject("globalParams").getString("20"), params, dp.getJSONObject("globalParams").getString("23"));
//
//		// 发送结果内容：result=0&description=发送短信成功&taskid=22430286151
//		String response = new String(result, dp.getJSONObject("globalParams").getString("23"));
//		logger.debug("发送返回结果：" + response);
//
//		// 解析发送结果
//		if (-1 == response.indexOf("result=0"))
//		{
//			throw new Exception("短信发送失败：" + response);
//
////			String resultCode = null, description = null, taskId = null;
////			String[] responseValus = response.split("&");
////			String[] temp = null;
////			for (String np : responseValus)
////			{
////				temp = np.split("=");
////
////				if ("result".equals(temp[0]))
////					resultCode = temp[1];
////				else
////					if ("description".equals(temp[0]))
////						description = temp[1];
////					else
////						if ("taskid".equals(temp[0]))
////							taskId = temp[1];
////			}
////
////			throw new Exception("短信发送失败：returnstatus-" + resultCode + "，description-" + description + "，" +
////					"taskId-" + taskId);
//		}
	}
}