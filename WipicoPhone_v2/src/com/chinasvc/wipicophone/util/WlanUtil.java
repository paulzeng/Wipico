package com.chinasvc.wipicophone.util;

import java.io.IOException;

import android.content.Context;

import com.chinasvc.wipico.client.BroadcastAddressUtil;

public class WlanUtil {

	private BroadcastAddressUtil broadcastAddressUtil;
	private Context mContext;

	public WlanUtil(Context context) {
		mContext = context;
	}

	/**
	 * 获取本地IP地址
	 * */
	public String getLocalIp() {
		String ip = null;
		broadcastAddressUtil = new BroadcastAddressUtil(mContext);
		try {
			ip = broadcastAddressUtil.getLocalIp();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ip;
	}

}
