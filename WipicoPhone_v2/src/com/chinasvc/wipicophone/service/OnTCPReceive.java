package com.chinasvc.wipicophone.service;

import java.io.File;

public interface OnTCPReceive {
	/** 接收到文件 的回调方法 */
	void onReceiveFileSucc(File file, String ip);

	/** 发送文件 的回调方法 */
	void onSendFileSucc(File file);

	/** 返回发送文件进度条的百分比 */
	void onSendProgressIncrease(int percent, String filePath);

	/** 返回接收进度条的百分比 */
	void onReceiveProgressIncrease(int percent, String filePath, String ip);
}
