package com.chinasvc.wipicophone.bean;

public class History {

	private int id;
	private String name;// 文件传输前的原名
	private String path;// 文件路径
	private int size;// 文件大小
	private int type;// 文件类型
	private String time;// 传输时间
	private String userName;// 用户
	private String userIp;// 用户
	private int state;// 传输状态，完成，未完成
	private int transfer;// 传输模式：发送或者接收
	private int userType;//
	private int progress;
	private int threadCount;// 线程计数器

	public static final int USER_TYPE_PHONE = 1;
	public static final int USER_TYPE_DEVICE = 2;

	public static final int TRANSFER_SEND = 0;
	public static final int TRANSFER_RECEIVE = 1;

	public static final int STATE_WAIT = 0;// 传输未完成
	public static final int STATE_TRANSFER = 1;// 传输中
	public static final int STATE_FINISH = 2;// 传输完成
	public static final int STATE_FAIL = 3;// 传输失败

	public static final int TYPE_IMAGE = 0;// 图片文件
	public static final int TYPE_VIDEO = 1;// 视频文件
	public static final int TYPE_AUDIO = 2;// 音频文件
	public static final int TYPE_OFFICE = 3;// Office文件

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getTransfer() {
		return transfer;
	}

	public void setTransfer(int transfer) {
		this.transfer = transfer;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	@Override
	public String toString() {
		return "History [id=" + id + ", name=" + name + ", path=" + path + ", size=" + size + ", type=" + type + ", time=" + time + ", userName=" + userName + ", userIp=" + userIp
				+ ", state=" + state + ", transfer=" + transfer + ", userType=" + userType + ", progress=" + progress + ", threadCount=" + threadCount + "]";
	}

}
