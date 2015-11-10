package com.chinasvc.wipico.server;

import com.chinasvc.wipico.bean.Device;
import com.chinasvc.wipico.tools.MediaInitiator;
import com.chinasvc.wipico.type.Video;
import com.chinasvc.wipico.util.DesUtils;
import com.chinasvc.wipico.util.WipicoConstant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 服务器控制视频操作类
 * 
 * 
 * @since 1.0.0
 * */
public class OperateVideo {

	private static OperateVideo instance;
	private Context mContext;

	/**
	 * 构造方法
	 * 
	 * @param context
	 * */
	public OperateVideo(Context context) {
		mContext = context;
	}

	/**
	 * 返回当前类的实例化对象
	 * 
	 * @param context
	 * */
	public static OperateVideo getInstance(Context context) {
		if (instance == null) {
			instance = new OperateVideo(context);
		}
		return instance;
	}

	/**
	 * 设置视频播放进度
	 * 
	 * @param device
	 *                所连接的设备
	 * @param position
	 *                进度值ms
	 * */
	public void setSeek(Device device, int progress) {
		Intent movieIntent = new Intent(Video.BROADCAST_ACTION_VIDEO);
		Bundle movieBundle = new Bundle();
		movieBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		movieBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Video.SERVER_CMD_MOVIE_ITEM_SEEKBAR);
		movieBundle.putInt(WipicoConstant.BUNDLE_MEDIA_SEEKBAR_PROGRESS_KEY, progress);// 读取进度值
		movieIntent.putExtras(movieBundle);
		mContext.sendBroadcast(movieIntent);
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
		
		Intent movieIntent = new Intent(Video.BROADCAST_ACTION_VIDEO);
		Bundle movieBundle = new Bundle();
		movieBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		movieBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Video.SERVER_CMD_MOVIE_ITEM_OPEN);
		movieBundle.putString(WipicoConstant.BUNDLE_MEDIA_URL_KEY, newPath);// 读取URL值
		movieIntent.putExtras(movieBundle);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			mContext.startActivity(MediaInitiator.startApp(WipicoConstant.PROTOCOL_REMOTE, Video.START_ACTION_VIDEO, newPath, device.getDeviceIp()));
		} catch (android.content.ActivityNotFoundException ex) {
			ex.printStackTrace();
		}
		mContext.sendBroadcast(movieIntent);
	}

	/**
	 * 播放视频播放器
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void play(Device device) {
		Intent movieIntent = new Intent(Video.BROADCAST_ACTION_VIDEO);
		Bundle movieBundle = new Bundle();
		if (device.getDeviceIp() != null && !"".equals(device.getDeviceIp())) {
			movieBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		}
		movieBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Video.SERVER_CMD_MOVIE_ITEM_PLAY);
		movieIntent.putExtras(movieBundle);
		mContext.sendBroadcast(movieIntent);
	}

	/**
	 * 暂停视频播放器
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void pause(Device device) {
		Intent movieIntent = new Intent(Video.BROADCAST_ACTION_VIDEO);
		Bundle movieBundle = new Bundle();
		if (device.getDeviceIp() != null && !"".equals(device.getDeviceIp())) {
			movieBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		}
		movieBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Video.SERVER_CMD_MOVIE_ITEM_PAUSE);
		movieIntent.putExtras(movieBundle);
		mContext.sendBroadcast(movieIntent);
	}

	/**
	 * 停止视频播放器
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void stop(Device device) {
		Intent movieIntent = new Intent(Video.BROADCAST_ACTION_VIDEO);
		Bundle movieBundle = new Bundle();
		movieBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		movieBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Video.SERVER_CMD_MOVIE_ITEM_STOP);
		movieIntent.putExtras(movieBundle);
		mContext.sendBroadcast(movieIntent);
	}

	/**
	 * 设置视频播放器静音
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void mute(Device device) {
		Intent movieIntent = new Intent(Video.BROADCAST_ACTION_VIDEO);
		Bundle movieBundle = new Bundle();
		movieBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		movieBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Video.SERVER_CMD_MOVIE__ITME_MUTE);
		movieIntent.putExtras(movieBundle);
		mContext.sendBroadcast(movieIntent);
	}

	/**
	 * 设置视频播放器非静音
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void disMute(Device device) {
		Intent movieIntent = new Intent(Video.BROADCAST_ACTION_VIDEO);
		Bundle movieBundle = new Bundle();
		movieBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		movieBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Video.SERVER_CMD_MOVIE__ITME_VOICE);
		movieIntent.putExtras(movieBundle);
		mContext.sendBroadcast(movieIntent);
	}

	/**
	 * 增加视频播放器音量
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void addVolume(Device device) {
		Intent movieIntent = new Intent(Video.BROADCAST_ACTION_VIDEO);
		Bundle movieBundle = new Bundle();
		movieBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		movieBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Video.SERVER_CMD_MOVIE_ITME_VOICE_ADD);
		movieIntent.putExtras(movieBundle);
		mContext.sendBroadcast(movieIntent);
	}

	/**
	 * 减少视频播放器音量
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void decreaseVolume(Device device) {
		Intent movieIntent = new Intent(Video.BROADCAST_ACTION_VIDEO);
		Bundle movieBundle = new Bundle();
		movieBundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, device.getDeviceIp());
		movieBundle.putInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY, Video.SERVER_CMD_MOVIE_ITME_VOICE_DESC);
		movieIntent.putExtras(movieBundle);
		mContext.sendBroadcast(movieIntent);
	}

}
