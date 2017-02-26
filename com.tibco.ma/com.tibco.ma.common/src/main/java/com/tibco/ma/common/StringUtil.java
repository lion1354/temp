package com.tibco.ma.common;

import java.util.UUID;

public class StringUtil {

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

	/**
	 * if src is not null and is not empty, return true.
	 * 
	 * @param src
	 * @return
	 */
	public static boolean notEmpty(String src) {
		if (src == null || "".equals(src)) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * if src is null or is empty, return true.
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmpty(String src) {
		return !notEmpty(src);

	}

	public static boolean notEmptyAndEqual(String src, String dest) {
		if (notEmpty(src)) {
			return src.equals(dest);
		} else {
			return false;
		}

	}

	public static int compare(String src1, String src2) {

		if (StringUtil.notEmpty(src1) && StringUtil.notEmpty(src2)) {
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
	public static String getString(String text, char spliter, Boolean bLeft)
			throws Exception {
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
	 * get uuid
	 * Aidan 2015/4/28
	 * 
	 * @return
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
				+ s.substring(19, 23) + s.substring(24);
	}
	
	
	public static long sublong(String value, int beginIndex, int endIndex) {
        String substring = value.substring(beginIndex, endIndex);
        return (substring.length() > 0) ? Long.parseLong(substring) : -1;
    }
	
	/**
	  * get the SMS identifying code
	  * @param numberFlag   is number
	  * @param length
	  * @return
	  */
	 public static String createRandom(boolean numberFlag, int length){
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

	
}
