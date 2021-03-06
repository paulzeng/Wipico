package com.chinasvc.wipico.client;

import android.content.Context;

import com.chinasvc.wipico.bean.Device;
import com.chinasvc.wipico.tools.HttpLocalIpUtil;
import com.chinasvc.wipico.type.Video;
import com.chinasvc.wipico.util.DesUtils;
import com.chinasvc.wipico.util.WipicoConstant;

/**
 * 客户端视频操作请求类
 * 
 * @since 1.0.0
 * */
public class VideoHelper {

	private static VideoHelper instance;

	private static Context mContext;

	/**
	 * 构造一个VideoHelper实例
	 * 
	 * @param context
	 *                context
	 * */
	public VideoHelper(Context context) {
		mContext = context;
	}

	/**
	 * 打开指定路径的视频
	 * 
	 * @param path
	 *                路径名称
	 * @param device
	 *                所连接的设备
	 * */
	public void openVideo(String path, Device device, boolean isEncrypt) {
		path = pathCorrect(path);
		String newPath = path;
		if (isEncrypt) {
			try {
				newPath = DesUtils.getInstance().encrypt(path);
			} catch (Exception e) {
				newPath = path;
			}
		}
		ActionSender.sendMediaOperate(WipicoConstant.SERVER_CMD_VIDEO, Video.SERVER_CMD_MOVIE_ITEM_OPEN, newPath, device.getDeviceIp());
	}

	/**
	 * 返回当前类的实例化对象
	 * 
	 * @param context
	 * */
	public static synchronized VideoHelper getInstance(Context context) {
		if (instance == null) {
			instance = new VideoHelper(context);
		}
		return instance;
	}

	/**
	 * 路径修正
	 * */
	private String pathCorrect(String path) {
		if (path == null) {
			return "";
		}
		if (!path.startsWith("http") && path.startsWith("/")) {
			path = "http://" + HttpLocalIpUtil.getInstance(mContext).getLocalIpAddress() + ":" + WipicoConstant.HTTP_PORT + path;
		}
		return path;
	}

	/**
	 * 设置进度
	 * 
	 * @param progress
	 *                进度值ms
	 * @param device
	 *                所连接的设备
	 * */
	public void setSeek(int progress, Device device) {
		ActionSender.sendMediaOperate(WipicoConstant.SERVER_CMD_VIDEO, Video.SERVER_CMD_MOVIE_ITEM_SEEKBAR, progress, device.getDeviceIp());
	}

	/**
	 * 退出音乐播放器
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void stop(Device device) {
		ActionSender.sendMediaOperate(WipicoConstant.SERVER_CMD_VIDEO, Video.SERVER_CMD_MOVIE_ITEM_STOP, 0, device.getDeviceIp());
	}

	/**
	 * 暂停音乐播放器
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void pause(Device device) {
		ActionSender.sendMediaOperate(WipicoConstant.SERVER_CMD_VIDEO, Video.SERVER_CMD_MOVIE_ITEM_PAUSE, 0, device.getDeviceIp());
	}

	/**
	 * 播放音乐
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void play(Device device) {
		ActionSender.sendMediaOperate(WipicoConstant.SERVER_CMD_VIDEO, Video.SERVER_CMD_MOVIE_ITEM_PLAY, 0, device.getDeviceIp());
	}

	/**
	 * 设置静音
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void mute(Device device) {
		ActionSender.sendMediaOperate(WipicoConstant.SERVER_CMD_VIDEO, Video.SERVER_CMD_MOVIE__ITME_MUTE, 0, device.getDeviceIp());
	}

	/**
	 * 设置非静音
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void disMute(Device device) {
		ActionSender.sendMediaOperate(WipicoConstant.SERVER_CMD_VIDEO, Video.SERVER_CMD_MOVIE__ITME_VOICE, 0, device.getDeviceIp());
	}

	/**
	 * 增加音乐
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void addVolume(Device device) {
		ActionSender.sendMediaOperate(WipicoConstant.SERVER_CMD_VIDEO, Video.SERVER_CMD_MOVIE_ITME_VOICE_ADD, 0, device.getDeviceIp());
	}

	/**
	 * 减少音量
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public void decreaseVolume(Device device) {
		ActionSender.sendMediaOperate(WipicoConstant.SERVER_CMD_VIDEO, Video.SERVER_CMD_MOVIE_ITME_VOICE_DESC, 0, device.getDeviceIp());
	}

}
