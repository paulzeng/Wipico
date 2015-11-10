package com.chinasvc.wipicophone;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinasvc.wipicophone.fragment.FragmentAdapter;
import com.chinasvc.wipicophone.fragment.LocalAudioFragment;
import com.chinasvc.wipicophone.fragment.LocalVideoFragment;

public class LocalMediaActivity extends BaseFragementActivity implements OnClickListener {

	private String TAG = "LocalMediaActivity";

	private View category_audio, category_video;
	private TextView category_audio_text, category_video_text;

	private ViewPager mViewPager;
	private FragmentAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_media);

		initView();
		initPager();
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		category_audio = findViewById(R.id.category_audio);
		category_audio.setOnClickListener(this);
		category_audio_text = (TextView) findViewById(R.id.category_audio_text);
		category_audio_text.setText(R.string.actionbar_title_audio);
		category_video = findViewById(R.id.category_video);
		category_video_text = (TextView) findViewById(R.id.category_video_text);
		category_video_text.setText(R.string.actionbar_title_video);
		category_video.setOnClickListener(this);

		category_audio_text.setSelected(false);
		category_video_text.setSelected(true);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				((MediaTabActivity) getParent()).mainActivity.hideShareView();
				switch (arg0) {
				case 0:
					category_audio_text.setSelected(false);
					category_video_text.setSelected(true);
					break;
				case 1:
					category_audio_text.setSelected(true);
					category_video_text.setSelected(false);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initPager() {
		mPagerAdapter = new FragmentAdapter(getSupportFragmentManager());

		mPagerAdapter.addFragment(new LocalVideoFragment());
		mPagerAdapter.addFragment(new LocalAudioFragment());

		// Initiate ViewPager
		mViewPager.setPageMarginDrawable(R.drawable.viewpager_margin);
		mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == category_audio) {
			category_audio_text.setSelected(true);
			category_video_text.setSelected(false);
			mViewPager.setCurrentItem(1);
		} else if (v == category_video) {
			category_audio_text.setSelected(false);
			category_video_text.setSelected(true);
			mViewPager.setCurrentItem(0);
		}
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed();
	}

}
