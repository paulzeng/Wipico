package com.chinasvc.wipicophone.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.chinasvc.wipico.bean.FileInfo;
import com.chinasvc.wipicophone.ImagePlayerActivity;
import com.chinasvc.wipicophone.ImageTabActivity;
import com.chinasvc.wipicophone.MainActivity;
import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.WipicoApplication;
import com.chinasvc.wipicophone.adapter.ImageAdapter;
import com.chinasvc.wipicophone.adapter.PopMenuAdapter;
import com.chinasvc.wipicophone.bean.PopMenu;
import com.chinasvc.wipicophone.bean.PopMenu.PopType;
import com.chinasvc.wipicophone.config.Config;
import com.chinasvc.wipicophone.image.FileHashKey;
import com.chinasvc.wipicophone.image.ImageDecoder;
import com.chinasvc.wipicophone.image.WipicoImage;
import com.chinasvc.wipicophone.util.DensityUtil;
import com.chinasvc.wipicophone.util.FileUtil;
import com.chinasvc.wipicophone.util.FileUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * */
public class LocalCameraFragment extends BaseFragment implements OnItemClickListener {

	private String TAG = "LocalCameraFragment";
	private boolean isDebug = true;

	private GridView mGridView;

	private LinearLayout emptyView;
	private TextView emptyValue;

	private List<WipicoImage> listDatas;
	private FileHashKey fileHashKey;

	private ImageAdapter adapter;

	private View layout;

	private View main_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.activity_image, container, false);
		return layout;
	}

	@Override
	public void onStart() {
		super.onStart();

		initThreadHandler();

		fileHashKey = new FileHashKey(Environment.getExternalStorageDirectory(), "/Wipico/image/", ".jpg");

		initView();
		setPopDatas();
	}

	private void initView() {
		main_layout = layout.findViewById(R.id.main_layout);

		emptyView = (LinearLayout) layout.findViewById(R.id.imageEmptyView);
		emptyValue = (TextView) layout.findViewById(R.id.imageEmptyValue);
		listDatas = new ArrayList<WipicoImage>();

		mGridView = (GridView) layout.findViewById(R.id.mGridView);
		mGridView.setOnItemClickListener(this);
		adapter = new ImageAdapter(getActivity(), listDatas);
		mGridView.setAdapter(adapter);

		if (WipicoApplication.listCameras.size() == 0) {
			new ScanImageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			listDatas.addAll(WipicoApplication.listCameras);
			adapter.notifyDataSetChanged();
			setView();
		}
	}

	private void setView() {
		adapter.notifyDataSetChanged();
		if (listDatas.size() <= 0) {
			emptyView.setVisibility(View.VISIBLE);
			if (!FileUtil.isExternalStorageAvailable()) {
				emptyValue.setText(R.string.msg_no_sdcard);
			} else {
				emptyValue.setText(R.string.msg_no_image);
			}
		} else {
			emptyView.setVisibility(View.GONE);
		}
	}

	private int mPosition;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		mPosition = position;

		int[] location = new int[2];
		arg1.getLocationInWindow(location);
		showPop(location[0], location[1], arg1.getHeight());
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
		// listPopDatas.add(new PopMenu(PopType.MULTI, "多选"));
		listPopDatas.add(new PopMenu(PopType.SHARE, getString(R.string.pop_share)));
	}

	private void showPop(int x, int y, int height) {
		LayoutInflater mLayoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = mLayoutInflater.inflate(R.layout.pop_list_menu, null);
		popListView = (ListView) popupView.findViewById(R.id.mListView);
		main_pop = popupView.findViewById(R.id.main_pop);
		popListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				pop.dismiss();
				if (listPopDatas.get(position).getType() == PopType.LOCAL_PLAY) {
					WipicoApplication.cameraPosition = mPosition;
					WipicoApplication.galleryPosition = -1;
					Intent intent = new Intent();
					intent.setClass(getActivity(), ImagePlayerActivity.class);
					intent.putExtra("mode", Config.MODE_LOCAL);
					intent.putExtra("type", Config.TYPE_CAMERA);
					startActivity(intent);
				} else if (listPopDatas.get(position).getType() == PopType.REMOTE_PLAY) {
					if (MainActivity.mDevice != null) {
						WipicoApplication.cameraPosition = mPosition;
						WipicoApplication.galleryPosition = -1;
						if (WipicoApplication.listCameras.size() == 0) {
							WipicoApplication.listCameras.addAll(listDatas);
						}
						WipicoImage image = WipicoApplication.listCameras.get(mPosition);
						mThreadHandler.obtainMessage(IMAGE_OPEN, image.getUrl()).sendToTarget();
						Intent intent = new Intent();
						intent.setClass(getActivity(), ImagePlayerActivity.class);
						intent.putExtra("mode", Config.MODE_REMOTE);
						intent.putExtra("type", Config.TYPE_CAMERA);
						startActivity(intent);
					} else {
						Toast.makeText(getActivity(), R.string.msg_no_device, Toast.LENGTH_SHORT).show();
					}
				} else if (listPopDatas.get(position).getType() == PopType.SHARE) {
					if (((ImageTabActivity) (getActivity().getParent())).mainActivity.users.size() > 0 || MainActivity.mDevice != null) {
						WipicoImage image = WipicoApplication.listCameras.get(mPosition);
						List<FileInfo> listShares = new ArrayList<FileInfo>();
						FileInfo bean = new FileInfo();
						bean.setPath(image.getPath());
						bean.setName(image.getName());
						bean.setSize(image.getSize());
						listShares.add(bean);
						((ImageTabActivity) (getActivity().getParent())).mainActivity.showShareView(listShares, 0);
					} else {
						Toast.makeText(getActivity(), R.string.msg_no_share, Toast.LENGTH_SHORT).show();
					}
				} else if (listPopDatas.get(position).getType() == PopType.MULTI) {

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

		pop.showAtLocation(main_layout, Gravity.NO_GRAVITY, x, correctPopPosition(y, height));
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

	/** 扫描SD卡 */
	private class ScanImageTask extends AsyncTask<Void, Void, Void> {

		private int count = 0;

		List<String> cameraPaths;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			cameraPaths = new ArrayList<String>();
			cameraPaths.add(Environment.getExternalStorageDirectory().getPath() + "/DCIM");
			cameraPaths.add(Environment.getExternalStorageDirectory().getPath() + "/Camera");
			count = 0;
			listDatas.clear();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (!FileUtil.isExternalStorageAvailable()) {
				listDatas.clear();
			} else {
				for (String camera : cameraPaths) {
					eachAllMedias(new File(camera));
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			adapter.notifyDataSetChanged();
			WipicoApplication.listCameras.addAll(listDatas);
			setView();
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			count = 0;
			adapter.notifyDataSetChanged();
		}

		/** 遍历所有文件夹，查找出图片文件 */
		public void eachAllMedias(File f) {
			if (f != null && f.exists() && f.isDirectory()) {
				File[] files = f.listFiles();
				if (files != null) {
					for (File file : f.listFiles()) {
						if (file.isDirectory() && !file.getName().startsWith(".")) {
							eachAllMedias(file);
						} else if (file.exists() && file.canRead() && FileUtils.isPicture(file)) {
							WipicoImage bean = new WipicoImage();
							bean.setPath(file.getPath());
							bean.setName(file.getName());
							bean.setSize((int) file.length());
							if (file.length() > 0.3 * 1024.0 * 1024.0) {
								if (!FileUtils.isFileExist(fileHashKey.getFile(file.getPath()))) {
									if (ImageDecoder.decodeFile(file) != null) {
										FileUtils.saveFile(ImageDecoder.decodeFile(file), fileHashKey.getFile(file.getPath()));
										bean.setUrl(fileHashKey.getFile(file.getPath()).getPath());
									} else {

										bean.setUrl(file.getPath());
									}
								} else {
									bean.setUrl(fileHashKey.getFile(file.getPath()).getPath());
								}
							} else {
								bean.setUrl(file.getPath());
							}
							count++;
							listDatas.add(bean);
							if (count >= 3) {
								publishProgress();
							}
						}
					}
				}
			}
		}
	}

}
