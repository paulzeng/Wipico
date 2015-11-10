package com.chinasvc.wipicophone.async;

import java.io.File;
import java.io.IOException;

import android.os.Handler;

import com.chinasvc.wipico.bean.FileInfo;
import com.chinasvc.wipico.file.WipicoFileClient;
import com.chinasvc.wipicophone.WipicoApplication;
import com.chinasvc.wipicophone.service.TCPFileListener;
import com.chinasvc.wipicophone.service.User;

/**
 * 队列线程上传
 * */
public class FTPUploaderThread extends Thread {

	/** 开始下载 */
	public final static int THREAD_BEGIN = 1;
	/** 下载结束 */
	public final static int THREAD_FINISHED = 2;
	/** 下载失败 */
	public final static int THREAD_FAILED = 3;

	// 是否线程已启动
	private boolean isStarted = false;
	private boolean isFinished = false;
	private Handler mHandler;

	private FileInfo bean;// 当前进行的File
	private WipicoFileClient mWipicoFileClient;

	private int type;

	public FTPUploaderThread(Handler mHandler, FileInfo bean, WipicoFileClient mWipicoFileClient) {
		this.mHandler = mHandler;
		this.bean = bean;
		this.mWipicoFileClient = mWipicoFileClient;
		type = 1;
	}

	private TCPFileListener fileListener;
	private User user;
	private File file;

	public FTPUploaderThread(User user, File file) {
		this.user = user;
		this.file = file;
		type = 2;
		fileListener = TCPFileListener.getInstance();
		if (!fileListener.isRunning()) {
			try {
				fileListener.open();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String savePath;

	public FTPUploaderThread(Handler mHandler, String savePath, FileInfo bean, WipicoFileClient mWipicoFileClient) {
		this.mHandler = mHandler;
		this.savePath = savePath;
		this.bean = bean;
		this.mWipicoFileClient = mWipicoFileClient;
		type = 3;
	}

	/** 开始下载任务 */
	@Override
	public void run() {
		if (type == 1) {
			isStarted = true;
			isFinished = false;
			if (mWipicoFileClient == null) {
				mHandler.obtainMessage(THREAD_FAILED, bean).sendToTarget();// 发送下载完毕的消息
			} else {
				mWipicoFileClient.upFolder("/sdcard/Wipico", new File(bean.getPath()));
				isFinished = true;
				mHandler.obtainMessage(THREAD_FINISHED, bean).sendToTarget();// 发送下载完毕的消息
			}
		} else if (type == 2) {
			isStarted = true;
			isFinished = false;
			if (!file.exists()) {
				// 文件不存在，发送失败
			}
			fileListener.sendFile(user.getIp(), file, WipicoApplication.appInstance.getFilePath(), null);// 发送文件
			isFinished = true;
		} else if (type == 3) {
			isStarted = true;
			isFinished = false;
			if (mWipicoFileClient == null) {
				mHandler.obtainMessage(THREAD_FAILED, bean).sendToTarget();// 发送下载完毕的消息
			} else {
				mWipicoFileClient.downFolder(savePath, new File(bean.getPath()));
				isFinished = true;
				mHandler.obtainMessage(THREAD_FINISHED, bean).sendToTarget();// 发送下载完毕的消息
			}
		}
	}

	public boolean isStarted() {
		return isStarted;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

}
