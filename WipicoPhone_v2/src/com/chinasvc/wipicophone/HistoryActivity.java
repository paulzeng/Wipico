package com.chinasvc.wipicophone;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.chinasvc.wipico.bean.FileInfo;
import com.chinasvc.wipicophone.adapter.HistoryAdapter;
import com.chinasvc.wipicophone.adapter.PopMenuAdapter;
import com.chinasvc.wipicophone.bean.History;
import com.chinasvc.wipicophone.bean.PopMenu;
import com.chinasvc.wipicophone.bean.PopMenu.PopType;
import com.chinasvc.wipicophone.db.HistoryDao;
import com.chinasvc.wipicophone.dialog.ConfirmDialog;
import com.chinasvc.wipicophone.dialog.PropertyDialog;
import com.chinasvc.wipicophone.dialog.ConfirmDialog.DialogClickListener;
import com.chinasvc.wipicophone.util.DensityUtil;
import com.chinasvc.wipicophone.util.FileTools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HistoryActivity extends Activity implements OnClickListener, OnItemClickListener {

	private ListView mListView;
	private HistoryAdapter mHistoryAdapter;

	private List<History> listDatas = new ArrayList<History>();

	private HistoryDao historyDao;

	private MainActivity mainActivity;

	private ImageView btnClear;

	private LinearLayout emptyView;
	private TextView emptyValue;

	private View main_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		((TextView) findViewById(R.id.actionbar_title)).setText(R.string.actionbar_title_history);

		registerReceiver();

		mainActivity = (MainActivity) getParent();

		initData();
		initView();

		setView();
	}

	private void initData() {
		listDatas.clear();
		historyDao = new HistoryDao();
		listDatas.addAll(historyDao.fetcheAll());
	}

	private void updateView() {
		listDatas.clear();
		listDatas.addAll(historyDao.fetcheAll());
		mHistoryAdapter.notifyDataSetChanged();
		setView();
	}

	private void initView() {
		main_layout = findViewById(R.id.main_layout);
		emptyView = (LinearLayout) findViewById(R.id.imageEmptyView);
		emptyValue = (TextView) findViewById(R.id.imageEmptyValue);

		btnClear = (ImageView) findViewById(R.id.actionbar_clear);
		btnClear.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.mListView);
		mHistoryAdapter = new HistoryAdapter(this, listDatas);
		mListView.setOnItemClickListener(this);

		mListView.setAdapter(mHistoryAdapter);
	}

	private void setView() {
		if (listDatas.size() <= 0) {
			emptyView.setVisibility(View.VISIBLE);
			emptyValue.setText(R.string.msg_no_history);
		} else {
			emptyView.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnClear && listDatas.size() > 0) {
			ConfirmDialog confirmDialog = new ConfirmDialog(this);
			confirmDialog.setTitle(R.string.dialog_title_note);
			confirmDialog.setMessage(R.string.dialog_msg_clear_history);
			confirmDialog.setClickListener(new DialogClickListener() {
				@Override
				public void onConfirmClickListener() {
					historyDao.deleteAll();
					initData();
					mHandler.obtainMessage().sendToTarget();
				}

				@Override
				public void onCancelClickListener() {
				}
			});
			confirmDialog.show();
		}
	}

	private int mPositon;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			updateView();
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		mPositon = position;
		int[] location = new int[2];
		setPopDatas();
		arg1.getLocationInWindow(location);
		showPop(location[1], arg1.getHeight());
	}

	private View popupView;
	private PopupWindow pop;
	private ListView popListView;
	private View main_pop;

	private List<PopMenu> listPopDatas = new ArrayList<PopMenu>();

	private void setPopDatas() {
		listPopDatas = new ArrayList<PopMenu>();
		listPopDatas.clear();
		if (listDatas.get(mPositon).getState() == History.STATE_FINISH) {
			listPopDatas.add(new PopMenu(PopType.LOCAL_PLAY, getString(R.string.pop_local_play)));
			listPopDatas.add(new PopMenu(PopType.SHARE, getString(R.string.pop_share)));
			listPopDatas.add(new PopMenu(PopType.DELETE, getString(R.string.pop_delete)));
		} else {
			listPopDatas.add(new PopMenu(PopType.CANCEL, getString(R.string.pop_cancel)));
		}
		listPopDatas.add(new PopMenu(PopType.PROPERTY, getString(R.string.pop_property)));
	}

	private void showPop(int y, int height) {
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = mLayoutInflater.inflate(R.layout.pop_list_menu, null);
		popListView = (ListView) popupView.findViewById(R.id.mListView);
		main_pop = popupView.findViewById(R.id.main_pop);
		popListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				pop.dismiss();
				if (listPopDatas.get(position).getType() == PopType.LOCAL_PLAY) {
					File file = new File(listDatas.get(mPositon).getPath());
					if (file.exists()) {
						FileTools.openFile(listDatas.get(mPositon).getPath(), getApplicationContext());
					} else {
						Toast.makeText(getApplicationContext(), R.string.msg_no_this_file, Toast.LENGTH_SHORT).show();
					}
				} else if (listPopDatas.get(position).getType() == PopType.DELETE) {
					historyDao.delete(listDatas.get(mPositon));
					listDatas.remove(mPositon);
					mHistoryAdapter.notifyDataSetChanged();
				} else if (listPopDatas.get(position).getType() == PopType.CANCEL) {
					// 取消文件传输
					if (listDatas.get(mPositon).getUserType() == History.USER_TYPE_PHONE && listDatas.get(mPositon).getTransfer() == History.TRANSFER_RECEIVE) {
						mainActivity.cancelReceiveShareTask(listDatas.get(mPositon).getId());
					} else {
						mainActivity.cancelShareTask(listDatas.get(mPositon).getId());
					}
					historyDao.delete(listDatas.get(mPositon));
					listDatas.remove(mPositon);
					mHistoryAdapter.notifyDataSetChanged();
				} else if (listPopDatas.get(position).getType() == PopType.SHARE) {
					File file = new File(listDatas.get(mPositon).getPath());
					if (file.exists()) {
						if (((MainActivity) getParent()).users.size() > 0 || MainActivity.mDevice != null) {
							List<FileInfo> listShares = new ArrayList<FileInfo>();
							FileInfo bean = new FileInfo();
							bean.setPath(listDatas.get(mPositon).getPath());
							bean.setSize(listDatas.get(mPositon).getSize());
							bean.setName(listDatas.get(mPositon).getName());
							listShares.add(bean);
							((MainActivity) getParent()).showShareView(listShares, 0);
						} else {
							Toast.makeText(getApplicationContext(), R.string.msg_no_share, Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), R.string.msg_no_this_file, Toast.LENGTH_SHORT).show();
					}
				} else if (listPopDatas.get(position).getType() == PopType.PROPERTY) {
					History bean = listDatas.get(mPositon);
					PropertyDialog dialog = new PropertyDialog(HistoryActivity.this);
					dialog.setName(bean.getName());
					dialog.setPath(bean.getPath());
					dialog.setSize(bean.getSize());
					dialog.setUser(bean.getTransfer(), bean.getUserName());
					dialog.setTime(Long.parseLong(bean.getTime()));
					dialog.show();

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

	/** 注册广播 */
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_UPDATE_HISTORY);
		registerReceiver(broadcastReceiver, filter);
	}

	/** 注销广播 */
	private void unregisterReceiver() {
		unregisterReceiver(broadcastReceiver);
	}

	public static final String ACTION_UPDATE_HISTORY = "com.chinasvc.wipico.ACTION_UPDATE_HISTORY";

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == ACTION_UPDATE_HISTORY) {
				mHandler.obtainMessage().sendToTarget();
			}
		}
	};

	@Override
	protected void onResume() {
		mHandler.obtainMessage().sendToTarget();
		mainActivity.clearHistoryCount();
		super.onResume();
	}

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
