package com.tibco.ma.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.tibco.ma.common.StringUtil;

public class SMSTest {

	public static void main(String[] args) {
		RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
				.setSocketTimeout(1000 * 60 * 3).setConnectTimeout(1000 * 60 * 3).build();

		CloseableHttpClient client = HttpClients
				.custom()
				.setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
				.setKeepAliveStrategy(
						DefaultConnectionKeepAliveStrategy.INSTANCE).build();

		HttpPost post = new HttpPost("http://api.sms.cn/mt/");

		// post.addHeader("Content-Type",
		// "application/x-www-form-urlencoded;charset=utf-8");// Set transcoding

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uid", "xxxxxx"));// user name
		params.add(new BasicNameValuePair("pwd", "xxxxxxxxx"));// password
		params.add(new BasicNameValuePair("mobile", "13xxxxxxxxxx"));// phone
																		// number
		params.add(new BasicNameValuePair("encode", "utf8"));
		// params.add(new BasicNameValuePair("content",
		// "%B7%A2%CB%CD%B2%E2%CA%D4"));
		try {
			params.add(new BasicNameValuePair(
					"content",
					java.net.URLEncoder.encode(
							"your identifying code is :"
									+ StringUtil.createRandom(true, 6)
									+ ",Please submit the verification code in the page to complete the verification.",
							"utf-8")));// set SMS content
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String result = null;
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(params, "UTF-8");
			post.setEntity(entity);

			HttpResponse httpResponse = client.execute(post);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				result = EntityUtils.toString(httpEntity);
			}

			client.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			result = e.getMessage().toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = e.getMessage().toString();
		} catch (IOException e) {
			e.printStackTrace();
			result = e.getMessage().toString();
		}

		System.out.println(result);
		// try {
		// System.out.println(java.net.URLEncoder.encode(result, "utf-8"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
	}
}
