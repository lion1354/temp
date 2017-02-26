/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.controller;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;

/**
 * 请求表单数据Wrapper类
 *
 * @author	Bruce
 * @version	2016-4-1 15:30:51
 */
public final class FormImpl implements IForm
{
     /**
      * Logger
      */
     private static final Logger lg = Logger.getLogger(FormImpl.class);
	/**
	 * 字符串分割符号
	 */
	private static final char SPLIT_CHAR = '|';
	/**
	 * 请求表单数据列表
	 */
	private Map<String, String[]> requestDatas;
	
	/**
	 * 构造函数
	 * 
	 * @param map 请求的表单数据map
	 */
	public FormImpl(Map<String, String[]> map)
	{
		requestDatas = map;
	}

	/**
	 * 获取表单请求数据的个数
	 * 
	 * @return int
	 */
	public int getSize()
	{
		if (null != requestDatas)
			return requestDatas.size();
		else
			return 0;
	}
	
	/**
	 * 获取请求的动作信息（名称为action的参数值）
	 * 
	 * @return String
	 */
	public String getAction()
	{
		return getString("method", SPLIT_CHAR);
	}
	
	/**
	 * 获取String类型的请求参数值
	 * 
	 * @param parameterName
	 * @return String
	 */
	public String get(String parameterName)
	{
		return getString(parameterName, SPLIT_CHAR);
	}
	
	/**
	 * 获取String类型的请求参数值
	 * 
	 * @param parameterName
	 * @return String
	 */
	public String getString(String parameterName)
	{
		return get(parameterName);
	}

	/**
	 * 获取String类型的请求参数值(多个值之间使用指定的分割符号)
	 * 
	 * @param parameterName	参数名称
	 * @param split							分割符号
	 * 
	 * @return String
	 */
	public String getString(String parameterName, char split)
	{
		String value = "";

		try
		{
			String[] parameter = getStrings(parameterName);
			for (int i = 0; i < parameter.length; i++)
				value+= parameter[i] + split;
		}
		catch (Exception e)
		{
			lg.error("参数获取失败:" + parameterName + "-->" + e.toString());
		}

		if (0 != value.length())
			value = value.substring(0, value.length() - 1);

		return value;
	}

	/**
	 * 获取String[]类型的请求参数值。
	 * 
	 * @param parameterName
	 */
	public String[] getStrings(String parameterName)
	{
		try
		{
			String[] parameter = (String[])requestDatas.get(parameterName);
			if (null != parameter)
				return parameter;
			else
				return new String[0];
		}
		catch (Exception e)
		{
			lg.error("参数获取失败:" + parameterName + "-->" + e.toString());
			return new String[0];
		}
	}

	/**
	 * 获取int类型的请求参数
	 * 
	 * @param parameterName
	 * @return int
	 */
	public int getInt(String parameterName)
	{
		String value = this.get(parameterName);
		
		if (-1 == value.indexOf(SPLIT_CHAR))
		{
			try
			{
				return Integer.parseInt(value);
			}
			catch (Exception e)
			{
				lg.error(parameterName + "参数的值为[" + value + "]，无法转换为int型数据");
				return 0;
			}
		}
		else
		{
			lg.error(parameterName + "参数的值为[" + value + "]，无法按照单一int型数据获取，建议使用getInts()方法");
			return 0;	
		}
	}
	
	/**
	 * 获取int数组类型的请求参数
	 * 
	 * @param parameterName
	 * @return int[]
	 */
	public int[] getInts(String parameterName)
	{
		try
		{
			String[] parameter = (String[])requestDatas.get(parameterName);
			if (null != parameter)
			{
				int[] value = new int[parameter.length];
				for (int i = 0; i < parameter.length; i++)
				{
					value[i] = Integer.parseInt(parameter[i]);
				}
				
				return value;
			}
			else
				return new int[0];
		}
		catch (Exception e)
		{
			lg.error("参数获取失败:" + parameterName + "-->" + e.toString());
			return new int[0];
		}
	}
	
	/**
	 * @param parameterName
	 * @return Integer
	 */
	public Integer getInteger(String parameterName)
	{
		String value = this.get(parameterName);
		
		if (-1 == value.indexOf(SPLIT_CHAR))
		{
			try
			{
				return new Integer(value);
			}
			catch (Exception e)
			{
				lg.error(parameterName + "参数的值为[" + value + "]，无法转换为int型数据");
				return new Integer(0);
			}
		}
		else
		{
			lg.error(parameterName + "参数的值为[" + value + "]，无法按照单一Integer型数据获取，建议使用getIntegers()方法");
			return new Integer(0);
		}
	}

	/**
	 * @param parameterName
	 * @return Integer[]
	 */
	public Integer[] getIntegers(String parameterName)
	{
		try
		{
			String[] parameter = (String[])requestDatas.get(parameterName);
			if (null != parameter)
			{
				Integer[] value = new Integer[parameter.length];
				for (int i = 0; i < parameter.length; i++)
				{
					value[i] = new Integer(parameter[i]);
				}
				
				return value;
			}
			else
				return new Integer[0];
		}
		catch (Exception e)
		{
			lg.error("参数获取失败:" + parameterName + "-->" + e.toString());
			return new Integer[0];
		}
	}
	
	/**
	 * @param parameterName
	 * @return long
	 */
	public long getlong(String parameterName)
	{
		String value = this.get(parameterName);
		
		if (-1 == value.indexOf(SPLIT_CHAR))
		{
			try
			{
				return Long.parseLong(value);
			}
			catch (Exception e)
			{
				lg.error(parameterName + "参数的值为[" + value + "]，无法转换为long型数据");
				return 0;
			}
		}
		else
		{
			lg.error(parameterName + "参数的值为[" + value + "]，无法按照单一long型数据获取，建议使用getlongs()方法");
			return 0;	
		}
	}
	
	/**
	 * @param parameterName
	 * @return long[]
	 */
	public long[] getlongs(String parameterName)
	{
		try
		{
			String[] parameter = (String[])requestDatas.get(parameterName);
			if (null != parameter)
			{
				long[] value = new long[parameter.length];
				for (int i = 0; i < parameter.length; i++)
				{
					value[i] = Long.parseLong(parameter[i]);
				}
				
				return value;
			}
			else
				return new long[0];
		}
		catch (Exception e)
		{
			lg.error("参数获取失败:" + parameterName + "-->" + e.toString());
			return new long[0];
		}
	}
	
	/**
	 * @param parameterName
	 * @return Long
	 */
	public Long getLong(String parameterName)
	{
		return new Long(this.getlong(parameterName));
	}
	
	/**
	 * @param parameterName
	 * @return Long[]
	 */
	public Long[] getLongs(String parameterName)
	{
		try
		{
			String[] parameter = (String[])requestDatas.get(parameterName);
			if (null != parameter)
			{
				Long[] value = new Long[parameter.length];
				for (int i = 0; i < parameter.length; i++)
				{
					value[i] = new Long(parameter[i]);
				}
				
				return value;
			}
			else
				return new Long[0];
		}
		catch (Exception e)
		{
			lg.error("参数获取失败:" + parameterName + "-->" + e.toString());
			return new Long[0];
		}
	}
	
	/**
	 * @param parameterName
	 * @return float
	 */
	public float getfloat(String parameterName)
	{
		String value = this.get(parameterName);
		
		if (-1 == value.indexOf(SPLIT_CHAR))
		{
			try
			{
				return Float.parseFloat(value);
			}
			catch (Exception e)
			{
				lg.error(parameterName + "参数的值为[" + value + "]，无法转换为float型数据");
				return 0;
			}
		}
		else
		{
			lg.error(parameterName + "参数的值为[" + value + "]，无法按照单一float型数据获取，建议使用getFloats()方法");
			return 0;	
		}
	}

	/**
	 * @param parameterName
	 * @return float[]
	 */
	public float[] getfloats(String parameterName)
	{
		try
		{
			String[] parameter = (String[])requestDatas.get(parameterName);
			if (null != parameter)
			{
				float[] value = new float[parameter.length];
				for (int i = 0; i < parameter.length; i++)
				{
					value[i] = Float.parseFloat(parameter[i]);
				}
				
				return value;
			}
			else
				return new float[0];
		}
		catch (Exception e)
		{
			lg.error("参数获取失败:" + parameterName + "-->" + e.toString());
			return new float[0];
		}
	}

	/**
	 * @param parameterName
	 * @return Float
	 */
	public Float getFloat(String parameterName)
	{
		return new Float(getfloat(parameterName));
	}

	/**
	 * @param parameterName
	 * @return Float[]
	 */
	public Float[] getFloats(String parameterName)
	{
		try
		{
			String[] parameter = (String[])requestDatas.get(parameterName);
			if (null != parameter)
			{
				Float[] value = new Float[parameter.length];
				for (int i = 0; i < parameter.length; i++)
				{
					value[i] = new Float(parameter[i]);
				}
				
				return value;
			}
			else
				return new Float[0];
		}
		catch (Exception e)
		{
			lg.error("参数获取失败:" + parameterName + "-->" + e.toString());
			return new Float[0];
		}
	}

	/**
	 * @param parameterName
	 * @return double
	 */
	public double getdouble(String parameterName)
	{
		String value = this.get(parameterName);
		
		if (-1 == value.indexOf(SPLIT_CHAR))
		{
			try
			{
				return Double.parseDouble(value);
			}
			catch (Exception e)
			{
				lg.error(parameterName + "参数的值为[" + value + "]，无法转换为double型数据");
				return 0;
			}
		}
		else
		{
			lg.error(parameterName + "参数的值为[" + value + "]，无法按照单一double型数据获取，建议使用getDoubles()方法");
			return 0;	
		}
	}
	
	/**
	 * @param parameterName
	 * @return double[]
	 */
	public double[] getdoubles(String parameterName)
	{
		try
		{
			String[] parameter = (String[])requestDatas.get(parameterName);
			if (null != parameter)
			{
				double[] value = new double[parameter.length];
				for (int i = 0; i < parameter.length; i++)
				{
					value[i] = Double.parseDouble(parameter[i]);
				}
				
				return value;
			}
			else
				return new double[0];
		}
		catch (Exception e)
		{
			lg.error("参数获取失败:" + parameterName + "-->" + e.toString());
			return new double[0];
		}
	}
	
	/**
	 * @param parameterName
	 * @return Double
	 */
	public Double getDouble(String parameterName)
	{
		return new Double(getdouble(parameterName));
	}

	/**
	 * @param parameterName
	 * @return Double[]
	 */
	public Double[] getDoubles(String parameterName)
	{
		try
		{
			String[] parameter = (String[])requestDatas.get(parameterName);
			if (null != parameter)
			{
				Double[] value = new Double[parameter.length];
				for (int i = 0; i < parameter.length; i++)
				{
					value[i] = new Double(parameter[i]);
				}
				
				return value;
			}
			else
				return new Double[0];
		}
		catch (Exception e)
		{
			lg.error("参数获取失败:" + parameterName + "-->" + e.toString());
			return new Double[0];
		}
	}

	/**
	 * @param parameterName
	 * @param pattenStyle
	 * @return java.util.Date
	 */
	public Date getDate(String parameterName, String pattenStyle)
	{
		return null;
	}
}
