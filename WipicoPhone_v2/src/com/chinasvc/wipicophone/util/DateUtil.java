package com.chinasvc.wipicophone.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	@SuppressLint("SimpleDateFormat")
	public static String getTime(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
		Date curDate = new Date(time);// 获取当前时间
		String result = formatter.format(curDate);
		return result;
	}

}
