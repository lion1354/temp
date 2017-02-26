package com.tibco.ma.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

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
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping("ma/1/jenkins")
public class JenkinsAction {

	private static Logger log = LoggerFactory
			.getLogger(JenkinsAction.class);
	
	@ApiOperation(value = "start jenkins server", notes = "start jenkins server")
	@RequestMapping(value = "start", method = RequestMethod.POST)
	public ResponseEntity<?> Start(@ApiParam(value = "jenkins server url") @RequestBody JSONObject jenkins){
		if(!jenkins.containsKey("url") || StringUtil.isEmpty(jenkins.getString("url")))
			return ResponseUtils.fail("url is null");
		
		String url = jenkins.getString("url");
		
		RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
				.setSocketTimeout(1000 * 60 * 3)
				.setConnectTimeout(1000 * 60 * 3).build();
		CloseableHttpClient client = HttpClients
				.custom()
				.setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
				.setKeepAliveStrategy(
						DefaultConnectionKeepAliveStrategy.INSTANCE).build();
		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpPost post = new HttpPost(uri);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
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
		return ResponseUtils.successWithValue(result);
	}
}
