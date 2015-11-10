package com.chinasvc.wipicophone.db;

import java.io.ByteArrayOutputStream;

import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.bean.GameBean;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class DBHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 2;
	public static final String DB_NAME = "chinavc_remote.db";
	private static DBHelper mDBHelper;

	private static Resources resources;

	public static final String OFFICE_LOCAL_TABLE = "local_office";// 本地Office表
	public static final String HISTORY_TABLE = "history";// 历史记录
	public static final String GAME_TABLE = "game_table";

	private static final String CREATE_OFFICE_LOCAL_TABLE = "CREATE TABLE IF NOT EXISTS " + OFFICE_LOCAL_TABLE + "( name text, path text, size integer )";
	private static final String CREATE_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + HISTORY_TABLE
			+ "( name text, path text, size integer, type integer, time text,  user text, state integer, transfer integer, progress integer, user_type integer )";

	private static final String CREATE_GAME_TABLE = "CREATE TABLE IF NOT EXISTS " + GAME_TABLE + "( name text , image blob, mpackage text )";

	public static synchronized DBHelper getInstance(Context context) {
		resources = context.getResources();
		if (mDBHelper == null) {
			mDBHelper = new DBHelper(context);
		}
		return mDBHelper;
	}

	private DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_OFFICE_LOCAL_TABLE);
		db.execSQL(CREATE_HISTORY_TABLE);

		db.execSQL(CREATE_GAME_TABLE);
		String[] units = resources.getStringArray(R.array.game_array);
		int i = 0;
		for (String temp : units) {
			String[] result = temp.split(",");
			int[] icons = { R.drawable.icon_thd, R.drawable.icon_race, R.drawable.icon_moto, R.drawable.icon_car3d };
			GameBean bean = new GameBean();
			bean.setName(result[0]);
			bean.setGamePackage(result[1]);
			bean.setIconBitmap(((BitmapDrawable) resources.getDrawable(icons[i])).getBitmap());

			ContentValues initialValues = new ContentValues();
			initialValues.put("name", result[0]);
			initialValues.put("mpackage", result[1]);
			initialValues.put("image", img(icons[i]));
			db.insert(GAME_TABLE, null, initialValues);
			i++;
		}
	}

	public byte[] img(int id) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Bitmap bitmap = ((BitmapDrawable) resources.getDrawable(id)).getBitmap();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
}