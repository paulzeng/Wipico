package com.chinasvc.wipicophone.util;

import java.util.Comparator;

import com.chinasvc.wipico.bean.FileInfo;

/**
 * 排序
 * 
 **/
public class FileComparator implements Comparator<FileInfo> {

	public int compare(FileInfo file1, FileInfo file2) {

		if (file1.isDirectory() && !file2.isDirectory()) {
			return -1000;
		} else if (!file1.isDirectory() && file2.isDirectory()) {
			return 1000;
		}
		// 相同类型按名称排序
		return file1.getName().compareTo(file2.getName());
	}
}