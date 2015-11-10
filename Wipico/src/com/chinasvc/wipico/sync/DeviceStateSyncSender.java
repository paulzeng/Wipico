package com.chinasvc.wipico.sync;

import android.content.Context;
import android.content.Intent;

/**
 * 服务器设备状态同步类
 **/
public class DeviceStateSyncSender {

	private Context mContext;
	private SyncThread syncThread;
	private int flag = 0;
	private String wifi = "";

	/**
	 * 构造方法
	 * 
	 * @param context
	 * */
	public DeviceStateSyncSender(Context context) {
		mContext = context;
	}

	/**
	 * 设置同步标记
	 * 
	 * @param flag
	 *                同步标记 <{@link SyncConstants.START_APPLICATION_DEFAULT}>
	 *                < {@link SyncConstants.START_APPLICATION_IMAGE}> <
	 *                {@link SyncConstants.START_APPLICATION_AUDIO}> <
	 *                {@link SyncConstants.START_APPLICATION_VIDEO}> <
	 *                {@link SyncConstants.START_APPLICATION_OFFICE}> <
	 *                {@link SyncConstants.START_APPLICATION_GAME}> <
	 *                {@link SyncConstants.START_APPLICATION_OTHER}>
	 * */
	public void setDeviceState(int flag) {
		this.flag = flag;
	}

	/**
	 * 设置当前所连接的Wifi ssid
	 * 
	 * @param wifiSSID
	 *                ssid
	 * */
	public void setDeviceSSID(String wifiSSID) {
		this.wifi = wifiSSID;
	}

	/**
	 * 开始同步
	 * */
	public void startSync() {
		if (syncThread != null) {
			syncThread.stopSync();
		}
		syncThread = new SyncThread();
		syncThread.startSync();
	}

	/**
	 * 关闭同步
	 * */
	public void closeAsync() {
		stopSync();
	}

	/**
	 * 关闭同步
	 * */
	private void stopSync() {
		if (syncThread != null) {
			syncThread.stopSync();
		}
	}

	private class SyncThread extends Thread {

		boolean needSync = true;

		/**
		 * 启动同步
		 * */
		public void startSync() {
			needSync = true;
			start();
		}

		/**
		 * 停止同步
		 * */
		public void stopSync() {
			needSync = false;
			try {
				join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (needSync) {
				Intent intent = new Intent();
				intent.setAction(SyncConstants.BROADCAST_WIPICO_SYNC_DEVICE);
				intent.putExtra(SyncConstants.START_APPLICATION_FLAG, flag);
				intent.putExtra(SyncConstants.WIFI_SSID, wifi);
				mContext.sendBroadcast(intent);
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
