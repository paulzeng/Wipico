package com.chinasvc.wipicophone.service;

import java.io.File;

/**
 * 进度条百分比传进UI界面的接口
 */
public interface OnProgressUpdate {
	void onSendProgressIncrease(int per, String filePath);

	void onReceiveProgressIncrease(int per, String filePath, String ip);

	void onReceiveSucc(File file, String ip);

	void onSendSucc(File file);

	void onReceiveFail(File file, String ip);

	void onSendFail(File file);

}
