package com.chinasvc.wipicophone.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd hh:mm:ss";
	public final static String EMPTY = "";

	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	public static String formatDate(Date date) {
		return formatDate(date, DEFAULT_DATE_PATTERN);
	}

	public static String getDate() {
		return formatDate(new Date(), DEFAULT_DATE_PATTERN);
	}

	public static String getDateTime() {
		return formatDate(new Date(), DEFAULT_DATETIME_PATTERN);
	}

	public static String formatDateTime(Date date) {
		return formatDate(date, DEFAULT_DATETIME_PATTERN);
	}

	public static String join(final ArrayList<String> array, String separator) {
		StringBuffer result = new StringBuffer();
		if (array != null && array.size() > 0) {
			for (String str : array) {
				result.append(str);
				result.append(separator);
			}
			result.delete(result.length() - 1, result.length());
		}
		return result.toString();
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static String generateTime(long time) {
		int totalSeconds = (int) (time / 1000);
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;
		return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
	}

}