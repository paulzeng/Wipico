package com.chinasvc.wipico.bean;

import java.io.File;
import org.apache.commons.net.ftp.FTPFile;

/**
 * FtpFile 转 FileInfo
 * */
public class FTPFile2FileInfo {

	/**
	 * 构造方法
	 * */
	public FTPFile2FileInfo() {
	}

	/**
	 * FtpFile 转 FileInfo
	 * 
	 * @param ftpFile
	 *                ftpFile
	 * @param remotePath
	 *                工作目录
	 * @param name
	 *                文件名
	 * */
	public static FileInfo ftpFile2fileInfo(FTPFile ftpFile, String remotePath, String name) {
		FileInfo fileInfo = new FileInfo();
		String fileName = ftpFile.getName();
		if (ftpFile.getType() == 1) {
			fileInfo.setDirectory(true);
		} else {
			fileInfo.setDirectory(false);
		}
		fileInfo.setName(fileName);
		fileInfo.setSize(ftpFile.getSize());
		if (remotePath.equals("/")) {
			fileInfo.setPath(remotePath + name);
		} else {
			fileInfo.setPath(remotePath + File.separator + fileName);
		}
		fileInfo.setCanRead(true);
		return fileInfo;
	}

}
