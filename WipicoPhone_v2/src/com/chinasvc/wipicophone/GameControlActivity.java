package com.chinasvc.wipicophone;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.chinasvc.wipico.client.ControlHelper;
import com.chinasvc.wipico.type.Control;
import com.chinasvc.wipicophone.widget.DirectionView;

public class GameControlActivity extends ControlBaseActivity implements OnTouchListener {

	/** 触摸板 */
	private DirectionView touchArea;

	/** 控制操作操作Handler */
	private Handler mBackHandler = null;

	private int sendid = 0;
	private int resultX = 0;
	private int resultY = 0;

	private static final int DEVICE_WIDTH = 860;
	private static final int DEVICE_HEIGHT = 490;

	private SensorManager mSensorManager = null;
	private Sensor mSensor = null;
	private int x, y, z;

	private ToggleButton toggleButton;

	private Button btnBack;
	private Button btnStop;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_control);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		initWidget();

		// 初始化监听处理Handler
		initTouchHandler();
	}

	private void initWidget() {
		touchArea = (DirectionView) findViewById(R.id.office_touch_area);
		touchArea.setOnTouchListener(this);

		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnStop = (Button) findViewById(R.id.btn_stop);
		btnStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mBackHandler.obtainMessage(HOME).sendToTarget();
				finish();
			}
		});

		toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					mSensorManager.registerListener(lsn, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
				} else {
					mSensorManager.unregisterListener(lsn);
				}
			}
		});

		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	private SensorEventListener lsn = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			x = (int) (event.values[SensorManager.DATA_X] * 1000);
			y = (int) (event.values[SensorManager.DATA_Y] * 1000);
			z = (int) (event.values[SensorManager.DATA_Z] * 1000);
			mBackHandler.obtainMessage(GRAVITY).sendToTarget();
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}
	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float fX = event.getX();
		float fY = event.getY();

		int pointers = event.getPointerCount();
		Message mMessage = new Message();
		mMessage.what = TOUCH;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchArea.onViewTouch(fX, fY);
			mMessage.arg1 = Control.TOUCH_DOWN;
			sendid = 0;
			break;
		case MotionEvent.ACTION_MOVE:
			touchArea.onViewTouch(fX, fY);
			mMessage.arg1 = Control.TOUCH_MOVE;
			break;
		case MotionEvent.ACTION_UP:
			touchArea.onViewTouch(-100, -100);
			mMessage.arg1 = Control.TOUCH_UP;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mMessage.arg1 = Control.TOUCH_POINTUP;
			break;
		default:
			break;
		}
		sendid++;
		Bundle mBundle = new Bundle();
		mMessage.arg2 = pointers + (sendid << 8);
		if (getAndroidSDKVersion() > 8) {
			PointerCoords pointerCoords = new PointerCoords();
			for (int i = 0; i < pointers; i++) {
				event.getPointerCoords(i, pointerCoords);
				resultX = (int) (((pointerCoords.x) / touchArea.getWidth()) * DEVICE_WIDTH);
				resultY = (int) (((pointerCoords.y - touchArea.getTop()) / touchArea.getHeight()) * DEVICE_HEIGHT);
				mBundle.putInt("x" + i, resultX);
				mBundle.putInt("y" + i, resultY);
				mBundle.putInt("id" + i, (int) (event.getPointerId(i)));
				mBundle.putInt("thm" + i, (int) (pointerCoords.touchMajor) / 10);
			}
		} else {
			resultX = (int) (((event.getX()) / touchArea.getWidth()) * DEVICE_WIDTH);
			resultY = (int) (((event.getY() - touchArea.getTop()) / touchArea.getHeight()) * DEVICE_HEIGHT);
			mBundle.putInt("x0", resultX);
			mBundle.putInt("y0", resultY);
			mBundle.putInt("id0", 0);
			mBundle.putInt("thm0", 1);
		}
		mMessage.setData(mBundle);
		mBackHandler.sendMessage(mMessage);
		return true;
	}

	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;
	}

	private final int HOME = 1;
	private final int TOUCH = 2;
	private final int GRAVITY = 3;
	private final int BACK = 4;

	/**
	 * 初始化遥控器控制发送的Handler
	 * */
	private void initTouchHandler() {
		HandlerThread handlerThread = new HandlerThread("HandlerThread");
		handlerThread.start();
		mBackHandler = new Handler(handlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				if (MainActivity.mDevice != null) {
					KeyEvent keyEvent;
					switch (msg.what) {
					case HOME:
						keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HOME);
						ControlHelper.sendControl(keyEvent, MainActivity.mDevice);
						break;
					case BACK:
						keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
						ControlHelper.sendControl(keyEvent, MainActivity.mDevice);
						break;
					case TOUCH:
						int[][] numdata;
						numdata = new int[12][8];
						Bundle mBundle = msg.getData();
						for (int i = 0; i < (msg.arg2 & 0xff); i++) {
							numdata[i][0] = mBundle.getInt("x" + i);
							numdata[i][1] = mBundle.getInt("y" + i);
							numdata[i][2] = mBundle.getInt("id" + i);
							numdata[i][3] = mBundle.getInt("thm" + i);
						}
						ControlHelper.sendTouch(msg.arg1, msg.arg2, numdata, MainActivity.mDevice);
						break;
					case GRAVITY:
						ControlHelper.sendGriavity(x, -y, z, MainActivity.mDevice);
						break;
					}
				}
			}
		};
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mSensorManager.unregisterListener(lsn);
		super.onDestroy();
	}

}