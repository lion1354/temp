package com.tibco.ma.test;


public class TestSingleton {
	private static String str;

	private TestSingleton() {
	}

	public static synchronized String getStr() {
		if (str == null) {
			str = new String("2");
		}
		return str;
	}

	public static void main(String[] args) {
		String str1 = TestSingleton.getStr();
		String str2 = TestSingleton.getStr();
		System.out.println(str1 == str2);

	}

}
