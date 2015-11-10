package com.chinasvc.wipicophone.service;

import java.io.IOException;

public abstract class Listener extends Thread {

	/** 没有动作 */
	public static final int NONE = 0;
	/** 增加用户 */
	public static final int ADD_USER = 1;
	/** 增加用户成功 */
	public static final int LOGIN_SUCC = 2;
	/** 删除用户 */
	public static final int REMOVE_USER = 3;
	/** 接收消息 */
	public static final int RECEIVE_MSG = 4;
	/** 接收文件 */
	public static final int RECEIVE_FILE = 5;
	/** 发送心跳包 */
	public static final int HEART_BEAT = 6;
	/** 心跳包回复 */
	public static final int HEART_BEAT_REPLY = 7;
	/** 请求发送文件 */
	public static final int ASK_SEND_FILE = 8;
	/** 回复请求发送文件 */
	public static final int REPLAY_SEND_FILE = 9;
	/** 请求发送头像 */
	public static final int REQUIRE_ICON = 10;
	/** 请求视屏聊天 */
	public static final int ASK_VIDEO = 11;
	/** 接受请求视屏聊天 */
	public static final int REPLAY_VIDEO_ALLOW = 12;
	/** 拒绝请求视屏聊天 */
	public static final int REPLAY_VIDEO_NOT_ALLOW = 13;
	/** 发送所有在线群聊信息 */
	public static final int TO_ALL_MESSAGE = 14;
	/** 取消接收文件传输 */
	public static final int CANCEL_RECEIVE_FILE = 15;
	/** 取消发送文件传输 */
	public static final int CANCEL_SEND_FILE = 16;

	/** 打开监听器 */
	abstract void open() throws IOException;

	/** 关闭监听器 */
	abstract void close() throws IOException;
}
