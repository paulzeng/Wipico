package com.chinasvc.wipicophone;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONException;
import org.json.JSONObject;

import com.chinasvc.wipico.WipicoClient;
import com.chinasvc.wipico.bean.Device;
import com.chinasvc.wipico.bean.FileInfo;
import com.chinasvc.wipico.client.ActionHelper;
import com.chinasvc.wipico.client.DeviceStatus;
import com.chinasvc.wipico.client.DeviceType;
import com.chinasvc.wipico.client.PulseHelper;
import com.chinasvc.wipico.client.ScanListener;
import com.chinasvc.wipico.file.WipicoFileClient;
import com.chinasvc.wipico.file.WipicoFileClient.FileExceptionListener;
import com.chinasvc.wipico.file.WipicoFileClient.FileTransferListener;
import com.chinasvc.wipico.sync.SyncPlayClient;
import com.chinasvc.wipico.sync.SyncPlayClient.DeviceSyncListener;
import com.chinasvc.wipicophone.adapter.PopMenuAdapter;
import com.chinasvc.wipicophone.async.FTPUploaderThread;
import com.chinasvc.wipicophone.bean.History;
import com.chinasvc.wipicophone.bean.PopMenu;
import com.chinasvc.wipicophone.bean.PopMenu.PopType;
import com.chinasvc.wipicophone.config.BroadcastConfig;
import com.chinasvc.wipicophone.db.HistoryDao;
import com.chinasvc.wipicophone.dialog.ConnectDialog;
import com.chinasvc.wipicophone.dialog.ConnectDialog.ConnectDialogListener;
import com.chinasvc.wipicophone.dialog.VerificationDialog;
import com.chinasvc.wipicophone.dialog.VerificationDialog.VerificationListener;
import com.chinasvc.wipicophone.service.ChatService;
import com.chinasvc.wipicophone.service.ChatService.MyBinder;
import com.chinasvc.wipicophone.service.Config;
import com.chinasvc.wipicophone.service.FileMessage;
import com.chinasvc.wipicophone.service.Listener;
import com.chinasvc.wipicophone.service.OnProgressUpdate;
import com.chinasvc.wipicophone.service.TCPFileListener;
import com.chinasvc.wipicophone.service.User;
import com.chinasvc.wipicophone.util.DensityUtil;
import com.chinasvc.wipicophone.util.MathUtil;
import com.chinasvc.wipicophone.util.PreferenceUtil;
import com.chinasvc.wipicophone.view.MenuAnimation;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends BaseTabActivity implements OnClickListener, OnProgressUpdate {

	private boolean isDebug = false;
	private String TAG = "MainActivity";

	private View nav_media, nav_image, nav_business, nav_application, nav_control;// 导航栏

	protected TabHost mTabHost;
	private Intent mImageIntent, mMediaIntent, mBusinessIntent, mApplicationIntent, mControlIntent;
	private Intent mTouchIntent, mMouseIntent, mOfficeControlIntent, mSettingIntent, mHistoryIntent;
	public static final String TAB_MEDIA = "media";
	public static final String TAB_IMAGE = "image";
	public static final String TAB_BUSINESS = "bussiness";
	public static final String TAB_APPLICATION = "application";
	public static final String TAB_CONTROL = "control";
	public static final String TAB_TOUCH = "touch";
	public static final String TAB_MOUSE = "mouse";
	public static final String TAB_OFFICE_CONTROL = "office_control";
	public static final String TAB_SETTING = "setting";
	public static final String TAB_HISTORY = "history ";

	private ImageView btnMenu;
	private ImageView icMenu;

	private View layout_menu;
	private LinearLayout layout_menu_dialog;
	private ImageView bg_menu_dialog;

	private Animation mainRotateRight;
	private Animation mainRotateLeft;

	private View menu_mouse, menu_touch, menu_control, menu_keyboard, menu_history, menu_setting;// 菜单栏
	private TextView history_message_1, history_message_2;

	private HistoryDao mHistoryDao = new HistoryDao();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();

		bindPhoneUserService();

		registerReceiver();
		initThreadHandler();

		initView();
		addTab();
		setView();

		initSV();// 初始化声音与振动
		startWipico();// 启动Wipico
	}

	private void initData() {
		isNetworkAvailable = false;
		listDevices = null;
		mDevice = null;
		device_state = 0;
		device_type = DeviceType.DEVICE_COMMON;
		deviceRunType = 0;
	}

	private void initView() {
		layout_menu = findViewById(R.id.layout_menu);
		layout_share = findViewById(R.id.layout_share);
		layout_share.setOnClickListener(this);
		btnShareCancel = (Button) findViewById(R.id.btnShareCancel);
		btnShareCancel.setOnClickListener(this);
		btnShare = (Button) findViewById(R.id.btnShare);
		btnShare.setOnClickListener(this);

		mImageIntent = new Intent(this, ImageTabActivity.class);
		mMediaIntent = new Intent(this, MediaTabActivity.class);
		mBusinessIntent = new Intent(this, OfficeTabActivity.class);
		mApplicationIntent = new Intent(this, ApplicationActivity.class);
		mControlIntent = new Intent(this, ControlActivity.class);
		mTouchIntent = new Intent(this, TouchActivity.class);
		mMouseIntent = new Intent(this, MouseActivity.class);
		mOfficeControlIntent = new Intent(this, OfficeControlActivity.class);
		mSettingIntent = new Intent(this, SettingActivity.class);
		mHistoryIntent = new Intent(this, HistoryActivity.class);

		nav_media = findViewById(R.id.nav_media);
		nav_image = findViewById(R.id.nav_image);
		nav_business = findViewById(R.id.nav_business);
		nav_application = findViewById(R.id.nav_application);
		nav_control = findViewById(R.id.nav_control);
		nav_media.setOnClickListener(this);
		nav_image.setOnClickListener(this);
		nav_business.setOnClickListener(this);
		nav_application.setOnClickListener(this);
		nav_control.setOnClickListener(this);

		initMenu();
	}

	private boolean rotated = false;
	private AtomicBoolean plusAnimationActive = new AtomicBoolean(false);

	private void initMenu() {
		btnMenu = (ImageView) findViewById(R.id.btn_menu);
		btnMenu.setOnClickListener(this);
		icMenu = (ImageView) findViewById(R.id.ic_menu);
		bg_menu_dialog = (ImageView) findViewById(R.id.bg_menu_dialog);
		bg_menu_dialog.setOnClickListener(this);
		layout_menu_dialog = (LinearLayout) findViewById(R.id.layout_menu_dialog);

		history_message_1 = (TextView) findViewById(R.id.history_message_1);
		history_message_2 = (TextView) findViewById(R.id.history_message_2);

		menu_mouse = findViewById(R.id.menu_mouse);
		menu_mouse.setOnClickListener(this);
		menu_touch = findViewById(R.id.menu_touch);
		menu_touch.setOnClickListener(this);
		menu_control = findViewById(R.id.menu_control);
		menu_control.setOnClickListener(this);
		menu_keyboard = findViewById(R.id.menu_keyboard);
		menu_keyboard.setOnClickListener(this);
		menu_history = findViewById(R.id.menu_history);
		menu_history.setOnClickListener(this);
		menu_setting = findViewById(R.id.menu_setting);
		menu_setting.setOnClickListener(this);

		mainRotateLeft = MenuAnimation.createMainButtonAnimation(getApplicationContext());
		mainRotateRight = MenuAnimation.createMainButtonInverseAnimation(getApplicationContext());
		Animation.AnimationListener plusAnimationListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				plusAnimationActive.set(false);
			}
		};

		mainRotateLeft.setAnimationListener(plusAnimationListener);
		mainRotateRight.setAnimationListener(plusAnimationListener);
	}

	private void setView() {
		setNav(TAB_MEDIA);
	}

	public void setNav(String nav) {
		if (nav.equals(TAB_MEDIA)) {
			nav_media.setSelected(true);
			nav_image.setSelected(false);
			nav_business.setSelected(false);
			nav_application.setSelected(false);
			nav_control.setSelected(false);
		} else if (nav.equals(TAB_IMAGE)) {
			nav_media.setSelected(false);
			nav_image.setSelected(true);
			nav_business.setSelected(false);
			nav_application.setSelected(false);
			nav_control.setSelected(false);
		} else if (nav.equals(TAB_BUSINESS)) {
			nav_media.setSelected(false);
			nav_image.setSelected(false);
			nav_business.setSelected(true);
			nav_application.setSelected(false);
			nav_control.setSelected(false);
		} else if (nav.equals(TAB_APPLICATION)) {
			nav_media.setSelected(false);
			nav_image.setSelected(false);
			nav_business.setSelected(false);
			nav_application.setSelected(true);
			nav_control.setSelected(false);
		} else if (nav.equals(TAB_CONTROL) || nav.equals(TAB_TOUCH) || nav.equals(TAB_MOUSE) || nav.equals(TAB_OFFICE_CONTROL) || nav.equals(TAB_CONTROL)) {
			nav_media.setSelected(false);
			nav_image.setSelected(false);
			nav_business.setSelected(false);
			nav_application.setSelected(false);
			nav_control.setSelected(true);
		} else {
			nav_media.setSelected(false);
			nav_image.setSelected(false);
			nav_business.setSelected(false);
			nav_application.setSelected(false);
			nav_control.setSelected(false);
		}
		this.mTabHost.setCurrentTabByTag(nav);
	}

	@SuppressWarnings("deprecation")
	private void addTab() {
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;
		localTabHost.addTab(buildTabSpec(TAB_MEDIA, TAB_MEDIA, R.drawable.ic_launcher, this.mMediaIntent));
		localTabHost.addTab(buildTabSpec(TAB_IMAGE, TAB_IMAGE, R.drawable.ic_launcher, this.mImageIntent));
		localTabHost.addTab(buildTabSpec(TAB_BUSINESS, TAB_BUSINESS, R.drawable.ic_launcher, this.mBusinessIntent));
		localTabHost.addTab(buildTabSpec(TAB_APPLICATION, TAB_APPLICATION, R.drawable.ic_launcher, this.mApplicationIntent));
		localTabHost.addTab(buildTabSpec(TAB_CONTROL, TAB_CONTROL, R.drawable.ic_launcher, this.mControlIntent));
		localTabHost.addTab(buildTabSpec(TAB_TOUCH, TAB_TOUCH, R.drawable.ic_launcher, this.mTouchIntent));
		localTabHost.addTab(buildTabSpec(TAB_MOUSE, TAB_MOUSE, R.drawable.ic_launcher, this.mMouseIntent));
		localTabHost.addTab(buildTabSpec(TAB_OFFICE_CONTROL, TAB_OFFICE_CONTROL, R.drawable.ic_launcher, this.mOfficeControlIntent));
		localTabHost.addTab(buildTabSpec(TAB_SETTING, TAB_SETTING, R.drawable.ic_launcher, this.mSettingIntent));
		localTabHost.addTab(buildTabSpec(TAB_HISTORY, TAB_HISTORY, R.drawable.ic_launcher, this.mHistoryIntent));

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				hideShareView();
			}
		});
	}

	private TabHost.TabSpec buildTabSpec(String tag, String resLabel, int resIcon, final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(resLabel, getResources().getDrawable(resIcon)).setContent(content);
	}

	@Override
	public void onClick(View arg0) {
		// TODO onClick();
		switch (arg0.getId()) {
		case R.id.nav_media:
			setNav(TAB_MEDIA);
			break;
		case R.id.nav_image:
			setNav(TAB_IMAGE);
			break;
		case R.id.nav_business:
			setNav(TAB_BUSINESS);
			break;
		case R.id.nav_application:
			setNav(TAB_APPLICATION);
			break;
		case R.id.nav_control:
			if (syncPlayClient != null) {
				if (syncPlayClient.getMediaType() == 0) {
					setNav(TAB_OFFICE_CONTROL);
				} else if (syncPlayClient.getMediaType() == 1) {
					if (MainActivity.mDevice != null) {
						Intent intent = new Intent();
						intent.setClass(this, ImagePlayerActivity.class);
						intent.putExtra("mode", com.chinasvc.wipicophone.config.Config.MODE_REMOTE);
						if (WipicoApplication.listCameras.size() > 0 && WipicoApplication.cameraPosition >= 0
								&& WipicoApplication.listCameras.size() > WipicoApplication.cameraPosition + 1) {
							intent.putExtra("type", com.chinasvc.wipicophone.config.Config.TYPE_CAMERA);
							startActivity(intent);
						} else if (WipicoApplication.listGallerys.size() > 0 && WipicoApplication.galleryPosition >= 0
								&& WipicoApplication.listGallerys.size() > WipicoApplication.galleryPosition + 1) {
							intent.putExtra("type", com.chinasvc.wipicophone.config.Config.TYPE_GALLERY);
							startActivity(intent);
						} else {
							intent.putExtra("type", com.chinasvc.wipicophone.config.Config.TYPE_CAMERA);
							startActivity(intent);
						}
					} else {
						Toast.makeText(this, R.string.msg_no_device, Toast.LENGTH_SHORT).show();
					}
				} else if (syncPlayClient.getMediaType() == 2) {
					if (MainActivity.mDevice != null) {
						Intent intent = new Intent();
						intent.setClass(this, AudioPlayerActivity.class);
						intent.putExtra("mode", com.chinasvc.wipicophone.config.Config.MODE_REMOTE);
						startActivity(intent);
					} else {
						Toast.makeText(this, R.string.msg_no_device, Toast.LENGTH_SHORT).show();
					}
				} else if (syncPlayClient.getMediaType() == 3) {
					if (MainActivity.mDevice != null) {
						Intent intent = new Intent(this, VideoControlActivity.class);
						Bundle bundle = new Bundle();
						if (WipicoApplication.listMovies.size() > 0 && WipicoApplication.videoPosition >= 0
								&& WipicoApplication.listMovies.size() > WipicoApplication.videoPosition + 1) {
							bundle.putString("url", WipicoApplication.listMovies.get(WipicoApplication.videoPosition).getPath());
							bundle.putString("title", WipicoApplication.listMovies.get(WipicoApplication.videoPosition).getName());
						} else {
							bundle.putString("title", getString(R.string.actionbar_title_video_control_default));
						}
						intent.putExtras(bundle);
						startActivity(intent);
					} else {
						Toast.makeText(this, R.string.msg_no_device, Toast.LENGTH_SHORT).show();
					}
				}
			}
			break;
		case R.id.btn_menu:
			hideShareView();
			changeMenu();
			break;
		case R.id.btnShare:
			if (shareMode == 0) {
				// 本地的文件
				setPopDatas();
				int[] location = new int[2];
				btnShare.getLocationInWindow(location);
				showPop(location[0]);
			} else {
				// 投影的下载
				hideShareView();
				remote2phone(listTempUpload);
			}
			break;
		case R.id.btnShareCancel:
			hideShareView();
			break;
		case R.id.bg_menu_dialog:
			hideShareView();
			changeMenu();
			break;
		case R.id.menu_mouse:
			changeMenu();
			setNav(TAB_MOUSE);
			break;
		case R.id.menu_touch:
			changeMenu();
			setNav(TAB_TOUCH);
			break;
		case R.id.menu_control:
			changeMenu();
			setNav(TAB_CONTROL);
			break;
		case R.id.menu_keyboard:
			changeMenu();
			Intent intent = new Intent();
			intent.setClass(this, KeyBoardActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_history:
			changeMenu();
			setNav(TAB_HISTORY);
			break;
		case R.id.menu_setting:
			changeMenu();
			setNav(TAB_SETTING);
			break;
		}
	}

	private void changeMenu() {
		if (plusAnimationActive.compareAndSet(false, true)) {
			if (!rotated) {
				icMenu.startAnimation(mainRotateLeft);
				// Menu显示
				bg_menu_dialog.setVisibility(View.VISIBLE);
				layout_menu_dialog.setVisibility(View.VISIBLE);
			} else {
				icMenu.startAnimation(mainRotateRight);
				bg_menu_dialog.setVisibility(View.INVISIBLE);
				layout_menu_dialog.setVisibility(View.INVISIBLE);
			}
			rotated = !rotated;
		}
	}

	/** +++++++++++++++++++连接设备+++++++++++++++++++++++ */
	public static WipicoClient mWipicoClient;
	public static boolean isNetworkAvailable = false;// 网络是否可用
	public static List<Device> listDevices = null;// 扫描到的设备列表
	public static Device mDevice = null;// 当前选择的设备列表
	public static int device_state = 0;// 设备连接状态
	public static int device_type = DeviceType.DEVICE_COMMON;

	public static SyncPlayClient syncPlayClient;// 同步器
	private static int deviceRunType = 0;// 设备端当前运行的的类别
	private static String deviceSSID = "";// 设备端的的WIFI SSID
	private static String deviceVersion = "";// 设备端的版本号
	public static WipicoFileClient mWipicoFileClient;// FTP客户端
	private PulseHelper mPulseHelper;// 心跳发射器

	/**
	 * 开启Wipico
	 * */
	public void startWipico() {
		listDevices = new ArrayList<Device>();
		mWipicoClient = new WipicoClient(getApplicationContext());
		mWipicoClient.setOnScanListener(new Scan());
		mWipicoClient.startWipico(true, 1);// 版本1进行常规扫描，版本2，进行SSID扫描
		startMediaAsync();
	}

	public void startMediaAsync() {
		syncPlayClient = new SyncPlayClient();
		syncPlayClient.startSync();
		syncPlayClient.setDeviceAsyncListener(new DeviceSyncListener() {
			@Override
			public void deviceSync(int type, String ssid, String version) {
				if (isDebug)
					Log.i(TAG, "deviceSync>>>>>>");
				if (deviceSSID != null && ssid != null && !deviceSSID.equals(ssid)) {
					deviceSSID = ssid;
				}
				if (deviceVersion != null && version != null && !deviceVersion.equals(version)) {
					deviceVersion = version;
				}
				if (deviceRunType != type) {
					// 当前设备运行的类别
					deviceRunType = type;
				}
			}

			@Override
			public void gameSync() {
				if (isDebug)
					Log.i(TAG, "游戏更新>>>>>>");
				// Intent intent = new
				// Intent(Constant.BROADCAST_UPDATE_GAME);
				// sendBroadcast(intent);
			}
		});
	}

	private class Scan implements ScanListener {
		@Override
		public void isNetworkAvailable(boolean isAvailable) {
			isNetworkAvailable = isAvailable;
		}

		@Override
		public void scanException() {
		}

		@Override
		public void connected() {
			isNetworkAvailable = true;
		}

		@Override
		public void connecting() {
		}

		@Override
		public void disconnected() {
			isNetworkAvailable = false;
			device_state = DeviceStatus.STATUS_NO_CONNECTED;
			sendBroadcast(new Intent(BroadcastConfig.BROADCAST_DEVICE_DISCONNECTED));
		}

		@Override
		public void disconnecting() {
		}

		@Override
		public void deviceStatus(int status) {
			device_state = status;
		}

		@Override
		public void deviceType(int arg0) {
			device_type = arg0;// 类型。wifi设备列表、wipico设备列表
		}

		@Override
		public void onResult(List<Device> listDatas) {
			listDevices.clear();
			listDevices.addAll(listDatas);
			if (isDebug)
				Log.i(TAG, "onResult 广播扫描结果>>>>>" + listDatas + "|" + MainActivity.device_state + "|" + listDevices.size() + "|" + WipicoApplication.needShowConnected);
			if (MainActivity.device_state == DeviceStatus.STATUS_NO_CONNECTED && MainActivity.isNetworkAvailable) {
				// 当前未连接，且网络连通，则获取上一次的连接设备，如果存在，且在24小时之内，则直接连接，否则不做任何处理
				Device preDevice = null;
				if (!PreferenceUtil.getInstance(getApplicationContext()).getDevice().equals("") && listDevices.size() > 0
						&& (System.currentTimeMillis() - PreferenceUtil.getInstance(getApplicationContext()).getSysTime()) < (3600 * 24 * 1000)) {
					for (Device bean : listDevices) {
						if (bean.getDeviceName().equals(PreferenceUtil.getInstance(getApplicationContext()).getDevice())) {
							preDevice = bean;
						}
					}
				}
				if (preDevice != null) {
					setDevice(preDevice);
				}
			}
			if (MainActivity.device_state == DeviceStatus.STATUS_NO_CONNECTED && listDevices.size() > 0 && WipicoApplication.needShowConnected) {
				WipicoApplication.needShowConnected = false;
				sendBroadcast(new Intent(BroadcastConfig.BROADCAST_DEVICE_CONNECTING));
			}
		}

		@Override
		public void onWifiResult(List<Device> arg0) {
		}
	}

	/**
	 * 关闭Wipico
	 * */
	public void stopWipico() {
		mThreadHandler.obtainMessage(DISCONNECT_FILE).sendToTarget();
		mWipicoClient.stopWipico();
	}

	private int position;// List内的设备的索引
	private ConnectDialog connectDialog;

	private void showConnectDialog() {
		if (isNetworkAvailable) {
			if (listDevices != null && listDevices.size() <= 0) {
				showToast(getString(R.string.msg_no_scan_device));
			} else {
				if (connectDialog == null || (connectDialog != null && !connectDialog.isShowing())) {
					final List<Device> devices = new ArrayList<Device>();
					devices.addAll(listDevices);
					if (devices != null && devices.size() > 0) {
						if (mDevice != null) {
							position = devices.indexOf(mDevice);
						} else {
							position = 0;
						}
						connectDialog = new ConnectDialog(this, devices, position);
						connectDialog.setCustomTitle(R.string.dialog_title_connect);
						connectDialog.setConnectDialogListener(new ConnectDialogListener() {
							@Override
							public void onConfirmClickListener() {
								if (position >= 0) {
									// 发送验证码
									verificationValue = MathUtil.getVerification();
									mThreadHandler.obtainMessage(CONNECT_REQUEST, devices.get(position)).sendToTarget();
									// 验证对话框
									verification(devices.get(position));
								}
							}

							@Override
							public void onCancelClickListener() {
							}
						});
						connectDialog.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
								position = arg2;
							}
						});
						connectDialog.show();
					}
				}
			}
		} else {
			showToast(getString(R.string.msg_no_net));
		}
	}

	private VerificationDialog verificationDialog;
	private String verificationValue;// 验证码

	/**
	 * 验证码验证
	 * */
	public void verification(final Device bean) {
		verificationDialog = new VerificationDialog(this, verificationValue);
		verificationDialog.setCustomTitle(R.string.dialog_title_verification);
		verificationDialog.setVerificationListener(new VerificationListener() {

			@Override
			public void onConfirmClickListener(String value) {
				if (value.equals(verificationValue)) {
					PreferenceUtil.getInstance(getApplicationContext()).setSysTime(System.currentTimeMillis());
					PreferenceUtil.getInstance(getApplicationContext()).setDevice(bean.getDeviceName());
					setDevice(bean);
					mThreadHandler.obtainMessage(CONNECT_OK, bean).sendToTarget();

					verificationDialog.dismiss();
				} else {
					verificationDialog.setError();
				}
			}

			@Override
			public void onCancelClickListener() {
			}

			@Override
			public void onAgainClickListener() {
				mThreadHandler.obtainMessage(CONNECT_REQUEST, bean).sendToTarget();
			}
		});
		verificationDialog.show();
	}

	public void setDevice(Device device) {
		if (device == null) {
			mDevice = null;
			MainActivity.mWipicoClient.setDevice(null);
			Intent intent = new Intent(BroadcastConfig.ACTION_DEVICE_DISCONNECTED);
			sendBroadcast(intent);
		} else {
			PreferenceUtil.getInstance(getApplicationContext()).setSysTime(System.currentTimeMillis());
			PreferenceUtil.getInstance(getApplicationContext()).setDevice(device.getDeviceName());
			mDevice = new Device(device.getDeviceName(), device.getDeviceIp());
			MainActivity.mWipicoClient.setDevice(mDevice);
			Intent intent = new Intent(BroadcastConfig.ACTION_DEVICE_CONNECTED);
			sendBroadcast(intent);
		}

		// 是否要发送心跳
		if (device == null) {
			if (mPulseHelper != null) {
				mPulseHelper.stopPulse();
				mPulseHelper = null;
			}
		} else {
			// 发送心跳
			if (mPulseHelper != null) {
				mPulseHelper.stopPulse();
				mPulseHelper = null;
			}
			mPulseHelper = new PulseHelper(mDevice, Build.PRODUCT);
			mPulseHelper.startPulse();
		}

		if (device != null) {
			// 重新连接FTP服务器
			mThreadHandler.obtainMessage(CONNECT_FILE, device).sendToTarget();
		} else {
			mThreadHandler.obtainMessage(DISCONNECT_FILE).sendToTarget();
		}

	}

	public Handler mThreadHandler = null;// 线程Handler
	private final int CONNECT_REQUEST = 0;
	private final int CONNECT_OK = 1;
	private final int CONNECT_FILE = 2;
	private final int DISCONNECT_FILE = 3;
	private final int CANCEL_FILE = 4;

	protected void initThreadHandler() {
		HandlerThread handlerThread = new HandlerThread("HandlerThread");
		handlerThread.start();
		mThreadHandler = new Handler(handlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case CONNECT_REQUEST:
					// 发送验证码
					if (msg.obj != null) {
						ActionHelper.sendVerification(1, Build.PRODUCT, verificationValue, (Device) msg.obj);
					}
					break;
				case CONNECT_OK:
					// 发送连接成功验证
					if (msg.obj != null) {
						ActionHelper.sendVerification(2, Build.PRODUCT, verificationValue, (Device) msg.obj);
					}
					break;
				case CONNECT_FILE:
					if (mWipicoFileClient != null) {
						mWipicoFileClient.disconnect();
						mWipicoFileClient = null;
					}

					mWipicoFileClient = new WipicoFileClient((Device) msg.obj);
					mWipicoFileClient.setFileExceptionListener(new FileExceptionListener() {
						@Override
						public void refused() {
							if (isDebug)
								Log.e("FileClient", "服务器拒绝连接>>>>>>>");
						}

						@Override
						public void exception() {
							if (mDevice != null && device_state == DeviceStatus.STATUS_CONNECTED) {
								if (isDebug)
									Log.e("FileClient", "服务器连接异常>>>>>>>");
								if (mWipicoFileClient != null) {
									mWipicoFileClient.disconnect();
									mWipicoFileClient = null;
								}
								mThreadHandler.obtainMessage(CONNECT_FILE, mDevice).sendToTarget();
							}
						}
					});
					mWipicoFileClient.setFileTransferListener(new FileTransferListener() {
						@Override
						public void transfer(long total, int bufferSize) {
							// TODO 文件传输进度
							if (count >= threadFinishedCount && threads.get(threadFinishedCount) != null && threads.get(threadFinishedCount).isStarted()
									&& !threads.get(threadFinishedCount).isFinished()) {
								if (mapHistorys.get(threadFinishedCount) != null) {
									int progress = (int) (((total * 1.0) / mapHistorys.get(threadFinishedCount).getSize()) * 100);
									mapHistorys.get(threadFinishedCount).setProgress(progress);
									mHistoryDao.update(mapHistorys.get(threadFinishedCount));
									uploadHandler.obtainMessage(UPDATE_HISTORY_PROGRESS).sendToTarget();
								}
							}
						}
					});
					if (mWipicoFileClient.connect()) {
						if (isDebug)
							Log.i("FileClient", "连接成功>>>>>>>应该去取游戏列表");
						checkShareFile();
						checkPPTFile();
						// Intent intent = new
						// Intent(Constant.BROADCAST_UPDATE_GAME);
						// sendBroadcast(intent);
					}
					break;
				case DISCONNECT_FILE:
					if (mWipicoFileClient != null) {
						mWipicoFileClient = null;
					}
					break;
				case CANCEL_FILE:
					mWipicoFileClient.cancel();
					break;
				}
			}
		};
	}

	/**
	 * 检查Share文件
	 * */
	private void checkShareFile() {
		if (MainActivity.mWipicoFileClient != null) {
			if (!MainActivity.mWipicoFileClient.isExistFile("/sdcard/Wipico")) {
				// 判断是否存在 /Office 文件，如果不存在，直接创建。存在，不处理
				MainActivity.mWipicoFileClient.creatFile("/sdcard/Wipico");
			} else {
			}
		}
	}

	/**
	 * 检查PPT文件
	 * */
	private void checkPPTFile() {
		if (MainActivity.mWipicoFileClient != null) {
			if (!MainActivity.mWipicoFileClient.isExistFile("/sdcard/Office")) {
				// 判断是否存在 /Office 文件，如果不存在，直接创建。存在，不处理
				MainActivity.mWipicoFileClient.creatFile("/sdcard/Office");
			} else {
			}
		}
	}

	/** ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ **/

	private Vibrator mVibrator;
	private SoundPool sp;// 声明一个SoundPool
	private int click_music;// 定义一个整型用load（）；来设置suondID
	private int recevier_music;// 定义一个整型用load（）；来设置suondID
	private int sender_music;// 定义一个整型用load（）；来设置suondID

	private void initSV() {
		mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);// 获取震动器
		sp = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);// 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
		click_music = sp.load(this, R.raw.click, 1); // 把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
		recevier_music = sp.load(this, R.raw.recevier_ok, 1);
		sender_music = sp.load(this, R.raw.send_start, 1);
	}

	public static final int SOUND = 0x100;
	public static final int VIBRATOR = 0x101;
	public static final int SEND_FILE = 0x102;
	public static final int RECEIVER_FILE = 0x103;

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SOUND:
				// 按键音启动
				if (PreferenceUtil.getInstance(getApplicationContext()).getSound() == 1) {
					sp.play(click_music, 0.3f, 0.3f, 0, 0, 1);
				}
				break;
			case SEND_FILE:
				// 按键音启动
				if (PreferenceUtil.getInstance(getApplicationContext()).getSound() == 1) {
					sp.play(sender_music, 1f, 1f, 0, 0, 1);
				}
				break;
			case RECEIVER_FILE:
				// 按键音启动
				if (PreferenceUtil.getInstance(getApplicationContext()).getSound() == 1) {
					sp.play(recevier_music, 1f, 1f, 0, 0, 1);
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

	/** +++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

	public List<User> users = new ArrayList<User>();// 扫描的用户
	public static MyBinder binder;
	private boolean binded;

	private MyServiceConnection connection;

	private UserBroadcastReceiver receiver = new UserBroadcastReceiver();// 用户变更广播接收器

	private void bindPhoneUserService() {
		Intent intent = new Intent(MainActivity.this, ChatService.class);
		startService(intent);
		bindService(intent, connection = new MyServiceConnection(), Context.BIND_AUTO_CREATE);
	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(Config.ACTION_ADD_USER);
		filter.addAction(Config.ACTION_LOGIN_SUCC);
		filter.addAction(Config.ACTION_REMOVE_USER);
		filter.addAction(BroadcastConfig.BROADCAST_DEVICE_CONNECTING);
		filter.addAction(ChatService.ACTION_ADD_SEND_TASK);
		filter.addAction(ChatService.ACTION_ADD_RECIVER_TASK);
		filter.addAction(ChatService.ACTION_CANCEL_RECEVIE_FILE);
		filter.addAction(ChatService.ACTION_CANCEL_SEND_FILE);
		filter.addAction(BroadcastConfig.BROADCAST_DEVICE_DISCONNECTED);
		registerReceiver(receiver, filter);
	}

	/**
	 * 用来通知刷新列表
	 */
	private class UserBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (binder != null) {
				if (Config.ACTION_ADD_USER.equals(intent.getAction())) {
					users.clear();
					Set<Entry<String, User>> set = binder.getUsers().entrySet();
					for (Entry<String, User> entry : set) {
						users.add(entry.getValue());
					}
				} else if (Config.ACTION_LOGIN_SUCC.equals(intent.getAction())) {
					users.clear();
					Set<Entry<String, User>> set = binder.getUsers().entrySet();
					for (Entry<String, User> entry : set) {
						users.add(entry.getValue());
					}
				} else if (Config.ACTION_REMOVE_USER.equals(intent.getAction())) {
					users.clear();
					Set<Entry<String, User>> set = binder.getUsers().entrySet();
					for (Entry<String, User> entry : set) {
						users.add(entry.getValue());
					}
				}
			} else {
				// 如果未绑定服务,重新绑定服务
				unbindService(connection);
				binded = false;
				bindService(new Intent(MainActivity.this, ChatService.class), connection = new MyServiceConnection(), Context.BIND_AUTO_CREATE);
			}

			if (intent.getAction() == BroadcastConfig.BROADCAST_DEVICE_CONNECTING) {
				if (layout_share.getVisibility() == View.VISIBLE) {
					hideShareView();
				} else {
					showConnectDialog();
				}
			} else if (intent.getAction() == BroadcastConfig.BROADCAST_DEVICE_DISCONNECTED) {
				setDevice(null);
			} else if (intent.getAction() == ChatService.ACTION_ADD_SEND_TASK) {
				// TODO 添加上传分享任务队列
				// try {
				// FileMessage fileMessage = new FileMessage(new
				// JSONObject(intent.getStringExtra("file_msg")));
				// List<FileInfo> listFileInfos = new
				// ArrayList<FileInfo>();
				// FileInfo file = new FileInfo();
				// file.setName(fileMessage.getName());
				// file.setPath(fileMessage.getFilePath());
				// file.setSize(fileMessage.getLength());
				// listFileInfos.add(file);
				// share2User(new
				// User(intent.getStringExtra("user_ip"),
				// intent.getStringExtra("user_name")),
				// listFileInfos);
				// } catch (JSONException e) {
				// e.printStackTrace();
				// }
			} else if (intent.getAction() == ChatService.ACTION_ADD_RECIVER_TASK) {
				// TODO 添加下载分享任务队列
				try {
					FileMessage fileMessage = new FileMessage(new JSONObject(intent.getStringExtra("file_msg")));
					List<FileInfo> listFileInfos = new ArrayList<FileInfo>();
					FileInfo file = new FileInfo();
					file.setName(fileMessage.getName());
					file.setPath(fileMessage.getFilePath());
					file.setSize(fileMessage.getLength());
					listFileInfos.add(file);
					reciverFromUser(new User(intent.getStringExtra("user_ip"), intent.getStringExtra("user_name")), listFileInfos);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (intent.getAction() == ChatService.ACTION_CANCEL_RECEVIE_FILE) {
				cancelReceiveTask(intent.getStringExtra("user_ip"), intent.getStringExtra("file_msg"));
			} else if (intent.getAction() == ChatService.ACTION_CANCEL_SEND_FILE) {
				cancelSendTask(intent.getStringExtra("user_ip"), intent.getStringExtra("file_msg"));
			}
		}
	}

	public class MyServiceConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (MyBinder) service;
			binded = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	}

	/** ++++++++++++++++++分享布局与PopWindow+++++++++++++++++++++++ **/

	private View layout_share;// 分享布局
	private Button btnShareCancel, btnShare;// 分享，取消
	private View popupView;
	private PopupWindow pop;
	private ListView popListView;
	private View main_pop;

	public void hideShareView() {
		layout_share.setVisibility(View.INVISIBLE);
		sendBroadcast(new Intent(BroadcastConfig.ACTION_HIDE_SHARE));
	}

	public boolean isShareVisible() {
		if (layout_share.getVisibility() == View.VISIBLE) {
			return true;
		} else {
			return false;
		}
	}

	private List<PopMenu> listPopDatas = new ArrayList<PopMenu>();

	/**
	 * 设置Pop菜单的内容
	 * */
	private void setPopDatas() {
		listPopDatas.clear();
		if (MainActivity.mDevice != null) {
			listPopDatas.add(new PopMenu(PopType.REMOTE_PLAY, getString(R.string.pop_remote)));
		}
		for (User user : users) {
			listPopDatas.add(new PopMenu(PopType.USER, user.getUserName(), user));
		}
	}

	/**
	 * 显示PopWindow
	 * */
	private void showPop(int x) {
		if (listPopDatas.size() <= 0) {
			Toast.makeText(this, R.string.msg_no_share, Toast.LENGTH_SHORT).show();
		} else {
			LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			popupView = mLayoutInflater.inflate(R.layout.pop_list_menu, null);
			popListView = (ListView) popupView.findViewById(R.id.mListView);
			main_pop = popupView.findViewById(R.id.main_pop);
			popListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
					// TODO share to user
					pop.dismiss();
					if (listPopDatas.get(position).getType() == PopType.USER) {
						// 分享到别的手机TCP传输
						if (listTempUpload.size() > 0) {
							binder.sendFile(listTempUpload.get(0).getPath(), listPopDatas.get(position).getUser());
							share2User(listPopDatas.get(position).getUser(), listTempUpload);
						} else {
						}
						hideShareView();
					} else if (listPopDatas.get(position).getType() == PopType.REMOTE_PLAY) {
						// 分享到投影仪 ,FTP上传
						share2Remote(listTempUpload);
						hideShareView();
					} else if (listPopDatas.get(position).getType() == PopType.SHARE) {

					} else if (listPopDatas.get(position).getType() == PopType.MULTI) {

					}
				}
			});
			popListView.setAdapter(new PopMenuAdapter(this, listPopDatas));
			pop = new PopupWindow(popupView, getResources().getDimensionPixelSize(R.dimen.gridview_column_3_width), LayoutParams.WRAP_CONTENT);
			pop.setFocusable(true);
			pop.update();
			pop.setOutsideTouchable(true);
			pop.setAnimationStyle(R.style.PopupAnimation);
			pop.setBackgroundDrawable(new BitmapDrawable());
			pop.showAtLocation(layout_menu, Gravity.NO_GRAVITY, (btnShare.getWidth() / 2 - getResources().getDimensionPixelSize(R.dimen.gridview_column_3_width) / 2 + x),
					correctPopPosition());
		}
	}

	/**
	 * 修正PopWindow的位置
	 * */
	private int correctPopPosition() {
		int offsetY;
		int popHeight = listPopDatas.size() * getResources().getDimensionPixelSize(R.dimen.pop_item_height) + DensityUtil.dip2px(this, 9);
		main_pop.setBackgroundResource(R.drawable.pop_bg_list_up);
		offsetY = DensityUtil.getScreenHeight(this) - getResources().getDimensionPixelSize(R.dimen.footbar_height) - popHeight;
		return offsetY;
	}

	/** +++++++++++++++++++++++FTP 单线程队列上传分享+++++++++++++++++++++++++ **/
	private Lock lock = new ReentrantLock();
	// 任务集合
	private Map<Integer, FTPUploaderThread> threads = new HashMap<Integer, FTPUploaderThread>();
	// 任务状态的数据
	public Map<Integer, History> mapHistorys = new HashMap<Integer, History>();// 下载队列历史数据
	private int threadFinishedCount = 0;// 已完成任务的数量
	private int count = 0; // 已添加多少个任务

	private int history_count = 0;

	/**
	 * 设置历史记录计数器
	 * */
	public void setHistoryCount(int count) {
		if (mTabHost.getCurrentTab() != 9) {
			history_count += count;
			if (history_count > 0) {
				history_message_1.setVisibility(View.VISIBLE);
				history_message_2.setVisibility(View.VISIBLE);
			} else {
				history_message_1.setVisibility(View.INVISIBLE);
				history_message_2.setVisibility(View.INVISIBLE);
			}
			history_message_1.setText(history_count + "");
			history_message_2.setText(history_count + "");
		}
	}

	/**
	 * 清空历史记录计数器
	 * */
	public void clearHistoryCount() {
		history_count = 0;
		history_message_1.setText(history_count + "");
		history_message_2.setText(history_count + "");
		history_message_1.setVisibility(View.INVISIBLE);
		history_message_2.setVisibility(View.INVISIBLE);
	}

	public void share2Remote(List<FileInfo> listDatas) {
		// TODO share to remote
		mHandler.obtainMessage(SEND_FILE).sendToTarget();
		for (FileInfo fileInfo : listDatas) {
			count++;
			lock.lock();
			threads.put(count, new FTPUploaderThread(uploadHandler, fileInfo, mWipicoFileClient));
			lock.unlock();
			// 添加一条数据
			History bean = new History();
			bean.setName(fileInfo.getName());
			bean.setSize((int) fileInfo.getSize());
			bean.setPath(fileInfo.getPath());
			bean.setUserName(mDevice.getDeviceName());
			bean.setTransfer(History.TRANSFER_SEND);
			bean.setState(History.STATE_WAIT);// 等待下载
			bean.setUserType(History.USER_TYPE_DEVICE);
			bean.setProgress(0);
			bean.setThreadCount(count);
			bean.setTime(System.currentTimeMillis() + "");
			bean.setId((int) mHistoryDao.add(bean));
			mapHistorys.put(count, bean);
		}

		setHistoryCount(listDatas.size());
		sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));

		uploadHandler.obtainMessage(FTPUploaderThread.THREAD_BEGIN).sendToTarget();
	}

	public void remote2phone(List<FileInfo> listDatas) {
		// TODO share to remote
		mHandler.obtainMessage(SEND_FILE).sendToTarget();
		for (FileInfo fileInfo : listDatas) {
			count++;
			lock.lock();
			threads.put(count, new FTPUploaderThread(uploadHandler, WipicoApplication.appInstance.getFilePath(), fileInfo, mWipicoFileClient));
			lock.unlock();
			// 添加一条数据
			History bean = new History();
			bean.setName(fileInfo.getName());
			bean.setSize((int) fileInfo.getSize());
			bean.setPath(fileInfo.getPath());
			bean.setUserName(mDevice.getDeviceName());
			bean.setTransfer(History.TRANSFER_RECEIVE);
			bean.setUserType(History.USER_TYPE_DEVICE);
			bean.setState(History.STATE_WAIT);// 等待下载
			bean.setProgress(0);
			bean.setThreadCount(count);
			bean.setTime(System.currentTimeMillis() + "");
			bean.setId((int) mHistoryDao.add(bean));
			mapHistorys.put(count, bean);
		}

		setHistoryCount(listDatas.size());// 新增计数器
		sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));

		uploadHandler.obtainMessage(FTPUploaderThread.THREAD_BEGIN).sendToTarget();
	}

	/***
	 * 分享到别的手机，添加到任务队列中
	 * 
	 * @param user
	 *                用户
	 * @param listDatas
	 *                添加的文件List
	 * */
	private void share2User(User user, List<FileInfo> listDatas) {
		mHandler.obtainMessage(SEND_FILE).sendToTarget();
		fileListener = TCPFileListener.getInstance();
		fileListener.setOnProgressUpdate(this);
		if (!fileListener.isRunning()) {
			try {
				fileListener.open();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (FileInfo fileInfo : listDatas) {
			count++;
			lock.lock();
			threads.put(count, new FTPUploaderThread(user, new File(fileInfo.getPath())));
			lock.unlock();
			// 添加一条数据
			History bean = new History();
			bean.setName(fileInfo.getName());
			bean.setSize((int) fileInfo.getSize());
			bean.setPath(fileInfo.getPath());
			bean.setUserName(user.getUserName());
			bean.setUserIp(user.getIp());
			bean.setTransfer(History.TRANSFER_SEND);
			bean.setState(History.STATE_WAIT);// 等待下载
			bean.setUserType(History.USER_TYPE_PHONE);
			bean.setProgress(0);
			bean.setThreadCount(count);
			bean.setTime(System.currentTimeMillis() + "");
			bean.setId((int) mHistoryDao.add(bean));
			mapHistorys.put(count, bean);
		}
		setHistoryCount(listDatas.size());
		sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));

		uploadHandler.obtainMessage(FTPUploaderThread.THREAD_BEGIN).sendToTarget();
	}

	public Map<String, History> receiverMaps = new HashMap<String, History>();
	private TCPFileListener fileListener;

	/**
	 * 接收文件从用户
	 * */
	private void reciverFromUser(User user, List<FileInfo> listInfos) {
		mHandler.obtainMessage(RECEIVER_FILE).sendToTarget();
		fileListener = TCPFileListener.getInstance();
		fileListener.setOnProgressUpdate(this);
		if (!fileListener.isRunning()) {
			try {
				fileListener.open();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (FileInfo fileInfo : listInfos) {
			History bean = new History();
			bean.setName(fileInfo.getName());
			bean.setSize((int) fileInfo.getSize());
			bean.setPath(fileInfo.getPath());
			bean.setUserName(user.getUserName());
			bean.setUserIp(user.getIp());
			bean.setUserType(History.USER_TYPE_PHONE);
			bean.setTransfer(History.TRANSFER_RECEIVE);
			bean.setState(History.STATE_WAIT);// 等待下载
			bean.setProgress(0);
			bean.setThreadCount(count);
			bean.setTime(System.currentTimeMillis() + "");
			bean.setId((int) mHistoryDao.add(bean));

			receiverMaps.put(user.getIp() + "" + bean.getPath(), bean);
		}
		setHistoryCount(listInfos.size());
		sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
	}

	private static final int UPDATE_HISTORY_PROGRESS = 10;

	private Handler uploadHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FTPUploaderThread.THREAD_BEGIN:
				lock.lock();
				if (count >= threadFinishedCount) {
					if (threads.get(threadFinishedCount) != null) {
						if (!threads.get(threadFinishedCount).isStarted()) {
							// 开始一个新的下载任务
							threads.get(threadFinishedCount).start();
							// 设置显示当前任务状态为正在下载
							mapHistorys.get(threadFinishedCount).setState(History.STATE_TRANSFER);
							mHistoryDao.update(mapHistorys.get(threadFinishedCount));
							// 正在下载更新下载列表ListView
							sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
						}
					} else {
						threadFinishedCount++;
						// 开始下一个任务
						uploadHandler.obtainMessage(FTPUploaderThread.THREAD_BEGIN).sendToTarget();
					}
				} else {
					// 无任务了
				}
				lock.unlock();
				break;
			case FTPUploaderThread.THREAD_FINISHED:
				lock.lock();
				if (count >= threadFinishedCount) {
					// 设置当前下载任务已完成
					if (mapHistorys.get(threadFinishedCount) != null) {
						mapHistorys.remove(threadFinishedCount);// 移除完成了的任务
						threads.remove(threadFinishedCount);// 移除完成了的线程队列
						threadFinishedCount++;
					}
					// 开始下一个任务
					Message message = new Message();
					message.what = FTPUploaderThread.THREAD_BEGIN;
					sendMessage(message);
				}
				lock.unlock();
				break;
			case FTPUploaderThread.THREAD_FAILED:
				lock.lock();
				if (count >= threadFinishedCount) {
					if (mapHistorys.get(threadFinishedCount) != null) {
						mapHistorys.get(threadFinishedCount).setState(History.STATE_FAIL);
						mHistoryDao.update(mapHistorys.get(threadFinishedCount));
						sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
					}
				}
				lock.unlock();
				break;
			case UPDATE_HISTORY_PROGRESS:
				sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
				break;
			}
		}
	};

	public void cancelShareTask(int id) {
		// TODO 取消任务
		int count = -1;
		Set<Entry<Integer, History>> set = mapHistorys.entrySet();
		for (Entry<Integer, History> entry : set) {
			History history = entry.getValue();
			if (history.getId() == id) {
				count = history.getThreadCount();
				if (history.getUserType() == History.USER_TYPE_DEVICE) {
					threads.remove(history.getThreadCount());
					if (history.getState() == History.STATE_WAIT) {
						break;
					} else if (history.getState() == History.STATE_TRANSFER) {
						mThreadHandler.obtainMessage(CANCEL_FILE).sendToTarget();
						// uploadHandler.sendEmptyMessageDelayed(FTPUploaderThread.THREAD_BEGIN,
						// 5000);
						break;
					}
				} else if (history.getUserType() == History.USER_TYPE_PHONE) {
					if (history.getTransfer() == History.TRANSFER_SEND) {
						// 发送方主动取消
						try {
							binder.sendMsg(WipicoApplication.appInstance.getMyUdpMessage(history.getName(), Listener.CANCEL_RECEIVE_FILE),
									InetAddress.getByName(history.getUserIp()));
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						threads.remove(history.getThreadCount());
						if (history.getState() == History.STATE_WAIT) {
							// 主动取消方，如果取消正在等待的任务，直接从任务队列删除
							break;
						} else if (history.getState() == History.STATE_TRANSFER) {
							// 主动取消方，如果取消正在传输的任务，则先关闭掉TCP;
							TCPFileListener.getInstance().cancelTCP();
							break;
						}
					}
				}
			}
		}
		if (count != -1) {
			mapHistorys.remove(count);
		}
	}

	/**
	 * 接收方
	 * */
	public void cancelReceiveShareTask(int id) {
		Set<Entry<String, History>> set = receiverMaps.entrySet();
		String key = "";
		for (Entry<String, History> entry : set) {
			History history = entry.getValue();
			if (history.getId() == id) {
				// 通知发送方取消，延迟删除接收文件
				key = entry.getKey();
				threads.remove(history.getThreadCount());
				try {
					binder.sendMsg(WipicoApplication.appInstance.getMyUdpMessage(history.getName(), Listener.CANCEL_SEND_FILE), InetAddress.getByName(history.getUserIp()));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				if (history.getState() == History.STATE_WAIT) {

				} else if (history.getState() == History.STATE_TRANSFER) {
					// TODO 延时删除文件
				}
			}
		}
		receiverMaps.remove(key);
	}

	/**
	 * 接收方主动取消任务，通知发送方，停止发送，同时删除接收方文件和数据库
	 * 
	 * **/
	private void cancelSendTask(String ip, String name) {
		Set<Entry<Integer, History>> set = mapHistorys.entrySet();
		int key = -1;
		for (Entry<Integer, History> entry : set) {
			History history = entry.getValue();
			if (history.getUserIp().equals(ip) && history.getName().equals(name)) {
				key = entry.getKey();
				// 删除数据库，取消发送
				threads.remove(history.getThreadCount());
				if (history.getState() == History.STATE_WAIT) {
				} else if (history.getState() == History.STATE_TRANSFER) {
					TCPFileListener.getInstance().cancelTCP();
				}
				mHistoryDao.delete(history);
				sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
			}
		}
		mapHistorys.remove(key);
	}

	/**
	 * 发送方通知接收方取消任务，接收方，删除文件及数据
	 * 
	 * */
	private void cancelReceiveTask(String ip, String path) {
		String key = ip + WipicoApplication.appInstance.getFilePath() + path;
		History history = receiverMaps.get(ip + WipicoApplication.appInstance.getFilePath() + path);
		if (history.getState() == History.STATE_WAIT) {
			mHistoryDao.delete(history);
			sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
			receiverMaps.remove(key);
		} else if (history.getState() == History.STATE_TRANSFER) {
			// 可能要删除文件，
			mHistoryDao.delete(history);
			sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
			receiverMaps.remove(key);
		}
	}

	private List<FileInfo> listTempUpload = new ArrayList<FileInfo>();

	private int shareMode = 0;

	/**
	 * 分享文件
	 * 
	 * @param path
	 *                文件路径
	 * */
	public void showShareView(List<FileInfo> listDatas, int mode) {
		shareMode = mode;
		if (shareMode == 0) {
			btnShare.setText(R.string.btn_share);
			listTempUpload = listDatas;
			layout_share.setVisibility(View.VISIBLE);
			setPopDatas();
			int[] location = new int[2];
			btnShare.getLocationInWindow(location);
			showPop(location[0]);
		} else {
			btnShare.setText(R.string.btn_share_phone);
			listTempUpload = listDatas;
			layout_share.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onReceiveFail(File file, String ip) {
		if (file != null && ip != null) {
			uploadHandler.obtainMessage(FTPUploaderThread.THREAD_FINISHED).sendToTarget();
			if (receiverMaps.get(ip + "" + file.getPath()) != null) {
				receiverMaps.get(ip + "" + file.getPath()).setState(History.STATE_FAIL);
				mHistoryDao.update(receiverMaps.get(ip + "" + file.getPath()));
				sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
				receiverMaps.remove(ip + "" + file.getPath());// 移除接收队列
			}
		}
	}

	@Override
	public void onSendFail(File file) {
		if (file != null) {
			uploadHandler.obtainMessage(FTPUploaderThread.THREAD_FINISHED).sendToTarget();
			if (mapHistorys.get(threadFinishedCount) != null) {
				mapHistorys.get(threadFinishedCount).setState(History.STATE_FAIL);
				mHistoryDao.update(mapHistorys.get(threadFinishedCount));
				sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
			}
		}
	}

	@Override
	public void onReceiveSucc(File file, String ip) {
		// 文件 接收完成
		if (receiverMaps.get(ip + "" + file.getPath()) != null) {
			receiverMaps.get(ip + "" + file.getPath()).setProgress(100);
			receiverMaps.get(ip + "" + file.getPath()).setState(History.STATE_FINISH);
			mHistoryDao.update(receiverMaps.get(ip + "" + file.getPath()));
			sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
			receiverMaps.remove(ip + "" + file.getPath());// 移除接收队列
		}
	}

	@Override
	public void onSendSucc(File file) {
		// 文件 发送完成 // 发送下载完毕的消息, 进行队列下一条任务
		uploadHandler.obtainMessage(FTPUploaderThread.THREAD_FINISHED).sendToTarget();
		if (mapHistorys.get(threadFinishedCount) != null) {
			mapHistorys.get(threadFinishedCount).setProgress(100);
			mapHistorys.get(threadFinishedCount).setState(History.STATE_FINISH);
			mHistoryDao.update(mapHistorys.get(threadFinishedCount));
			sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
		}
	}

	@Override
	public void onSendProgressIncrease(int per, String filePath) {
		// TODO 发送进度
		if (isDebug) {
			Log.i(TAG, "发送文件进度>>>>>" + per);
		}
		if (mapHistorys.get(threadFinishedCount) != null) {
			mapHistorys.get(threadFinishedCount).setProgress(per);
			if (per == 100) {
				mapHistorys.get(threadFinishedCount).setState(History.STATE_FINISH);
			} else {
				mapHistorys.get(threadFinishedCount).setState(History.STATE_TRANSFER);
			}
			mHistoryDao.update(mapHistorys.get(threadFinishedCount));
			sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
		}

	}

	@Override
	public void onReceiveProgressIncrease(int per, String filePath, String ip) {
		// TODO 接收进度
		if (isDebug) {
			Log.i(TAG, "接收文件进度>>>>>" + per + "|" + filePath);
		}
		if (receiverMaps.get(ip + "" + filePath) != null) {
			receiverMaps.get(ip + "" + filePath).setProgress(per);
			if (per == 100) {
				receiverMaps.get(ip + "" + filePath).setState(History.STATE_FINISH);
			} else {
				receiverMaps.get(ip + "" + filePath).setState(History.STATE_TRANSFER);
			}
			mHistoryDao.update(receiverMaps.get(ip + "" + filePath));
			sendBroadcast(new Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
		} else {
			// File file = new File(filePath);
			// History bean = new History();
			// bean.setName(file.getName());
			// bean.setPath(file.getPath());
			// bean.setUser("手机");
			// bean.setTransfer(History.TRANSFER_RECEIVE);
			// bean.setState(History.STATE_TRANSFER);// 等待下载
			// bean.setProgress(per);
			// bean.setTime(System.currentTimeMillis() + "");
			// bean.setId((int) mHistoryDao.add(bean));
			// receiverMaps.put(ip + "" + filePath, bean);
			// sendBroadcast(new
			// Intent(HistoryActivity.ACTION_UPDATE_HISTORY));
		}
	}

	/** ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ **/

	private Toast mToast;

	@SuppressWarnings("static-access")
	private void showToast(String value) {
		if (mToast == null) {
			mToast = new Toast(this);
		}
		mToast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();

		if (binded)
			unbindService(connection);
		stopService(new Intent(this, ChatService.class));// 局域网聊天分享
		unregisterReceiver(receiver);
		syncPlayClient.stopSync();// 取消媒体同步
		stopWipico();// 停止Wipico
		super.onDestroy();
	}

	private long exitTime;

	@Override
	public void onBackPressed() {
		// 2次返回退出
		if (layout_share.getVisibility() == View.VISIBLE) {
			hideShareView();
		} else {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), R.string.msg_exit, Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
			}
		}
	}

}
