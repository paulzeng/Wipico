package com.chinasvc.wipico.client;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * 网络管理类
 * 
 * @since 1.0.0
 * */
public class NetworkManage {

	private boolean isDebug = false;
	private String TAG = "MainActivity";

	private Context mContext;

	private WifiManager wifiManager;
	private ConnectivityManager connectionManager;

	private NetworkManageListener listener;

	private NetThread netThread;

	private boolean isNetAvailable = false;

	/**
	 * 构造NetworkManage实例
	 * 
	 * @param context
	 * */
	public NetworkManage(Context context) {
		this.mContext = context;
		wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		connectionManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * 网络管理监听接口
	 * */
	public interface NetworkManageListener {

		/**
		 * 有可用网络
		 * */
		public void netAvailable();

		/**
		 * 无可用网络
		 * */
		public void netUnavailable();

	}

	/**
	 * 设置网络状态监听类
	 * 
	 * @param listener
	 *                网络状态监听类
	 * */
	public void setOnNetworkManageListener(NetworkManageListener listener) {
		this.listener = listener;
	}

	/**
	 * 开始检查网络，默认为无可用网络
	 * */
	public void startCheckNet() {
		if (isDebug) {
			Log.i(TAG, "开始检查网络" + Thread.currentThread());
		}
		if (netThread != null) {
			netThread.stopThread();
			netThread = null;
		}
		netThread = new NetThread();
		netThread.startThread();
	}

	/**
	 * 关闭检查网络监听
	 * */
	public void closeCheckNet() {
		if (isDebug) {
			Log.i(TAG, "关闭检查网络" + Thread.currentThread());
		}
		if (netThread != null) {
			netThread.stopThread();
			netThread = null;
		}
	}

	/**
	 * 网络检查线程类
	 * */
	private class NetThread extends Thread {

		private boolean running = false;

		protected void startThread() {
			running = true;
			start();
		}

		protected void stopThread() {
			running = false;
			try {
				join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (running) {
				checkNet();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 检查是否有可用网络
	 * */
	private void checkNet() {
		if (wifiManager.isWifiEnabled()) {
			// WIFI开关已经开启
			NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
			if (networkInfo != null) {
				if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					// WIFI网络 不提示任何信息
					if (listener != null && !isNetAvailable) {
						isNetAvailable = true;
						listener.netAvailable();
					}
				} else {
					if (listener != null && isNetAvailable) {
						isNetAvailable = false;
						listener.netUnavailable();
					}
				}
			} else {
				// 无可用网络
				if (listener != null && isNetAvailable) {
					isNetAvailable = false;
					listener.netUnavailable();
				}
			}
		} else {
			// WIFI开关已经关闭
			String apname = BroadcastAddressUtil.getApBroadcastIp();
			String tmp = BroadcastAddressUtil.execCommand("ip route", apname == null ? "wlan0" : apname);
			if ((apname == null || apname.equals("")) && tmp == null) {
				if (listener != null && isNetAvailable) {
					isNetAvailable = false;
					listener.netUnavailable();
				}
			} else {
				if (listener != null && !isNetAvailable) {
					isNetAvailable = true;
					listener.netAvailable();
				}
			}
		}
	}

}
