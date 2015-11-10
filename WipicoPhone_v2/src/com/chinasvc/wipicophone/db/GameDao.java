package com.chinasvc.wipicophone.db;

import java.util.ArrayList;
import java.util.List;

import com.chinasvc.wipicophone.bean.GameBean;
import com.chinasvc.wipicophone.util.BitmapUtil;

import android.content.ContentValues;
import android.database.Cursor;

public class GameDao extends DBDao {

	public void add(GameBean bean) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", bean.getName());
		initialValues.put("mpackage", bean.getGamePackage());
		initialValues.put("image", BitmapUtil.bitmap2Byte(bean.getIconBitmap()));
		mSQLiteDatabase.insert(DBHelper.GAME_TABLE, null, initialValues);
	}

	public void addAll(List<GameBean> listDatas) {
		for (GameBean bean : listDatas) {
			ContentValues initialValues = new ContentValues();
			initialValues.put("name", bean.getName());
			initialValues.put("mpackage", bean.getGamePackage());
			initialValues.put("image", BitmapUtil.bitmap2Byte(bean.getIconBitmap()));
			mSQLiteDatabase.insert(DBHelper.GAME_TABLE, null, initialValues);
		}
	}

	public void update(GameBean bean) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", bean.getName());
		initialValues.put("mpackage", bean.getGamePackage());
		initialValues.put("image", BitmapUtil.bitmap2Byte(bean.getIconBitmap()));
		mSQLiteDatabase.update(DBHelper.GAME_TABLE, initialValues, "mpackage='" + bean.getGamePackage() + "'", null);
	}

	public void delete(GameBean bean) {
		mSQLiteDatabase.delete(DBHelper.GAME_TABLE, "mpackage='" + bean.getGamePackage() + "'", null);
	}

	public void deleteAll() {
		mSQLiteDatabase.delete(DBHelper.GAME_TABLE, "rowid>" + 3, null);
	}

	/**
	 * 查找
	 * */
	public boolean fetchByPackage(String mPackage) {
		String[] query = new String[] { "rowid as _id", "name", "mpackage", "image" };
		Cursor cursor = mSQLiteDatabase.query(false, DBHelper.GAME_TABLE, query, "mpackage='" + mPackage + "'", null, null, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}

	public List<GameBean> fetchAll() {
		List<GameBean> listDatas = new ArrayList<GameBean>();
		String[] query = new String[] { "rowid as _id", "name", "mpackage", "image" };
		Cursor cursor = mSQLiteDatabase.query(false, DBHelper.GAME_TABLE, query, null, null, null, null, " _id asc ", null);
		GameBean bean = null;
		if (cursor.moveToFirst()) {
			do {
				bean = new GameBean();
				bean.setId(cursor.getLong(0));
				bean.setName(cursor.getString(1));
				bean.setGamePackage(cursor.getString(2));
				bean.setIconBitmap(BitmapUtil.byte2bitmap(cursor.getBlob(3)));
				listDatas.add(bean);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return listDatas;
	}

}
