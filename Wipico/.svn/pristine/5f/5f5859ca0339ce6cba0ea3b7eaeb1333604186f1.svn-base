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
 * 图片接收处理类
 * 
 * @since 1.0.0
 * */
public class ImageReceiver {

	private Context mContext;

	private float currentMiddleX;// 当前缩放中心点的X坐标
	private float currentMiddleY;// 当前缩放中心点的Y坐标
	private float currentScale;// 当前缩放比例

	private float distanceX;// 滚动的X坐标
	private float distanceY;// 滚动的Y坐标

	private float motionEventX;// 点击事件的x坐标
	private float motionEventY;// 点击事件的y坐标

	private SyncThread syncThread;

	private ImageReceiveListener receiveListener;

	/**
	 * 构造ImageReceiver实例
	 * 
	 * @param context
	 * */
	public ImageReceiver(Context context) {
		mContext = context;
		// 开启广播接收
		IntentFilter filter = new IntentFilter();
		filter.addAction(com.chinasvc.wipico.type.Image.BROADCAST_ACTION_IMAGE);
		mContext.registerReceiver(mReceiver, filter);
	}

	/**
	 * 开启图片接收控制
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
			try {
				String path = bundle.getString(WipicoConstant.BUNDLE_MEDIA_URL_KEY);
				String newPath = "";
				newPath = URLDecoder.decode(path, "utf-8");
				if (receiveListener != null) {
					receiveListener.openImage(newPath);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 开启图片同步
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
	 * 停止图片同步
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
		 * 启动同步
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
			// TODO Auto-generated method stub
			while (needSync) {
				Intent intent = new Intent();
				intent.setAction(PlayerConstant.BROADCAST_UPDATE_MEDIA);
				Bundle bundle = new Bundle();
				bundle.putInt(PlayerConstant.KEY_PLAY_FLAG, PlayerConstant.PLAY_IMAGE);
				bundle.putInt(PlayerConstant.KEY_PLAY_STATE, PlayerConstant.PLAYER_PAUSE);
				bundle.putInt(PlayerConstant.KEY_PLAY_DURATION, 0);
				bundle.putInt(PlayerConstant.KEY_PLAY_PROGRESS, 0);
				bundle.putInt(PlayerConstant.KEY_PLAY_VOLUME, 0);
				bundle.putInt(PlayerConstant.KEY_PLAY_VOLUME_MAX, 0);
				bundle.putBoolean(PlayerConstant.KEY_PLAY_SILENT, false);
				intent.putExtras(bundle);
				mContext.sendBroadcast(intent);
				try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 关闭图片接收控制
	 * */
	public void stopReceive() {
		mContext.unregisterReceiver(mReceiver);
		stopSync();
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = new Bundle();
			bundle = intent.getExtras();
			int itemCmd = bundle.getInt(WipicoConstant.BUNDLE_SERVER_ITEM_KEY);
			switch (itemCmd) {
			case com.chinasvc.wipico.type.Image.SERVER_CMD_IMAGE_ITEM_OPEN:
				String path = bundle.getString(com.chinasvc.wipico.type.Image.BUNDLE_MEDIA_URL_KEY);
				String newPath = "";
				try {
					newPath = URLDecoder.decode(path, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (receiveListener != null) {
					receiveListener.openImage(newPath);
				}
				break;
			case com.chinasvc.wipico.type.Image.SERVER_CMD_IMAGE_ITEM_STOP:
				if (receiveListener != null) {
					receiveListener.stop();
				}
				break;
			case com.chinasvc.wipico.type.Image.SERVER_CMD_IMAGE_ITEM_TURN_LEFT:
				if (receiveListener != null) {
					receiveListener.turnLeft();
				}
				break;
			case com.chinasvc.wipico.type.Image.SERVER_CMD_IMAGE_ITEM_TURN_RIGHT:
				if (receiveListener != null) {
					receiveListener.turnRight();
				}
				break;
			case com.chinasvc.wipico.type.Image.SERVER_CMD_IMAGE_ITEM_ZOOM_IN:
				if (receiveListener != null) {
					receiveListener.zoomIn();
				}
				break;
			case com.chinasvc.wipico.type.Image.SERVER_CMD_IMAGE_ITEM_ZOOM_OUT:
				if (receiveListener != null) {
					receiveListener.zoomOut();
				}
				break;
			case com.chinasvc.wipico.type.Image.SERVER_CMD_IMAGE_ITEM_CHANGE:
				int type = bundle.getInt(com.chinasvc.wipico.type.Image.BUNDLE_IMAGE_ACTION_FLAG);
				switch (type) {
				case com.chinasvc.wipico.type.Image.IMAGE_FLAG_ONSCALEEND:
					// 发 currentScale，currentMiddleX，
					// currentMiddleY
					currentScale = bundle.getFloat(com.chinasvc.wipico.type.Image.BUNDLE_IMAGE_CURRENT_SCALE);
					currentMiddleX = bundle.getFloat(com.chinasvc.wipico.type.Image.BUNDLE_IMAGE_CURRENT_MIDDLEX);
					currentMiddleY = bundle.getFloat(com.chinasvc.wipico.type.Image.BUNDLE_IMAGE_CURRENT_MIDDLEY);
					if (receiveListener != null) {
						receiveListener.onScaleEnd(currentScale, currentMiddleX, currentMiddleY);
					}
					break;
				case com.chinasvc.wipico.type.Image.IMAGE_FLAG_ONSCALE:
					// 发 currentScale，currentMiddleX，
					// currentMiddleY
					currentScale = bundle.getFloat(com.chinasvc.wipico.type.Image.BUNDLE_IMAGE_CURRENT_SCALE);
					currentMiddleX = bundle.getFloat(com.chinasvc.wipico.type.Image.BUNDLE_IMAGE_CURRENT_MIDDLEX);
					currentMiddleY = bundle.getFloat(com.chinasvc.wipico.type.Image.BUNDLE_IMAGE_CURRENT_MIDDLEY);
					if (receiveListener != null) {
						receiveListener.onScale(currentScale, currentMiddleX, currentMiddleY);
					}
					break;
				case com.chinasvc.wipico.type.Image.IMAGE_FLAG_ONSCROLL:
					// 发 distanceX ，distanceY；
					distanceX = bundle.getFloat(com.chinasvc.wipico.type.Image.BUNDLE_IMAGE_DISTANCE_X);
					distanceY = bundle.getFloat(com.chinasvc.wipico.type.Image.BUNDLE_IMAGE_DISTANCE_Y);
					if (receiveListener != null) {
						receiveListener.onScroll(distanceX, distanceY);
					}
					break;
				case com.chinasvc.wipico.type.Image.IMAGE_FLAG_ONDOUBLETAP:
					// 发 motionEventX，motionEventY
					// Switch between the original scale and
					// 3x scale.
					motionEventX = bundle.getFloat(com.chinasvc.wipico.type.Image.BUNDLE_IMAGE_EVENT_X);
					motionEventY = bundle.getFloat(com.chinasvc.wipico.type.Image.BUNDLE_IMAGE_EVENT_Y);
					if (receiveListener != null) {
						receiveListener.onDoubleTap(motionEventX, motionEventY);
					}
					break;
				}
				break;
			}
		}
	};

	/**
	 * 设置控制接收监听器
	 * 
	 * @param receiveListener
	 *                控制接收监听器
	 * */
	public void setReceiveListener(ImageReceiveListener receiveListener) {
		this.receiveListener = receiveListener;
	}

}
