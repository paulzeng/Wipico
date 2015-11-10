package com.chinasvc.wipicophone.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

public class FileTools {

	/**
	 * 计算sdcard上的剩余空间
	 * 
	 * @return
	 */
	public static long freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long sdFreeMB = (long) stat.getAvailableBlocks() * stat.getBlockSize();
		return sdFreeMB;
	}

	/**
	 * 用各自的应用程序打开文件 eg: .mP3能被音乐播放器打开
	 * 
	 * @param path
	 *                文件路径
	 * */
	public static void openFile(String path, Context context) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(path);
		String type = FileUtils.getMimeType(file.getName());
		if (type == null || type.equals("")) {
			type = "*/*";
		}
		intent.setDataAndType(Uri.fromFile(file), type);
		context.startActivity(intent);
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

	/**
	 * 删除文件（包括子文件）
	 * 
	 * @param file
	 */
	public static void delete(File file) {
		if (file.isDirectory()) {
			File[] childs = file.listFiles();
			for (File bean : childs)
				delete(bean);
		}
		file.delete();
	}

	public static boolean isFileExist(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 创建目录
	 */
	public static void createDir(String filePath) {
		if (isExternalStorageAvailable()) {
			File file = new File(filePath);
			if (!file.exists())
				file.mkdirs();
		}
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

	/**
	 * 隐藏软键盘
	 */
	public static void hideSoftInput(Context context) {
		if (context == null)
			return;
		InputMethodManager manager = ((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE));
		View view = ((Activity) context).getCurrentFocus();
		if (view == null)
			return;
		manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 外部存储是否可用
	 */
	public static boolean isExternalStorageAvailable() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

}
