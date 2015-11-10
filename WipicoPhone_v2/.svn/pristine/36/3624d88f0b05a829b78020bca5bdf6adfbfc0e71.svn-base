package com.chinasvc.wipicophone.dialog;

import com.chinasvc.wipicophone.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class VerificationDialog extends Dialog implements OnClickListener {

	private Button btnConfirm, btnCancel, btnAgain;
	private TextView msgTitle;

	public EditText verificationEt;
	private Context mContext;

	public VerificationDialog(Context context, String verification) {
		super(context, R.style.edit_dialog);
		mContext = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true);
		setContentView(R.layout.dialog_verification);
		msgTitle = (TextView) findViewById(R.id.dialogTitle);

		btnAgain = (Button) findViewById(R.id.btnAgain);
		btnAgain.setOnClickListener(this);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);

		verificationEt = (EditText) findViewById(R.id.verification);
	}

	public void setVerificationListener(VerificationListener listener) {
		verificationListener = listener;
	}

	public void setError() {
		verificationEt.setError(mContext.getText(R.string.dialog_msg_verification_error));
	}

	private VerificationListener verificationListener;

	public interface VerificationListener {
		void onConfirmClickListener(String value);

		void onAgainClickListener();

		void onCancelClickListener();
	}

	public void setCustomTitle(String title) {
		msgTitle.setText(title);
	}

	public void setCustomTitle(int title) {
		msgTitle.setText(title);
	}

	@Override
	public void onClick(View v) {
		if (v == btnConfirm) {
			if (verificationListener != null) {
				verificationListener.onConfirmClickListener(verificationEt.getText().toString());
			}
		} else if (v == btnAgain) {
			if (verificationListener != null) {
				verificationListener.onAgainClickListener();
			}
		} else if (v == btnCancel) {
			if (verificationListener != null) {
				verificationListener.onCancelClickListener();
			}
			dismiss();
		}
	}
}