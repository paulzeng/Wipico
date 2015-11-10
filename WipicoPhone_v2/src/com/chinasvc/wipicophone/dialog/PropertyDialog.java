package com.chinasvc.wipicophone.dialog;

import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.bean.History;
import com.chinasvc.wipicophone.util.DateUtil;
import com.chinasvc.wipicophone.util.FileTools;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class PropertyDialog extends Dialog implements OnClickListener {

	private Button btnConfirm;
	private TextView title;

	private TextView fileName, fileSize, filePath, fileUserType, fileUserName, fileTime;

	public PropertyDialog(Context context) {
		super(context, R.style.edit_dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true);
		setContentView(R.layout.dialog_property);

		title = (TextView) findViewById(R.id.title);
		fileName = (TextView) findViewById(R.id.file_name);
		fileSize = (TextView) findViewById(R.id.file_size);
		filePath = (TextView) findViewById(R.id.file_path);
		fileUserName = (TextView) findViewById(R.id.user_name);
		fileUserType = (TextView) findViewById(R.id.user_type);
		fileTime = (TextView) findViewById(R.id.file_time);

		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(this);
	}

	public void setClickListener(DialogClickListener clickListener) {
		exitClickListener = clickListener;
	}

	public void setCustomTitle(String value) {
		title.setText(value);
	}

	public void setCustomTitle(int value) {
		title.setText(value);
	}

	public void setUser(int type, String name) {
		if (History.TRANSFER_RECEIVE == type) {
			fileUserType.setText(R.string.dialog_msg_receive);
		} else {
			fileUserType.setText(R.string.dialog_msg_send);
		}
		fileUserName.setText(name);
	}

	public void setTime(long time) {
		fileTime.setText(DateUtil.getTime(time));
	}

	public void setName(String name) {
		fileName.setText(name);
	}

	public void setSize(long size) {
		fileSize.setText(FileTools.formetFileSize(size));
	}

	public void setPath(String path) {
		filePath.setText(path);
	}

	private DialogClickListener exitClickListener;

	public interface DialogClickListener {
		void onConfirmClickListener();

		void onCancelClickListener();
	}

	@Override
	public void onClick(View v) {
		if (v == btnConfirm) {
			if (exitClickListener != null) {
				exitClickListener.onConfirmClickListener();
			}
			dismiss();
		}
	}

}
