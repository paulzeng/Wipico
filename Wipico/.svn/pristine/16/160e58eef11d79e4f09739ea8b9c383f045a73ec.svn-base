package com.chinasvc.wipico.client;

import com.chinasvc.wipico.bean.Device;
import com.chinasvc.wipico.type.Keyboard;
import com.chinasvc.wipico.util.WipicoConstant;

/**
 * 任务操作类，如：文件、服务、办公、输入法等的操作类
 * 
 * @since 1.0.0
 * */
public class ActionHelper {

	/**
	 * 打开设备端的文件
	 * 
	 * @param path
	 *                路径名称
	 * @param device
	 *                要操作的设备
	 * */
	public static void openFile(String path, Device device) {
		ActionSender.sendFile(WipicoConstant.SERVER_CMD_OPEN_FILE, path, device.getDeviceIp());
	}

	/**
	 * 打开Apk
	 * 
	 * @param mPackage
	 *                包名
	 * @param mClass
	 *                主类名称
	 * @param flag
	 *                Intent 标记<li><{@link Intent.setFlag(int flag)}>
	 * @param device
	 *                要操作的设备
	 * */
	public static void openApk(String mPackage, String mClass, int flag, Device device) {
		ActionSender.openApk(WipicoConstant.SERVER_CMD_OPEN_APP_BY_CLASS, mPackage, mClass, flag, device.getDeviceIp());
	}

	/**
	 * 打开游戏
	 * 
	 * @param mPackage
	 *                包名
	 * @param device
	 *                要操作的设备
	 * */
	public static void openGame(String mPackage, Device device) {
		ActionSender.openGame(WipicoConstant.SERVER_CMD_OPEN_GAME, mPackage, device.getDeviceIp());
	}

	/**
	 * 安装游戏
	 * 
	 * @param mPackage
	 *                包名
	 * @param url
	 *                下载的URL
	 * @param device
	 *                要操作的设备
	 * */
	public static void installGame(String mPackage, String url, Device device) {
		ActionSender.installGame(WipicoConstant.SERVER_CMD_INSTALL_GAME, mPackage, url, device.getDeviceIp());
	}

	/**
	 * 卸载游戏
	 * 
	 * @param mPackage
	 *                包名
	 * @param device
	 *                要操作的设备
	 * */
	public static void uninstallGame(String mPackage, Device device) {
		ActionSender.uninstallGame(WipicoConstant.SERVER_CMD_UNINSTALL_GAME, mPackage, device.getDeviceIp());
	}

	/**
	 * 打开设备端Dlna服务
	 * 
	 * @param device
	 *                要操作的设备
	 * */
	public static void startDlnaService(Device device) {
		ActionSender.sendCmd(WipicoConstant.SERVER_CMD_START_DLNA, device.getDeviceIp());
	}

	/**
	 * 停止设备端Dlna服务
	 * 
	 * @param device
	 *                要操作的设备
	 * */
	public static void stopDlnaService(Device device) {
		ActionSender.sendCmd(WipicoConstant.SERVER_CMD_STOP_DLNA, device.getDeviceIp());
	}

	/**
	 * 键盘输入
	 * 
	 * @param value
	 *                输入的值
	 * @param device
	 *                要操作的设备
	 * */
	public static void sendKeyboardWrite(String value, Device device) {
		ActionSender.sendKeyBoard(Keyboard.KEYBOARD_SUBMIT, value, device.getDeviceIp());
	}

	/**
	 * 键盘删除
	 * 
	 * @param Device
	 *                device 发往的IP地址
	 * */
	public static void sendKeyboardDelete(Device device) {
		ActionSender.sendCmd(Keyboard.KEYBOARD_SUBMIT, device.getDeviceIp());
	}

	/**
	 * 屏幕调整
	 * 
	 * @param actionType
	 *                动作标记<li><{@link com.chinasvc.wipico.type.Screen}>
	 * @param Device
	 *                发往的Device
	 * */
	public static void sendAdjustScreen(int actionType, Device device) {
		ActionSender.sendScreen(actionType, device.getDeviceIp());
	}

	/**
	 * 屏幕分辨率调整
	 * 
	 * @param actionType
	 *                动作标记<li><{@link com.chinasvc.wipico.type.Resolution}>
	 * @param Device
	 *                发往的Device
	 * */
	public static void sendResolution(int actionType, Device device) {
		ActionSender.sendResolution(actionType, device.getDeviceIp());
	}

	/**
	 * 恢复出厂设置
	 * 
	 * @param Device
	 *                发往的Device
	 * */
	public static void sendRecovery(Device device) {
		ActionSender.sendRecovery(device.getDeviceIp());
	}

	/**
	 * 发送Office,需开启文件管理器服务器,和客户端
	 * 
	 * @param Device
	 *                device 发往的IP地址
	 * 
	 * */
	public static void openOffice(String path, Device device) {
		ActionSender.sendOffice(WipicoConstant.SERVER_CMD_OFFICE, path, device.getDeviceIp());
	}

	/**
	 * 发送验证
	 * 
	 * @param flag
	 *                类型, 2为连接成功 ,1为请求验证
	 * 
	 * @param deviceName
	 *                设备名称
	 * 
	 * @param verification
	 *                验证码
	 * 
	 * @param device
	 *                发往的设备
	 * */
	public static void sendVerification(int flag, String deviceName, String verification, Device device) {
		ActionSender.sendVerification(WipicoConstant.SERVER_CMD_VERIFICATION, flag, deviceName, verification, device.getDeviceIp());
	}

	/**
	 * 更改Wifi (广联专用)
	 * 
	 * @param ssid
	 *                Wifi 的SSID
	 * @param password
	 *                Wifi密码
	 * @param capabilities
	 *                加密方式
	 * @param device
	 *                设备
	 * */
	public static void changeWifi(String ssid, String password, String capabilities, Device device) {
		ActionSender.sendSSID(WipicoConstant.WIFI_CONNECT, ssid, password, capabilities, device.getDeviceIp());
	}

	/**
	 * 设置投影仪Wifi（投影仪专用）
	 * 
	 * @param ssid
	 *                Wifi 的SSID
	 * @param password
	 *                Wifi密码
	 * @param capabilities
	 *                加密方式
	 * @param device
	 *                设备
	 * */
	public static void setWifi(String ssid, String password, String capabilities, Device device) {
		ActionSender.sendSSID(WipicoConstant.SET_WIFI, ssid, password, capabilities, device.getDeviceIp());
	}

	/**
	 * 忘记Wifi(广联专用)
	 * 
	 * @param ssid
	 *                Wifi 的SSID
	 * @param device
	 *                设备
	 * */
	public static void forgetWifi(String ssid, Device device) {
		ActionSender.sendForgetSSID(WipicoConstant.WIFI_FORGET_CONNECT, ssid, device.getDeviceIp());
	}

}
