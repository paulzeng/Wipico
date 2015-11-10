package com.chinasvc.wipico.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

import android.util.Log;

import com.chinasvc.wipico.bean.Device;
import com.chinasvc.wipico.bean.FTPFile2FileInfo;
import com.chinasvc.wipico.bean.FileInfo;
import com.chinasvc.wipico.util.FileUtil;

/**
 * Wipico 文件管理客户端
 * 
 * @since 1.0.0
 * */
public class WipicoFileClient {

	private String TAG = "WipicoFileClient";

	private FTPClient ftpClient;

	private String server;
	private int port;
	private String userName;
	private String userPassword;

	/** 上传下载的当前传输大小 */
	public static long currentSize = 0;

	private FileExceptionListener exceptionListener;

	private final static String FILE_USER_NAME = "csvc";
	private final static String FILE_PASSWORD = "admin";
	private final static int FILE_PORT = 32221;

	/**
	 * 构造WipicoFileClient实例
	 * 
	 * @param device
	 *                所连接的设备
	 * */
	public WipicoFileClient(Device device) {
		this.server = device.getDeviceIp();
		this.port = FILE_PORT;
		this.userName = FILE_USER_NAME;
		this.userPassword = FILE_PASSWORD;
		ftpClient = new FTPClient();
	}

	/**
	 * 判断是否连接
	 * */
	public boolean isConnect() {
		if (ftpClient != null && ftpClient.isConnected()) {
			int reply;
			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				return false;
			} else {
				return true;
			}
		} else {
			ftpClient = new FTPClient();
			return false;
		}
	}

	/**
	 * 链接文件服务器
	 * 
	 * @return 是否连接成功
	 */
	public boolean connect() {
		try {
			int reply;
			if (port > 0) {
				ftpClient.connect(server, port);
			} else {
				// 默认端口号
				ftpClient.connect(server);
			}
			ftpClient.login(userName, userPassword);// 登录
			ftpClient.enterLocalPassiveMode();// 被动模式
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setBufferSize(1024 * 1024);

			ftpClient.setCopyStreamListener(new CopyStreamListener() {
				@Override
				public void bytesTransferred(long total, int bufferSize, long arg2) {
					currentSize += bufferSize;
					if (fileTransferListener != null) {
						fileTransferListener.transfer(total, bufferSize);
					}
				}

				@Override
				public void bytesTransferred(CopyStreamEvent arg0) {
					// TODO Auto-generated method stub
				}
			});

			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				// 服务器拒绝访问
				if (exceptionListener != null) {
					exceptionListener.refused();
				}
				disconnect();
				return false;
			} else {
				return true;
			}
		} catch (IOException e) {
			if (exceptionListener != null) {
				exceptionListener.exception();
			}
			disconnect();
			e.printStackTrace();
			return false;
		}
	}

	private FileInputStream mFileInputStream;

	public void cancel() {
		if (!isConnect()) {
			// 未连接成功了
			connect();
		}
		try {
			if (mFileInputStream != null) {
				mFileInputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * 上传文件或者文件夹
	 * 
	 * @param remotePath
	 *                服务器要保存的目录
	 * @param file
	 *                要上传的文件,本地路径
	 * @return 是否成功
	 * */
	public boolean upFolder(String remotePath, File file) {
		String fileName = file.getName();
		String filePath = file.getPath();
		if (!isConnect()) {
			// 未连接成功了
			connect();
		}
		if (file.isDirectory()) {
			try {
				ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(), "iso-8859-1"));
				ftpClient.makeDirectory(new String(fileName.getBytes(), "iso-8859-1"));
				String[] files = file.list();
				ftpClient.changeWorkingDirectory(new String((remotePath + File.separator + fileName).getBytes(), "iso-8859-1"));
				for (int i = 0; i < files.length; i++) {
					File file1 = new File(filePath + File.separator + files[i]);
					if (file1.isDirectory()) {
						// 是目录，递归调用
						String newRemotePath = remotePath + File.separator + fileName;
						upFolder(newRemotePath, file1);
						ftpClient.changeWorkingDirectory(new String((remotePath + File.separator + fileName).getBytes(), "iso-8859-1"));
					} else {
						// 是文件直接上传
						File file2 = new File(filePath + File.separator + files[i]);
						FileInputStream in = new FileInputStream(file2);
						mFileInputStream = in;
						ftpClient.storeFile(new String(file2.getName().getBytes(), "iso-8859-1"), in);
						in.close();
						mFileInputStream = null;
					}
				}
				return true;
			} catch (IOException e) {
				if (exceptionListener != null) {
					exceptionListener.exception();
				}
				e.printStackTrace();
				return false;
			}
		} else {
			try {
				File file2 = new File(file.getPath());
				ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(), "iso-8859-1"));
				FileInputStream input = new FileInputStream(file2);
				mFileInputStream = input;
				ftpClient.storeFile(new String(file2.getName().getBytes(), "iso-8859-1"), input);
				input.close();
				mFileInputStream = null;
				return true;
			} catch (UnsupportedEncodingException e) {
				if (exceptionListener != null) {
					exceptionListener.exception();
				}
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				if (exceptionListener != null) {
					exceptionListener.exception();
				}
				e.printStackTrace();
				return false;
			}
		}
	}

	/***
	 * 下载文件
	 * 
	 * @param remotePath
	 *                服务器要保存的目录
	 * @param file
	 *                要下载的文件
	 * @return 是否成功
	 * */
	public boolean downFolder(String localPath, File file) {
		if (!isConnect()) {
			// 未连接成功了
			connect();
		}
		try {
			File myFlie = new File(file.getPath());
			ftpClient.changeWorkingDirectory(new String(myFlie.getParent().getBytes(), "iso-8859-1"));// 转移到FTP服务器目录
			File localFile = new File(localPath + File.separator + file.getName());
			FileOutputStream is = new FileOutputStream(localFile);
			ftpClient.retrieveFile(new String(file.getName().getBytes(), "iso-8859-1"), is);
			is.close();
			return true;
		} catch (IOException e) {
			if (exceptionListener != null) {
				exceptionListener.exception();
			}
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 返回FTP目录下的文件列表
	 * 
	 * @param ftpDirectory
	 * @return
	 */
	public ArrayList<FileInfo> getFileList(String remotePath) {
		ArrayList<FileInfo> listInfos = new ArrayList<FileInfo>();
		if (!isConnect()) {
			// 未连接成功了
			connect();
		}
		try {
			ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(), "iso-8859-1"));// 转移到FTP服务器目录
			FTPFile[] listFiles = ftpClient.listFiles();
			for (FTPFile ftpFile : listFiles) {
				listInfos.add(FTPFile2FileInfo.ftpFile2fileInfo(ftpFile, remotePath, ftpFile.getName()));
			}
		} catch (Exception e) {
			exceptionListener.exception();
			e.printStackTrace();
		}
		return listInfos;
	}

	/**
	 * 返回FTP 根目录下的SD卡，TF卡，USB 列表
	 * 
	 * @param ftpDirectory
	 * @return
	 */
	public ArrayList<FileInfo> getFileRootList(String remotePath, String tfPathName, String sdcardName, String usbPathName) {
		ArrayList<FileInfo> listInfos = new ArrayList<FileInfo>();
		if (!isConnect()) {
			// 未连接成功了
			connect();
		}
		try {
			ftpClient.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] listFiles = ftpClient.listFiles();
			for (FTPFile ftpFile : listFiles) {
				String name = ftpFile.getName();
				if (name.equalsIgnoreCase("extsd")) {
					ftpFile.setName(tfPathName);
					listInfos.add(FTPFile2FileInfo.ftpFile2fileInfo(ftpFile, remotePath, name));
				} else if (name.equalsIgnoreCase("sdcard")) {
					ftpFile.setName(sdcardName);
					listInfos.add(FTPFile2FileInfo.ftpFile2fileInfo(ftpFile, remotePath, name));
				} else if (name.equalsIgnoreCase("usbhost1")) {
					ftpFile.setName(usbPathName);
					listInfos.add(FTPFile2FileInfo.ftpFile2fileInfo(ftpFile, remotePath, name));
				}
			}
		} catch (Exception e) {
			exceptionListener.exception();
			e.printStackTrace();
		}
		return listInfos;
	}

	/**
	 * 获取Ftp服务器下remotePath文件内的全部Office文件
	 * 
	 * @param remotePath
	 *                /sdcard/ppt 根目录默认为/mnt
	 * @return
	 */
	public ArrayList<FileInfo> getOfficeList(String remotePath) {
		ArrayList<FileInfo> listInfos = new ArrayList<FileInfo>();
		if (!isConnect()) {
			// 未连接成功了
			connect();
		}
		try {
			ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(), "iso-8859-1"));// 转移到FTP服务器目录
			FTPFile[] listFiles = ftpClient.listFiles();
			for (FTPFile ftpFile : listFiles) {
				String fileName = ftpFile.getName();
				fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
				if (ftpFile.isDirectory() || FileUtil.isOfficeFile(fileName)) {
					listInfos.add(FTPFile2FileInfo.ftpFile2fileInfo(ftpFile, remotePath, ftpFile.getName()));
				}
			}
		} catch (Exception e) {
			exceptionListener.exception();
			e.printStackTrace();
		}
		return listInfos;
	}

	/**
	 * 获取服务器下remotePath文件内的全部Image文件
	 * 
	 * @param remotePath
	 *                /sdcard/ppt 根目录默认为/mnt
	 * @return
	 */
	public ArrayList<FileInfo> getImageList(String remotePath) {
		ArrayList<FileInfo> listInfos = new ArrayList<FileInfo>();
		if (!isConnect()) {
			// 未连接成功了
			connect();
		}
		try {
			ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(), "iso-8859-1"));// 转移到FTP服务器目录
			FTPFile[] listFiles = ftpClient.listFiles();
			for (FTPFile ftpFile : listFiles) {
				if (ftpFile.isDirectory() || FileUtil.isPicture(FileUtil.getFileExtension(ftpFile.getName()))) {
					listInfos.add(FTPFile2FileInfo.ftpFile2fileInfo(ftpFile, remotePath, ftpFile.getName()));
				}
			}
		} catch (Exception e) {
			exceptionListener.exception();
			e.printStackTrace();
		}
		return listInfos;
	}

	/**
	 * 获取服务器下remotePath文件内的全部Image文件
	 * 
	 * @param remotePath
	 *                /sdcard/ppt 根目录默认为/mnt
	 * @return
	 */
	public ArrayList<FileInfo> getAudioOrVideoList(String remotePath) {
		ArrayList<FileInfo> listInfos = new ArrayList<FileInfo>();
		if (!isConnect()) {
			// 未连接成功了
			connect();
		}
		try {
			ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(), "iso-8859-1"));// 转移到FTP服务器目录
			FTPFile[] listFiles = ftpClient.listFiles();
			for (FTPFile ftpFile : listFiles) {
				if (ftpFile.isDirectory() || FileUtil.isAudio(FileUtil.getFileExtension(ftpFile.getName())) || FileUtil.isVideo((FileUtil.getFileExtension(ftpFile.getName())))) {
					listInfos.add(FTPFile2FileInfo.ftpFile2fileInfo(ftpFile, remotePath, ftpFile.getName()));
				}
			}
		} catch (Exception e) {
			exceptionListener.exception();
			e.printStackTrace();
		}
		return listInfos;
	}

	/**
	 * 获取服务器下remotePath文件内的全部Audio文件
	 * 
	 * @param remotePath
	 *                /sdcard/ppt 根目录默认为/mnt
	 * @return
	 */
	public ArrayList<FileInfo> getAudioList(String remotePath) {
		ArrayList<FileInfo> listInfos = new ArrayList<FileInfo>();
		if (!isConnect()) {
			// 未连接成功了
			connect();
		}
		try {
			ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(), "iso-8859-1"));// 转移到FTP服务器目录
			FTPFile[] listFiles = ftpClient.listFiles();
			for (FTPFile ftpFile : listFiles) {
				if (ftpFile.isDirectory() || FileUtil.isAudio(FileUtil.getFileExtension(ftpFile.getName()))) {
					listInfos.add(FTPFile2FileInfo.ftpFile2fileInfo(ftpFile, remotePath, ftpFile.getName()));
				}
			}
		} catch (Exception e) {
			exceptionListener.exception();
			e.printStackTrace();
		}
		return listInfos;
	}

	/**
	 * 获取服务器下remotePath文件内的全部Video文件
	 * 
	 * @param remotePath
	 *                /sdcard/ppt 根目录默认为/mnt
	 * @return
	 */
	public ArrayList<FileInfo> getVideoList(String remotePath) {
		ArrayList<FileInfo> listInfos = new ArrayList<FileInfo>();
		if (!isConnect()) {
			// 未连接成功了
			connect();
		}
		try {
			ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(), "iso-8859-1"));// 转移到FTP服务器目录
			FTPFile[] listFiles = ftpClient.listFiles();
			for (FTPFile ftpFile : listFiles) {
				if (ftpFile.isDirectory() || FileUtil.isVideo(FileUtil.getFileExtension(ftpFile.getName()))) {
					listInfos.add(FTPFile2FileInfo.ftpFile2fileInfo(ftpFile, remotePath, ftpFile.getName()));
				}
			}
		} catch (Exception e) {
			exceptionListener.exception();
			e.printStackTrace();
		}
		return listInfos;
	}

	/**
	 * 是否存在某个文件
	 * 
	 * @param filePath
	 *                文件路径
	 * */
	public boolean isExistFile(String filePath) {
		if (!isConnect()) {
			// 未连接成功了
			connect();
		}
		File file = new File(filePath);
		FTPFile[] ftpFiles;
		try {
			ftpClient.changeWorkingDirectory(new String(file.getParent().getBytes(), "iso-8859-1"));
			ftpFiles = ftpClient.listFiles(file.getParent());
			for (FTPFile ftpFile : ftpFiles) {
				if (ftpFile.getName().equals(file.getName())) {
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			if (exceptionListener != null) {
				exceptionListener.exception();
			}
			e.printStackTrace();
			return false;
		}
	}

	public boolean creatFile(String ftpFilePath) {
		if (!isConnect()) {
			// 未连接成功了
			connect();
		}
		try {
			File file = new File(ftpFilePath);
			ftpClient.makeDirectory(new String(file.getName().getBytes(), "iso-8859-1"));
			return true;
		} catch (IOException e) {
			exceptionListener.exception();
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 关闭连接
	 */
	public void disconnect() {
		try {
			if (ftpClient != null && ftpClient.isConnected())
				ftpClient.logout();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ftpClient != null) {
				try {
					ftpClient.disconnect();
					ftpClient = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 设置异常监听接口
	 * */
	public void setFileExceptionListener(FileExceptionListener exceptionListener) {
		this.exceptionListener = exceptionListener;
	}

	/**
	 * 文件管理服务连接异常接口
	 * */
	public interface FileExceptionListener {
		/**
		 * 连接异常
		 * */
		public void exception();

		/**
		 * 拒绝连接
		 * */
		public void refused();
	}

	private FileTransferListener fileTransferListener;

	/**
	 * 设置异常监听接口
	 * */
	public void setFileTransferListener(FileTransferListener fileTransferListener) {
		this.fileTransferListener = fileTransferListener;
	}

	/**
	 * 传输进度监听
	 * */
	public interface FileTransferListener {
		/**
		 * 传输进度
		 * */
		public void transfer(long total, int bufferSize);

	}

}
