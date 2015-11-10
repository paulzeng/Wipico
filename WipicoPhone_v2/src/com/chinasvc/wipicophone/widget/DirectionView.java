package com.chinasvc.wipicophone.widget;

import com.chinasvc.wipicophone.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class DirectionView extends View {

	private Bitmap bitmap_direction;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// update();
			if (msg.what == 0) {
				invalidate();
			}
		}
	};

	public DirectionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		bitmap_direction = BitmapFactory.decodeResource(getResources(), R.drawable.touch_point);
	}

	public DirectionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		bitmap_direction = BitmapFactory.decodeResource(getResources(), R.drawable.touch_point);
	}

	public void onViewTouch(float fX, float fY) {
		this.currentX = fX;
		this.currentY = fY;
		handler.sendEmptyMessage(0);
	}

	public float currentX = -100;
	public float currentY = -100;

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap_direction, currentX, currentY, null);
	}

}
