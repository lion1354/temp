package com.popular.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DateUtils {
	private static DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat defaultDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static DateFormat idDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	/**
	 * Generate string in "yyyyMMddHHmmssSSS" format.
	 * 
	 * @return
	 */
	public static String getID() {
		return idDateFormat.format(new Date());
	}

	public static Date getNowDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * Formats Date object to String "yyyy-MM-dd".
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		if (date == null) {
			return null;
		}
		return defaultDateFormat.format(date);
	}

	/**
	 * Formats Date object to String "yyyy-MM-dd", if the date is null, return
	 * defaultValue.
	 * 
	 * @param date
	 * @param defaultValue
	 * @return
	 */
	public static String formatWithDefault(Date date, String defaultValue) {
		if (date == null) {
			return defaultValue;
		}
		return defaultDateFormat.format(date);
	}

	/**
	 * Formats Date object to String "yyyy-MM-dd HH:mm:ss", if the date is null,
	 * return defaultValue.
	 * 
	 * @param date
	 * @param defaultValue
	 * @return
	 */
	public static String formatDateTimeWithDefault(Date date, String defaultValue) {
		if (date == null) {
			return defaultValue;
		}
		return defaultDateTimeFormat.format(date);
	}

	/**
	 * Formats Date object to String "yyyy-MM-dd HH:mm:ss".
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		if (date == null) {
			return null;
		}
		return defaultDateTimeFormat.format(date);
	}

	/**
	 * Formats Date object to specific String.
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date, String format) {
		if (date == null) {
			return null;
		}
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * Parses "yyyy-MM-dd" String to Date.
	 * 
	 * @param src
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String src) throws ParseException {
		return defaultDateFormat.parse(src);
	}

	/**
	 * Parses "yyyy-MM-dd HH:mm:ss" String to Date.
	 * 
	 * @param src
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDateTime(String src) throws ParseException {
		return defaultDateTimeFormat.parse(src);
	}

	/**
	 * Parses specific String to Date.
	 * 
	 * @param src
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String src, String format) throws ParseException {
		DateFormat df = new SimpleDateFormat(format);
		return df.parse(src);
	}

	/**
	 * Return the calendar with hour, minute, second and millisecond is 0.
	 * 
	 * @return
	 */
	public static Calendar getYMDCalender() {

		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c;

	}

	/**
	 * Retrieve the year, month and day of the date.
	 * 
	 * @return an integer array, the first element is year, the second is the
	 *         month, and the last is the day.
	 */
	public static int[] getYearMonthDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTime(date);
		int ret[] = new int[3];
		ret[0] = c.get(Calendar.YEAR);
		ret[1] = c.get(Calendar.MONTH);
		ret[2] = c.get(Calendar.DAY_OF_MONTH);
		return ret;
	}

	public static Map<Integer, List<Integer>> getYearMonthNumber(Date from, Date to) {
		if (from.after(to)) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(from);
		int yearFrom = c.get(Calendar.YEAR);
		int monthFrom = c.get(Calendar.MONTH);
		c.setTime(to);
		int yearTo = c.get(Calendar.YEAR);
		int monthTo = c.get(Calendar.MONTH);

		int yearNumber = yearTo - yearFrom + 1;

		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		for (int i = 0; i < yearNumber; i++) {
			if (map.get(yearFrom + i) == null) {
				map.put(yearFrom + i, new ArrayList<Integer>());

			}
			if (i == 0) {
				if (yearNumber > 1) {
					for (int j = monthFrom; j < 12; j++) {
						map.get(yearFrom + i).add(j + 1);
					}
				} else {
					for (int j = monthFrom; j < monthTo + 1; j++) {
						map.get(yearFrom + i).add(j + 1);
					}
				}

			} else if (i == yearNumber - 1) {
				for (int j = 0; j < monthTo + 1; j++) {
					map.get(yearFrom + i).add(j + 1);
				}
			} else {
				for (int j = 0; j < 12; j++) {
					map.get(yearFrom + i).add(j + 1);
				}

			}

		}

		return map;

	}

	public static boolean sameYMD(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return false;
		} else {
			return defaultDateFormat.format(d1).equals(defaultDateFormat.format(d2));

		}

	}

	public static void main(String[] args) {
		try {
			Date from = parse("2011-05-01");
			Date to = parse("2012-10-02");
			Map<Integer, List<Integer>> ret = getYearMonthNumber(from, to);
			Set<Integer> keys = ret.keySet();

			for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
				Integer integer = (Integer) iterator.next();
				System.out.println(integer + "-" + ret.get(integer));
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
