package com.chinasvc.wipicophone.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.chinasvc.wipicophone.AudioPlayerActivity;
import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.WipicoApplication;
import com.chinasvc.wipicophone.bean.AudioBean;
import com.chinasvc.wipicophone.config.BroadcastConfig;
import com.chinasvc.wipicophone.config.Config;
import com.chinasvc.wipicophone.util.StringUtils;

/**
 * 歌词的界面
 * */
public class AudioPlayerFragment extends BaseFragment implements OnClickListener, OnSeekBarChangeListener {

	private View layout = null;
	private ImageButton btnRemote, btnPrev, btnPlay, btnNext, btnMode;
	private SeekBar mSeekBar;
	private TextView startTime, endTime;
	private TextView audioTitle, audioSummary;

	private int mode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.fragment_audio_player, container, false);
		return layout;
	}

	private void initView() {
		mode = ((AudioPlayerActivity) getActivity()).mode;

		btnRemote = (ImageButton) getActivity().findViewById(R.id.btnRemote);
		btnRemote.setOnClickListener(this);
		btnPrev = (ImageButton) getActivity().findViewById(R.id.btnPrev);
		btnPrev.setOnClickListener(this);
		btnPlay = (ImageButton) getActivity().findViewById(R.id.btnPlay);
		btnPlay.setOnClickListener(this);
		btnPlay.setImageResource(R.drawable.audio_play);
		isPlaying = false;
		btnNext = (ImageButton) getActivity().findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
		btnMode = (ImageButton) getActivity().findViewById(R.id.btnMode);
		btnMode.setOnClickListener(this);

		if (mode == Config.MODE_LOCAL) {
			btnRemote.setVisibility(View.GONE);
			btnMode.setVisibility(View.GONE);
		} else {
			btnRemote.setVisibility(View.VISIBLE);
			btnMode.setVisibility(View.VISIBLE);
		}

		mSeekBar = (SeekBar) getActivity().findViewById(R.id.seekBar);
		mSeekBar.setOnSeekBarChangeListener(this);
		mSeekBar.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// Pressed state
					mSeekBar.setThumb(getResources().getDrawable(R.drawable.player_thumb_pressed));
					break;
				case MotionEvent.ACTION_UP:
					mSeekBar.setThumb(getResources().getDrawable(R.drawable.player_thumb_normal));
					break;
				}
				return false;
			}
		});
		mSeekBar.setThumb(getActivity().getResources().getDrawable(R.drawable.player_thumb_selector));
		startTime = (TextView) getActivity().findViewById(R.id.start_time);
		endTime = (TextView) getActivity().findViewById(R.id.end_time);
		startTime.setText(StringUtils.generateTime(0));
		endTime.setText(StringUtils.generateTime(0));

		audioTitle = (TextView) getActivity().findViewById(R.id.audio_title);
		audioSummary = (TextView) getActivity().findViewById(R.id.audio_summary);
	}

	@Override
	public void onStart() {
		initThreadHandler();
		initView();
		registerReceiver();
		initPlayView();
		super.onStart();
	}

	public static String BROADCAST_PLAYER_OPEN = "com.chinasvc.player.BROADCAST_OPEN";
	public static String BROADCAST_PLAYER_OPEN_KEY = "player_key_path";
	public static String BROADCAST_PLAYER_PAUSE = "com.chinasvc.player.BROADCAST_PAUSE";
	public static String BROADCAST_PLAYER_PLAY = "com.chinasvc.player.BROADCAST_PLAY";

	/** 注册广播 */
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BROADCAST_PLAYER_OPEN);
		filter.addAction(BROADCAST_PLAYER_PAUSE);
		filter.addAction(BROADCAST_PLAYER_PLAY);
		filter.addAction(BroadcastConfig.BROADCAST_AUDIO_ASYNC);
		getActivity().registerReceiver(broadcastReceiver, filter);
	}

	/** 注销广播 */
	private void unregisterReceiver() {
		getActivity().unregisterReceiver(broadcastReceiver);
	}

	private static final int UPDATE_PROGRESS = 1;

	private static final int PROGRESS = 2;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case UPDATE_PROGRESS:
				if (mode == Config.MODE_REMOTE) {
					isRemoteTouch = false;
				} else {
					if (((AudioPlayerActivity) getActivity()).mAudioView != null) {
						mSeekBar.setProgress(((AudioPlayerActivity) getActivity()).mAudioView.getCurrentPosition());
						startTime.setText(StringUtils.generateTime(((AudioPlayerActivity) getActivity()).mAudioView.getCurrentPosition()));
					}
					mHandler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 500);
				}
				break;
			case PROGRESS:
				// 投影进度同步更新
				if (!isRemoteTouch && mode == Config.MODE_REMOTE) {
					mSeekBar.setMax(msg.arg2);
					mSeekBar.setProgress(msg.arg1);
					startTime.setText(StringUtils.generateTime(msg.arg1));
					endTime.setText(StringUtils.generateTime(msg.arg2));
				}
				break;
			}
		}
	};

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(BROADCAST_PLAYER_OPEN)) {
				openAudio();
			} else if (intent.getAction().equals(BROADCAST_PLAYER_PAUSE)) {
				mHandler.removeMessages(UPDATE_PROGRESS);
			} else if (intent.getAction().equals(BROADCAST_PLAYER_PLAY)) {
				mHandler.removeMessages(UPDATE_PROGRESS);
				mHandler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 500);
			} else if (intent.getAction().equals(BroadcastConfig.BROADCAST_AUDIO_ASYNC)) {
				mHandler.obtainMessage(PROGRESS, intent.getIntExtra(BroadcastConfig.KEY_PROGRESS, 0), intent.getIntExtra(BroadcastConfig.KEY_DURATION, 0),
						intent.getIntExtra(BroadcastConfig.KEY_STATE, 0)).sendToTarget();
			}
		}
	};

	private boolean isPlaying = false;

	private void initPlayView() {
		if (WipicoApplication.audioPosition >= 0 && WipicoApplication.listMusics.size() > 0 && WipicoApplication.listMusics.size() > WipicoApplication.audioPosition + 1) {
			AudioBean bean = WipicoApplication.listMusics.get(WipicoApplication.audioPosition);
			String path = bean.getPath();
			String title = path.substring(path.lastIndexOf("/") + 1);
			audioTitle.setText(title);
			audioSummary.setText(bean.getArtist() + " " + bean.getSpecial());
			if (mode == Config.MODE_LOCAL) {
				mSeekBar.setMax(((AudioPlayerActivity) getActivity()).mAudioView.getDuration());
				endTime.setText(StringUtils.generateTime(((AudioPlayerActivity) getActivity()).mAudioView.getDuration()));
			} else {
				mSeekBar.setProgress(0);
				startTime.setText(StringUtils.generateTime(0));
				endTime.setText(StringUtils.generateTime(0));
			}
		} else {
			audioTitle.setText(R.string.actionbar_title_audio_control_default);
			audioSummary.setText("");
			mSeekBar.setProgress(0);
			startTime.setText(StringUtils.generateTime(0));
			endTime.setText(StringUtils.generateTime(0));
		}
	}

	private void openAudio() {
		if (WipicoApplication.audioPosition >= 0 && WipicoApplication.listMusics.size() > 0) {
			AudioBean bean = WipicoApplication.listMusics.get(WipicoApplication.audioPosition);
			String path = bean.getPath();
			String title = path.substring(path.lastIndexOf("/") + 1);
			audioTitle.setText(title);
			audioSummary.setText(bean.getArtist() + " " + bean.getSpecial());
			if (mode == Config.MODE_LOCAL && ((AudioPlayerActivity) getActivity()).mAudioView != null) {
				mSeekBar.setMax(((AudioPlayerActivity) getActivity()).mAudioView.getDuration());
				endTime.setText(StringUtils.generateTime(((AudioPlayerActivity) getActivity()).mAudioView.getDuration()));
			}
			isPlaying = true;
			btnPlay.setImageResource(R.drawable.audio_pause);
			mHandler.sendEmptyMessage(UPDATE_PROGRESS);
		}
	}

	@Override
	public void onDestroy() {
		mHandler.removeMessages(UPDATE_PROGRESS);
		unregisterReceiver();
		super.onDestroy();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == btnRemote) {
			// 音量-
			if (mode == Config.MODE_REMOTE) {
				mThreadHandler.obtainMessage(AUDIO_DOWN).sendToTarget();
			} else {

			}
		} else if (view == btnMode) {
			// 音+
			if (mode == Config.MODE_REMOTE) {
				mThreadHandler.obtainMessage(AUDIO_UP).sendToTarget();
			} else {

			}
		} else if (view == btnPrev) {
			prevAudio();
		} else if (view == btnNext) {
			nextAudio();
		} else if (view == btnPlay) {
			if (isPlaying) {
				isPlaying = false;
				btnPlay.setImageResource(R.drawable.audio_play);
				Intent intent = new Intent();
				intent.setAction(AudioPlayerActivity.BROADCAST_PAUSE);
				getActivity().sendBroadcast(intent);
				if (mode == Config.MODE_LOCAL) {

				} else {
					mThreadHandler.obtainMessage(AUDIO_PAUSE).sendToTarget();
				}
			} else {
				isPlaying = true;
				btnPlay.setImageResource(R.drawable.audio_pause);
				Intent intent = new Intent();
				intent.setAction(AudioPlayerActivity.BROADCAST_PLAY);
				getActivity().sendBroadcast(intent);
				if (mode == Config.MODE_LOCAL) {

				} else {
					mThreadHandler.obtainMessage(AUDIO_PLAY).sendToTarget();
				}
			}
		}
	}

	private void nextAudio() {
		if (WipicoApplication.listMusics.size() > 0) {
			if (WipicoApplication.audioPosition >= WipicoApplication.listMusics.size() - 1) {
				WipicoApplication.audioPosition = 0;
			} else if (WipicoApplication.audioPosition < WipicoApplication.listMusics.size() - 1 && WipicoApplication.audioPosition >= 0) {
				WipicoApplication.audioPosition++;
			} else {
				WipicoApplication.audioPosition = 0;
			}
			Intent intent = new Intent();
			intent.setAction(AudioPlayerActivity.BROADCAST_OPEN);
			getActivity().sendBroadcast(intent);
		}
	}

	private void prevAudio() {
		if (WipicoApplication.listMusics.size() > 0) {
			if (WipicoApplication.audioPosition <= 0) {
				WipicoApplication.audioPosition = WipicoApplication.listMusics.size() - 1;
			} else if (WipicoApplication.audioPosition > 0 && WipicoApplication.audioPosition <= WipicoApplication.listMusics.size() - 1) {
				WipicoApplication.audioPosition--;
			} else {
				WipicoApplication.audioPosition = 0;
			}
			Intent intent = new Intent();
			intent.setAction(AudioPlayerActivity.BROADCAST_OPEN);
			getActivity().sendBroadcast(intent);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		if (mode == Config.MODE_LOCAL) {
			mHandler.removeMessages(UPDATE_PROGRESS);
		} else {
			mHandler.removeMessages(UPDATE_PROGRESS);
			isRemoteTouch = true;
		}
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (mode == Config.MODE_LOCAL) {
			Intent intent = new Intent();
			intent.setAction(AudioPlayerActivity.BROADCAST_SEEK);
			intent.putExtra(AudioPlayerActivity.BROADCAST_POS_KEY, seekBar.getProgress());
			getActivity().sendBroadcast(intent);

			mHandler.removeMessages(UPDATE_PROGRESS);
			mHandler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 1000);
		} else {
			mThreadHandler.obtainMessage(AUDIO_SEEKBAR, seekBar.getProgress(), 0).sendToTarget();
			mHandler.removeMessages(UPDATE_PROGRESS);
			mHandler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 1000);
		}
	}

	private boolean isRemoteTouch = false;

}
