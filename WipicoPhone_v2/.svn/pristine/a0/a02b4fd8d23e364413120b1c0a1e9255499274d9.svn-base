package com.chinasvc.wipicophone.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Preference数据处理类
 * 
 * @author
 * */
public class PreferenceUtil {

	public SharedPreferences preference;

	// ----------------Preference-------------------
	public static String PRFERENCE_SOUND = "sound";
	public static String PRFERENCE_VIBRATION = "vibration";

	public static String PRFERENCE_DEVICE = "device";
	public static String PRFERENCE_SYS_TIME = "sys_time";

	public static String PRFERENCE_USER_CODE = "user_code";
	public static String PRFERENCE_USER_NAME = "user_name";

	private static PreferenceUtil instance;

	/**
	 * 返回当前类的实例化对象
	 * 
	 * @param context
	 *                context
	 * */
	public static synchronized PreferenceUtil getInstance(Context context) {
		if (instance == null) {
			instance = new PreferenceUtil(context);
		}
		return instance;
	}

	public PreferenceUtil(Context context) {
		preference = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void setSysTime(long value) {
		android.content.SharedPreferences.Editor editor = preference.edit();
		editor.putLong(PRFERENCE_SYS_TIME, value);
		editor.commit();
	}

	public long getSysTime() {
		return preference.getLong(PRFERENCE_SYS_TIME, 0);
	}

	/**
	 * 按键音
	 * */
	public void setSound(int value) {
		android.content.SharedPreferences.Editor editor = preference.edit();
		editor.putInt(PRFERENCE_SOUND, value);
		editor.commit();
	}

	/**
	 * 按键音
	 **/
	public int getSound() {
		return preference.getInt(PRFERENCE_SOUND, 1);
	}

	/**
	 * 按键振动
	 * */
	public void setVibration(int value) {
		android.content.SharedPreferences.Editor editor = preference.edit();
		editor.putInt(PRFERENCE_VIBRATION, value);
		editor.commit();
	}

	/**
	 * 按键振动
	 **/
	public int getVibration() {
		return preference.getInt(PRFERENCE_VIBRATION, 0);
	}

	public void setDevice(String value) {
		android.content.SharedPreferences.Editor editor = preference.edit();
		editor.putString(PRFERENCE_DEVICE, value);
		editor.commit();
	}

	public String getDevice() {
		return preference.getString(PRFERENCE_DEVICE, "");
	}

	/**
	 * 用户名
	 * */
	public void setUserName(String value) {
		android.content.SharedPreferences.Editor editor = preference.edit();
		editor.putString(PRFERENCE_USER_NAME, value);
		editor.commit();
	}

	/**
	 * 用户名
	 * */
	public String getUserName() {
		return preference.getString(PRFERENCE_USER_NAME, "Admin");
	}

	/**
	 * 手机识别码
	 * */
	public void setUserCode(String value) {
		android.content.SharedPreferences.Editor editor = preference.edit();
		editor.putString(PRFERENCE_USER_CODE, value);
		editor.commit();
	}

	/**
	 * 手机识别码
	 * */
	public String getUserCode() {
		return preference.getString(PRFERENCE_USER_CODE, "");
	}

}
