package com.chinasvc.wipico.type;

/**
 * 鼠标配置
 * */
public class Mouse {

	/** Mouse wheel */
	public static final int MOUSE_WHEEL = 0x488C;

	/** Mouse move */
	public static int MOUSE_MOVE = 0x4889;

	public static final int MOUSE_LEFT_CLICK = 0;// 左键单击
	public static final int MOUSE_LEFT_LONG_CLICK = 1;// 左键长按
	public static final int MOUSE_LEFT_DOUBLE_CLICK = 2;// 左键双击
	/** 左键单击 */
	public static final int MOUSE_LEFT = 0x488A;
	/** 左键长按 */
	public static final int MOUSE_LEFTLONG = 0x4892;
	/** 右键键单击 */
	public static final int MOUSE_RIGHT = 0x488B;
	/** 中间键点击 */
	public static final int MOUSE_MIDKEY = 0x488D;

}
