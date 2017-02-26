package com.popular.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class StringUtils {

	/**
	 * Return the string value, if not empty, else return the default string.
	 * 
	 * @param src
	 * @return
	 */
	public static String getValueWithDefault(String src, String defStr) {
		if (isEmpty(src)) {
			return defStr;
		} else {
			return src;
		}

	}

	/**
	 * Return the string lower case value, if empty return "".
	 * 
	 * @param src
	 * @return
	 */
	public static String toLowerCase(String src) {
		if (isEmpty(src)) {
			return "";
		} else {
			return src.toLowerCase();
		}

	}

	public static boolean notEmptyAndEqual(String src, String dest) {
		if (notEmpty(src)) {
			return src.equals(dest);
		} else {
			return false;
		}

	}

	public static int compare(String src1, String src2) {

		if (StringUtils.notEmpty(src1) && StringUtils.notEmpty(src2)) {
			return src1.compareTo(src2);
		} else {
			if (isEmpty(src1)) {
				return 1;
			} else if (isEmpty(src2)) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	// rest = xxx.yy.zzzz, if bLeft = true: return "xxx.yy"
	// // if bLeft = false: return "zzzz"
	/**
	 * 
	 */
	public static String getString(String text, char spliter, Boolean bLeft) throws Exception {
		if (text == null)
			return null;

		String thePart = text;

		int sLength = thePart.length();
		int curIndex = thePart.lastIndexOf(spliter);

		if (thePart != null && thePart.length() > 0) {
			sLength = thePart.length();
			curIndex = thePart.lastIndexOf(spliter);
			if (bLeft)
				thePart = thePart.substring(0, curIndex);
			else
				thePart = thePart.substring(curIndex + 1, sLength);
		}

		return thePart;
	}

	/**
	 * get uuid Aidan 2015/4/28
	 * 
	 * @return
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
	}

	public static long sublong(String value, int beginIndex, int endIndex) {
		String substring = value.substring(beginIndex, endIndex);
		return (substring.length() > 0) ? Long.parseLong(substring) : -1;
	}

	/**
	 * get the SMS identifying code
	 * 
	 * @param numberFlag
	 *            is number
	 * @param length
	 * @return
	 */
	public static String createRandom(boolean numberFlag, int length) {
		String retStr = "";
		String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);

		return retStr;
	}

	/**
	 * 检测字符串是否不为空(null,"","null")
	 *
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s) {
		return s != null && !"".equals(s) && !"null".equals(s);
	}

	/**
	 * 检测字符串是否为空(null,"","null")
	 *
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s) || "null".equals(s);
	}

	/**
	 * 字符串转换为字符串数组
	 *
	 * @param str
	 *            字符串
	 * @param splitRegex
	 *            分隔符
	 * @return
	 */
	public static String[] str2StrArray(String str, String splitRegex) {
		if (isEmpty(str)) {
			return null;
		}
		return str.split(splitRegex);
	}

	/**
	 * 用默认的分隔符(,)将字符串转换为字符串数组
	 *
	 * @param str
	 *            字符串
	 * @return
	 */
	public static String[] str2StrArray(String str) {
		return str2StrArray(str, ",\\s*");
	}

	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
	 *
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String date2Str(Date date) {
		return date2Str(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
	 *
	 * @param date
	 * @return
	 */
	public static Date str2Date(String date) {
		if (notEmpty(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return new Date();
		} else {
			return null;
		}
	}

	/**
	 * 按照参数format的格式，日期转字符串
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2Str(Date date, String format) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} else {
			return "";
		}
	}

	/**
	 * 获取UUID
	 *
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().toUpperCase();
	}

	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
}
