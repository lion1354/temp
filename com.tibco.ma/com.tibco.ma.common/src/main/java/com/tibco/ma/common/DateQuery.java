package com.tibco.ma.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateQuery {
	private static final Logger log = LoggerFactory.getLogger(DateQuery.class);

	private static DateFormat dateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
	private static TimeZone timeZone = TimeZone.getTimeZone("PST");

	public static Date parsePSTDatetime(String date) throws ParseException {
		dateTimeFormat.setTimeZone(timeZone);
		return dateTimeFormat.parse(date);
	}

	public static Date parsePSTDatetime(String date, String dataFormat)
			throws ParseException {
		DateFormat df = new SimpleDateFormat(dataFormat);
		df.setTimeZone(timeZone);
		return df.parse(date);
	}

	/**
	 * 
	 * format the timestamp to Pacific Standard Time with format 'yyyy-MM-dd
	 * HH:mm:ss'
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String formatPSTDatetime(long timestamp) {
		dateTimeFormat.setTimeZone(timeZone);
		return dateTimeFormat.format(new Date(timestamp));
	}

	/**
	 * format the timestamp to Pacific Standard Time with format 'yyyy-MM-dd'
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String formatPSTDate(long timestamp) {
		dateFormat.setTimeZone(timeZone);
		return dateFormat.format(new Date(timestamp));
	}

	public static String formatDate(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	public static String formatDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	public static String formatDateTime(Date date) {
		return dateTimeFormat.format(date);
	}

	public static Date parseDate(String date, String format)
			throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.parse(date);
	}

	public static Date parseDate(String date, String format, TimeZone tz)
			throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setTimeZone(tz);
		return dateFormat.parse(date);
	}

	public static String getTodayDate() {
		return formatDate(new Date());
	}

	public static String getTodayDateTime() {
		return formatDateTime(new Date());
	}

	public static String getTodayYear() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static long getDateTimeInUTCMilliSecsZ(String time)
			throws ParseException {
		long timeInMillis = 0;
		SimpleDateFormat format = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
		java.util.Date date;
		date = format.parse(time);
		timeInMillis = date.getTime();

		return timeInMillis;
	}

	public static long getDateTimeInUTCMilliSecsz(String time)
			throws ParseException {
		long timeInMillis = 0;

		SimpleDateFormat format = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		Date date;
		date = format.parse(time);
		timeInMillis = date.getTime();

		return timeInMillis;
	}

	public static long getDateTimeInUTCMilliSecsX(String time) {
		time = time.replace("T", "");
		time = time.replace(":", "");
		long timeInMillis = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHHmmssZ");
		java.util.Date date;
		try {
			date = format.parse(time);
			timeInMillis = date.getTime();
		} catch (ParseException e) {
			log.error("{}", e);
			e.printStackTrace();
		}

		return timeInMillis;
	}

	public static long getCurrentTimeInMillis() {
		Date now = new Date();
		return now.getTime();
	}

	public static long getDateTime(String dateTime) {
		if (StringUtil.isEmpty(dateTime)) {
			return 0;
		}
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(dateTime).getTime();
		} catch (ParseException e) {
			log.error("{}", e);
			return 0;
		}
	}

	public static String getTimeZoneDatetime(String time, String timeZone) {
		if (StringUtil.isEmpty(time) || StringUtil.isEmpty(timeZone)) {
			return "";
		}
		try {
			DateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd\'T\'hh:ss:mm.SSS\'Z\'");
			sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
			return sdf.format(new Date(Long.parseLong(time)));
		} catch (NumberFormatException e) {
			log.error("{}", e);
			return time;
		}
	}
}
