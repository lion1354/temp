/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.controller;

import java.util.Date;

/**
 * 请求表单数据Wrapper接口类
 *
 * @author	Bruce
 * @version	2016-3-31 14:47:01
 */
public interface IForm
{
	/**
	 * 获取表单请求数据的个数
	 * 
	 * @return int
	 */
	int getSize();
	/**
	 * 获取请求的动作信息（名称为action的参数值）
	 * 
	 * @return String
	 */
	String getAction();
	/**
	 * @param parameterName 参数名称
	 *
	 * @return String
	 */
	String get(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return String
	 */
	String getString(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return String[]
	 */
	String[] getStrings(String parameterName);
	/**
		* 获取String类型的请求参数值(多个值之间使用指定的分割符号)
		* 
		* @param parameterName		参数名称
		* @param split							分割符号
		* 
		* @return String
		*/
	String getString(String parameterName, char split);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return int
	 */
	int getInt(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return int[]
	 */
	int[] getInts(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return Integer
	 */
	Integer getInteger(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return Integer[]
	 */
	Integer[] getIntegers(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return long
	 */
	long getlong(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return long[]
	 */
	long[] getlongs(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return Long
	 */
	Long getLong(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return Long[]
	 */
	Long[] getLongs(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return float
	 */
	float getfloat(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return float[]
	 */
	float[] getfloats(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return Float
	 */
	Float getFloat(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return Float[]
	 */
	Float[] getFloats(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return double
	 */
	double getdouble(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return double[]
	 */
	double[] getdoubles(String parameterName);
	/**
	 * @param parameterName 参数名称
	 * 
	 * @return Double
	 */
	Double getDouble(String parameterName);
	/**
	 * @param parameterName     参数名称
	 * 
	 * @return Double[]
	 */
	Double[] getDoubles(String parameterName);
	/**
	 * @param parameterName     参数名称
	 * @param pattenStyle       日期Pattern
	 * 
	 * @return java.util.Date
	 */
	Date getDate(String parameterName, String pattenStyle);
}
