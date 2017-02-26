package com.popular.test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {

	String test = "{\"userId\":\"12313\",\"photos\":[{\"photoUrl\":\"John\",\"des\":\"Doe\"},{\"photoUrl\":\"Anna\",\"des\":\"Smith\"},{\"photoUrl\":\"Peter\",\"des\":\"Jones\"}]}";

	public void testA() {
	}

	public static void main(String[] args) {
		String infos = "{\"userId\":\"12313\",\"photos\":[{\"photoUrl\":\"John\",\"des\":\"Doe\"},{\"photoUrl\":\"Anna\",\"des\":\"Smith\"},{\"photoUrl\":\"Peter\",\"des\":\"Jones\"}]}";
		JSONObject json = JSONObject.fromObject(infos);
		Integer userId = Integer.parseInt(json.getString("userId"));
		JSONArray array = json.getJSONArray("photos");
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			String photoUrl = jsonObject.getString("photoUrl");
			String des = jsonObject.getString("des");
			System.out.println(userId + photoUrl + des);
		}
	}

	public class Inner {
		public void print(String str) {
			Test outer = new Test();
			String i = test;
			outer.testA();
		}
	}
}
