package com.chinasvc.wipicophone.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * 鼠标左键的Button
 * 
 * @author billy
 * */
public class MouseKey extends Button {

	/** 按键类型监听器 */
	public OnClickModeListener onClickModeListener;

	/** 是否是双击事件 */
	private boolean isDoubleClick = false;
	/** 是否已经发送了长按事件 */
	private boolean isSendLongClick = false;

	/** 上一次点击的时间 */
	private long preClickDown = 0;
	/** 现在点击的时间 */
	private long nowClickDown = 0;

	/** 点击计数器 */
	private int clickCount = 0;

	public static final int MOUSE_LEFT_CLICK = 0;// 左键单击
	public static final int MOUSE_LEFT_LONG_CLICK = 1;// 左键长按
	public static final int MOUSE_LEFT_DOUBLE_CLICK = 2;// 左键双击

	public MouseKey(Context context) {
		super(context);
	}

	public MouseKey(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MouseKey(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private static final int LONG_CLICK_TIME = 500;
	private static final int DOUBLE_CLICK_TIME = 300;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:// 0
			if (clickCount < 2) {
				clickCount++;
			}
			// 设定Now -> Down的时间
			if (preClickDown == 0) {
				preClickDown = event.getDownTime();
				nowClickDown = event.getDownTime();
			} else {
				nowClickDown = event.getDownTime();
			}
			break;
		case MotionEvent.ACTION_MOVE:// 2
			if ((event.getEventTime() - nowClickDown) >= LONG_CLICK_TIME) {
				// 当前按的时间与 Now->Down的时间长于500秒算长按
				if (!isSendLongClick) {
					onClickModeListener.onClickModeListener(MOUSE_LEFT_LONG_CLICK);
					isSendLongClick = true;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (preClickDown == nowClickDown) {
				if (isSendLongClick) {
					// 如果第一次是长按的话，上次已经处理了，不做处理，否则按第一次点击处理办法
					isSendLongClick = false;
				} else {
					firstClick();
				}
			} else if ((event.getEventTime() - preClickDown) < DOUBLE_CLICK_TIME) {
				// 如果Up的时间和上一次Down的时间小于300ms则算双击
				isDoubleClick = true;
				onClickModeListener.onClickModeListener(MOUSE_LEFT_DOUBLE_CLICK);
			} else if ((event.getEventTime() - preClickDown) >= DOUBLE_CLICK_TIME) {
				// 如果Up的时间和上一次Down的时间大于300ms && 不是长按事件则算单击
				singleClickCheck();
			}
			// 重新赋值上一次点击时间
			preClickDown = nowClickDown;
			break;
		default:
			break;
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 用于第一次点击判断是单击还是双击
	 * */
	private void firstClick() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (clickCount == 1) {
					// 延时 300ms,判断如果按钮被点击的次数依然为1，则算单击
					onClickModeListener.onClickModeListener(MOUSE_LEFT_CLICK);
				} else {
				}
			}
		}, DOUBLE_CLICK_TIME);
	}

	/**
	 * 用于非第一次单击事件延时判断
	 * */
	private void singleClickCheck() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (isDoubleClick) {
					isDoubleClick = false;
				} else {
					if (isSendLongClick) {
						isSendLongClick = false;
					} else {
						onClickModeListener.onClickModeListener(MOUSE_LEFT_CLICK);
						isDoubleClick = false;
					}
				}
			}
		}, DOUBLE_CLICK_TIME);
	}

	public void setOnClickModeListener(OnClickModeListener onClickModeListener) {
		this.onClickModeListener = onClickModeListener;
	}

	/**
	 * 按键类型的监听器
	 * */
	public interface OnClickModeListener {

		/**
		 * 点击类型
		 * 
		 * @param action
		 *                点击类别
		 * */
		public void onClickModeListener(int action);
	}

}
