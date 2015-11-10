package com.chinasvc.wipico.client;

import com.chinasvc.wipico.bean.Device;
import com.chinasvc.wipico.util.WipicoConstant;

/**
 * 设备心跳帮助类
 * */
public class PulseHelper {

	private PulseThread pulseThread = null;

	private Device mDevice;
	private String deviceName;

	/**
	 * 构造PulseHelper实例
	 * 
	 * @param device
	 *                所连接的设备
	 * @param deviceName
	 *                移动设备名称
	 * */
	public PulseHelper(Device device, String deviceName) {
		mDevice = device;
		this.deviceName = deviceName;
	}

	/**
	 * 开启心跳同步
	 * */
	public void startPulse() {
		if (pulseThread != null) {
			pulseThread.stopPulse();
			pulseThread = null;
		}
		pulseThread = new PulseThread();
		pulseThread.startPulse();
	}

	/**
	 * 停止心跳同步
	 * */
	public void stopPulse() {
		if (pulseThread != null) {
			pulseThread.stopPulse();
			pulseThread = null;
		}
	}

	/**
	 * 心跳线程
	 * */
	private class PulseThread extends Thread {
		private boolean isRunning = true;

		public void stopPulse() {
			isRunning = false;
			try {
				join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public void startPulse() {
			isRunning = true;
			start();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isRunning) {
				// 每1s钟发送一下心跳
				ActionSender.sendPulse(WipicoConstant.PULSE, deviceName, mDevice.getDeviceIp());
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
