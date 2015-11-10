package com.chinasvc.wipicophone.dialog;

import com.chinasvc.wipicophone.R;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PasswordDialog extends Dialog implements OnClickListener {

	private Button btnConfirm, btnCancel;
	private TextView title;
	private EditText editText;

	private ScanResult mScanResult;

	public PasswordDialog(Context context, ScanResult scanResult) {
		super(context, R.style.edit_dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mScanResult = scanResult;
		setCancelable(true);
		setContentView(R.layout.dialog_password);
		title = (TextView) findViewById(R.id.title);
		editText = (EditText) findViewById(R.id.edittext);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
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

	private DialogClickListener exitClickListener;

	public interface DialogClickListener {
		void onConfirmClickListener(String password, ScanResult scanResult);

		void onCancelClickListener();
	}

	@Override
	public void onClick(View v) {
		if (v == btnConfirm) {
			if (exitClickListener != null) {
				exitClickListener.onConfirmClickListener(editText.getText().toString(), mScanResult);
			}
			dismiss();
		} else if (v == btnCancel) {
			if (exitClickListener != null) {
				exitClickListener.onCancelClickListener();
			}
			dismiss();
		}
	}

}
