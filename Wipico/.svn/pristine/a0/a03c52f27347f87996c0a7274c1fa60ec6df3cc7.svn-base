package com.chinasvc.wipico.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.chinasvc.wipico.WipicoClient;
import com.chinasvc.wipico.bean.Device;
import com.chinasvc.wipico.util.DeviceUtil;
import com.chinasvc.wipico.util.WifiAdmin;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.util.Log;

/**
 * 扫描设备线程类
 * 
 * @since 1.0.0
 * */
public class Discovery extends Thread {

	private boolean isDebug = false;
	private String TAG = "Discovery";

	/** 扫描设备的端口号 */
	private final static int SCANNING_EQUIPMENT_PORT = 34341;
	private final static String DISCOVERY_REQ = "pico:requ";
	private final static String DISCOVERY_RESP = "pico:live";

	private DatagramSocket sendSocket;
	private DatagramPacket sendPacket;
	private DatagramPacket recePacket;
	private InetAddress destIPAddress;

	private List<Device> listDevices = new ArrayList<Device>();
	private List<Device> listWifiDevices = new ArrayList<Device>();

	private boolean running = true;
	private boolean isSend;
	private int receive;// 最大接收数量

	private int default_bufferSize = 50;
	private byte[] recedata = new byte[default_bufferSize];

	private ScanListener mScanListener;

	private WipicoClient mWipicoClient;

	private Context mContext;

	private int wipicoVersion;

	/**
	 * 构造Discovery实例
	 * 
	 * @param context
	 *                Context
	 * @param wipicoClient
	 *                Wipico 客户端
	 * @param wipicoVersion
	 *                Wipico 版本
	 * */
	public Discovery(String ip, Context context, WipicoClient wipicoClient, int wipicoVersion) {
		mWipicoClient = wipicoClient;
		listDevices = new ArrayList<Device>();
		mContext = context;
		this.wipicoVersion = wipicoVersion;
		try {
			this.destIPAddress = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (destIPAddress != null) {
			sendPacket = new DatagramPacket(DISCOVERY_REQ.getBytes(), DISCOVERY_REQ.getBytes().length, destIPAddress, SCANNING_EQUIPMENT_PORT);
			recePacket = new DatagramPacket(recedata, recedata.length, destIPAddress, SCANNING_EQUIPMENT_PORT);
		}
	}

	/**
	 * 初始化线程类
	 * 
	 * @param ip
	 *                广播地址
	 * */
	public void initialization(String ip) {
		listDevices = new ArrayList<Device>();
		try {
			this.destIPAddress = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (destIPAddress != null) {
			sendPacket = new DatagramPacket(DISCOVERY_REQ.getBytes(), DISCOVERY_REQ.getBytes().length, destIPAddress, SCANNING_EQUIPMENT_PORT);
			recePacket = new DatagramPacket(recedata, recedata.length, destIPAddress, SCANNING_EQUIPMENT_PORT);
		}
	}

	/**
	 * 关闭扫描设备
	 * */
	public void closeScan() {
		stopMulticast();// 关闭组播
		try {
			if (isDebug)
				Log.i(TAG, "关闭扫描" + Thread.currentThread());
			running = false;
			join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开始扫描设备
	 * */
	public void startScan() {
		startMulticast();// 开启组播
		if (isDebug)
			Log.i(TAG, "开始扫描" + Thread.currentThread());
		running = true;
		satus = -1;
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (running) {
			if (isDebug)
				Log.i(TAG, "5s扫描设备>>>>>>>>>>>>" + Thread.currentThread());
			receive = 50;// 最大接收数量
			isSend = false;
			String recestr = null;
			try {
				listDevices.clear();
				sendSocket = new DatagramSocket();
				sendSocket.setBroadcast(true);
				sendSocket.setSoTimeout(3000);
				sendSocket.send(sendPacket);
				while (receive-- > 0) {
					sendSocket.receive(recePacket);
					recestr = new String(recedata, recePacket.getOffset(), recePacket.getLength());
					if (recestr.startsWith(DISCOVERY_RESP)) {
						Device device = new Device();
						device.setDeviceName(recestr.substring(9));
						device.setDeviceIp(recePacket.getAddress().getHostAddress());
						if (listDevices.indexOf(device) < 0) {
							listDevices.add(device);
							if (device.getDeviceIp().equals("10.8.8.1")) {
								isSend = true;
								if (isDebug)
									Log.i(TAG, "扫描到10.8.8.1" + listDevices + "|" + Thread.currentThread());
								sendResult();
								break;
							}
						}
					}

					// 每次接收完UDP数据后，重置长度。否则可能会导致下次收到数据包被截断。
					recePacket.setLength(default_bufferSize);
				}
			} catch (SocketTimeoutException e) {
				if (isDebug)
					Log.i(TAG, "扫描3s超时" + listDevices + "|" + Thread.currentThread());
				if (!isSend) {
					sendResult();
				}
			} catch (SocketException e) {
				receive = 0;
				if (isDebug)
					Log.i(TAG, "SocketException>>>>>>" + Thread.currentThread());
				if (mScanListener != null) {
					mScanListener.scanException();
				}
			} catch (IOException e) {
				receive = 0;
				if (isDebug)
					Log.i(TAG, "SocketException>>>>>>" + Thread.currentThread());
				if (mScanListener != null) {
					mScanListener.scanException();
				}
			}
			sendSocket.close();

			if (wipicoVersion >= 2) {
				// 版本2才进行SSID 扫描
				List<ScanResult> listScanResults = WifiAdmin.getInstance(mContext).getScanResults();
				if (listScanResults != null) {
					for (ScanResult bean : listScanResults) {
						if (DeviceUtil.isMatchSSID(DeviceUtil.ssidCorrect(bean.SSID))) {
							listWifiDevices.add(new Device(DeviceUtil.ssidCorrect(bean.SSID), bean.capabilities, null));
						}
					}
				}
				sendWifiResult();
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置扫描监听接口
	 * 
	 * @param scanListener
	 *                扫描回调接口
	 * */
	public void setOnScanListener(ScanListener scanListener) {
		this.mScanListener = scanListener;
	}

	private List<Device> preListDatas;

	private int satus = -1;

	private void sendResult() {
		if (mScanListener != null) {
			listDevices = fixListDevices(listDevices, listMulticasts);
			if (preListDatas != null && !preListDatas.equals(listDevices)) {
				preListDatas.clear();
				preListDatas.addAll(listDevices);
				mScanListener.onResult(listDevices);
			} else if (preListDatas == null) {
				preListDatas = new ArrayList<Device>();
				preListDatas.addAll(listDevices);
				mScanListener.onResult(listDevices);
			}
			if (satus != DeviceUtil.checkDevice(mWipicoClient.getDevice(), listDevices)) {
				// 检查是否掉线
				satus = DeviceUtil.checkDevice(mWipicoClient.getDevice(), listDevices);
				mScanListener.deviceStatus(satus);
			}
		}
	}

	private MulticastThread mMulticastThread;

	private List<Device> listMulticasts = new ArrayList<Device>();

	private void startMulticast() {
		listMulticasts = new ArrayList<Device>();
		if (mMulticastThread != null) {
			mMulticastThread.closeThread();
			mMulticastThread = null;
		}
		mMulticastThread = new MulticastThread();
		mMulticastThread.startThread();
	}

	private void stopMulticast() {
		if (mMulticastThread != null) {
			mMulticastThread.closeThread();
			mMulticastThread = null;
		}
	}

	/** 组播线程 */
	private class MulticastThread extends Thread {

		private MulticastLock multicastLock;
		private static final String TAG = "MulticastThread";
		private static final int MULTICAST_PORT = 32345;
		private static final String GROUP_IP = "226.0.0.36";
		private boolean isRunning = true;

		public void closeThread() {
			isRunning = false;
			try {
				join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public void startThread() {
			isRunning = true;
			start();
		}

		@Override
		public void run() {
			WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
			multicastLock = wifiManager.createMulticastLock("multicast.test");
			multicastLock.acquire();
			try {
				findServerIpAddress();
			} catch (IOException e) {
				e.printStackTrace();
			}
			multicastLock.release();
		}

		public String findServerIpAddress() throws IOException {
			String deviceName = null;
			MulticastSocket multicastSocket = new MulticastSocket(MULTICAST_PORT);
			multicastSocket.setLoopbackMode(true);
			InetAddress group = InetAddress.getByName(GROUP_IP);
			multicastSocket.joinGroup(group);
			multicastSocket.setSoTimeout(5000);
			DatagramPacket packet;
			while (isRunning) {
				List<Device> lists = new ArrayList<Device>();
				try {
					byte[] receiveData = new byte[64];
					packet = new DatagramPacket(receiveData, receiveData.length);
					multicastSocket.receive(packet);
					String deviceIp = packet.getAddress().toString();
					deviceIp = deviceIp.substring(1, deviceIp.length());// ip地址
					StringBuilder packetContent = new StringBuilder();
					for (int i = 0; i < receiveData.length; i++) {
						if (receiveData[i] == 0) {
							break;
						}
						packetContent.append((char) receiveData[i]);// 拼接设备名称
					}
					deviceName = packetContent.toString();// 设备名称
					Device device = new Device();
					device.setDeviceName(deviceIp);
					device.setDeviceIp(deviceName);
					lists.add(device);
					if (deviceName.equals(deviceIp)) {
						// find server ip address:
						// deviceName
						break;
					} else {
						// not find server ip address,
						// continue …
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				} catch (SocketTimeoutException e) {
					if (isDebug) {
					}
					listMulticasts.clear();
					listMulticasts.addAll(lists);
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return deviceName;
		}
	}

	/**
	 * 组播/广播数据合成
	 * */
	private List<Device> fixListDevices(List<Device> listBroadcasts, List<Device> listMulticasts) {
		if (isDebug) {
			Log.i(TAG, "组播/广播数据合成>>>>>" + listBroadcasts + "\n" + listMulticasts);
		}
		for (Device device : listMulticasts) {
			if (!listBroadcasts.contains(device)) {
				listBroadcasts.add(device);
			}
		}
		return listBroadcasts;
	}

	private List<Device> preWifiListDatas;

	/** 发送扫描结果 */
	private void sendWifiResult() {
		if (mScanListener != null) {
			if (preWifiListDatas != null && !preWifiListDatas.equals(listWifiDevices)) {
				preWifiListDatas.clear();
				preWifiListDatas.addAll(listWifiDevices);
				mScanListener.onWifiResult(listWifiDevices);
			} else if (preWifiListDatas == null) {
				preWifiListDatas = new ArrayList<Device>();
				preWifiListDatas.addAll(listWifiDevices);
				mScanListener.onWifiResult(listWifiDevices);
			}
			listWifiDevices.clear();
		}
	}
}
