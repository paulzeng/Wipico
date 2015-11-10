package com.chinasvc.wipicophone.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.chinasvc.wipico.bean.Device;

public class DeviceTool {

	private static String TAG = "DeviceTool";

	private static int count = 0;// 计数器

	/**
	 * 检查设备状态
	 * 
	 * @return 0,未连接设备；1正常；2断线
	 * */
	public static int checkDevice(Device device, List<Device> listDevices) {
		if (device == null) {
			return 0;
		} else {
			if (listDevices.indexOf(device) >= 0) {
				count = 0;
				return 1;
			} else {
				if (count >= 2) {
					return 2;// 断线了
				} else {
					count++;
					return 1;
				}
			}
		}
	}

	public static boolean isMatchSSID(String ssid) {
		String reg = "^([a-zA-Z0-9_-])+\\-([0-9_-]){5}";
		return startCheck(reg, ssid);
	}

	/**
	 * 正则表达式判断字符串是否符合正则表达式规则
	 * 
	 * @param reg
	 *                正则表达式
	 * @param string
	 *                要判断的字符串
	 * */
	public static boolean startCheck(String reg, String string) {
		boolean tem = false;
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(string);
		tem = matcher.matches();
		return tem;
	}
}
