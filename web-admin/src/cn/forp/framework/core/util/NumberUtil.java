/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.util;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

/**
 * 数字工具类
 *
 * @author	Bruce
 * @version	2012-3-19 10:11:21
 */
public final class NumberUtil
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(NumberUtil.class);
	/**
	 * 默认除法运算精度
	 */
	private static final int DEF_DIV_SCALE = 10;
  /**
   * 金额数字
   */
  public final static String[] CHINESE_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾"};
	/**
	 * 金额单位
	 */
	private final static String[] MONEY_UNIT = {"", "拾", "佰", "仟", "万", "亿"};

	private NumberUtil(){}

  /**
   * 百分比格式
   *
   * @param number	要格式化的数字
   */
  public static String getPercentage(double number, int fractionDigits)
  {
    NumberFormat nf = NumberFormat.getPercentInstance();

    nf.setMaximumFractionDigits(fractionDigits);
    nf.setMinimumFractionDigits(fractionDigits);

    return nf.format(number);
  }

  /**
   * 格式化float数字（指定小数位数）
   *
   * @param number	金额
   * @param digits	小数位个数
   */
  public static String format(double number, int digits)
  {
    String pattern = "0.";
    for (int i = 0; i < digits; i++)
    {
      pattern += "0";
    }

    DecimalFormat format = new DecimalFormat(pattern);
      return format.format(number);
  }

  /**
   * 格式化double类型数字输出形式（去除小数位无用的0）
   *
   * @param number
   * @return 字符形式数字
   */
  public static String format(double number)
  {
    if (number == (int) number)
      return String.valueOf((int) number);
    else
      return String.valueOf(number);
  }

  //----------------------------------------------------------------------------
  //								精确的四则运算
  //----------------------------------------------------------------------------

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1	被加数
	 * @param v2    加数
	 * 
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2)
	{
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1	被减数
	 * @param v2	减数
	 * 
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2)
	{
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1	被乘数
	 * @param v2	乘数
	 * 
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2)
	{
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1	被除数
	 * @param v2	除数
	 * 
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2)
	{
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1			被除数
	 * @param v2			除数
	 * @param scale		表示表示需要精确到小数点以后几位。
	 * 
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale)
	{
		if (scale < 0)
			throw new IllegalArgumentException("运算精度不能小于0");

		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v			需要四舍五入的数字
	 * @param scale		小数点后保留几位
	 * 
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale)
	{
		if (scale < 0)
			throw new IllegalArgumentException("运算精度不能小于0");

		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 获取大写的金额信息
	 * 
	 * @param money
	 * @return 大写金额字符串
	 * @throws Exception
	 */
	public static String getChineseMoney(double money)
	{
		// System.out.println("金额：" + money);
		String result = "", tmp = "";

		tmp = NumberFormat.getInstance().format(money);
		// 删除数字分组的符号。
		tmp = tmp.replaceAll(",", "");
		tmp = tmp.replace('.', ',');
		logger.debug("字符串格式：" + tmp);
		String[] moneyPart = tmp.split(",");
		for (int j = 0; j < moneyPart.length; j++)
			logger.debug("拆分后的数组：" + moneyPart[j]);

		// 转换整数部分
		logger.debug("整数：" + moneyPart[0]);
		int numbers = moneyPart[0].length(), num = 0;
		logger.debug("整数长度：" + numbers);
		for (int i = 0; i < numbers; i++)
		{
			num = Integer.parseInt(moneyPart[0].substring(i, i + 1));
			if (0 == num)
			{
				if (5 == (numbers - i))	//万位
					result += "万";
				else
					if (9 == (numbers - i))	//亿位
						result += "亿";
				
				continue;
			}
			else
				result += CHINESE_NUMBER[num];

			if (numbers - i > 4) // 大于千位
			{
				if ((numbers - i) < 9)	//千万以内
				{
					if (5 == (numbers - i)) // 整万
						result += "万";
					else
						result += MONEY_UNIT[(numbers - i) % 5];	//中间位数
				}
				else	//亿位
				{
					if (9 == (numbers - i)) // 整亿位
						result += "亿";
					else
						result += MONEY_UNIT[numbers - i - 9];	//中间位数
				}
			}
			else
				// 千位以内转换
				result += MONEY_UNIT[numbers - i - 1];
		}

		// 转换小数部分
		if (2 == moneyPart.length)
		{
			System.out.println("小数：" + moneyPart[1]);
			result += "元";
			// 角
			num = Integer.parseInt(moneyPart[1].substring(0, 1));
			if (0 != num)
				result += CHINESE_NUMBER[num] + "角";

			// 分
			if (moneyPart[1].length() > 1)
			{
				num = Integer.parseInt(moneyPart[1].substring(1, 2));
				if (0 != num)
					 result += CHINESE_NUMBER[num]	+ "分";
			}
		}
		else
			result += "元整";

		return result;
	}

  //----------------------------------------------------------------------------
  //								随机数方法区域
  //----------------------------------------------------------------------------

	/**
	 * 产生浮点类型的随机数
	 * 
	 * @param from		起始值
	 * @param to			结束值
	 * @param scale		精度（小数位数）
	 * @return 随机数
	 */
	public static float getRandomFloat(float from, float to, int scale)
	{
    if(from >= to)
        return -1F;

    float value = -1F;
    Random random = new Random();

    if((int)(to - from) > 0)
        value = from + (float)random.nextInt((int)(to - from));	//整数随机数字
    else
        value = from;	//小数随机数字

    float temp;
    for(temp = to; temp >= to; temp = value + random.nextFloat());	//循环获取第1个小于to的随机小数

    NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMaximumFractionDigits(scale);

    return Float.parseFloat(nf.format(temp));
	}

	/**
	 * 产生整型的随机数
	 * 
	 * @param from		起始值
	 * @param to			结束值
	 * @return 随机数
	 */
	public static int getRandomInt(int from, int to)
	{
		if (from >= to)
			return -1;
		else
		{
			int value = -1;
			Random random = new Random();
			
			// 整数随机数字
			value = from + random.nextInt((int)(to - from));
			
			return value;
		}
	}

	/**
	 * 产生指定位数的随机数字
	 * 
	 * @param length	位数
	 * @return 随机数
	 */
	public static String getRandomNumbers(int length)
	{
		String result = "";
		
		Random random = new Random();
		for (int i = 0; i <length; i++)
			result += random.nextInt(10);

		return result;
	}

	/**
	 * 测试方法
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
//		logger.info("5981230485010.01--->" + getChineseMoney(5981230485010.01));
//		logger.info("3459--->" + getChineseMoney(3459));
//		logger.info("120000--->" + getChineseMoney(120000));
//		logger.info("随机浮点数：" + getRandomFloat(1.05f, 1.25f, 2));
//		logger.info("随机浮点数：" + getRandomFloat(1.05f, 1.25f, 2));
//		logger.info("随机浮点数：" + getRandomFloat(1.05f, 1.25f, 2));
//		logger.info("随机整数：" + getRandomInt(-5, 10));
//		logger.info("随机整数：" + getRandomInt(-5, 10));
//		logger.info("随机整数：" + getRandomInt(-5, 10));
//		logger.info("23.0000001--->" + format(23.001f));
//		logger.info("23.0000000--->" + format(23.000f));
//		logger.info("8位随机数：" + getRandomNumbers(8));
//		logger.info("2位随机数：" + getRandomNumbers(2));
//		System.out.println(getMoney(-100000, 2));
//		System.out.println(getMoney(100000, 2));
		System.out.println(format(0, 2));
		System.out.println(getPercentage(0, 2));
	}
}