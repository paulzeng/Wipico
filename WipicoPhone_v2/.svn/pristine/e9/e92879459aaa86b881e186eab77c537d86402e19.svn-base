package com.chinasvc.wipicophone.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinasvc.wipico.util.WifiAdmin;
import com.chinasvc.wipicophone.MainActivity;
import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.adapter.WlanAdapter;

public class WlanDialog extends Dialog implements OnClickListener {

	private Button btnConfirm, btnCancel;
	private TextView msgTitle;
	private Context mContext;
	private ListView mListView;

	private WlanAdapter mWlanAdapter;

	private int currentPosition = 0;

	public WlanDialog(Context context, int currentPosition) {
		super(context, R.style.edit_dialog);
		mContext = context;
		this.currentPosition = currentPosition;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true);

		setContentView(R.layout.dialog_connect);
		msgTitle = (TextView) findViewById(R.id.dialogTitle);

		WifiAdmin.getInstance(context).startScan();
		mHandler.sendEmptyMessage(SCAN_WIFI);

		mWlanAdapter = new WlanAdapter(context, listDatas);

		mListView = (ListView) findViewById(R.id.mListView);
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 单选
		mListView.setAdapter(mWlanAdapter);

		mListView.setItemChecked(currentPosition, true);// 初始选中的位置
		mListView.setSelection(currentPosition);// 初始显示的位置

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				WlanDialog.this.currentPosition = arg2;
			}
		});

		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
	}

	private final int SCAN_WIFI = 0;
	private List<ScanResult> listDatas = new ArrayList<ScanResult>();

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case SCAN_WIFI:
				if (WifiAdmin.getInstance(mContext).isWifiEnable()) {
					List<ScanResult> lists = WifiAdmin.getInstance(mContext).getScanResults();
					if (lists != null) {
						listDatas.clear();
						for (ScanResult scanResult : lists) {
							if (MainActivity.mDevice != null && !MainActivity.mDevice.getDeviceName().equalsIgnoreCase(scanResult.SSID)) {
								listDatas.add(scanResult);
							}
						}
					} else {
						// 无网
						listDatas.clear();
					}
				} else {
					// 无网
					listDatas.clear();
				}
				mWlanAdapter.notifyDataSetChanged();
				sendEmptyMessageDelayed(SCAN_WIFI, 3000);
				break;
			}
		}
	};

	public void setConnectDialogListener(ConnectDialogListener listener) {
		connectDialogListener = listener;
	}

	private ConnectDialogListener connectDialogListener;

	public interface ConnectDialogListener {
		void onConfirmClickListener(ScanResult scanResult);

		void onCancelClickListener();
	}

	public void setOnItemClickListener(OnItemClickListener itemClickListener) {
		mListView.setOnItemClickListener(itemClickListener);
	}

	public void setCustomTitle(String title) {
		msgTitle.setText(title);
	}

	public void setCustomTitle(int title) {
		msgTitle.setText(title);
	}

	public int getPosition() {
		return currentPosition;
	}

	@Override
	protected void onStop() {
		mHandler.removeMessages(SCAN_WIFI);
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		if (v == btnConfirm) {
			if (connectDialogListener != null) {
				connectDialogListener.onConfirmClickListener(listDatas.get(currentPosition));
			}
			dismiss();
		} else if (v == btnCancel) {
			dismiss();
			if (connectDialogListener != null) {
				connectDialogListener.onCancelClickListener();
			}
		}
	}
}