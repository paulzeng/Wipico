package com.chinasvc.wipicophone;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ControlActivity extends ControlBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		((TextView) findViewById(R.id.actionbar_title)).setText(R.string.actionbar_title_control);

		initThreadHandler();
	}

	public void onKeyClick(View view) {
		mThreadHandler.obtainMessage(KEY_VALUE, Integer.valueOf((String) view.getTag()), 0).sendToTarget();
		((MainActivity) getParent()).mHandler.obtainMessage(MainActivity.SOUND).sendToTarget();
		((MainActivity) getParent()).mHandler.obtainMessage(MainActivity.VIBRATOR).sendToTarget();
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed();
	}

}
