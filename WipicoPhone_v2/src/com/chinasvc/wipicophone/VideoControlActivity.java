package com.chinasvc.wipicophone;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.chinasvc.wipico.sync.SyncPlayClient.VideoSyncListener;
import com.chinasvc.wipicophone.util.StringUtils;

public class VideoControlActivity extends MediaBaseActivity {

	private TextView mMediaTitle;
	private View btn_back;
	private SeekBar mSeekBar;
	private ImageView media_backward, media_forward, media_volum_down, media_volum_up, media_controller_player;
	private ImageButton voice;
	private ImageButton stop;
	private boolean isSlien = false;

	private String path = "";
	private String title = "";

	private TextView media_current_time, media_total_time;

	private ImageView thumbnails;// 视频预览图

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_control);
		((TextView) findViewById(R.id.actionbar_title)).setText(R.string.actionbar_title_video_control);

		initThreadHandler();

		path = getIntent().getExtras().getString("url");
		title = getIntent().getExtras().getString("title");

		initView();

		MainActivity.syncPlayClient.setOnVideoAsyncListener(new VideoAsync());
	}

	private class VideoAsync implements VideoSyncListener {
		@Override
		public void syncPlay(int progress, int duration, int status, boolean isSilent, int volume, int volumeMax) {
			mHandler.obtainMessage(0, progress, duration, status).sendToTarget();
		}
	}

	private void initView() {
		mMediaTitle = (TextView) findViewById(R.id.name);
		thumbnails = (ImageView) findViewById(R.id.thumbnails);
		if (title != null && !title.equals("")) {
			mMediaTitle.setText(title);
		} else {
			mMediaTitle.setText(path.substring(path.lastIndexOf("/") + 1));
		}

		if (path != null && !path.equals("")) {
			Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, Images.Thumbnails.MINI_KIND);
			if (bitmap != null) {
				thumbnails.setImageBitmap(bitmap);
			}
		}
		mSeekBar = (SeekBar) findViewById(R.id.seekBar);
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChange());

		btn_back = findViewById(R.id.actionbar_back);
		btn_back.setOnClickListener(new OnClick());
		// btn_stop = findViewById(R.id.btn_stop);
		// btn_stop.setOnClickListener(new OnClick());

		media_backward = (ImageView) findViewById(R.id.prev);
		media_backward.setOnClickListener(new OnClick());
		media_forward = (ImageView) findViewById(R.id.next);
		media_forward.setOnClickListener(new OnClick());
		media_volum_down = (ImageView) findViewById(R.id.volDesc);
		media_volum_down.setOnClickListener(new OnClick());
		media_volum_up = (ImageView) findViewById(R.id.volAdd);
		media_volum_up.setOnClickListener(new OnClick());
		media_controller_player = (ImageView) findViewById(R.id.play);
		media_controller_player.setOnClickListener(new OnClick());
		media_controller_player.setImageResource(R.drawable.media_btn_pause_selector);
		voice = (ImageButton) findViewById(R.id.voice);
		voice.setOnClickListener(new OnClick());
		stop = (ImageButton) findViewById(R.id.stop);
		stop.setOnClickListener(new OnClick());

		thumbnails = (ImageView) findViewById(R.id.thumbnails);

		media_current_time = (TextView) findViewById(R.id.current_time);
		media_total_time = (TextView) findViewById(R.id.total_time);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private class OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (media_backward == v) {
				mSeekProgress = mSeekBar.getProgress() - 10000;
				if (mSeekProgress > 0) {
					mThreadHandler.obtainMessage(VIDEO_SEEKBAR, mSeekProgress, 0).sendToTarget();
				} else {
					mSeekProgress = 0;
					mThreadHandler.obtainMessage(VIDEO_SEEKBAR, mSeekProgress, 0).sendToTarget();
				}
			} else if (media_forward == v) {
				mSeekProgress = mSeekBar.getProgress() + 10000;
				if (mSeekProgress > mSeekBar.getMax()) {
					mSeekProgress = mSeekBar.getMax();
					mThreadHandler.obtainMessage(VIDEO_SEEKBAR, mSeekProgress, 0).sendToTarget();
				} else {
					mThreadHandler.obtainMessage(VIDEO_SEEKBAR, mSeekProgress, 0).sendToTarget();
				}
			} else if (media_volum_down == v) {
				mThreadHandler.sendEmptyMessage(VIDEO_DOWN);
			} else if (media_volum_up == v) {
				mThreadHandler.sendEmptyMessage(VIDEO_UP);
			} else if (media_controller_player == v) {
				if (isPlay) {
					isPlay = !isPlay;
					media_controller_player.setImageResource(R.drawable.media_btn_play_selector);
					mThreadHandler.sendEmptyMessage(VIDEO_PAUSE);
				} else {
					isPlay = !isPlay;
					media_controller_player.setImageResource(R.drawable.media_btn_pause_selector);
					mThreadHandler.sendEmptyMessage(VIDEO_PLAY);
				}
			} else if (v == btn_back) {
				finish();
			} else if (v == voice) {
				if (isSlien) {
					isSlien = !isSlien;
					voice.setImageResource(R.drawable.media_btn_volume_selector);// 非静音
					mThreadHandler.sendEmptyMessage(VIDEO_VOICE);
				} else {
					isSlien = !isSlien;
					voice.setImageResource(R.drawable.media_btn_volume_mute_selector);// 静音
					mThreadHandler.sendEmptyMessage(VIDEO_SILENT);
				}
			} else if (v == stop) {
				mThreadHandler.sendEmptyMessage(VIDEO_STOP);
				finish();
			}
		}
	}

	private boolean isPlay = true;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (!isTouchSeekBar) {
				mSeekBar.setProgress(msg.arg1);
				mSeekBar.setMax(msg.arg2);
				media_current_time.setText(StringUtils.generateTime(msg.arg1));
				media_total_time.setText(StringUtils.generateTime(msg.arg2));
			}
		}
	};

	private int mSeekProgress;// 拖动后的进度
	private boolean isTouchSeekBar;

	private class OnSeekBarChange implements OnSeekBarChangeListener {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (fromUser) {
				mSeekProgress = progress;
				media_current_time.setText(StringUtils.generateTime(progress));
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			isTouchSeekBar = true;
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			mThreadHandler.obtainMessage(VIDEO_SEEKBAR, mSeekProgress, 0).sendToTarget();
			delay(500);
			isTouchSeekBar = false;
		}
	}

	private void delay(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
