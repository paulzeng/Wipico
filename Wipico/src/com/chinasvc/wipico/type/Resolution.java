package com.chinasvc.wipico.type;

/**
 * 分辨率调整配置
 * */
public class Resolution {

	/** 屏幕类型 */
	public static final String RESOLUTION_TYPE = "resolution_type";

	/** 1280*720 50HZ */
	public static final int RESOLUTION_1280_720_50HZ = 0;

	/** 1280*720 60HZ */
	public static final int RESOLUTION_1280_720_60HZ = 1;

	/** 1920*1080 50HZ */
	public static final int RESOLUTION_1920_1080_50HZ = 2;

	/** 1920*1080 60HZ */
	public static final int RESOLUTION_1920_1080_60HZ = 3;

	/** 屏幕分辨率广播Action */
	public static final String BROADCAST_ACTION_RESOLUTION = "android.intent.action.myResolutionReceiver";

}
