/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.util;

import cn.forp.framework.core.FORP;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author	Bruce
 * @version	2016-4-1 17:31:54
 */
public class DateUtil
{
	/**
	 * Log4j lg
	 */
	private final static Logger lg = LoggerFactory.getLogger(Excel.class);

	/**
	 * 天干（农历）
	 */
	private final static String[] TIAN_GAN = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
	/**
	 * 地支（农历）
	 */
	private final static String[] DI_ZHI = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
	/**
	 * 中国星期
	 */
	private final static String[] CHINESE_WEEK = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	/**
	 * 农历年信息
	 */
	private final static long[] CHINESE_YEAR = new long[] { 0x04bd8, 0x04ae0,
		0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0,
		0x055d2, 0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540,
		0x0d6a0, 0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5,
		0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
		0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3,
		0x092e0, 0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0,
		0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0,
		0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8,
		0x0e950, 0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570,
		0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5,
		0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0,
		0x195a6, 0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50,
		0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0,
		0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
		0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7,
		0x025d0, 0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50,
		0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954,
		0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260,
		0x0ea65, 0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0,
		0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0,
		0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20,
		0x0ada0 };

	private DateUtil(){}

  /**
   * 获取当前日期
   *
   * @return yyyy-MM-dd
   */
  public static String getTodayDate()
  {
    return DateFormatUtils.format(new Date(), FORP.PATTERN_DATE);
  }

  /**
   * 获取当前日期时间
   *
   * @return yyyy-MM-dd HH:mm:ss
   */
  public static String getTodayDateTime()
  {
    return DateFormatUtils.format(new Date(), FORP.PATTERN_DATE_TIME);
  }

  /**
   * 获取当前时间
   *
   * @return HH:mm:ss
   */
  public static String getTodayTime()
  {
    return DateFormatUtils.format(new Date(), FORP.PATTERN_TIME);
  }

  /**
   * 获取当前月1日日期
   *
   * @return yyyy-MM-dd
   */
  public static String getFirstDateOfThisMonth()
  {
    Calendar now = Calendar.getInstance();
    now.set(Calendar.DAY_OF_MONTH, 1);

    return DateFormatUtils.format(now, FORP.PATTERN_DATE);
  }

	//=================================================================
	//		农历
	//=================================================================

  /**
   * 计算农历的月份、日信息
   *
   * @param year
   * @param month
   * @param day
   *
   * @return 农历信息
   */
  @SuppressWarnings("deprecation")
  private static String getChineseMonthAndDay(int year, int month, int day)
  {
		long[] nongDate = new long[7];
		int i = 0, temp = 0, leap = 0;
		Date baseDate = new Date(0, 0, 31);
		Date objDate = new Date(year - 1900, month - 1, day);
		long offset = (objDate.getTime() - baseDate.getTime()) / 86400000L;
		nongDate[5] = offset + 40;
		nongDate[4] = 14;

		for (i = 1900; i < 2050 && offset > 0; i++)
		{
			temp = getYearDays(i);
			offset -= temp;
			nongDate[4] += 12;
		}

		if (offset < 0)
		{
			offset += temp;
			i--;
			nongDate[4] -= 12;
		}

		nongDate[0] = i;
		nongDate[3] = i - 1864;
		leap = getLeapMonths(i); // 闰哪个月
		nongDate[6] = 0;

		for (i = 1; i < 13 && offset > 0; i++)
		{
			// 闰月
			if (leap > 0 && i == (leap + 1) && nongDate[6] == 0)
			{
				--i;
				nongDate[6] = 1;
				temp = getLeapDays((int) nongDate[0]);
			}
			else
				temp = getMonthDays((int) nongDate[0], i);

			// 解除闰月
			if (nongDate[6] == 1 && i == (leap + 1)) nongDate[6] = 0;
				offset -= temp;

			if (nongDate[6] == 0) nongDate[4]++;
		}

		if (offset == 0 && leap > 0 && i == leap + 1)
		{
			if (nongDate[6] == 1)
				nongDate[6] = 0;
			else
			{
				nongDate[6] = 1;
				--i;
				--nongDate[4];
			}
		}

		if (offset < 0)
		{
			offset += temp;
			--i;
			--nongDate[4];
		}

		//月份
		nongDate[1] = i;
		//日
		nongDate[2] = offset + 1;
		String result = "";

		if (1 == nongDate[1])
			result+= "正月";
		else
			if (nongDate[1] <= 10)
				result+= NumberUtil.CHINESE_NUMBER[(int)nongDate[1]] + "月";
			else
				result+= "十" + NumberUtil.CHINESE_NUMBER[(int)nongDate[1] % 10] + "月";

		if (nongDate[2] <= 10)
			result+= "初" + NumberUtil.CHINESE_NUMBER[(int)nongDate[2]];
		else
			if (20 == nongDate[2])
				result+= "二十";
			else
				if (nongDate[2] > 10 && nongDate[2] < 20)
					result+= "十" + NumberUtil.CHINESE_NUMBER[(int)nongDate[2] % 10];
				else
					if (nongDate[2] > 20 && nongDate[2] < 30)
						result+= "廿" + NumberUtil.CHINESE_NUMBER[(int)nongDate[2] % 10];
					else
						if (30 == nongDate[2])
							result+= "三十";

		return result;
  }

  /**
   * 获取中国农历日期信息（比如：甲子年 五月初八）
   *
   * @param date	日期（yyyy-MM-dd）
   *
   * @return 甲子年 五月初八
   */
  public static String getChineseDate(String date)
  {
    lg.debug("-------->getChineseDate()");
    lg.debug("日期：{}", date);

    String[] tmp = date.split("-");
    int year = Integer.parseInt(tmp[0]);
    int month = Integer.parseInt(tmp[1]);
    int day = Integer.parseInt(tmp[2]);
    int pos = 0;

    String chineseDate = "";
    //天干下标
    pos = (6 + (year - 1900)) % 10;
    lg.debug("天干下标：{}", pos);
    chineseDate+= TIAN_GAN[pos];
    //天干下标
    pos = (year - 1900) % 12;
    lg.debug("地支下标：", pos);
    chineseDate+= DI_ZHI[pos];

    chineseDate+= "年 ";

    chineseDate+= getChineseMonthAndDay(year, month, day);

    lg.debug("<--------getChineseDate()");
    return chineseDate;
  }

  /**
   * 获取指定日期对应的星期信息
   *
   * @param date String日期
   *
   * @return int
   */
  public static int getWeek(String date) throws Exception
  {
    Calendar cld = DateUtils.toCalendar(DateUtils.parseDate(date, FORP.PATTERN_DATE));
      return cld.get(Calendar.DAY_OF_WEEK);
  }

  /**
   * 获取中文的星期信息
   *
   * @param date	日期（yyyy-MM-dd）
   *
   * @return 中文星期信息
   */
  public static String getChineseWeek(String date) throws Exception
  {
    return CHINESE_WEEK[getWeek(date) - 1];
  }

	/**
	 * 计算农历 y年的总天数
	 *
	 * @param year
	 * @return 天数
	 */
	private static int getYearDays(int year)
	{
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1)
		{
			if (0 != (CHINESE_YEAR[year - 1900] & i))
				sum += 1;
		}
		
		return (sum + getLeapDays(year));
	}
	
	/**
	 * 计算农历 y年闰月的天数
	 * 
	 * @param year  年份
	 * @return 天数
	 */
	private static int getLeapDays(int year)
	{
		if (0 != getLeapMonths(year))
		{
			if (0 != (CHINESE_YEAR[year - 1900] & 0x10000))
				return 30;
			else
				return 29;
		}
		else
			return 0;
	}
	
	/**
	 * 计算农历 y年闰哪个月 1-12 , 没闰传回 0
	 * 
	 * @param year  年份
	 * @return 月份
	 */
	private static int getLeapMonths(int year)
	{
		return (int) (CHINESE_YEAR[year - 1900] & 0xf);
	}

	/**
	 * 计算农历 y年m月的总天数
	 * 
	 * @param year  年份
	 * @param month 月份
	 * 
	 * @return 天数
	 */
	private static int getMonthDays(int year, int month)
	{
		if (0 == (CHINESE_YEAR[year - 1900] & (0x10000 >> month)))
			return 29;
		else
			return 30;
	}
}