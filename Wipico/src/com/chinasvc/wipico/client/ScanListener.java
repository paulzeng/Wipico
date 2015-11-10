package com.chinasvc.wipico.client;

import java.util.List;

import com.chinasvc.wipico.bean.Device;

/**
 * 扫描回调接口
 * */
public interface ScanListener {

	/**
	 * 有无可用信号
	 * 
	 * @param isAvailable
	 *                是否有可用网络
	 * */
	public void isNetworkAvailable(boolean isAvailable);

	/**
	 * 设备连接状态
	 * 
	 * @param status
	 *                连接状态 <{@link DeviceStatus}>
	 * */
	public void deviceStatus(int status);

	/**
	 * 设备连接类型
	 * 
	 * @param status
	 *                连接类型 <{@link DeviceType}>
	 * */
	public void deviceType(int status);

	/**
	 * 扫描设备异常
	 * */
	public void scanException();

	/**
	 * 扫描结果返回
	 * 
	 * @param listDevices
	 *                扫描结果列表
	 * */
	public void onResult(List<Device> listDevices);

	/**
	 * Wifi SSID扫描结果返回
	 * 
	 * @param listDevices
	 *                扫描结果列表
	 * */
	public void onWifiResult(List<Device> listDevices);

	/**
	 * Wifi连接上
	 * */
	public void connected();

	/**
	 * Wifi正在连接
	 * */
	public void connecting();

	/**
	 * Wifi断开
	 * */
	public void disconnected();

	/**
	 * Wifi 正在断开
	 * */
	public void disconnecting();

}
