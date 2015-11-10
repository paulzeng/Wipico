package com.chinasvc.wipico.server;

import com.chinasvc.wipico.bean.Device;
import com.chinasvc.wipico.tools.MediaInitiator;
import com.chinasvc.wipico.type.Audio;
import com.chinasvc.wipico.util.DesUtils;
import com.chinasvc.wipico.util.WipicoConstant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 服务器控制音频操作类
 * 
 * @since 1.0.0
 * */
public class OperateAudio {

	private static OperateAudio instance;
	private Context mContext;

	/**
	 * 构造方法
	 * 
	 * @param context
	 * */
	public OperateAudio(Context context) {
		mContext = context;
	}

	/**
	 * 返回当前类的实例化对象
	 * 
	 * @param context
	 * */
	public static OperateAudio getInstance(Context context) {
		if (instance == null) {
			instance = new OperateAudio(context);
		}
		return instance;
	}

	/**
	 * 设置音频的播放进度
	 * 
	 * @param device
	 *                所连接的设备
	 * @param position
	 *                设置的进度值
	 * */
	public void setSeek(Device device, int progress) {
		Intent musicIntent = new Intent(Audio.BROADCAST_ACTION_AUDIO);
		Bundle musicBundle = new Bundle();
		musicBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		musicBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Audio.SERVER_CMD_MUSIC_ITME_SEEKBAR);
		musicBundle.putInt(WipicoConstant.BUNDLE_MEDIA_SEEKBAR_PROGRESS_KEY, progress);// 读取进度值
		musicIntent.putExtras(musicBundle);
		mContext.sendBroadcast(musicIntent);
	}

	/**
	 * 打开指定路径的音频
	 * 
	 * @param device
	 *                所连接的设备
	 * @param path
	 *                音频路径
	 * */
	public void openVideo(Device device, String path) {
		String newPath = path;
		if (!newPath.startsWith("http")) {
			try {
				newPath = DesUtils.getInstance().decrypt(newPath);
			} catch (Exception e) {
			}
		}

		Intent musicIntent = new Intent(Audio.BROADCAST_ACTION_AUDIO);
		Bundle musicBundle = new Bundle();
		musicBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());

		Intent i = new Intent("com.android.music.musicservicecommand");
		i.putExtra("command", "stop");
		mContext.sendBroadcast(i);

		musicBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Audio.SERVER_CMD_MUSIC_ITME_OPEN);
		musicBundle.putString(WipicoConstant.BUNDLE_MEDIA_URL_KEY, newPath);// 读取URL值
		musicIntent.putExtras(musicBundle);

		try {
			mContext.startActivity(MediaInitiator.startApp(WipicoConstant.PROTOCOL_REMOTE, Audio.START_ACTION_AUDIO, newPath, device.getDeviceIp()));
		} catch (android.content.ActivityNotFoundException ex) {
			return;
		}

		mContext.sendBroadcast(musicIntent);
	}

	/**
	 * 播放音频播放器
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void play(Device device) {
		Intent musicIntent = new Intent(Audio.BROADCAST_ACTION_AUDIO);
		Bundle musicBundle = new Bundle();
		musicBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		musicBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Audio.SERVER_CMD_MUSIC_ITME_PLAY);
		musicIntent.putExtras(musicBundle);
		mContext.sendBroadcast(musicIntent);
	}

	/**
	 * 暂停音频播放器
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void pause(Device device) {
		Intent musicIntent = new Intent(Audio.BROADCAST_ACTION_AUDIO);
		Bundle musicBundle = new Bundle();
		musicBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		musicBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Audio.SERVER_CMD_MUSIC_ITME_PAUSE);
		musicIntent.putExtras(musicBundle);
		mContext.sendBroadcast(musicIntent);
	}

	/**
	 * 停止音频播放器
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void stop(Device device) {
		Intent musicIntent = new Intent(Audio.BROADCAST_ACTION_AUDIO);
		Bundle musicBundle = new Bundle();
		musicBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		musicBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Audio.SERVER_CMD_MUSIC_ITME_STOP);
		musicIntent.putExtras(musicBundle);
		mContext.sendBroadcast(musicIntent);
	}

	/**
	 * 设置音频播放器静音
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void mute(Device device) {
		Intent musicIntent = new Intent(Audio.BROADCAST_ACTION_AUDIO);
		Bundle musicBundle = new Bundle();
		musicBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		musicBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Audio.SERVER_CMD_MUSIC_ITME_MUTE);
		musicIntent.putExtras(musicBundle);
		mContext.sendBroadcast(musicIntent);
	}

	/**
	 * 设置音频播放器非静音
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void disMute(Device device) {
		Intent musicIntent = new Intent(Audio.BROADCAST_ACTION_AUDIO);
		Bundle musicBundle = new Bundle();
		musicBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		musicBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Audio.SERVER_CMD_MUSIC_ITME_VOICE);
		musicIntent.putExtras(musicBundle);
		mContext.sendBroadcast(musicIntent);
	}

	/**
	 * 增加音频播放器音量
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void addVolume(Device device) {
		Intent musicIntent = new Intent(Audio.BROADCAST_ACTION_AUDIO);
		Bundle musicBundle = new Bundle();
		musicBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		musicBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Audio.SERVER_CMD_MUSIC_ITME_VOICE_ADD);
		musicIntent.putExtras(musicBundle);
		mContext.sendBroadcast(musicIntent);
	}

	/**
	 * 减少音频播放器音量
	 * 
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void decreaseVolume(Device device) {
		Intent musicIntent = new Intent(Audio.BROADCAST_ACTION_AUDIO);
		Bundle musicBundle = new Bundle();
		musicBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		musicBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Audio.SERVER_CMD_MUSIC_ITME_VOICE_DESC);
		musicIntent.putExtras(musicBundle);
		mContext.sendBroadcast(musicIntent);
	}

}
