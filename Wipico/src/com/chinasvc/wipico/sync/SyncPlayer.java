package com.chinasvc.wipico.sync;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.chinasvc.wipico.util.WipicoConstant;

/**
 * 服务器同步发送类
 * 
 * @since 1.0.0
 * */
public class SyncPlayer {

	/**
	 * 发送数据
	 * 
	 * @param data
	 *                要发送的数据
	 * @param ip
	 *                发往的IP
	 * */
	protected static void sendData(byte data[], String ip) {
		InetAddress destIPAddress;
		try {
			destIPAddress = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			destIPAddress = null;
			return;
		}
		DatagramPacket sendPacket = new DatagramPacket(data, data.length, destIPAddress, WipicoConstant.SYNC_UDP_PORT);
		DatagramSocket sendSocket = null;
		try {
			sendSocket = new DatagramSocket();
			sendSocket.setSoTimeout(2000);
			sendSocket.send(sendPacket);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (sendSocket != null) {
			sendSocket.close();
		}
	}

	/**
	 * 发送多媒体同步信息
	 * 
	 * @param flag
	 *                播放类别,1图片.2音乐.3视频,0为啥都没
	 * @param progress
	 *                进度值
	 * @param totalTime
	 *                总时间
	 * @param status
	 *                播放状态,1为正在播放，-1为播放结束标志,0为暂停
	 * @param isMute
	 *                是否静音
	 * @param volume
	 *                当前音量
	 * @param volumeMax
	 *                最大音量
	 * @param ip
	 *                发往的IP
	 * */
	public static void sendProgresss(int flag, int progress, int totalTime, int status, boolean isMute, int volume, int volumeMax, String ip) {
		ByteArrayOutputStream bos = null;
		DataOutputStream oos = null;
		if (ip == null) {
			return;
		}
		try {
			bos = new ByteArrayOutputStream(64);
			oos = new DataOutputStream(bos);
			oos.writeInt(flag);// 播放标记
			oos.writeInt(progress);// 进度
			oos.writeInt(totalTime);// 总时间
			oos.writeInt(status);// 播放状态
			if (isMute) {
				oos.writeInt(1);// 是否静音
			} else {
				oos.writeInt(0);// 是否静音
			}
			oos.writeInt(volume);// 音量当前
			oos.writeInt(volumeMax);// 音量最大
			oos.flush();
			sendData(bos.toByteArray(), ip);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
	}

	/**
	 * 发送设备状态同步
	 * 
	 * @param flag
	 *                同步类型 < {@link SyncConstants.START_APPLICATION_DEFAULT}
	 *                > < {@link SyncConstants.START_APPLICATION_IMAGE}> <
	 *                {@link SyncConstants.START_APPLICATION_AUDIO}> <
	 *                {@link SyncConstants.START_APPLICATION_VIDEO}> <
	 *                {@link SyncConstants.START_APPLICATION_OFFICE} > <
	 *                {@link SyncConstants.START_APPLICATION_GAME}> <
	 *                {@link SyncConstants.START_APPLICATION_OTHER}>
	 * @param wifi
	 *                设备所连接的Wifi
	 * @param version
	 *                Wipico 服务器的版本号
	 * @param ip
	 *                发往的ip
	 * */
	public static void sendDeviceSync(int appType, String wifi, String version, String ip) {
		ByteArrayOutputStream bos = null;
		DataOutputStream oos = null;
		if (ip == null) {
			return;
		}
		try {
			bos = new ByteArrayOutputStream(64);
			oos = new DataOutputStream(bos);
			oos.writeInt(100);//
			oos.writeInt(appType);//
			oos.writeUTF(wifi);//
			oos.writeUTF(version);//
			oos.flush();
			sendData(bos.toByteArray(), ip);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
	}

	/**
	 * 游戏列表同步
	 * 
	 * @param ip
	 *                发往的ip
	 * */
	public static void sendGameSync(String ip) {
		ByteArrayOutputStream bos = null;
		DataOutputStream oos = null;
		if (ip == null) {
			return;
		}
		try {
			bos = new ByteArrayOutputStream(64);
			oos = new DataOutputStream(bos);
			oos.writeInt(200);//
			oos.flush();
			sendData(bos.toByteArray(), ip);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
	}

	/**
	 * Wifi列表同步(用于IOS请求Wifi列表)
	 * 
	 * @param jsonWifi
	 *                Wifi列表
	 * @param ip
	 *                发往的ip
	 * */
	public static void sendWifiList(String jsonWifi, String ip) {
		ByteArrayOutputStream bos = null;
		DataOutputStream oos = null;
		if (ip == null) {
			return;
		}
		try {
			bos = new ByteArrayOutputStream(64);
			oos = new DataOutputStream(bos);
			oos.writeInt(400);//
			oos.writeUTF(jsonWifi);//
			oos.flush();
			sendData(bos.toByteArray(), ip);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
	}

}
