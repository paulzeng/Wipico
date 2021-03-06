package com.chinasvc.wipicophone;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinasvc.wipicophone.fragment.FragmentAdapter;
import com.chinasvc.wipicophone.fragment.LocalCameraFragment;
import com.chinasvc.wipicophone.fragment.LocalGalleryFragment;

public class LocalImageActivity extends FragmentActivity implements OnClickListener {

	private String TAG = "LocalMediaActivity";

	private View category_gallery, category_camera;
	private TextView category_gallery_text, category_camera_text;

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

		category_gallery = findViewById(R.id.category_video);
		category_gallery.setOnClickListener(this);
		category_gallery_text = (TextView) findViewById(R.id.category_video_text);
		category_gallery_text.setText(R.string.actionbar_title_camera);

		category_camera = findViewById(R.id.category_audio);
		category_camera_text = (TextView) findViewById(R.id.category_audio_text);
		category_camera_text.setText(R.string.actionbar_title_gallery);
		category_camera.setOnClickListener(this);

		category_gallery_text.setSelected(true);
		category_camera_text.setSelected(false);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				((ImageTabActivity) getParent()).mainActivity.hideShareView();
				switch (arg0) {
				case 0:
					category_gallery_text.setSelected(true);
					category_camera_text.setSelected(false);
					break;
				case 1:
					category_gallery_text.setSelected(false);
					category_camera_text.setSelected(true);
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
		mPagerAdapter.addFragment(new LocalCameraFragment());
		mPagerAdapter.addFragment(new LocalGalleryFragment());

		// Initiate ViewPager
		mViewPager.setPageMarginDrawable(R.drawable.viewpager_margin);
		mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == category_gallery) {
			category_gallery_text.setSelected(true);
			category_camera_text.setSelected(false);
			mViewPager.setCurrentItem(0);
		} else if (v == category_camera) {
			category_gallery_text.setSelected(false);
			category_camera_text.setSelected(true);
			mViewPager.setCurrentItem(1);
		}
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed();
	}

}
