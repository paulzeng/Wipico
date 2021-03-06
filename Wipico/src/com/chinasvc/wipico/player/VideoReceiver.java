package com.chinasvc.wipico.player;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.chinasvc.wipico.util.WipicoConstant;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * 视频接收处理类
 * 
 * @since 1.0.0
 * */
public class VideoReceiver {

	private Context mContext;

	private SyncThread syncThread;

	private VideoReceiveListener mVideoReceiveListener;

	private MediaState mMediaState;

	/**
	 * 构造VideoReceiver实例对象
	 * 
	 * @param context
	 * */
	public VideoReceiver(Context context) {
		mContext = context;
		// 开启广播接收
		IntentFilter filter = new IntentFilter();
		filter.addAction(com.chinasvc.wipico.type.Video.BROADCAST_ACTION_VIDEO);
		mContext.registerReceiver(mReceiver, filter);
	}

	/**
	 * 开启视频接收控制
	 * */
	public void startReceive() {
		startSync();// 启动同步
		onNewIntent(((Activity) mContext).getIntent());
	}

	/**
	 * Activity onNewIntent 方法
	 * 
	 * <{@link Activity.onNewIntent}>
	 * */
	public void onNewIntent(Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			String path = bundle.getString(WipicoConstant.BUNDLE_MEDIA_URL_KEY);
			// mVideoReceiveListener.openVideo(path);
			String newPath = "";
			try {
				newPath = URLDecoder.decode(path, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (mVideoReceiveListener != null) {
				mVideoReceiveListener.openVideo(newPath);
			}
		}
	}

	/**
	 * 设置视频状态信息
	 * 
	 * @param mediaState
	 *                视频当前状态信息对象
	 * */
	public void setMediaState(MediaState mediaState) {
		mMediaState = mediaState;
	}

	/**
	 * 开启视频同步
	 * */
	private void startSync() {
		if (syncThread != null) {
			syncThread.stopSync();
			syncThread = null;
		}
		syncThread = new SyncThread();
		syncThread.startSync();
	}

	/**
	 * 关闭视频同步
	 * */
	private void stopSync() {
		if (syncThread != null) {
			syncThread.stopSync();
			syncThread = null;
		}
	}

	/**
	 * 播放同步线程
	 * */
	private class SyncThread extends Thread {

		boolean needSync = true;

		/**
		 * 启动同步
		 * */
		public void startSync() {
			needSync = true;
			start();
		}

		/**
		 * 停止同步
		 * */
		public void stopSync() {
			needSync = false;
			try {
				join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (needSync) {
				if (mMediaState != null) {
					Intent intent = new Intent();
					intent.setAction(PlayerConstant.BROADCAST_UPDATE_MEDIA);
					Bundle bundle = new Bundle();
					bundle.putInt(PlayerConstant.KEY_PLAY_FLAG, PlayerConstant.PLAY_VIDEO);
					bundle.putInt(PlayerConstant.KEY_PLAY_STATE, mMediaState.getPlayState());
					bundle.putInt(PlayerConstant.KEY_PLAY_DURATION, mMediaState.getDuration());
					bundle.putInt(PlayerConstant.KEY_PLAY_PROGRESS, mMediaState.getPosition());
					bundle.putInt(PlayerConstant.KEY_PLAY_VOLUME, mMediaState.getVolume());
					bundle.putInt(PlayerConstant.KEY_PLAY_VOLUME_MAX, mMediaState.getVolumeMax());
					bundle.putBoolean(PlayerConstant.KEY_PLAY_SILENT, mMediaState.isSilent());
					intent.putExtras(bundle);
					mContext.sendBroadcast(intent);
				}
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = new Bundle();
			bundle = intent.getExtras();
			int itemCmd = bundle.getInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY);
			switch (itemCmd) {
			case com.chinasvc.wipico.type.Video.SERVER_CMD_MOVIE_ITEM_SEEKBAR:
				int progress = bundle.getInt(WipicoConstant.BUNDLE_MEDIA_SEEKBAR_PROGRESS_KEY);
				if (mVideoReceiveListener != null) {
					mVideoReceiveListener.setVideoProgress(progress);
				}
				break;
			case com.chinasvc.wipico.type.Video.SERVER_CMD_MOVIE_ITEM_OPEN:
				String path = bundle.getString(WipicoConstant.BUNDLE_MEDIA_URL_KEY);
				String newPath = "";
				try {
					newPath = URLDecoder.decode(path, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (mVideoReceiveListener != null) {
					mVideoReceiveListener.openVideo(newPath);
				}
				break;
			case com.chinasvc.wipico.type.Video.SERVER_CMD_MOVIE_ITEM_STOP:
				if (mVideoReceiveListener != null) {
					mVideoReceiveListener.stop();
				}
				break;
			case com.chinasvc.wipico.type.Video.SERVER_CMD_MOVIE_ITEM_PAUSE:
				if (mVideoReceiveListener != null) {
					mVideoReceiveListener.pause();
				}
				break;
			case com.chinasvc.wipico.type.Video.SERVER_CMD_MOVIE_ITEM_PLAY:
				if (mVideoReceiveListener != null) {
					mVideoReceiveListener.play();
				}
				break;
			case com.chinasvc.wipico.type.Video.SERVER_CMD_MOVIE__ITME_MUTE:
				if (mVideoReceiveListener != null) {
					mVideoReceiveListener.mute();
				}
				break;
			case com.chinasvc.wipico.type.Video.SERVER_CMD_MOVIE__ITME_VOICE:
				if (mVideoReceiveListener != null) {
					mVideoReceiveListener.disMute();
				}
				break;
			case com.chinasvc.wipico.type.Video.SERVER_CMD_MOVIE_ITME_VOICE_ADD:
				if (mVideoReceiveListener != null) {
					mVideoReceiveListener.addVolume();
				}
				break;
			case com.chinasvc.wipico.type.Video.SERVER_CMD_MOVIE_ITME_VOICE_DESC:
				if (mVideoReceiveListener != null) {
					mVideoReceiveListener.decreaseVolume();
				}
				break;
			}

		}
	};

	/**
	 * 关闭多媒体接收控制
	 * */
	public void closeReceive() {
		mContext.unregisterReceiver(mReceiver);
		stopSync();
	}

	/**
	 * 设置视频控制接收监听器
	 * 
	 * @param videoReceiveListener
	 *                控制接收监听器
	 * */
	public void setVideoReceiveListener(VideoReceiveListener videoReceiveListener) {
		this.mVideoReceiveListener = videoReceiveListener;
	}

}
