package com.chinasvc.wipicophone.dialog;

import com.chinasvc.wipicophone.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmDialog extends Dialog implements OnClickListener {

	private Button btnConfirm, btnCancel;
	private TextView title;
	private TextView message;

	public ConfirmDialog(Context context) {
		super(context, R.style.edit_dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true);
		setContentView(R.layout.dialog_confirm);
		title = (TextView) findViewById(R.id.title);
		message = (TextView) findViewById(R.id.message);
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

	public void setMessage(String msg) {
		message.setText(msg);
	}

	public void setMessage(int msg) {
		message.setText(msg);
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
		} else if (v == btnCancel) {
			if (exitClickListener != null) {
				exitClickListener.onCancelClickListener();
			}
			dismiss();
		}
	}

}
