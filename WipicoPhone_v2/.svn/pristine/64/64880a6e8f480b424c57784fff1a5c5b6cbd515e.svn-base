package com.chinasvc.wipicophone;

import com.chinasvc.wipicophone.config.BroadcastConfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class ImageTabActivity extends BaseTabActivity implements OnClickListener {

	private TabHost mTabHost;

	private Intent mCameraIntent;
	private Intent mGalleryIntent;

	private Button ab_left, ab_right;

	public MainActivity mainActivity;

	private ImageView actionbar_scan, actionbar_state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_tab);

		registerReceiver();

		mainActivity = ((MainActivity) getParent());

		initValue();
		addTabs();

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				mainActivity.hideShareView();
			}
		});
	}

	private void initValue() {
		ab_left = (Button) findViewById(R.id.ab_left);
		ab_right = (Button) findViewById(R.id.ab_right);
		actionbar_scan = (ImageView) findViewById(R.id.actionbar_scan);
		actionbar_scan.setOnClickListener(this);
		actionbar_state = (ImageView) findViewById(R.id.actionbar_state);

		ab_left.setOnClickListener(this);
		ab_right.setOnClickListener(this);

		this.mGalleryIntent = new Intent(this, LocalImageActivity.class);
		this.mCameraIntent = new Intent(this, RemoteImageActivity.class);
	}

	@SuppressWarnings("deprecation")
	private void addTabs() {
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;
		localTabHost.addTab(buildTabSpec(tab_camera, tab_camera, R.drawable.ic_launcher, mCameraIntent));
		localTabHost.addTab(buildTabSpec(tab_gallery, tab_camera, R.drawable.ic_launcher, mGalleryIntent));
	}

	private TabHost.TabSpec buildTabSpec(String tag, String resLabel, int resIcon, final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(resLabel, getResources().getDrawable(resIcon)).setContent(content);
	}

	private String tab_camera = "camera";
	private String tab_gallery = "gallery";

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.ab_left:
			this.mTabHost.setCurrentTabByTag(tab_gallery);
			ab_left.setSelected(true);
			ab_right.setSelected(false);
			break;
		case R.id.ab_right:
			if (MainActivity.mDevice != null) {
				if (MainActivity.mWipicoFileClient != null) {
					this.mTabHost.setCurrentTabByTag(tab_camera);
					ab_left.setSelected(false);
					ab_right.setSelected(true);
				} else {
					Toast.makeText(this, getString(R.string.msg_no_ftp), Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, R.string.msg_no_device, Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.actionbar_scan:
			Intent intent = new Intent();
			intent.setAction(BroadcastConfig.BROADCAST_DEVICE_CONNECTING);
			sendBroadcast(intent);
			break;
		}
	}

	/** 注册广播 */
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadcastConfig.ACTION_DEVICE_CONNECTED);
		filter.addAction(BroadcastConfig.ACTION_DEVICE_DISCONNECTED);
		registerReceiver(broadcastReceiver, filter);
	}

	/** 注销广播 */
	private void unregisterReceiver() {
		unregisterReceiver(broadcastReceiver);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == BroadcastConfig.ACTION_DEVICE_CONNECTED) {
				actionbar_state.setImageResource(R.drawable.ic_ab_scan_connected);
			} else if (intent.getAction() == BroadcastConfig.ACTION_DEVICE_DISCONNECTED) {
				actionbar_state.setImageResource(R.drawable.ic_ab_scan_disconnected);
			}
		}
	};

	@Override
	protected void onResume() {
		mTabHost.setCurrentTabByTag(tab_gallery);
		ab_left.setSelected(true);
		ab_right.setSelected(false);

		if (MainActivity.mDevice != null) {
			actionbar_state.setImageResource(R.drawable.ic_ab_scan_connected);
		} else {
			actionbar_state.setImageResource(R.drawable.ic_ab_scan_disconnected);
		}
		super.onResume();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		unregisterReceiver();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed();
	}

}
