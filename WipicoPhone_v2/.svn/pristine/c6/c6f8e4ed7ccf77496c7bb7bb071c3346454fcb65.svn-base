package com.chinasvc.wipicophone.db;

import java.util.ArrayList;
import java.util.List;

import com.chinasvc.wipicophone.bean.History;

import android.content.ContentValues;
import android.database.Cursor;

public class HistoryDao extends DBDao {

	public long add(History bean) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", bean.getName());
		initialValues.put("path", bean.getPath());
		initialValues.put("size", bean.getSize());
		initialValues.put("type", bean.getType());
		initialValues.put("time", bean.getTime());
		initialValues.put("user", bean.getUserName());
		initialValues.put("state", bean.getState());
		initialValues.put("transfer", bean.getTransfer());
		initialValues.put("progress", bean.getProgress());
		initialValues.put("user_type", bean.getUserType());
		return mSQLiteDatabase.insert(DBHelper.HISTORY_TABLE, null, initialValues);
	}

	public void addAll(List<History> listDatas) {
		for (History bean : listDatas) {
			ContentValues initialValues = new ContentValues();
			initialValues.put("name", bean.getName());
			initialValues.put("path", bean.getPath());
			initialValues.put("size", bean.getSize());
			initialValues.put("type", bean.getType());
			initialValues.put("time", bean.getTime());
			initialValues.put("user", bean.getUserName());
			initialValues.put("state", bean.getState());
			initialValues.put("transfer", bean.getTransfer());
			initialValues.put("progress", bean.getProgress());
			initialValues.put("user_type", bean.getUserType());
			mSQLiteDatabase.insert(DBHelper.HISTORY_TABLE, null, initialValues);
		}
	}

	public void update(History bean) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", bean.getName());
		initialValues.put("path", bean.getPath());
		initialValues.put("size", bean.getSize());
		initialValues.put("type", bean.getType());
		initialValues.put("time", bean.getTime());
		initialValues.put("user", bean.getUserName());
		initialValues.put("state", bean.getState());
		initialValues.put("transfer", bean.getTransfer());
		initialValues.put("progress", bean.getProgress());
		initialValues.put("user_type", bean.getUserType());
		mSQLiteDatabase.update(DBHelper.HISTORY_TABLE, initialValues, "rowid=" + bean.getId(), null);
	}

	public void delete(History bean) {
		mSQLiteDatabase.delete(DBHelper.HISTORY_TABLE, "rowid=" + bean.getId(), null);
	}

	public void deleteAll() {
		mSQLiteDatabase.delete(DBHelper.HISTORY_TABLE, null, null);
	}

	// name text, path text, size integer, type integer, time text, user
	// text, state integer, transfer integer

	public List<History> fetcheAll() {
		List<History> listDatas = new ArrayList<History>();
		String[] query = new String[] { "rowid as _id", "name", "path", "size", "type", "time", "user", "state", "transfer", "progress", "user_type" };
		Cursor cursor = mSQLiteDatabase.query(false, DBHelper.HISTORY_TABLE, query, null, null, null, null, " time desc ", null);
		History bean = null;
		if (cursor.moveToFirst()) {
			do {
				bean = new History();
				bean.setId(cursor.getInt(0));
				bean.setName(cursor.getString(1));
				bean.setPath(cursor.getString(2));
				bean.setSize(cursor.getInt(3));
				bean.setType(cursor.getInt(4));
				bean.setTime(cursor.getString(5));
				bean.setUserName(cursor.getString(6));
				bean.setState(cursor.getInt(7));
				bean.setTransfer(cursor.getInt(8));
				bean.setProgress(cursor.getInt(9));
				bean.setUserType(cursor.getInt(10));
				listDatas.add(bean);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return listDatas;
	}

}
