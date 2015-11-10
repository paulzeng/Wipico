package com.chinasvc.wipicophone.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import com.chinasvc.wipico.bean.FileInfo;

import android.content.Context;


/**
 * 
 * 
 * */
public class FileActivityHelper {

	/** 获取一个文件夹下的所有文件 **/
	public static ArrayList<FileInfo> getFiles(Context context, String path) {
		File f = new File(path);
		File[] files = f.listFiles();
		if (files == null) {
			// Toast.makeText(context, "文件为空,无法打开!\t" + path,
			// Toast.LENGTH_SHORT).show();
			return null;
		}
		ArrayList<FileInfo> fileList = new ArrayList<FileInfo>();
		// 获取文件列表
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.canRead() && !file.getName().startsWith(".")) {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setName(file.getName());
				fileInfo.setDirectory(file.isDirectory());
				fileInfo.setPath(file.getPath());
				fileInfo.setSize(file.length());
				fileInfo.setCanRead(file.canRead());
				fileInfo.setChecked(false);
				fileList.add(fileInfo);
			}
		}
		// 排序
		Collections.sort(fileList, new FileComparator());
		return fileList;
	}
}
