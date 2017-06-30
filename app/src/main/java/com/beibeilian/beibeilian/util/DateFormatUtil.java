package com.beibeilian.beibeilian.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间格式化工具 转换成上午，下午，几天前等
 *
 * @author 吴平原
 *
 */
@SuppressLint("SimpleDateFormat")
public class DateFormatUtil {

	// private static final String TAG = Class.class.getName();
	private static final String TAG = "DDDateFormat";
	/**
	 * 时间转换格式
	 */
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * String 转换为 Date
	 *
	 * @param time
	 * @return
	 */
	public static Date getDateByString(String time) {
		Date date = null;
		try {
			date = getSDF().parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * Date 转换为 String
	 *
	 * @param date
	 * @return
	 */
	public static String getStringByDate(Date date) {
		return getSDF().format(date);
	}

	/**
	 * Date 转换为 String
	 *
	 * @return
	 */
	public static String getStringByDate() {
		return getSDF().format(new Date());
	}

	/**
	 * 获取SimpleDateFormat
	 *
	 * @return
	 */
	private static SimpleDateFormat getSDF() {
		return new SimpleDateFormat(DATE_FORMAT);
	}

	/**
	 * Calendar转换为String
	 *
	 * @param c
	 * @return
	 */
	public static String getStringByCalendar(Calendar c) {
		String dateStr = null;
		dateStr = getSDF().format(c.getTime());
		return dateStr;
	}

	/**
	 * String 转换为Calendar
	 *
	 * @param time
	 * @return
	 */
	public static Calendar getCalendarByString(String time) {
		Calendar c = Calendar.getInstance();
		Date date = getDateByString(time);
		c.setTime(date);
		return c;
	}

	/**
	 * 获取当前时间
	 *
	 * @return
	 */
	public static String getCurrentTime() {
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = getSDF().format(curDate);
		return str;
	}

	/**
	 * 根据一个日期，返回是星期几的字符串
	 *
	 * @param txtDate
	 * @return
	 */
	public static String getWeek(String txtDate) {
		Date date = getDateByString(txtDate);
		String week = new SimpleDateFormat("EEEE").format(date);
		Log.w(TAG, "week=" + week);
		return week;
	}

	/**
	 * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
	 *
	 * @param sdate
	 * @param num
	 * @return
	 */
	public static String getWeek(String sdate, String num) {
		// 再转换为时间
		Date dd = getDateByString(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(dd);
		if (num.equals("1")) // 返回星期一所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		else if (num.equals("2")) // 返回星期二所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		else if (num.equals("3")) // 返回星期三所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		else if (num.equals("4")) // 返回星期四所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		else if (num.equals("5")) // 返回星期五所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		else if (num.equals("6")) // 返回星期六所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		else if (num.equals("0")) // 返回星期日所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}

	/**
	 * 计算两个日期间的间隔天数
	 *
	 * @param oldTime
	 *            之前的时间
	 * @param newTime
	 *            之后的时间
	 * @return
	 */
	public static long getDaysFromTwoDate(String oldTime, String newTime) {
		if (oldTime == null || oldTime.equals("")) {
			return 0;
		}
		if (newTime == null || newTime.equals("")) {
			return 0;
		}

		SimpleDateFormat sDateFormat = new SimpleDateFormat(DATE_FORMAT);
		long days = 0;
		try {
			Date date1 = sDateFormat.parse(oldTime);
			Date date2 = sDateFormat.parse(newTime);
			days = (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000); // 通过getTime()方法，把时间Date转换成毫秒格式Long类型，进行计算
		} catch (Exception e) {
			e.printStackTrace();
		}
		return days;
	}

	/**
	 * 计算两个日期间的毫秒数
	 *
	 * @param oldTime
	 *            之前的时间
	 * @param newTime
	 *            之后的时间
	 * @return
	 */
	public static long getSecondsFromTwoDate(String oldTime, String newTime) {
		if (oldTime == null || oldTime.equals("")) {
			// oldTime = "2013-4-30 17:39:32";
			return 0;
		}
		if (newTime == null || newTime.equals("")) {
			return 0;
		}

		SimpleDateFormat sDateFormat = new SimpleDateFormat(DATE_FORMAT);
		long min = 0;
		try {
			Date date1 = sDateFormat.parse(oldTime);
			Date date2 = sDateFormat.parse(newTime);
			min = (date2.getTime() - date1.getTime()) / 1000; // 通过getTime()方法，把时间Date转换成毫秒格式Long类型，进行计算
			// DDLog.e(TAG, "oldTime------------->="+oldTime);
			// DDLog.e(TAG, "newTime------------->="+newTime);
			// DDLog.e(TAG, "计算两个日期间的毫秒数------------->="+min+"秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return min;
	}

	/**
	 * 计算两个日期之间的分钟数
	 *
	 * @param oldTime
	 * @param newTime
	 * @return
	 */
	public static int getMinitesFromTwoDate(String oldTime, String newTime) {
		// long seconds = getSecondsFromTwoDate(oldTime, newTime);
		int min = -1;
		int year = getYear(newTime) - getYear(oldTime);
		if (year == 0) {
			int month = getMonth(newTime) - getMonth(oldTime);
			if (month == 0) {
				int day = getDay(newTime) - getDay(oldTime);
				if (day == 0) {
					int hour = Integer.parseInt(getHour(newTime).trim()) - Integer.parseInt(getHour(oldTime).trim());
					if (hour == 0) {
						min = Integer.parseInt(getMinute(newTime).trim()) - Integer.parseInt(getMinute(oldTime).trim());
					}
				}
			}
		}
		return min;
	}

	/**
	 * 判断二个时间是否在同一个周
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeekDates(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (0 == subYear) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
			// 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	/**
	 * 获取月份
	 *
	 * @param time
	 * @return
	 */
	public static int getMonth(String time) {
		return getCalendarByString(time).get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取月的天数
	 *
	 * @param time
	 * @return
	 */
	public static int getMonthMax(String time) {
		int year = getYear(time);
		int month = getMonth(time);
		if (month == 2) {
			return (year % 4 == 0 && (year % 400 == 0 || year % 100 != 0)) ? 29 : 28;
		} else {
			int k = (int) (Math.abs(month - 7.5));
			return (k % 2) == 0 ? 31 : 30;
		}
	}

	/**
	 * 获取年份
	 *
	 * @param time
	 * @return
	 */
	public static int getYear(String time) {
		return getCalendarByString(time).get(Calendar.YEAR);
	}

	/**
	 * 获取日期
	 *
	 * @param time
	 * @return
	 */
	public static int getDay(String time) {
		return getCalendarByString(time).get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取小时
	 *
	 * @param time
	 * @return
	 */
	public static String getHour(String time) {
		String hourStr = null;
		int hour = getCalendarByString(time).get(Calendar.HOUR_OF_DAY);
		if (hour < 10) {
			hourStr = "0" + hour;
		} else {
			hourStr = hour + "";
		}
		return hourStr;
	}

	/**
	 * 获取分钟
	 *
	 * @param time
	 * @return
	 */
	public static String getMinute(String time) {
		String minuteStr = null;
		int minute = getCalendarByString(time).get(Calendar.MINUTE);
		if (minute < 10) {
			minuteStr = "0" + minute;
		} else {
			minuteStr = minute + "";
		}
		return minuteStr;
	}

	/**
	 * 获取时、分
	 *
	 * @param time
	 * @return
	 */
	public static String getHourAndMin(String time) {
		// return getCalendarByString(time).get(Calendar.DAY_OF_MONTH);
		return getHour(time) + ":" + getMinute(time);
	}

	/**
	 * 获取时间
	 *
	 * @param time
	 * @return
	 */
	public static String getDDTime(String time) {
		StringBuffer timeStr = new StringBuffer();
		String currentTime = getCurrentTime();
		// DDLog.e(TAG, "time-------------->="+time);
		// DDLog.e(TAG, "currentTime-------------->="+currentTime);
		int year = getYear(currentTime) - getYear(time);
		int month_time = getMonth(time);
		int day_time = getDay(time);
		int month = getMonth(currentTime) - month_time;
		int day = getDay(currentTime) - day_time;
		// DDLog.w(TAG, "day-------------->="+day);
		if (year != 0) {
			timeStr.append(getYear(time) + "年" + month_time + "月" + day_time + "日" + " ");
		} else if (year == 0 && month == 0) {

			switch (day) {
				case 0:
					String morningOrEvening = getMorningOrEvening(time);
					String hourAndMin = getHourAndMin(time);
					timeStr.append(morningOrEvening + "" + hourAndMin);
					Log.w(TAG, timeStr.toString());
					break;
				case 1:
					timeStr.append("昨天 ");
					morningOrEvening = getMorningOrEvening(time);
					hourAndMin = getHourAndMin(time);
					timeStr.append(morningOrEvening + "" + hourAndMin);
					Log.w(TAG, timeStr.toString());
					break;
				case 2:
					timeStr.append("前天 ");
					morningOrEvening = getMorningOrEvening(time);
					hourAndMin = getHourAndMin(time);
					timeStr.append(morningOrEvening + "" + hourAndMin);
					Log.w(TAG, timeStr.toString());
					break;
				default:
					timeStr.append(month_time + "月" + day_time + "日" + " ");
					break;
			}
		} else if (year == 0 && month > 0) {

			timeStr.append(month_time + "月" + day_time + "日" + " ");
		}
		// timeStr.append(month + "月" + day + "日" + " ");

		// String morningOrEvening = getMorningOrEvening(time);
		// String hourAndMin = getHourAndMin(time);
		// timeStr.append(morningOrEvening + "" + hourAndMin);
		// Log.w(TAG, timeStr.toString());
		return timeStr.toString();
	}

	/**
	 * 产生周序列,即得到当前时间所在的年度是第几周
	 *
	 * @return
	 */
	public static String getSeqWeek() {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
		if (week.length() == 1)
			week = "0" + week;
		String year = Integer.toString(c.get(Calendar.YEAR));
		return year + week;
	}

	/**
	 * 获取一天中时间段
	 *
	 * @return
	 */
	public static String getMorningOrEvening(String time) {

		int hours = getDateByString(time).getHours();
		String timeName = null;
		switch (hours) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				timeName = "凌晨";
				break;
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
				timeName = "上午";
				break;
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
				timeName = "下午";
				break;
			case 19:
			case 20:
			case 21:
			case 22:
			case 23:
			case 24:
				timeName = "晚上";
				break;
		}
		// Log.w(TAG, "现在是"+timeName+","+hours+"点");
		return timeName;
	}

	/**
	 * 获取较短的时间信息
	 *
	 * @param time
	 * @return
	 */
	public static String getShortTime(String time) {
		String shortstring = null;
		long now = Calendar.getInstance().getTimeInMillis();
		Date date = getDateByString(time);
		if (date == null)
			return shortstring;
		long deltime = (now - date.getTime()) / 1000;
		if (deltime > 365 * 24 * 60 * 60) {
			shortstring = (int) (deltime / (365 * 24 * 60 * 60)) + "年前";
		} else if (deltime > 3 * 24 * 60 * 60) {
			shortstring = (int) (deltime / (24 * 60 * 60)) + "天前";
		} else if (deltime > 2 * 24 * 60 * 60 && deltime <= 3 * 24 * 60 * 60) {
			shortstring = "前天";
			// shortstring = (int) (deltime / (24 * 60 * 60)) + "天前";
		} else if (deltime > 24 * 60 * 60 && deltime <= 24 * 60 * 60) {
			shortstring = "昨天";
			// shortstring = (int) (deltime / (24 * 60 * 60)) + "天前";
		} else if (deltime > 60 * 60) {
			shortstring = (int) (deltime / (60 * 60)) + "小时前";
		} else if (deltime > 60) {
			shortstring = (int) (deltime / (60)) + "分前";
		} else if (deltime > 1) {
			shortstring = deltime + "秒前";
		} else {
			shortstring = "1秒前";
		}
		// Log.w(TAG, "间隔时间deltime="+deltime+",shortstring="+shortstring);
		return shortstring;
	}

}
