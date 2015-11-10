package com.chinasvc.wipicophone.fragment;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentAdapter extends FragmentStatePagerAdapter {

	private final ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

	public FragmentAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	public void addFragment(Fragment fragment) {
		mFragments.add(fragment);
		notifyDataSetChanged();
	}

	public int getCount() {
		return mFragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

}
