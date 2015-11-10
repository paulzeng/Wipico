package com.chinasvc.wipicophone.service;

public class Config {

	public static final String ENCOD = "utf-8";
	public static final String ALL_ADDRESS = "255.255.255.255";

	/** 文本消息端口 */
	public static final int MESSAGE_PORT = 32483;
	/** 文件消息端口 */
	public static final int FILE_PORT = 32486;
	/** 语音消息端口 */
	public static final int VOICE_PORT = 32483;
	/** 视屏流端口 */
	public static final int VIDEO_PORT = 32483;
	/** 局域网内所有ip */


	/** 广播Action */

	/** 其他用户上线，添加用户 */
	public static final String ACTION_ADD_USER = "com.chinasvc.ACTION_ADD_USER";
	/** 自己登陆成功，用户列表更新 */
	public static final String ACTION_LOGIN_SUCC = "com.chinasvc.ACTION_LOGIN_SUCC";
	/** 其他用户下线，用户列表更新 */
	public static final String ACTION_REMOVE_USER = "com.chinasvc.ACTION_REMOVE_USER";

	public static final String ACTION_HEARTBEAT = "com.chinasvc.ACTION_HEARTBEAT";// 心跳
	public static final String ACTION_NOTIFY_DATA = "com.chinasvc.ACTION_NOTIFY_DATA";// 请求文件发送

}
