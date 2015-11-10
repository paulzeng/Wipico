package com.chinasvc.wipicophone.fragment;

import com.chinasvc.wipicophone.AudioPlayerActivity;
import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.WipicoApplication;
import com.chinasvc.wipicophone.adapter.AudioPlayerAdapter;
import com.chinasvc.wipicophone.util.FileUtil;
import com.chinasvc.wipicophone.util.MultimediaUtil;
import com.chinasvc.wipicophone.widget.SideBar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 歌曲列表
 * */
public class AudioListFragment extends Fragment implements OnItemClickListener {
	private String TAG = "AudioListFragment";

	private View layout = null;
	private AudioPlayerAdapter adapter;

	private ListView mListView;
	private SideBar indexBar;
	private TextView mDialogText;

	private LinearLayout emptyView;

	private TextView emptyValue;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.fragment_audio_list, container, false);
		return layout;
	}

	@Override
	public void onStart() {
		super.onStart();

		registerReceiver();
		initView();
	}

	private void initView() {
		emptyView = (LinearLayout) getActivity().findViewById(R.id.musicEmptyView);
		emptyValue = (TextView) getActivity().findViewById(R.id.musicEmptyValue);

		mListView = (ListView) getActivity().findViewById(R.id.mListView);
		mListView.setOnItemClickListener(this);
		indexBar = (SideBar) getActivity().findViewById(R.id.sideBar);

		mDialogText = (TextView) getActivity().findViewById(R.id.sideBarDialog);
		mDialogText.setVisibility(View.INVISIBLE);

		indexBar.setTextView(mDialogText);

		if (WipicoApplication.listMusics == null) {
			MultimediaUtil multimediaUtil = new MultimediaUtil(getActivity());
			WipicoApplication.listMusics = multimediaUtil.getMusic();
		}

		if (WipicoApplication.listMusics.size() <= 0) {
			emptyView.setVisibility(View.VISIBLE);
			if (!FileUtil.isExternalStorageAvailable()) {
				emptyValue.setText(R.string.msg_no_sdcard);
			} else {
				emptyValue.setText(R.string.msg_no_music);
			}
			indexBar.setVisibility(View.GONE);
		} else {
			emptyView.setVisibility(View.GONE);
			indexBar.setVisibility(View.VISIBLE);
			// 实例化自定义内容适配类
			adapter = new AudioPlayerAdapter(getActivity(), WipicoApplication.listMusics, true);
			// 为listView设置适配
			mListView.setAdapter(adapter);
			// 设置SideBar的ListView内容实现点击a-z中任意一个进行定位
			indexBar.setListView(mListView, adapter);
		}
	}

	@Override
	public void onDestroy() {
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

	public static String BROADCAST_UPDATE_LIST = "com.chinasvc.BROADCAST_UPDATE_LIST";

	/** 注册广播 */
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BROADCAST_UPDATE_LIST);
		getActivity().registerReceiver(broadcastReceiver, filter);
	}

	/** 注销广播 */
	private void unregisterReceiver() {
		getActivity().unregisterReceiver(broadcastReceiver);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(BROADCAST_UPDATE_LIST)) {
				adapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		WipicoApplication.audioPosition = position;
		((AudioPlayerActivity) getActivity()).openAudio();
	}
}
