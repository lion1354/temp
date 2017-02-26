package com.tibco.ma.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MyTest {

	public static void main(String[] args) throws Exception {
//		String body = "{subject: 'test',message: 'message',addresses: [ 'houyaowei@163.com', 'yahou@tibco-support.com' ] }";
//		JSONObject json = JSONObject.fromObject(body);
////		JSONArray array =  json.getJSONArray("addresses");
//		Object jo =  json.get("addresses");
//		if(jo instanceof JSONArray){
//			System.out.println("array");
//			String[] str = {};
//			String[] address = (String[]) ((JSONArray) jo).toArray(str);
//			System.out.println(Arrays.toString(address));
//		}
//		if(jo instanceof String){
//			System.out.println("String");
//		}
		
		System.out.println(URLDecoder.decode("subject=test&message=message&addresses=houyaowei%40163.com", "utf-8"));
	}

}
