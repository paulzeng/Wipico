package com.chinasvc.wipico.bean;

/**
 * 手机移动设备，服务器取得的当前所连接的手机设备的信息
 * 
 * @since 2.0
 * */
public class PhoneDevice {

	/** 设备名称 */
	private String deviceName;

	/** 设备Ip */
	private String deviceIp;

	/**
	 * 构造PhoneDevice实例
	 * */
	public PhoneDevice() {
	}

	/**
	 * 构造PhoneDevice实例
	 * 
	 * @param deviceName
	 *                设备名称
	 * @param deviceIp
	 *                设备ip
	 * */
	public PhoneDevice(String deviceName, String deviceIp) {
		this.deviceName = deviceName;
		this.deviceIp = deviceIp;
	}

	/** 获取设备名称 */
	public String getDeviceName() {
		return deviceName;
	}

	/** 设置设备名称 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/** 获取设备IP */
	public String getDeviceIp() {
		return deviceIp;
	}

	/** 设置设备ip */
	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceIp == null) ? 0 : deviceIp.hashCode());
		result = prime * result + ((deviceName == null) ? 0 : deviceName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhoneDevice other = (PhoneDevice) obj;
		if (deviceIp == null) {
			if (other.deviceIp != null)
				return false;
		} else if (!deviceIp.equals(other.deviceIp))
			return false;
		if (deviceName == null) {
			if (other.deviceName != null)
				return false;
		} else if (!deviceName.equals(other.deviceName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PhoneDevice [deviceName=" + deviceName + ", deviceIp=" + deviceIp + "]";
	}

}
