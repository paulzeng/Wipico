package com.chinasvc.wipicophone.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.chinasvc.wipico.bean.FileInfo;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class LocalFileUtil {

	private Context context;

	public LocalFileUtil() {
	}

	public LocalFileUtil(Context context) {
		this.context = context;
	}

	/**
	 * 用各自的应用程序打开文件 eg: .mP3能被音乐播放器打开
	 * 
	 * @param path
	 *                文件路径
	 * */
	public void openFile(String path) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(path);
		String type = FileUtil.getMIMEType(file.getName());
		intent.setDataAndType(Uri.fromFile(file), type);
		this.context.startActivity(intent);
	}

	/**
	 * 获取该目录下所有文件
	 * 
	 * */
	public List<FileInfo> viewFiles(String filePath) {
		List<FileInfo> fileLists = FileActivityHelper.getFiles(context, filePath);
		if (fileLists != null) {
			return fileLists;
		} else {
			fileLists = new ArrayList<FileInfo>();
			return fileLists;
		}
	}

	/**
	 * 获取Office文件
	 */
	public List<FileInfo> scanSDOffice(File file, List<FileInfo> list) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					File tmp = files[i];
					if (tmp.isFile()) {
						String fileName = tmp.getName();
						String filePath = tmp.getName();
						long size = tmp.length();
						if (fileName.indexOf(".") >= 0) {
							fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
							if (FileUtil.isOfficeFile(fileName)) {
								FileInfo info = new FileInfo();
								info.setName(filePath);
								info.setPath(tmp.getAbsolutePath());
								info.setSize(size);
								list.add(info);
							}
						}
					} else {
						scanSDOffice(tmp, list);
					}
				}
			}
		} else {
			if (file.isFile()) {
				String fileName = file.getName();
				String filePath = file.getName();
				if (fileName.indexOf(".") >= 0) {
					fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
					if (FileUtil.isOfficeFile(fileName)) {
						FileInfo info = new FileInfo();
						info.setName(filePath);
						info.setPath(file.getAbsolutePath());
						info.setSize(file.length());
						list.add(info);
					}
				}
			}
		}
		return list;
	}

}
