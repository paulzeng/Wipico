package com.chinasvc.wipicophone.view;

import com.chinasvc.wipicophone.R;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MenuAnimation {

	public static Animation createMainButtonAnimation(Context context) {
		return AnimationUtils.loadAnimation(context, R.anim.sat_main_rotate_left);
	}

	public static Animation createMainButtonInverseAnimation(Context context) {
		return AnimationUtils.loadAnimation(context, R.anim.sat_main_rotate_right);
	}

}
