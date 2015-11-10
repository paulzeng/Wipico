package com.chinasvc.wipicophone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasvc.wipico.sync.SyncPlayClient.AudioSyncListener;
import com.chinasvc.wipicophone.audioplayer.AudioPlayManager;
import com.chinasvc.wipicophone.audioplayer.AudioView;
import com.chinasvc.wipicophone.bean.AudioBean.PlayState;
import com.chinasvc.wipicophone.config.BroadcastConfig;
import com.chinasvc.wipicophone.config.Config;
import com.chinasvc.wipicophone.fragment.AudioListFragment;
import com.chinasvc.wipicophone.fragment.AudioPlayerFragment;
import com.chinasvc.wipicophone.fragment.FragmentAdapter;

public class AudioPlayerActivity extends MediaBaseFragmentActivity implements OnClickListener, OnErrorListener, OnPreparedListener, OnCompletionListener {

	private ViewPager mViewPager;
	private FragmentAdapter mPagerAdapter;

	private ImageView btnBack, btnStop;

	public AudioView mAudioView;

	private boolean isPaused = false;
	private boolean isOnline = false;

	public int mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_audio_player);
		((TextView) findViewById(R.id.actionbar_title)).setText(R.string.actionbar_title_audio_control);
		registerReceiver();

		initThreadHandler();

		mode = getIntent().getIntExtra("mode", Config.MODE_LOCAL);

		initView();

		initPager();

		if (mode == Config.MODE_LOCAL) {
			// 500ms后本地直接播放
			initMedia();
		}
		mHandler.sendEmptyMessageDelayed(PLAY_AUDIO, 500);

		MainActivity.syncPlayClient.setOnAudioSyncListener(new AudioAsync());

	}

	private class AudioAsync implements AudioSyncListener {
		@Override
		public void syncPlay(int progress, int duration, int status, boolean isSilent, int volume, int volumeMax) {
			if (status == -1) {
				if (WipicoApplication.listMusics.size() == 1) {
					Toast.makeText(getApplicationContext(), R.string.audio_msg_no_next, Toast.LENGTH_SHORT).show();
				} else {
					if (WipicoApplication.listMusics.size() > 0) {
						if (WipicoApplication.audioPosition >= WipicoApplication.listMusics.size() - 1) {
							WipicoApplication.audioPosition = 0;
						} else if (WipicoApplication.audioPosition < WipicoApplication.listMusics.size() - 1 && WipicoApplication.audioPosition >= 0) {
							WipicoApplication.audioPosition++;
						} else {
							WipicoApplication.audioPosition = 0;
						}
						mThreadHandler.obtainMessage(AUDIO_SEND, WipicoApplication.listMusics.get(WipicoApplication.audioPosition).getPath()).sendToTarget();
						AudioPlayManager.setAudioBean(WipicoApplication.listMusics.get(WipicoApplication.audioPosition));
						AudioPlayManager.setPlayState(PlayState.PLAYING);

						// 更新List列表
						Intent intent1 = new Intent();
						intent1.setAction(AudioListFragment.BROADCAST_UPDATE_LIST);
						sendBroadcast(intent1);

						// 广播一个播放状态
						Intent intent2 = new Intent();
						intent2.setAction(AudioPlayerFragment.BROADCAST_PLAYER_OPEN);
						sendBroadcast(intent2);
					}
				}
			}
			Intent intent = new Intent();
			intent.setAction(BroadcastConfig.BROADCAST_AUDIO_ASYNC);
			intent.putExtra(BroadcastConfig.KEY_STATE, status);
			intent.putExtra(BroadcastConfig.KEY_PROGRESS, progress);
			intent.putExtra(BroadcastConfig.KEY_DURATION, duration);
			sendBroadcast(intent);
		}
	}

	private static final int PLAY_AUDIO = 0;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case PLAY_AUDIO:
				openAudio();
				break;
			}
		}
	};

	@Override
	public void onBackPressed() {
		stop();
		super.onBackPressed();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver();
		super.onDestroy();
	}

	public static String BROADCAST_OPEN = "com.chinasvc.BROADCAST_OPEN";
	public static String BROADCAST_PAUSE = "com.chinasvc.BROADCAST_PAUSE";
	public static String BROADCAST_PLAY = "com.chinasvc.BROADCAST_PLAY";
	public static String BROADCAST_SEEK = "com.chinasvc.BROADCAST_SEEK";
	public static String BROADCAST_POS_KEY = "key_pos";

	/** 注册广播 */
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BROADCAST_OPEN);
		filter.addAction(BROADCAST_PAUSE);
		filter.addAction(BROADCAST_PLAY);
		filter.addAction(BROADCAST_SEEK);
		registerReceiver(broadcastReceiver, filter);
	}

	/** 注销广播 */
	private void unregisterReceiver() {
		unregisterReceiver(broadcastReceiver);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(BROADCAST_OPEN)) {
				openAudio();
			} else if (intent.getAction().equals(BROADCAST_PAUSE)) {
				if (mode == Config.MODE_REMOTE) {
					AudioPlayManager.setPlayState(PlayState.PAUSE);
					updateList();
				} else {
					pause();
				}
			} else if (intent.getAction().equals(BROADCAST_PLAY)) {
				if (mode == Config.MODE_REMOTE) {
					AudioPlayManager.setPlayState(PlayState.PLAYING);
					updateList();
				} else {
					play();
				}
			} else if (intent.getAction().equals(BROADCAST_SEEK)) {
				int pos = intent.getIntExtra(BROADCAST_POS_KEY, 0);
				setAudioProgress(pos);
			}
		}
	};

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		btnStop = (ImageView) findViewById(R.id.actionbar_stop);
		btnStop.setOnClickListener(this);

		if (mode == Config.MODE_REMOTE) {
			btnStop.setVisibility(View.VISIBLE);
		} else {
			btnStop.setVisibility(View.GONE);
		}

		btnBack = (ImageView) findViewById(R.id.actionbar_back);
		btnBack.setOnClickListener(this);

	}

	private void initPager() {
		mPagerAdapter = new FragmentAdapter(getSupportFragmentManager());
		mPagerAdapter.addFragment(new AudioListFragment());
		mPagerAdapter.addFragment(new AudioPlayerFragment());
		// mPagerAdapter.addFragment(new AudioLrcFragment());

		// Initiate ViewPager
		mViewPager.setPageMarginDrawable(R.drawable.viewpager_margin);
		mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(1);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnBack) {
			stop();
			finish();
		} else if (v == btnStop) {
			mThreadHandler.obtainMessage(AUDIO_STOP).sendToTarget();
			finish();
		}
	}

	private void initMedia() {
		if (mAudioView == null) {
			mAudioView = new AudioView(this);
		}
		mAudioView.setOnErrorListener(this);
		mAudioView.setOnPreparedListener(this);
		mAudioView.setOnCompletionListener(this);
	}

	public void stop() {
		AudioPlayManager.remove();
		updateList();
		if (mAudioView != null) {
			mAudioView.stopPlayback();
			isOnline = false;
			mAudioView = null;
		}
	}

	public void pause() {
		if (isOnline) {
			playPause(false);
			AudioPlayManager.setPlayState(PlayState.PAUSE);
			updateList();
		}
	}

	public void play() {
		if (isOnline) {
			playPause(true);
			AudioPlayManager.setPlayState(PlayState.PLAYING);
			updateList();
		}
	}

	private void playPause(boolean play) {
		if (mAudioView == null)
			return;
		if (isPaused && play) { // will play
			mAudioView.start();
			isPaused = false;
		}
		if (!isPaused && !play) { // will pause
			mAudioView.pause();
			isPaused = true;
		}
	}

	public void openAudio() {
		// TODO Auto-generated method stub
		if (WipicoApplication.audioPosition >= 0 && WipicoApplication.listMusics.size() > 0) {
			if (mode == Config.MODE_REMOTE) {
				// 广播一个播放状态 ,提示远程正在播放
				mThreadHandler.obtainMessage(AUDIO_SEND, WipicoApplication.listMusics.get(WipicoApplication.audioPosition).getPath()).sendToTarget();
				AudioPlayManager.setAudioBean(WipicoApplication.listMusics.get(WipicoApplication.audioPosition));
				AudioPlayManager.setPlayState(PlayState.PLAYING);
				updateList();

				// 广播一个播放状态
				Intent intent = new Intent();
				intent.setAction(AudioPlayerFragment.BROADCAST_PLAYER_OPEN);
				sendBroadcast(intent);
			} else {
				String path = "file://" + WipicoApplication.listMusics.get(WipicoApplication.audioPosition).getPath();
				if (path != null && !path.equals("")) {
					Uri uri = Uri.parse(path);
					if (mAudioView != null) {
						mAudioView.stopPlayback();
						mAudioView.setAudioURI(uri);
					}
					playPause(true);
				}
				AudioPlayManager.setAudioBean(WipicoApplication.listMusics.get(WipicoApplication.audioPosition));
				AudioPlayManager.setPlayState(PlayState.PAUSE);
				updateList();
			}
		}
	}

	public void setAudioProgress(int progress) {
		// TODO Auto-generated method stub
		if (isOnline) {
			mAudioView.seekTo(progress);
		}
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// 播放完成
		isOnline = false;
		if (WipicoApplication.audioPosition >= WipicoApplication.listMusics.size() - 1) {
			WipicoApplication.audioPosition = 0;
		} else if (WipicoApplication.audioPosition < WipicoApplication.listMusics.size() - 1 && WipicoApplication.audioPosition >= 0) {
			WipicoApplication.audioPosition++;
		}

		AudioPlayManager.remove();
		updateList();

		openAudio();

		Intent intent = new Intent();
		intent.setAction(AudioPlayerFragment.BROADCAST_PLAYER_PAUSE);
		sendBroadcast(intent);
	}

	private void updateList() {
		Intent intent = new Intent();
		intent.setAction(AudioListFragment.BROADCAST_UPDATE_LIST);
		sendBroadcast(intent);
	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		int total = mAudioView.getDuration();
		mAudioView.start();
		isOnline = true;

		AudioPlayManager.setPlayState(PlayState.PLAYING);
		updateList();

		// 广播一个播放状态
		Intent intent = new Intent();
		intent.setAction(AudioPlayerFragment.BROADCAST_PLAYER_OPEN);
		sendBroadcast(intent);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		mAudioView.stopPlayback();
		isOnline = false;
		// 播放结束

		AudioPlayManager.remove();
		updateList();

		Intent intent = new Intent();
		intent.setAction(AudioPlayerFragment.BROADCAST_PLAYER_PAUSE);
		sendBroadcast(intent);
		return false;
	}

}
