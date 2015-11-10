package com.chinasvc.wipicophone;

import com.chinasvc.wipico.type.Control;
import com.chinasvc.wipico.type.Mouse;
import com.chinasvc.wipicophone.widget.ArrayWheelAdapter;
import com.chinasvc.wipicophone.widget.DirectionView;
import com.chinasvc.wipicophone.widget.MouseKey;
import com.chinasvc.wipicophone.widget.OnWheelScrollListener;
import com.chinasvc.wipicophone.widget.WheelView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.MotionEvent.PointerCoords;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MouseActivity extends ControlBaseActivity implements OnClickListener, OnTouchListener, MouseKey.OnClickModeListener {

	/** 鼠标左键 */
	private MouseKey leftButton = null;
	/** 鼠标右键 */
	private ImageView rightButton = null;
	/** 鼠标控制板区域 */
	private DirectionView mouseArea = null;
	private WheelView mouseMid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initThreadHandler();

		setContentView(R.layout.activity_mouse);
		((TextView) findViewById(R.id.actionbar_title)).setText(R.string.actionbar_title_mouse);

		// 初始化控件
		initWidget();
	}

	protected void initWidget() {
		leftButton = (MouseKey) findViewById(R.id.mouse_left);
		leftButton.setOnClickListener(this);
		leftButton.setOnClickModeListener(this);

		rightButton = (ImageView) findViewById(R.id.mouse_right);
		rightButton.setOnClickListener(this);

		mouseMid = (WheelView) findViewById(R.id.mouse_mid);
		mouseMid.setOnClickListener(this);
		initiWheelView();

		mouseArea = (DirectionView) findViewById(R.id.mouse_area);
		mouseArea.setOnTouchListener(this);
	}

	private void initiWheelView() {
		String[] drawables = { "____", "____", "____", "____", "____", "____", "____", "____", "____", "____", "____", "____", "____", "____", "____", "____", "____", "____", };
		mouseMid.setAdapter(new ArrayWheelAdapter<String>(drawables));
		mouseMid.setVisibleItems(9);
		mouseMid.setCyclic(true);
		mouseMid.setCurrentItem(5);

		mouseMid.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel, float direction) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
			}

			@Override
			public void onScrolling(WheelView wheel, float direction) {
				if (direction > 0) {
					// 上滑动
					mThreadHandler.obtainMessage(MOUSE_WHEEL, Mouse.MOUSE_WHEEL, 1).sendToTarget();
				} else {
					mThreadHandler.obtainMessage(MOUSE_WHEEL, Mouse.MOUSE_WHEEL, -1).sendToTarget();
				}
			}
		});
	}

	@Override
	public void onClickModeListener(int action) {
		// TODO Auto-generated method stub
		switch (action) {
		// 单击左键
		case MouseKey.MOUSE_LEFT_CLICK:
			mThreadHandler.obtainMessage(MOUSE, Mouse.MOUSE_LEFT, 0).sendToTarget();
			((MainActivity) getParent()).mHandler.obtainMessage(MainActivity.SOUND).sendToTarget();
			((MainActivity) getParent()).mHandler.obtainMessage(MainActivity.VIBRATOR).sendToTarget();
			break;
		// 长按左键
		case MouseKey.MOUSE_LEFT_LONG_CLICK:
			mThreadHandler.obtainMessage(MOUSE, Mouse.MOUSE_LEFTLONG, 0).sendToTarget();
			((MainActivity) getParent()).mHandler.obtainMessage(MainActivity.SOUND).sendToTarget();
			((MainActivity) getParent()).mHandler.obtainMessage(MainActivity.VIBRATOR).sendToTarget();
			break;
		// 双击
		case MouseKey.MOUSE_LEFT_DOUBLE_CLICK:
			mThreadHandler.obtainMessage(MOUSE, Mouse.MOUSE_LEFT, 0).sendToTarget();
			((MainActivity) getParent()).mHandler.obtainMessage(MainActivity.SOUND).sendToTarget();
			((MainActivity) getParent()).mHandler.obtainMessage(MainActivity.VIBRATOR).sendToTarget();
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View view) {
		if (view == rightButton) {
			// 点击右键
			mThreadHandler.obtainMessage(MOUSE, Mouse.MOUSE_RIGHT, 0).sendToTarget();
			((MainActivity) getParent()).mHandler.obtainMessage(MainActivity.SOUND).sendToTarget();
			((MainActivity) getParent()).mHandler.obtainMessage(MainActivity.VIBRATOR).sendToTarget();
		} else if (view == mouseMid) {
			mThreadHandler.obtainMessage(MOUSE, Mouse.MOUSE_MIDKEY, 0).sendToTarget();
		}
	}

	public static int mousex = 0;
	public static int mousey = 0;
	public static int clickevent = 0;
	private static long prevdowntime = 0l;
	private static long prevuptime = 0l;
	private static int prevupx = 0;
	private static int prevupy = 0;
	private static int sendid = 0;

	@SuppressLint("NewApi")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float fX = event.getX();
		float fY = event.getY();
		if (v.getId() == R.id.mouse_area) {
			int pointers = event.getPointerCount();
			if (pointers > 1) {
				Message mMessage = new Message();
				mMessage.what = TOUCH;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mouseArea.onViewTouch(fX, fY);
					mMessage.arg1 = Control.TOUCH_DOWN;
					sendid = 0;
					break;
				case MotionEvent.ACTION_MOVE:
					mouseArea.onViewTouch(fX, fY);
					mMessage.arg1 = Control.TOUCH_MOVE;
					break;
				case MotionEvent.ACTION_UP:
					mouseArea.onViewTouch(-100, -100);
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
				PointerCoords pointerCoords = new PointerCoords();
				for (int i = 0; i < pointers; i++) {
					event.getPointerCoords(i, pointerCoords);
					mBundle.putInt("x" + i, (int) (pointerCoords.x));
					mBundle.putInt("y" + i, (int) (pointerCoords.y));
					mBundle.putInt("id" + i, (int) (event.getPointerId(i)));
					mBundle.putInt("thm" + i, (int) (pointerCoords.touchMajor) / 10);
				}
				mMessage.setData(mBundle);
				mThreadHandler.sendMessage(mMessage);
				return true;
			}

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mouseArea.onViewTouch(fX, fY);
				prevdowntime = event.getDownTime();
				if (Math.abs(prevuptime - event.getEventTime()) > 200 || ((Math.abs(prevupx - event.getX()) > 6) && (Math.abs(prevupy - event.getY()) > 6))) {
					clickevent = 0;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				mouseArea.onViewTouch(fX, fY);
				int absx = (int) event.getX() - mousex;
				int absy = (int) event.getY() - mousey;
				mThreadHandler.obtainMessage(MOUSE_MOVE, absx, absy).sendToTarget();
				break;
			case MotionEvent.ACTION_UP:
				mouseArea.onViewTouch(-100, -100);
				if (Math.abs(prevdowntime - event.getDownTime()) < 100) {
					if (clickevent > 0) {
						mThreadHandler.obtainMessage(MOUSE, Mouse.MOUSE_LEFT, 0).sendToTarget();
					}
				}
				clickevent = 0;
				if (Math.abs(prevdowntime - event.getEventTime()) < 100) {
					clickevent++;
				}
				prevuptime = event.getEventTime();
				prevupx = (int) event.getX();
				prevupy = (int) event.getY();
				break;
			default:
				break;
			}
			mousex = (int) event.getX();
			mousey = (int) event.getY();

		}
		return true;
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed();
	}

}