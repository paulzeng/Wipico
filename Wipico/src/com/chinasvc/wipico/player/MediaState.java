package com.chinasvc.wipico.player;

/**
 * 多媒体状态类
 * 
 * @since 1.0.0
 * */
public class MediaState {

	private int duration;
	private int position;
	private int playState = 0;
	private int volume = 0;
	private int volumeMax = 0;
	private boolean isSilent = false;

	/**
	 * 是否静音
	 * */
	public boolean isSilent() {
		return isSilent;
	}

	/**
	 * 设置是否静音
	 * */
	public void setSilent(boolean isSilent) {
		this.isSilent = isSilent;
	}

	/**
	 * 播放状态
	 * */
	public int getPlayState() {
		return playState;
	}

	/**
	 * 设置播放状态
	 * */
	public void setPlayState(int playState) {
		this.playState = playState;
	}

	/**
	 * 获取媒体的总时长
	 * */
	public int getDuration() {
		return duration;
	}

	/**
	 * 设置媒体的总时长
	 * 
	 * @param duration
	 *                当期的播放位置，单位为ms
	 * */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * 获取当前媒体的播放位置
	 * */
	public int getPosition() {
		return position;
	}

	/**
	 * 设置当前媒体的播放位置
	 * 
	 * @param position
	 *                当期的播放位置，单位为ms
	 * */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * 获取当前音量
	 * */
	public int getVolume() {
		return volume;
	}

	/**
	 * 设置当前音量
	 * */
	public void setVolume(int volume) {
		this.volume = volume;
	}

	/**
	 * 获取最大音量
	 * */
	public int getVolumeMax() {
		return volumeMax;
	}

	/**
	 * 设置最大音量
	 * */
	public void setVolumeMax(int volumeMax) {
		this.volumeMax = volumeMax;
	}

	@Override
	public String toString() {
		return "MediaState [duration=" + duration + ", position=" + position + ", playState=" + playState + ", volume=" + volume + ", volumeMax=" + volumeMax + ", isSilent=" + isSilent + "]";
	}

}
