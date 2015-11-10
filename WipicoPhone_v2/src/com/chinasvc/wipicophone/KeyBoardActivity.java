package com.chinasvc.wipicophone;

import com.chinasvc.wipicophone.util.PreferenceUtil;

import android.app.Service;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class KeyBoardActivity extends ControlBaseActivity implements OnClickListener {

	private Button key_qus, key_2;

	private ImageButton key_shift;

	private ImageButton btn_back;
	private Button btn_submit, btn_clear;

	private EditText edittext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keyboard);

		initThreadHandler();
		initView();
		initSV();
	}

	private Vibrator mVibrator;
	private SoundPool sp;// 声明一个SoundPool
	private int music;// 定义一个整型用load（）；来设置suondID

	private void initSV() {
		mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);// 获取震动器
		sp = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);// 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
		music = sp.load(this, R.raw.click, 1); // 把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
	}

	public static final int SOUND = 0x100;
	public static final int VIBRATOR = 0x101;
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case SOUND:
				// 按键音启动
				if (PreferenceUtil.getInstance(getApplicationContext()).getSound() == 1) {
					sp.play(music, 0.3f, 0.3f, 0, 0, 1);
				}
				break;
			case VIBRATOR:
				// 按键振动启动
				if (PreferenceUtil.getInstance(getApplicationContext()).getVibration() == 1) {
					mVibrator.vibrate(50);
				}
				break;
			}
		}
	};

	private void initView() {
		key_qus = (Button) findViewById(R.id.key_qus);
		key_qus.setText("?\n/");
		key_2 = (Button) findViewById(R.id.key_2);
		key_2.setText("@\n2");

		key_shift = (ImageButton) findViewById(R.id.key_shift);
		key_shift.setOnClickListener(this);

		btn_back = (ImageButton) findViewById(R.id.btnBack);
		btn_back.setOnClickListener(this);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_clear.setOnClickListener(this);
		edittext = (EditText) findViewById(R.id.edittext);
	}

	public void onKeyClick(View view) {
		mThreadHandler.obtainMessage(KEY_VALUE, Integer.valueOf((String) view.getTag()), 0).sendToTarget();
		mHandler.obtainMessage(MainActivity.SOUND).sendToTarget();
		mHandler.obtainMessage(MainActivity.VIBRATOR).sendToTarget();
	}

	public void onKeyClick2(View view) {
		if (key_shift.isSelected()) {
			mThreadHandler.obtainMessage(KEY_VALUE, Integer.valueOf((String) view.getTag()) + 200, 0).sendToTarget();
		} else {
			mThreadHandler.obtainMessage(KEY_VALUE, Integer.valueOf((String) view.getTag()), 0).sendToTarget();
		}
		mHandler.obtainMessage(MainActivity.SOUND).sendToTarget();
		mHandler.obtainMessage(MainActivity.VIBRATOR).sendToTarget();
	}

	@Override
	public void onClick(View v) {
		if (v == key_shift) {
			if (key_shift.isSelected()) {
				key_shift.setSelected(false);
			} else {
				key_shift.setSelected(true);
			}
		} else if (v == btn_back) {
			finish();
		} else if (v == btn_submit) {
			if (!(edittext.getText().toString()).equals("")) {
				mThreadHandler.obtainMessage(KEYBOARD_WRITE, edittext.getText().toString()).sendToTarget();
				mHandler.obtainMessage(MainActivity.SOUND).sendToTarget();
				mHandler.obtainMessage(MainActivity.VIBRATOR).sendToTarget();
			}
		} else if (v == btn_clear) {
			mThreadHandler.obtainMessage(KEYBOARD_DELETE, edittext.getText().toString()).sendToTarget();
			edittext.setText("");
			mHandler.obtainMessage(MainActivity.SOUND).sendToTarget();
			mHandler.obtainMessage(MainActivity.VIBRATOR).sendToTarget();
		}
	}

}
