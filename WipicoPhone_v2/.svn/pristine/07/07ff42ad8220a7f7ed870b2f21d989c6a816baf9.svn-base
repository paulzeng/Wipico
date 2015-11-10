package com.chinasvc.wipicophone.util;

import java.text.DecimalFormat;

public class MathUtil {

	/**
	 * 保留一位小数点
	 */
	public static String keep1decimal(double value) {
		DecimalFormat df = new DecimalFormat("##0.#");
		return df.format(value);
	}

	/**
	 * 保留两个小数点
	 */
	public static String keep2decimal(double value) {
		DecimalFormat df = new DecimalFormat("##0.##");
		return df.format(value);
	}

	public static double stringtoDouble(String value) {
		if (value != null && !value.equals(""))
			return Double.parseDouble(value);
		return 0;
	}

	public static float stringtoFloat(String value) {
		if (value != null && !value.equals(""))
			return Float.parseFloat(value);
		return 0;
	}

	public static int boolean2int(boolean value) {
		if (value) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 生成验证码
	 * */
	public static String getVerification() {
		String result = "";
		int random = (int) (10000 * (Math.random()));
		if (random < 10) {
			result = (new StringBuilder("000")).append(random).toString();
		} else if (random < 100) {
			result = (new StringBuilder("00")).append(random).toString();
		} else if (random < 1000) {
			result = (new StringBuilder("0")).append(random).toString();
		} else {
			result = String.valueOf(random);
		}
		return result;
	}

}
