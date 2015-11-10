package com.chinasvc.wipicophone.db;

import java.util.ArrayList;
import java.util.List;

import com.chinasvc.wipico.bean.FileInfo;


import android.content.ContentValues;
import android.database.Cursor;

public class OfficeLocalDao extends DBDao {

	public void add(FileInfo bean) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", bean.getName());
		initialValues.put("path", bean.getPath());
		initialValues.put("size", bean.getSize());
		mSQLiteDatabase.insert(DBHelper.OFFICE_LOCAL_TABLE, null, initialValues);
	}

	public void addAll(List<FileInfo> listDatas) {
		for (FileInfo bean : listDatas) {
			ContentValues initialValues = new ContentValues();
			initialValues.put("name", bean.getName());
			initialValues.put("path", bean.getPath());
			initialValues.put("size", bean.getSize());
			mSQLiteDatabase.insert(DBHelper.OFFICE_LOCAL_TABLE, null, initialValues);
		}
	}

	public void update(FileInfo bean) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", bean.getName());
		initialValues.put("path", bean.getPath());
		initialValues.put("size", bean.getSize());
		mSQLiteDatabase.update(DBHelper.OFFICE_LOCAL_TABLE, initialValues, "rowid=" + bean.getId(), null);
	}

	public void delete(FileInfo bean) {
		mSQLiteDatabase.delete(DBHelper.OFFICE_LOCAL_TABLE, "rowid=" + bean.getId(), null);
	}

	public void deleteAll() {
		mSQLiteDatabase.delete(DBHelper.OFFICE_LOCAL_TABLE, null, null);
	}

	public List<FileInfo> fetchOffice() {
		List<FileInfo> goods = new ArrayList<FileInfo>();
		String[] query = new String[] { "rowid as _id", "name", "path", "size" };
		Cursor cursor = mSQLiteDatabase.query(false, DBHelper.OFFICE_LOCAL_TABLE, query, null, null, null, null, " _id asc ", null);
		FileInfo bean = null;
		if (cursor.moveToFirst()) {
			do {
				bean = new FileInfo();
				bean.setId(cursor.getLong(0));
				bean.setName(cursor.getString(1));
				bean.setPath(cursor.getString(2));
				bean.setSize(cursor.getLong(3));
				goods.add(bean);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return goods;
	}

	public List<FileInfo> fetchWord() {
		List<FileInfo> goods = new ArrayList<FileInfo>();
		String[] query = new String[] { "rowid as _id", "name", "path", "size" };
		Cursor cursor = mSQLiteDatabase.query(false, DBHelper.OFFICE_LOCAL_TABLE, query, null, null, null, null, " _id asc ", null);
		FileInfo bean = null;
		if (cursor.moveToFirst()) {
			do {
				bean = new FileInfo();
				bean.setId(cursor.getLong(0));
				bean.setName(cursor.getString(1));
				bean.setPath(cursor.getString(2));
				bean.setSize(cursor.getLong(3));
				if (bean.getName().endsWith(".doc") || bean.getName().endsWith(".docx")) {
					goods.add(bean);
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return goods;
	}

	public List<FileInfo> fetchExcel() {
		List<FileInfo> goods = new ArrayList<FileInfo>();
		String[] query = new String[] { "rowid as _id", "name", "path", "size" };
		Cursor cursor = mSQLiteDatabase.query(false, DBHelper.OFFICE_LOCAL_TABLE, query, null, null, null, null, " _id asc ", null);
		FileInfo bean = null;
		if (cursor.moveToFirst()) {
			do {
				bean = new FileInfo();
				bean.setId(cursor.getLong(0));
				bean.setName(cursor.getString(1));
				bean.setPath(cursor.getString(2));
				bean.setSize(cursor.getLong(3));
				if (bean.getName().endsWith(".xls") || bean.getName().endsWith(".xlsx")) {
					goods.add(bean);
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return goods;
	}

	public List<FileInfo> fetchPPT() {
		List<FileInfo> goods = new ArrayList<FileInfo>();
		String[] query = new String[] { "rowid as _id", "name", "path", "size" };
		Cursor cursor = mSQLiteDatabase.query(false, DBHelper.OFFICE_LOCAL_TABLE, query, null, null, null, null, " _id asc ", null);
		FileInfo bean = null;
		if (cursor.moveToFirst()) {
			do {
				bean = new FileInfo();
				bean.setId(cursor.getLong(0));
				bean.setName(cursor.getString(1));
				bean.setPath(cursor.getString(2));
				bean.setSize(cursor.getLong(3));
				if (bean.getName().endsWith(".ppt") || bean.getName().endsWith(".pps") || bean.getName().endsWith(".pptx")) {
					goods.add(bean);
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return goods;
	}

	public List<FileInfo> fetchPDF() {
		List<FileInfo> goods = new ArrayList<FileInfo>();
		String[] query = new String[] { "rowid as _id", "name", "path", "size" };
		Cursor cursor = mSQLiteDatabase.query(false, DBHelper.OFFICE_LOCAL_TABLE, query, null, null, null, null, " _id asc ", null);
		FileInfo bean = null;
		if (cursor.moveToFirst()) {
			do {
				bean = new FileInfo();
				bean.setId(cursor.getLong(0));
				bean.setName(cursor.getString(1));
				bean.setPath(cursor.getString(2));
				bean.setSize(cursor.getLong(3));
				if (bean.getName().endsWith(".pdf")) {
					goods.add(bean);
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return goods;
	}
}
