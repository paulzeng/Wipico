package com.chinasvc.wipicophone.audioplayer;

import com.chinasvc.wipicophone.bean.AudioBean;
import com.chinasvc.wipicophone.bean.AudioBean.PlayState;

public class AudioPlayManager {

	private static AudioBean bean;

	public static void setAudioBean(AudioBean audioBean) {
		if (bean != null) {
			bean.setPlayState(PlayState.NORMAL);
			bean = audioBean;
		} else {
			bean = audioBean;
		}
	}

	public static void setPlayState(PlayState value) {
		if (bean != null) {
			bean.setPlayState(value);
		}
	}

	public static void remove() {
		if (bean != null) {
			bean.setPlayState(PlayState.NORMAL);
		}
	}

}
