package com.chinasvc.wipicophone;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.adapter.ImagePagerAdapter;
import com.chinasvc.wipicophone.config.Config;
import com.chinasvc.wipicophone.image.WipicoImage;
import com.chinasvc.wipicophone.widget.GestureDetector;
import com.chinasvc.wipicophone.widget.ImageViewTouch;
import com.chinasvc.wipicophone.widget.ScaleGestureDetector;
import com.chinasvc.wipicophone.widget.ViewPager;
import com.chinasvc.wipico.bean.ImageFlag;
import com.chinasvc.wipico.bean.ImageTransform;

public class ImagePlayerActivity extends MediaBaseActivity implements ViewPager.OnPageChangeListener, OnClickListener {

	private ImageView imageLeft, imageRight;
	private ImageView multimediaPlay;
	private ImageView btnStop;
	private ImageView btnBack;

	private TextView name, index;

	private ViewPager mViewPager;
	private ImagePagerAdapter mPagerAdapter;

	private static final int PAGER_MARGIN_DP = 40;

	private boolean mPaused;
	private boolean mOnScale = false;
	private boolean mOnPagerScoll = false;
	private boolean mControlsShow = false;
	private GestureDetector mGestureDetector;
	private ScaleGestureDetector mScaleGestureDetector;

	private boolean isPlay = false;

	private final int HANDLER_ADAPTER = 1;
	private final int HANDLER_PLAY = 2;

	private List<WipicoImage> listTemps;

	public int mode;
	public int type;

	@SuppressLint("HandlerLeak")
	private Handler uiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_ADAPTER:
				mViewPager.setAdapter(mPagerAdapter);
				if (type == Config.TYPE_GALLERY) {
					if (WipicoApplication.listGallerys.size() <= 0) {
						mViewPager.setCurrentItem(0, false);
					} else {
						if (WipicoApplication.galleryPosition < 0) {
							WipicoApplication.galleryPosition = 0;
						}
						mViewPager.setCurrentItem(WipicoApplication.galleryPosition, false);
					}
				} else if (type == Config.TYPE_CAMERA) {
					if (WipicoApplication.listCameras.size() <= 0) {
						mViewPager.setCurrentItem(0, false);
					} else {
						if (WipicoApplication.cameraPosition < 0) {
							WipicoApplication.cameraPosition = 0;
						}
						mViewPager.setCurrentItem(WipicoApplication.cameraPosition, false);
					}
				}
				break;
			case HANDLER_PLAY:
				if (type == Config.TYPE_GALLERY) {
					if (WipicoApplication.listGallerys.size() <= 0) {
						mViewPager.setCurrentItem(0);
					} else {
						if (WipicoApplication.galleryPosition < 0) {
							WipicoApplication.galleryPosition = 0;
						}
						mViewPager.setCurrentItem(WipicoApplication.galleryPosition);
					}
				} else if (type == Config.TYPE_CAMERA) {
					if (WipicoApplication.listCameras.size() <= 0) {
						mViewPager.setCurrentItem(0);
					} else {
						if (WipicoApplication.cameraPosition < 0) {
							WipicoApplication.cameraPosition = 0;
						}
						mViewPager.setCurrentItem(WipicoApplication.cameraPosition);
					}
				}
				setFileName();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_player);

		mode = getIntent().getIntExtra("mode", Config.MODE_LOCAL);
		type = getIntent().getIntExtra("type", Config.TYPE_GALLERY);

		if (type == Config.TYPE_GALLERY) {
			if (WipicoApplication.listGallerys.size() <= 0) {
				listTemps = new ArrayList<WipicoImage>();
				WipicoImage bean = new WipicoImage();
				bean.setName(getString(R.string.actionbar_title_image_control_default));
				bean.setUrl("assets://image_default.png");
				listTemps.add(bean);
			}

		} else if (type == Config.TYPE_CAMERA) {
			if (WipicoApplication.listCameras.size() <= 0) {
				listTemps = new ArrayList<WipicoImage>();
				WipicoImage bean = new WipicoImage();
				bean.setName(getString(R.string.actionbar_title_image_control_default));
				bean.setUrl("assets://image_default.png");
				listTemps.add(bean);
			}
		}

		initThreadHandler();

		initValue();
		initWidget();

		initUiHandler();
		mUiHandler.sendEmptyMessage(SET_ADAPTER);
	}

	private void initValue() {
		isPlay = false;
	}

	private void initWidget() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		setupOnTouchListeners(mViewPager);
		float scale = getResources().getDisplayMetrics().density;
		int pagerMarginPixels = (int) (PAGER_MARGIN_DP * scale + 0.5f);
		mViewPager.setPageMargin(pagerMarginPixels);
		mViewPager.setPageMarginDrawable(new ColorDrawable(Color.TRANSPARENT));
		mViewPager.setOnPageChangeListener(this);

		name = (TextView) findViewById(R.id.name);
		index = (TextView) findViewById(R.id.index);

		imageLeft = (ImageView) findViewById(R.id.turn_left);
		imageLeft.setOnClickListener(this);
		imageRight = (ImageView) findViewById(R.id.turn_right);
		imageRight.setOnClickListener(this);

		multimediaPlay = (ImageView) findViewById(R.id.play);
		multimediaPlay.setOnClickListener(this);
		multimediaPlay.setImageResource(R.drawable.image_player_play);

		btnBack = (ImageView) findViewById(R.id.actionbar_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		btnStop = (ImageView) findViewById(R.id.stop);
		btnStop.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		isPlay = false;
		imageLeft.setEnabled(true);
		imageRight.setEnabled(true);
		multimediaPlay.setImageResource(R.drawable.image_player_play);
		if (type == Config.TYPE_GALLERY) {
			mViewPager.setCurrentItem(WipicoApplication.galleryPosition);
		} else if (type == Config.TYPE_CAMERA) {
			mViewPager.setCurrentItem(WipicoApplication.cameraPosition);
		}
		setFileName();
		super.onResume();
	}

	@Override
	protected void onPause() {
		isPlay = false;
		super.onPause();
	}

	private final int SET_ADAPTER = 0;
	private Handler mUiHandler;

	@SuppressLint("HandlerLeak")
	private void initUiHandler() {
		HandlerThread handlerThread = new HandlerThread("HandlerThread");
		handlerThread.start();
		mUiHandler = new Handler(handlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SET_ADAPTER:
					if (type == Config.TYPE_GALLERY) {
						if (WipicoApplication.listGallerys.size() <= 0) {
							mPagerAdapter = new ImagePagerAdapter(ImagePlayerActivity.this, listTemps);
						} else {
							mPagerAdapter = new ImagePagerAdapter(ImagePlayerActivity.this, WipicoApplication.listGallerys);
						}
					} else if (type == Config.TYPE_CAMERA) {
						if (WipicoApplication.listCameras.size() <= 0) {
							mPagerAdapter = new ImagePagerAdapter(ImagePlayerActivity.this, listTemps);
						} else {
							mPagerAdapter = new ImagePagerAdapter(ImagePlayerActivity.this, WipicoApplication.listCameras);
						}
					}
					uiHandler.sendEmptyMessage(HANDLER_ADAPTER);
					break;
				}
			}
		};
	}

	/**
	 * 设置显示的文件名称
	 * */
	protected void setFileName() {
		if (type == Config.TYPE_GALLERY) {
			if (WipicoApplication.listGallerys.size() <= 0) {
				name.setText(getString(R.string.actionbar_title_image_control_default));
			} else {
				if (WipicoApplication.galleryPosition < 0) {
					WipicoApplication.galleryPosition = 0;
				}
				name.setText(WipicoApplication.listGallerys.get(WipicoApplication.galleryPosition).getName());
				index.setText((WipicoApplication.galleryPosition + 1) + " / " + WipicoApplication.listGallerys.size());
			}
		} else if (type == Config.TYPE_CAMERA) {
			if (WipicoApplication.listCameras.size() <= 0) {
				name.setText(getString(R.string.actionbar_title_image_control_default));
			} else {
				if (WipicoApplication.cameraPosition < 0) {
					WipicoApplication.cameraPosition = 0;
				}
				name.setText(WipicoApplication.listCameras.get(WipicoApplication.cameraPosition).getName());
				index.setText((WipicoApplication.cameraPosition + 1) + " / " + WipicoApplication.listCameras.size());
			}
		}
	}

	@Override
	public void onClick(View view) {
		if (view == multimediaPlay) {
			if (isPlay) {
				isPlay = false;
				imageLeft.setEnabled(true);
				imageRight.setEnabled(true);
				multimediaPlay.setImageResource(R.drawable.image_player_play);
			} else {
				isPlay = true;
				initPlay();
				imageLeft.setEnabled(false);
				imageRight.setEnabled(false);
				multimediaPlay.setImageResource(R.drawable.image_player_pause);
			}
		} else if (view == imageLeft) {
			// 左转
			if (getCurrentImageView() != null) {
				getCurrentImageView().zoomTurnLeft();
				if (mode == Config.MODE_REMOTE) {
					mThreadHandler.obtainMessage(IMAGE_TURN_LEFT).sendToTarget();
				}
			}
		} else if (view == btnStop) {
			if (mode == Config.MODE_REMOTE) {
				mThreadHandler.obtainMessage(IMAGE_STOP).sendToTarget();
			}
			finish();
		} else if (view == imageRight) {
			// 右转
			if (getCurrentImageView() != null) {
				getCurrentImageView().zoomTurnRight();
				if (mode == Config.MODE_REMOTE) {
					mThreadHandler.obtainMessage(IMAGE_TURN_RIGHT).sendToTarget();
				}
			}
		}
	}

	private void initPlay() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isPlay) {
					if (type == Config.TYPE_GALLERY) {
						if (WipicoApplication.listGallerys.size() <= 0) {

						} else {
							if (WipicoApplication.galleryPosition < 0) {
								WipicoApplication.galleryPosition = 0;
							}
							if (WipicoApplication.galleryPosition == (WipicoApplication.listGallerys.size() - 1)) {
								WipicoApplication.galleryPosition = 0;
							} else {
								WipicoApplication.galleryPosition = WipicoApplication.galleryPosition + 1;
							}
							uiHandler.sendEmptyMessage(HANDLER_PLAY);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else if (type == Config.TYPE_CAMERA) {
						if (WipicoApplication.listCameras.size() <= 0) {

						} else {
							if (WipicoApplication.cameraPosition < 0) {
								WipicoApplication.cameraPosition = 0;
							}
							if (WipicoApplication.cameraPosition == (WipicoApplication.listCameras.size() - 1)) {
								WipicoApplication.cameraPosition = 0;
							} else {
								WipicoApplication.cameraPosition = WipicoApplication.cameraPosition + 1;
							}
							uiHandler.sendEmptyMessage(HANDLER_PLAY);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}

				}
			}
		}).start();
	}

	private void setupOnTouchListeners(View rootView) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
			mScaleGestureDetector = new ScaleGestureDetector(this, new MyOnScaleGestureListener());
		}
		mGestureDetector = new GestureDetector(this, new MyGestureListener());
		OnTouchListener rootListener = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// NOTE: gestureDetector may handle onScroll..
				if (!isPlay) {
					if (!mOnScale) {
						if (!mOnPagerScoll) {
							mGestureDetector.onTouchEvent(event);
						}
					}

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
						if (!mOnPagerScoll) {
							mScaleGestureDetector.onTouchEvent(event);
						}
					}

					ImageViewTouch imageView = getCurrentImageView();
					if (imageView == null) {
						return true;
					}

					if (!mOnScale && imageView.mBitmapDisplayed != null && imageView.mBitmapDisplayed.getBitmap() != null) {
						Matrix m = imageView.getImageViewMatrix();
						RectF rect = new RectF(0, 0, imageView.mBitmapDisplayed.getBitmap().getWidth(), imageView.mBitmapDisplayed.getBitmap().getHeight());
						m.mapRect(rect);
						// 图片超出屏幕范围后移动
						if (!(rect.right > imageView.getWidth() + 0.1 && rect.left < -0.1)) {
							try {
								mViewPager.onTouchEvent(event);
							} catch (ArrayIndexOutOfBoundsException e) {
								// why?
							}
						}
					}
				}
				return true;
			}
		};
		rootView.setOnTouchListener(rootListener);
	}

	private ImageViewTouch getCurrentImageView() {
		return (ImageViewTouch) mPagerAdapter.views.get((mViewPager.getCurrentItem()));
	}

	@Override
	public void onPageSelected(int position, int prePosition) {
		ImageViewTouch preImageView = mPagerAdapter.views.get(prePosition);
		if (preImageView != null) {
			preImageView.setImageBitmapResetBase(preImageView.mBitmapDisplayed.getBitmap(), true);
		}

		if (type == Config.TYPE_GALLERY) {
			WipicoApplication.galleryPosition = position;
			setFileName();// 改变标题
			if (WipicoApplication.listGallerys.size() <= 0) {
			} else {
				if (mode == Config.MODE_REMOTE) {
					mThreadHandler.obtainMessage(IMAGE_OPEN, WipicoApplication.listGallerys.get(position).getUrl()).sendToTarget();
				}
			}
		} else if (type == Config.TYPE_CAMERA) {
			WipicoApplication.cameraPosition = position;
			setFileName();// 改变标题
			if (WipicoApplication.listCameras.size() <= 0) {
			} else {
				if (mode == Config.MODE_REMOTE) {
					mThreadHandler.obtainMessage(IMAGE_OPEN, WipicoApplication.listCameras.get(position).getUrl()).sendToTarget();
				}
			}
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		mOnPagerScoll = true;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_DRAGGING) {
			mOnPagerScoll = true;
		} else if (state == ViewPager.SCROLL_STATE_SETTLING) {
			mOnPagerScoll = false;
		} else {
			mOnPagerScoll = false;
		}
	}

	private class MyOnScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

		float currentScale;
		float currentMiddleX;
		float currentMiddleY;

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			if (!isPlay) {
				final ImageViewTouch imageView = getCurrentImageView();
				if (imageView == null) {
					return;
				}
				sendActionCmd(ImageFlag.IMAGE_FLAG_ONSCALEEND, setCMDValue(currentMiddleX, currentMiddleY, currentScale, 0, 0, 0, 0));
				if (currentScale > imageView.mMaxZoom) {
					// 当前比例超过了最大比例
					imageView.zoomToNoCenterWithAni(currentScale / imageView.mMaxZoom, 1, currentMiddleX, currentMiddleY);
					currentScale = imageView.mMaxZoom;
					imageView.zoomToNoCenterValue(currentScale, currentMiddleX, currentMiddleY);
				} else if (currentScale < imageView.mMinZoom) {
					// 当前比例小于最小比例
					imageView.zoomToNoCenterWithAni(currentScale, imageView.mMinZoom, currentMiddleX, currentMiddleY);
					currentScale = imageView.mMinZoom;
					imageView.zoomToNoCenterValue(currentScale, currentMiddleX, currentMiddleY);
				} else {
					imageView.zoomToNoCenter(currentScale, currentMiddleX, currentMiddleY);
				}
				imageView.center(true, true);

				// NOTE: 延迟修正缩放后可能移动问题
				imageView.postDelayed(new Runnable() {
					@Override
					public void run() {
						mOnScale = false;
					}
				}, 300);
			}
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			mOnScale = true;
			return true;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector, float mx, float my) {
			if (!isPlay) {
				ImageViewTouch imageView = getCurrentImageView();
				if (imageView == null) {
					return true;
				}
				float ns = imageView.getScale() * detector.getScaleFactor();

				currentScale = ns;
				currentMiddleX = mx;
				currentMiddleY = my;

				if (detector.isInProgress()) {
					sendActionCmd(ImageFlag.IMAGE_FLAG_ONSCALE, setCMDValue(currentMiddleX, currentMiddleY, currentScale, 0, 0, 0, 0));
					imageView.zoomToNoCenter(ns, mx, my);
				}
			}
			return true;
		}
	}

	private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			if (!isPlay) {
				if (mOnScale) {
					return true;
				}
				if (mPaused) {
					return false;
				}
				ImageViewTouch imageView = getCurrentImageView();
				if (imageView == null) {
					return true;
				}
				sendActionCmd(ImageFlag.IMAGE_FLAG_ONSCROLL, setCMDValue(0, 0, 0, distanceX, distanceY, 0, 0));
				imageView.panBy(-distanceX, -distanceY);
				// 超出边界效果去掉这个
				imageView.center(true, true);
			}
			return true;
		}

		@Override
		public boolean onUp(MotionEvent e) {
			return super.onUp(e);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if (mControlsShow) {
			} else {
			}
			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (!isPlay) {
				if (mPaused) {
					return false;
				}
				ImageViewTouch imageView = getCurrentImageView();
				if (imageView == null) {
					return true;
				}
				// Switch between the original scale and 3x
				// scale.
				sendActionCmd(ImageFlag.IMAGE_FLAG_ONDOUBLETAP, setCMDValue(0, 0, 0, 0, 0, e.getX(), e.getY()));

				if (imageView.mBaseZoom < 1) {
					if (imageView.getScale() > 2F) {

						imageView.zoomTo(1f);
					} else {
						imageView.zoomToPoint(3f, e.getX(), e.getY());
					}
				} else {
					if (imageView.getScale() > (imageView.mMinZoom + imageView.mMaxZoom) / 2f) {
						imageView.zoomTo(imageView.mMinZoom);
					} else {
						imageView.zoomToPoint(imageView.mMaxZoom, e.getX(), e.getY());
					}
				}
			}
			return true;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		mPaused = false;
	}

	@Override
	public void onStop() {
		super.onStop();
		isPlay = false;
		mPaused = false;
	}

	@Override
	public void onDestroy() {
		isPlay = false;
		super.onDestroy();
	}

	/**
	 * middleX,middleY,scale,disX,disY,eventX,eventY
	 * */
	private float[] setCMDValue(float middleX, float middleY, float scale, float disX, float disY, float eventX, float eventY) {
		float[] value = new float[7];
		value[0] = middleX;
		value[1] = middleY;
		value[2] = scale;
		value[3] = disX;
		value[4] = disY;
		value[5] = eventX;
		value[6] = eventY;
		return value;
	}

	/**
	 * 图片转换
	 * 
	 * @param flag
	 *                转换模式
	 * */
	private void sendActionCmd(int flag, float[] value) {
		if (mode == Config.MODE_REMOTE) {
			Message msg = new Message();
			msg.what = IMAGE_TRANSFORM;// 一级指令
			ImageTransform imageTransform = new ImageTransform(flag, value[0], value[1], value[2], value[3], value[4], value[5], value[6]);
			msg.obj = imageTransform;
			mThreadHandler.sendMessage(msg);
		}
	}

}
