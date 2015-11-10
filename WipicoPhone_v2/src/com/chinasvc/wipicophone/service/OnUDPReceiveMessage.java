package com.chinasvc.wipicophone.service;

/**
 * 接收消息监听接口
 * */
public interface OnUDPReceiveMessage {

	/** 当有数据被放进消息队列后的回调方法 */
	void onReceive(int type);

	void onReceive(int type, UDPMessage udpMessage);

	/** 发送失败 */
	void sendFailure();

}
