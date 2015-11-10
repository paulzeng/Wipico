package com.chinasvc.wipico.server;

import com.chinasvc.wipico.type.Keyboard;

import android.content.Context;
import android.content.Intent;

/**
 * 键盘输入删除类
 * 
 * @since 1.0.0
 * */
public class OperateKeyBroad {

	private Context mContext;

	/** 类的实例 */
	private static OperateKeyBroad instance;

	/**
	 * 构造OperateKeyBroad实例
	 * 
	 * @param context
	 * */
	public OperateKeyBroad(Context context) {
		mContext = context;
	}

	/**
	 * 返回当前类的实例化对象
	 * 
	 * @param context
	 * */
	public static OperateKeyBroad getInstance(Context context) {
		if (instance == null) {
			instance = new OperateKeyBroad(context);
		}
		return instance;
	}

	/**
	 * 输入法写
	 * 
	 * @param value
	 *                写入的值
	 * */
	public void write(String value) {
		Intent intent = new Intent();
		intent.setAction(Keyboard.ACTION_KEYBOARD_SUBMIT);
		intent.putExtra(Keyboard.KEY_PARAM, value);
		mContext.sendBroadcast(intent);
	}

	/**
	 * 输入法删
	 * */
	public void delete() {
		Intent intent = new Intent();
		intent.setAction(Keyboard.ACTION_KEYBOARD_DELETE);
		mContext.sendBroadcast(intent);
	}

}
