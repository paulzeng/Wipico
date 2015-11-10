package com.chinasvc.wipicophone.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.chinasvc.wipico.bean.FileInfo;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

/*文件工具类*/
@SuppressLint({ "SdCardPath", "DefaultLocale" })
public class FileUtil {
	private static final String TAG = "FileUtil";

	/**
	 * 外部存储是否可用
	 */
	public static boolean isExternalStorageAvailable() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	/**
	 * 获取外置SD卡路径
	 * 
	 * @return
	 */
	public static List<String> getSDCardPath() {
		String cmd = "cat /proc/mounts";
		Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
		List<String> results = new ArrayList<String>();
		results.add(Environment.getExternalStorageDirectory().getPath());
		List<String> rootPaths = new ArrayList<String>();
		try {
			Process p = run.exec(cmd);// 启动另一个进程来执行命令
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
			String lineStr;
			while ((lineStr = inBr.readLine()) != null) {
				// 获得命令执行后在控制台的输出信息
				if (lineStr.contains("vfat") && lineStr.contains("vold")) {
					String[] strArray = lineStr.split(" ");
					if (strArray != null && strArray.length >= 5) {
						if (!rootPaths.contains(strArray[0]) && !results.contains(strArray[1])) {
							results.add(strArray[1]);
							rootPaths.add(strArray[0]);
						}
					}
					if (strArray[1].contains(Environment.getExternalStorageDirectory().getPath())) {
						rootPaths.add(strArray[0]);
					}
				}
			}
			inBr.close();
			in.close();
			return results;
		} catch (Exception e) {
			results.clear();
			results.add(Environment.getExternalStorageDirectory().getPath());
			return results;
		}
	}

	/** 获取SD路径 **/
	public static String getSDPath() {
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = Environment.getExternalStorageDirectory();// 获取根目录
			return sdDir.getPath();
		}
		return "/sdcard";
	}

	/** 获取MIME类型 **/
	@SuppressLint("DefaultLocale")
	public static String getMIMEType(String name) {
		String type = "";
		String end = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
		if (end.equals("apk")) {
			return "application/vnd.android.package-archive";
		} else if (end.equals("mp4") || end.equals("avi") || end.equals("3gp") || end.equals("rmvb")) {
			type = "video";
		} else if (end.equals("mp3") || end.equals("mid") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("txt") || end.equals("log")) {
			type = "text";
		} else {
			type = "*";
		}
		type += "/*";
		return type;
	}

	/** 获取文件信息 **/
	public static FileInfo getFileInfo(File f) {
		FileInfo info = new FileInfo();
		info.setName(f.getName());
		info.setDirectory(f.isDirectory());
		calcFileContent(info, f);
		return info;
	}

	public static boolean isExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/** 计算文件内容 **/
	private static void calcFileContent(FileInfo info, File f) {
		if (f.isFile()) {
			info.setSize(info.getSize() + f.length());
		}
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; ++i) {
					File tmp = files[i];
					if (tmp.isDirectory()) {
						info.setFolderCount((info.getFolderCount() + 1));
					} else if (tmp.isFile()) {
						info.setFileCount((info.getFileCount() + 1));
					}
					if (info.getFileCount() + info.getFolderCount() >= 10000) { // 超过一万不计算
						break;
					}
					calcFileContent(info, tmp);
				}
			}
		}
	}

	/** 转换文件大小 **/
	public static String formetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");// 保留2位小数
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = fileS + " B";
		} else if (fileS < 1024 * 1024) {
			fileSizeString = df.format((double) fileS / 1024) + " K";
		} else if (fileS < 1024 * 1024 * 1024) {
			fileSizeString = df.format((double) fileS / 1024 / 1024) + " M";
		} else {
			fileSizeString = df.format((double) fileS / 1024 / 1024 / 1024) + " G";
		}
		return fileSizeString;
	}

	/** 合并路径 **/
	public static String combinPath(String path, String fileName) {
		Log.i(TAG, "" + (path.endsWith(File.separator) ? "" : File.separator));
		return path + (path.endsWith(File.separator) ? "" : File.separator) + fileName;
	}

	/** 删除文件,同时也要删除该文件夹中的内容(文件或文件夹) **/
	public static void deleteFile(File f) {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; ++i) {
					deleteFile(files[i]);
				}
			}
		}
		f.delete();// 不为文件夹才可以删除该文件
	}

	/** 复制文件 **/
	public static boolean copyFile(File src, File tar) throws Exception {
		// 如果复制为一个文件
		if (src.isFile()) {
			InputStream is = new FileInputStream(src);
			OutputStream op = new FileOutputStream(tar);
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(op);
			byte[] bt = new byte[1024 * 8];
			int len = bis.read(bt);
			while (len != -1) {
				bos.write(bt, 0, len);
				len = bis.read(bt);
			}
			bis.close();
			bos.close();
		}
		// 如果复制为一个目录则将目录下的所有文件复制
		if (src.isDirectory()) {
			File[] f = src.listFiles();
			tar.mkdir();
			for (int i = 0; i < f.length; i++) {
				copyFile(f[i].getAbsoluteFile(), new File(tar.getAbsoluteFile() + File.separator + f[i].getName()));
			}
		}
		return true;
	}

	/** 移动文件 就是把原来的删除，然后复制即可 **/
	public static boolean moveFile(File src, File tar) throws Exception {
		if (copyFile(src, tar)) {
			deleteFile(src);
			return true;
		}
		return false;
	}

	/**
	 * .ppt|.pps|.pptx|.doc|.docx|.xls|.xlsx|.pdf
	 * 
	 * */
	public static boolean isOfficeFile(String fileName) {
		if (fileName.equalsIgnoreCase("ppt") || fileName.equalsIgnoreCase("pps") || fileName.equalsIgnoreCase("pptx") || fileName.equalsIgnoreCase("doc") || fileName.equalsIgnoreCase("docx")
				|| fileName.equalsIgnoreCase("xls") || fileName.equalsIgnoreCase("xlsx") || fileName.equalsIgnoreCase("pdf")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isFileExist(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

}
