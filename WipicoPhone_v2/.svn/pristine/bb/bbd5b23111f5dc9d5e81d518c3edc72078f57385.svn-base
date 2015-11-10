package com.chinasvc.wipicophone.service;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class TCPListener extends Listener {

	private int port;
	private boolean go;
	// 用来接收数据
	private ServerSocket server;
	// 标识是否开启
	private boolean running;

	/**
	 * 初始化操作 设置保存路径 端口初始化
	 */
	abstract void init();

	private void createServer() throws IOException {
		init();
		server = new ServerSocket(port);
		go = true;
		running = true;
		start();
	}

	@Override
	public void run() {
		while (go) {
			try {
				onReceiveData(server.accept());
			} catch (IOException e) {
				e.printStackTrace();
				noticeReceiveError(null, null);
			}
		}
		running = false;
		try {
			if (server != null)
				server.close();
			server = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract void onReceiveData(Socket socket) throws IOException;

	/** 通知用户接收文件出错 */
	public abstract void noticeReceiveError(File file, String ip);

	/** 通知用户发送文件出错 */
	public abstract void noticeSendFileError(File file);

	@Override
	public void open() throws IOException {
		createServer();
		setPriority(MAX_PRIORITY);
	}

	@Override
	public void close() throws IOException {
		go = false;
		running = false;
		interrupt();
		if (server != null)
			server.close();
		server = null;
	}

	public boolean isRunning() {
		return running;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
