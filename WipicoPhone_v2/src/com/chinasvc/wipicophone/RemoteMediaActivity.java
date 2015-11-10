package com.chinasvc.wipicophone;

import java.util.ArrayList;
import java.util.List;

import com.chinasvc.wipico.bean.FileInfo;
import com.chinasvc.wipicophone.adapter.FileRemoteAdapter;
import com.chinasvc.wipicophone.adapter.PopMenuAdapter;
import com.chinasvc.wipicophone.bean.PopMenu;
import com.chinasvc.wipicophone.bean.PopMenu.PopType;
import com.chinasvc.wipicophone.dialog.LoadingDialog;
import com.chinasvc.wipicophone.util.DensityUtil;
import com.chinasvc.wipicophone.widget.ptr.PullToRefreshListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RemoteMediaActivity extends ControlBaseActivity implements OnItemClickListener {

	private PullToRefreshListView mListView;
	private FileRemoteAdapter mFileListViewAdapter;

	private HorizontalScrollView mHorizontalScrollView;
	private LinearLayout scrollView_layout;

	private List<FileInfo> listDatas = new ArrayList<FileInfo>();

	private final String FTP_ROOT_FILE = "/";
	public static String DEVICE_SDCARD = "/sdcard";
	public static String DEVICE_TF = "/extsd";
	public static String DEVICE_USB = "/usbhost1";

	private String serverPath = FTP_ROOT_FILE;
	private View main_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote_list);

		initThreadHandler();

		initView();

		serverPath = FTP_ROOT_FILE;

		setPopDatas();
	}

	@Override
	protected void onResume() {
		getServerList(getString(R.string.text_root_path), true);
		super.onResume();
	}

	private void initView() {
		main_layout = findViewById(R.id.main_layout);

		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		scrollView_layout = (LinearLayout) findViewById(R.id.scrollView_layout);

		mListView = (PullToRefreshListView) findViewById(R.id.fileListView);
		mFileListViewAdapter = new FileRemoteAdapter(this, listDatas);
		mListView.setAdapter(mFileListViewAdapter);
		mListView.setOnItemClickListener(this);
	}

	private LoadingDialog loadingDialog;

	private List<FileInfo> listServerDatas = new ArrayList<FileInfo>();

	private void getServerList(String name, boolean isOnResume) {
		if (!isLoading) {
			if (name == null) {
				if (MainActivity.mWipicoFileClient != null) {
					mHandler.obtainMessage(0).sendToTarget();
				} else {
					Toast.makeText(RemoteMediaActivity.this, R.string.msg_no_ftp, Toast.LENGTH_SHORT).show();
				}
			} else {
				if (MainActivity.mWipicoFileClient != null) {
					if (isOnResume) {
						if (serverPath.equals(FTP_ROOT_FILE)) {
							scrollView_layout.removeAllViews();
							addPathView(serverPath, getString(R.string.text_root_path));// 添加根目录
						}
					} else {
						addPathView(serverPath, name);// 添加根目录
					}
					mHandler.obtainMessage(0).sendToTarget();
				} else {
					Toast.makeText(RemoteMediaActivity.this, R.string.msg_no_ftp, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private Handler mHandler = new Handler() {
		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			if (MainActivity.mWipicoFileClient != null) {
				new GetServerDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				new GetServerDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		}
	};

	private boolean isLoading = false;

	private class GetServerDataTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			isLoading = true;
			if (loadingDialog == null) {
				loadingDialog = new LoadingDialog(getParent());
				loadingDialog.show();
			} else {
				loadingDialog.show();
			}
			listServerDatas.clear();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			// 更新FTP服务器文件列表
			if (MainActivity.mWipicoFileClient != null) {
				if (serverPath.equals(FTP_ROOT_FILE)) {
					listServerDatas = MainActivity.mWipicoFileClient.getFileRootList(serverPath, getString(R.string.file_tf_name), getString(R.string.file_sdcard_name),
							getString(R.string.file_usb_name));
				} else {
					listServerDatas = MainActivity.mWipicoFileClient.getAudioOrVideoList(serverPath);
				}
				return true;
			} else {
				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				listDatas.clear();
				listDatas.addAll(listServerDatas);
				mFileListViewAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(getApplicationContext(), R.string.msg_data_error, Toast.LENGTH_SHORT).show();
			}
			loadingDialog.dismiss();
			isLoading = false;
			super.onPostExecute(result);
		}
	}

	/**
	 * 移除路径标题导航视图
	 * 
	 * @param v
	 *                当前点击的导航路径
	 * */
	private void removePathView(View v) {
		scrollView_layout.removeViews((Integer) v.getTag(), scrollView_layout.getChildCount() - (Integer) v.getTag());
	}

	/**
	 * 添加路径标题导航
	 * 
	 * @param path
	 *                路径
	 * @param name
	 *                文件名
	 * */
	private void addPathView(String path, String name) {
		LayoutInflater contentInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = contentInflater.inflate(R.layout.layout_path, null, true);
		TextView tv = (TextView) view.findViewById(R.id.path);
		int index = scrollView_layout.getChildCount() + 1;
		tv.setText(name);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removePathView(v);
				serverPath = v.getContentDescription().toString();
				getServerList(null, false);
			}
		});
		view.setContentDescription(path);
		view.setTag(index);
		scrollView_layout.addView(view);
		int off = scrollView_layout.getMeasuredWidth() - mHorizontalScrollView.getWidth();
		if (off > 0) {
			mHorizontalScrollView.scrollTo(off, 0);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (!listDatas.get(arg2 - 1).isDirectory()) {
			// 点击的是文件
			mPosition = arg2;
			int[] location = new int[2];
			arg1.getLocationInWindow(location);
			showPop(location[1], arg1.getHeight());
		} else if (listDatas.get(arg2 - 1).isDirectory()) {
			serverPath = listDatas.get(arg2 - 1).getPath();
			getServerList(listDatas.get(arg2 - 1).getName(), false);
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
		listPopDatas.add(new PopMenu(PopType.LOCAL_PLAY, getString(R.string.pop_play)));
		// listPopDatas.add(new PopMenu(PopType.REMOTE_PLAY, "推送播放"));
		// listPopDatas.add(new PopMenu(PopType.MULTI, "多选"));
		listPopDatas.add(new PopMenu(PopType.SHARE, getString(R.string.pop_share)));
	}

	private int mPosition;

	private void showPop(int y, int height) {
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = mLayoutInflater.inflate(R.layout.pop_list_menu, null);
		popListView = (ListView) popupView.findViewById(R.id.mListView);
		main_pop = popupView.findViewById(R.id.main_pop);
		popListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				pop.dismiss();
				FileInfo bean = listDatas.get(mPosition - 1);
				if (listPopDatas.get(position).getType() == PopType.LOCAL_PLAY) {
					mThreadHandler.obtainMessage(OPEN_FILE, "/mnt" + bean.getPath()).sendToTarget();
				} else if (listPopDatas.get(position).getType() == PopType.REMOTE_PLAY) {

				} else if (listPopDatas.get(position).getType() == PopType.SHARE) {
					List<FileInfo> listShares = new ArrayList<FileInfo>();
					FileInfo file = new FileInfo();
					file.setPath(bean.getPath());
					file.setName(bean.getName());
					file.setSize(bean.getSize());
					listShares.add(file);
					((MediaTabActivity) getParent()).mainActivity.showShareView(listShares, 1);
				} else if (listPopDatas.get(position).getType() == PopType.MULTI) {

				}
			}
		});
		popListView.setAdapter(new PopMenuAdapter(this, listPopDatas));
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
		int popHeight = listPopDatas.size() * getResources().getDimensionPixelSize(R.dimen.pop_item_height) + DensityUtil.dip2px(this, 9);
		if ((y + height / 2 + popHeight) > (DensityUtil.getScreenHeight(this) - getResources().getDimensionPixelSize(R.dimen.footbar_height))) {
			main_pop.setBackgroundResource(R.drawable.pop_bg_list_up);
			offsetY = (y + height / 2) - popHeight;
		} else {
			main_pop.setBackgroundResource(R.drawable.pop_bg_list_down);
			offsetY = y + height / 2;
		}
		return offsetY;
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed();
	}

}
