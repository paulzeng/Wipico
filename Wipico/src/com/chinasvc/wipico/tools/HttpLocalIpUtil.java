package com.chinasvc.wipico.tools;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.conn.util.InetAddressUtils;

import com.chinasvc.wipico.client.BroadcastAddressUtil;

import android.content.Context;

/**
 * Http工具类
 * */
public class HttpLocalIpUtil {

	private Context mContext;

	private static HttpLocalIpUtil instance;

	/**
	 * 构造HttpLocalIpUtil实例
	 * */
	public HttpLocalIpUtil(Context context) {
		mContext = context;
	}

	/**
	 * 返回当前类的实例化对象
	 * 
	 * @param context
	 * */
	public static HttpLocalIpUtil getInstance(Context context) {
		if (instance == null) {
			instance = new HttpLocalIpUtil(context);
		}
		return instance;
	}

	/**
	 * 获取本地Ip地址
	 * */
	public String getLocalIpAddress() {
		String localIp = null;
		BroadcastAddressUtil broadcastService = new BroadcastAddressUtil(mContext);
		try {
			localIp = broadcastService.getLocalIp();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (localIp != null) {
			return localIp;
		}

		try {
			String ipv4;
			ArrayList<NetworkInterface> mylist = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface ni : mylist) {
				ArrayList<InetAddress> ialist = Collections.list(ni.getInetAddresses());
				for (InetAddress address : ialist) {
					if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = address.getHostAddress())) {
						return ipv4;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

}
