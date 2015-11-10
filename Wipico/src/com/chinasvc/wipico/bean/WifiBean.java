package com.chinasvc.wipico.bean;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

/**
 * Wifi 列表 Bean 用于IOS请求Wifi列表返回用
 * */
public class WifiBean {

	/** SSID */
	private String SSID;
	/** 加密方式 */
	private String capabilities;

	public WifiBean(String ssid, String capabilities) {
		this.SSID = ssid;
		this.capabilities = capabilities;
	}

	public WifiBean(JSONObject object) throws JSONException {
		SSID = new String(Base64.decode(object.getString("SSID").getBytes(), Base64.DEFAULT));
		capabilities = new String(Base64.decode(object.getString("capabilities").getBytes(), Base64.DEFAULT));
	}

	public String getSSID() {
		return SSID;
	}

	public void setSSID(String ssid) {
		SSID = ssid;
	}

	public String getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(String capabilities) {
		this.capabilities = capabilities;
	}

	/**
	 * 采用JSONObject数据结构
	 */
	public String toString() {
		JSONObject object = new JSONObject();
		try {
			object.put("SSID", Base64.encodeToString(SSID.getBytes(), Base64.DEFAULT));
			object.put("capabilities", Base64.encodeToString(capabilities.getBytes(), Base64.DEFAULT));
			return object.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

}
