package com.chinasvc.wipicophone.fragment;

import com.chinasvc.wipicophone.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 歌词的界面
 * */
public class AudioLrcFragment extends Fragment {

	private View lrc_view = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		lrc_view = inflater.inflate(R.layout.fragment_audio_lrc, container, false);
		return lrc_view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onDestroy() {
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

}
