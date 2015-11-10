package com.chinasvc.wipicophone;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class BaseActivity extends Activity {

	private WakeLock wakeLock = null;

	@Override
	protected void onStart() {
		acquireWakeLock();
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		releaseWakeLock();
		super.onDestroy();
	}

	/**
	 * 获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
	 * */
	private void acquireWakeLock() {
		if (null == wakeLock) {
			PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
			// 保持CPU 运转，屏幕和键盘灯有可能是关闭的.PowerManager.PARTIAL_WAKE_LOCK
			// 当锁被释放时，保持屏幕亮起一段时间. PowerManager.ON_AFTER_RELEASE
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "PostLocationService");
			if (null != wakeLock) {
				wakeLock.acquire();
			}
		}
	}

	/**
	 * 释放设备电源锁
	 * */
	private void releaseWakeLock() {
		if (null != wakeLock) {
			wakeLock.release();
			wakeLock = null;
		}
	}

}
