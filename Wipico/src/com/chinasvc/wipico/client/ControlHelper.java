package com.chinasvc.wipico.client;

import com.chinasvc.wipico.bean.Device;

import android.view.KeyEvent;

/**
 * 控制操作请求类
 * */
public class ControlHelper {

	/**
	 * 发送遥控器控制
	 * 
	 * @param keyEvent
	 *                按键事件
	 * @param device
	 *                所连接的设备
	 * */
	public static void sendControl(KeyEvent keyEvent, Device device) {
		ControlSender.sendKey(keyEvent.getKeyCode(), device.getDeviceIp());
	}

	/**
	 * 发送遥控器控制
	 * 
	 * @param keyCode
	 *                键码
	 * @param device
	 *                所连接的设备
	 * */
	public static void sendControl(int keyCode, Device device) {
		ControlSender.sendKey(keyCode, device.getDeviceIp());
	}

	/**
	 * 发送鼠标事件类型，非主线程中调用
	 * 
	 * @param eventType
	 *                要发送的鼠标操作类型 <li>{Mouse.MOUSE_WHEEL} <li>
	 *                {Mouse.MOUSE_MOVE}
	 * @param x
	 *                要发送的x坐标
	 * @param y
	 *                要发送的y坐标
	 * @param device
	 *                所连接的设备
	 * */
	public static void sendMouse(int eventType, int x, int y, Device device) {
		ControlSender.sendMouse(eventType, x, y, device.getDeviceIp());
	}

	/**
	 * 发送体感遥控
	 * 
	 * @param x
	 *                要发送的x坐标
	 * @param y
	 *                要发送的y坐标
	 * @param z
	 *                要发送的z坐标
	 * @param device
	 *                所连接的设备
	 * */
	public static void sendGriavity(int x, int y, int z, Device device) {
		ControlSender.sendGravity(x, y, z, device.getDeviceIp());
	}

	/**
	 * 发送触碰类型事件
	 * 
	 * @param act
	 *                要发送的触碰类型
	 * @param pointsseqid
	 *                event.getPointerCount();触碰的点数
	 * 
	 * @param data
	 *                data[i][0] x，触碰点的x坐标；
	 * 
	 *                data[i][1] y，触碰点的Y坐标；
	 * 
	 *                data[i][2]event.getPointerId(i),事件指针标识符关联到一个特定的指针数据索引。
	 * 
	 *                data[i][3] PointerCoords.touchMajor,椭圆接触区主轴的长度
	 * @param device
	 *                所连接的设备
	 * */
	public static void sendTouch(int act, int pointsseqid, int[][] data, Device device) {
		ControlSender.sendTouch(act, pointsseqid, data, device.getDeviceIp());
	}

}
