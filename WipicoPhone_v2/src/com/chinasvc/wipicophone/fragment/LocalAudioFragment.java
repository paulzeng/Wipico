package com.chinasvc.wipicophone.fragment;

import java.util.ArrayList;
import java.util.List;

import com.chinasvc.wipico.bean.FileInfo;
import com.chinasvc.wipicophone.AudioPlayerActivity;
import com.chinasvc.wipicophone.MainActivity;
import com.chinasvc.wipicophone.MediaTabActivity;
import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.WipicoApplication;
import com.chinasvc.wipicophone.adapter.AudioPlayerAdapter;
import com.chinasvc.wipicophone.adapter.PopMenuAdapter;
import com.chinasvc.wipicophone.bean.AudioBean;
import com.chinasvc.wipicophone.bean.PopMenu;
import com.chinasvc.wipicophone.bean.PopMenu.PopType;
import com.chinasvc.wipicophone.config.Config;
import com.chinasvc.wipicophone.util.DensityUtil;
import com.chinasvc.wipicophone.util.FileUtil;
import com.chinasvc.wipicophone.util.MultimediaUtil;
import com.chinasvc.wipicophone.widget.SideBar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * */
public class LocalAudioFragment extends BaseFragment implements OnItemClickListener {

	private String TAG = "LocalAudioFragment";
	private boolean isDebug = true;

	private ListView mListView;
	private AudioPlayerAdapter adapter;
	private SideBar indexBar;
	private TextView mDialogText;

	private LinearLayout emptyView;

	private TextView emptyValue;

	private View main_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.activity_local_audio, container, false);
		return layout;
	}

	@Override
	public void onStart() {
		super.onStart();
		initThreadHandler();
		initData();
		initView();

		setView();

		setPopDatas();
	}

	private boolean isCheckMode() {
		return ((MediaTabActivity) (getActivity().getParent())).mainActivity.isShareVisible();
	}

	private void initData() {
		if (FileUtil.isExternalStorageAvailable()) {
			MultimediaUtil multimediaUtil = new MultimediaUtil(getActivity());
			WipicoApplication.listMusics = multimediaUtil.getMusic();
		} else {
			WipicoApplication.listMusics = new ArrayList<AudioBean>();
		}
	}

	private void initView() {
		main_layout = getActivity().findViewById(R.id.main_layout);

		emptyView = (LinearLayout) getActivity().findViewById(R.id.musicEmptyView);
		emptyValue = (TextView) getActivity().findViewById(R.id.musicEmptyValue);

		mListView = (ListView) getActivity().findViewById(R.id.mListView);
		mListView.setOnItemClickListener(this);
		indexBar = (SideBar) getActivity().findViewById(R.id.sideBar);

		mDialogText = (TextView) getActivity().findViewById(R.id.sideBarDialog);
		mDialogText.setVisibility(View.INVISIBLE);

		indexBar.setTextView(mDialogText);
	}

	private void setView() {
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
			adapter = new AudioPlayerAdapter(getActivity(), WipicoApplication.listMusics, false);
			// 为listView设置适配
			mListView.setAdapter(adapter);
			// 设置SideBar的ListView内容实现点击a-z中任意一个进行定位
			indexBar.setListView(mListView, adapter);
		}
	}

	private void reSetView() {
		initData();
		setView();
		adapter.clearListSelects();
	}

	private int audioPosition = 0;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if (isCheckMode()) {
			if (WipicoApplication.listMusics.get(position).isChecked()) {
				WipicoApplication.listMusics.get(position).setChecked(false);
			} else {
				WipicoApplication.listMusics.get(position).setChecked(true);
			}
			adapter.notifyDataSetChanged();
		} else {
			audioPosition = position;
			int[] location = new int[2];
			arg1.getLocationInWindow(location);
			showPop(location[1], arg1.getHeight());
		}
	}

	private View popupView;
	private PopupWindow pop;
	private ListView popListView;
	private View main_pop;

	private List<PopMenu> listPopDatas = new ArrayList<PopMenu>();

	private void setPopDatas() {
		listPopDatas = new ArrayList<PopMenu>();
		listPopDatas.clear();
		listPopDatas.add(new PopMenu(PopType.REMOTE_PLAY, getString(R.string.pop_remote_play)));
		listPopDatas.add(new PopMenu(PopType.LOCAL_PLAY, getString(R.string.pop_local_play)));
		//listPopDatas.add(new PopMenu(PopType.MULTI, getString(R.string.pop_multi)));
		listPopDatas.add(new PopMenu(PopType.SHARE, getString(R.string.pop_share)));
	}

	private void showPop(int y, int height) {
		LayoutInflater mLayoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = mLayoutInflater.inflate(R.layout.pop_list_menu, null);
		popListView = (ListView) popupView.findViewById(R.id.mListView);
		main_pop = popupView.findViewById(R.id.main_pop);
		popListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				pop.dismiss();
				WipicoApplication.audioPosition = audioPosition;
				AudioBean bean = WipicoApplication.listMusics.get(WipicoApplication.audioPosition);
				if (listPopDatas.get(position).getType() == PopType.LOCAL_PLAY) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), AudioPlayerActivity.class);
					intent.putExtra("mode", Config.MODE_LOCAL);
					startActivity(intent);
				} else if (listPopDatas.get(position).getType() == PopType.REMOTE_PLAY) {
					if (MainActivity.mDevice != null) {
						mThreadHandler.obtainMessage(AUDIO_SEND, bean.getPath()).sendToTarget();
						Intent intent = new Intent();
						intent.setClass(getActivity(), AudioPlayerActivity.class);
						intent.putExtra("mode", Config.MODE_REMOTE);
						startActivity(intent);
					} else {
						Toast.makeText(getActivity(), R.string.msg_no_device, Toast.LENGTH_SHORT).show();
					}
				} else if (listPopDatas.get(position).getType() == PopType.SHARE) {
					if (((MediaTabActivity) (getActivity().getParent())).mainActivity.users.size() > 0 || MainActivity.mDevice != null) {
						List<FileInfo> listShares = new ArrayList<FileInfo>();
						FileInfo file = new FileInfo();
						file.setPath(bean.getPath());
						file.setSize(bean.getSize());
						file.setName(bean.getName());
						listShares.add(file);
						((MediaTabActivity) (getActivity().getParent())).mainActivity.showShareView(listShares, 0);
					} else {
						Toast.makeText(getActivity(), R.string.msg_no_share, Toast.LENGTH_SHORT).show();
					}
				} else if (listPopDatas.get(position).getType() == PopType.MULTI) {
					// 多选
					bean.setChecked(true);
					adapter.notifyDataSetChanged();
					List<FileInfo> listShares = new ArrayList<FileInfo>();
					FileInfo file = new FileInfo();
					file.setPath(bean.getPath());
					file.setSize(bean.getSize());
					file.setName(bean.getName());
					listShares.add(file);
					((MediaTabActivity) (getActivity().getParent())).mainActivity.showShareView(listShares, 0);
				}
			}
		});
		popListView.setAdapter(new PopMenuAdapter(getActivity(), listPopDatas));
		pop = new PopupWindow(popupView, getResources().getDimensionPixelSize(R.dimen.gridview_column_3_width), LayoutParams.WRAP_CONTENT);
		pop.setFocusable(true);
		pop.update();
		pop.setOutsideTouchable(true);
		pop.setAnimationStyle(R.style.PopupAnimation);
		pop.setBackgroundDrawable(new BitmapDrawable());

		pop.showAtLocation(main_layout, Gravity.TOP, 0, correctPopPosition(y, height));
	}

	private int correctPopPosition(int y, int height) {
		int offsetY;
		int popHeight = listPopDatas.size() * getResources().getDimensionPixelSize(R.dimen.pop_item_height) + DensityUtil.dip2px(getActivity(), 9);
		if ((y + height / 2 + popHeight) > (DensityUtil.getScreenHeight(getActivity()) - getResources().getDimensionPixelSize(R.dimen.footbar_height))) {
			main_pop.setBackgroundResource(R.drawable.pop_bg_list_up);
			offsetY = (y + height / 2) - popHeight;
		} else {
			main_pop.setBackgroundResource(R.drawable.pop_bg_list_down);
			offsetY = y + height / 2;
		}
		return offsetY;
	}

}
