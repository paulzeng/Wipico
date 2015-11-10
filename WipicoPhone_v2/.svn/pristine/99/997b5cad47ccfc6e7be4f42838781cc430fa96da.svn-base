package com.chinasvc.wipicophone.dialog;

import java.util.List;

import com.chinasvc.wipico.bean.Device;
import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.adapter.PopWindowAdapter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ConnectDialog extends Dialog implements OnClickListener {

	private Button btnConfirm, btnCancel;
	private TextView msgTitle;
	private Context mContext;
	private ListView mListView;

	private int currentPosition = 0;

	public ConnectDialog(Context context, List<Device> listDatas, int currentPosition) {
		super(context, R.style.edit_dialog);
		mContext = context;
		this.currentPosition = currentPosition;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true);
		setContentView(R.layout.dialog_connect);
		msgTitle = (TextView) findViewById(R.id.dialogTitle);

		mListView = (ListView) findViewById(R.id.mListView);
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 单选
		mListView.setAdapter(new PopWindowAdapter(context, listDatas));

		mListView.setItemChecked(currentPosition, true);// 初始选中的位置
		mListView.setSelection(currentPosition);// 初始显示的位置

		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
	}

	public void setConnectDialogListener(ConnectDialogListener listener) {
		connectDialogListener = listener;
	}

	private ConnectDialogListener connectDialogListener;

	public interface ConnectDialogListener {
		void onConfirmClickListener();

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
	public void onClick(View v) {
		if (v == btnConfirm) {
			if (connectDialogListener != null) {
				connectDialogListener.onConfirmClickListener();
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