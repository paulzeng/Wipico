package com.chinasvc.wipicophone;

import com.chinasvc.wipico.bean.ImageTransform;
import com.chinasvc.wipico.client.AudioHelper;
import com.chinasvc.wipico.client.ControlHelper;
import com.chinasvc.wipico.client.ImageHelper;
import com.chinasvc.wipico.client.VideoHelper;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

public class MediaBaseFragmentActivity extends FragmentActivity {

	public Handler mThreadHandler = null;// 线程Handler

	public final int IMAGE_OPEN = 1;
	public final int IMAGE_TURN_LEFT = 2;
	public final int IMAGE_TURN_RIGHT = 3;
	public final int IMAGE_STOP = 4;
	public final int IMAGE_TRANSFORM = 5;

	public final int VIDEO_SEND = 11;
	public final int VIDEO_STOP = 12;
	public final int VIDEO_PLAY = 13;
	public final int VIDEO_PAUSE = 14;
	public final int VIDEO_UP = 15;
	public final int VIDEO_DOWN = 16;
	public final int VIDEO_SEEKBAR = 17;

	public final int AUDIO_SEND = 21;
	public final int AUDIO_STOP = 22;
	public final int AUDIO_PLAY = 23;
	public final int AUDIO_PAUSE = 24;
	public final int AUDIO_UP = 25;
	public final int AUDIO_DOWN = 26;
	public final int AUDIO_SEEKBAR = 27;

	public static final int STOP = 0;

	/**
	 * 初始化遥控器控制发送的Handler
	 * */
	@SuppressLint("HandlerLeak")
	public void initThreadHandler() {
		HandlerThread handlerThread = new HandlerThread("HandlerThread");
		handlerThread.start();
		mThreadHandler = new Handler(handlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				if (MainActivity.mDevice != null) {
					switch (msg.what) {
					case IMAGE_OPEN:
						ImageHelper.getInstance(MediaBaseFragmentActivity.this).openImage((String) msg.obj, MainActivity.mDevice, false);
						break;
					case IMAGE_TURN_LEFT:
						ImageHelper.getInstance(MediaBaseFragmentActivity.this).turnLeft(MainActivity.mDevice);
						break;
					case IMAGE_TURN_RIGHT:
						ImageHelper.getInstance(MediaBaseFragmentActivity.this).turnRight(MainActivity.mDevice);
						break;
					case IMAGE_STOP:
						ImageHelper.getInstance(MediaBaseFragmentActivity.this).stop(MainActivity.mDevice);
						break;
					case IMAGE_TRANSFORM:
						ImageHelper.getInstance(MediaBaseFragmentActivity.this).transform(MainActivity.mDevice, (ImageTransform) msg.obj);
						break;
					case VIDEO_SEND:
						VideoHelper.getInstance(MediaBaseFragmentActivity.this).openVideo((String) msg.obj, MainActivity.mDevice, false);
						break;
					case VIDEO_STOP:
						VideoHelper.getInstance(MediaBaseFragmentActivity.this).stop(MainActivity.mDevice);
						mThreadHandler.sendEmptyMessageDelayed(STOP, 0);
						break;
					case VIDEO_PLAY:
						VideoHelper.getInstance(MediaBaseFragmentActivity.this).play(MainActivity.mDevice);
						break;
					case VIDEO_PAUSE:
						VideoHelper.getInstance(MediaBaseFragmentActivity.this).pause(MainActivity.mDevice);
						break;
					case VIDEO_UP:
						VideoHelper.getInstance(MediaBaseFragmentActivity.this).addVolume(MainActivity.mDevice);
						break;
					case VIDEO_DOWN:
						VideoHelper.getInstance(MediaBaseFragmentActivity.this).decreaseVolume(MainActivity.mDevice);
						break;
					case VIDEO_SEEKBAR:
						VideoHelper.getInstance(MediaBaseFragmentActivity.this).setSeek(msg.arg1, MainActivity.mDevice);
						break;
					case AUDIO_SEND:
						AudioHelper.getInstance(MediaBaseFragmentActivity.this).openAudio((String) msg.obj, MainActivity.mDevice, false);
						break;
					case AUDIO_STOP:
						AudioHelper.getInstance(MediaBaseFragmentActivity.this).stop(MainActivity.mDevice);
						mThreadHandler.sendEmptyMessageDelayed(STOP, 0);
						break;
					case AUDIO_PLAY:
						AudioHelper.getInstance(MediaBaseFragmentActivity.this).play(MainActivity.mDevice);
						break;
					case AUDIO_PAUSE:
						AudioHelper.getInstance(MediaBaseFragmentActivity.this).pause(MainActivity.mDevice);
						break;
					case AUDIO_UP:
						AudioHelper.getInstance(MediaBaseFragmentActivity.this).addVolume(MainActivity.mDevice);
						break;
					case AUDIO_DOWN:
						AudioHelper.getInstance(MediaBaseFragmentActivity.this).decreaseVolume(MainActivity.mDevice);
						break;
					case AUDIO_SEEKBAR:
						AudioHelper.getInstance(MediaBaseFragmentActivity.this).setSeek(msg.arg1, MainActivity.mDevice);
						break;
					case STOP:
						KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
						ControlHelper.sendControl(keyEvent, MainActivity.mDevice);
						break;
					}
				} else {
					Toast.makeText(MediaBaseFragmentActivity.this, R.string.msg_no_device, Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

}
