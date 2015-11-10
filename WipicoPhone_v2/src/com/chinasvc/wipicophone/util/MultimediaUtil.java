package com.chinasvc.wipicophone.util;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.chinasvc.wipico.bean.FileInfo;
import com.chinasvc.wipicophone.bean.AudioBean;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

@SuppressLint("DefaultLocale")
public class MultimediaUtil {

	private Context mContext;

	public MultimediaUtil(Context context) {
		mContext = context;
	}

	String[] movieCondition = new String[] { MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DURATION };

	public List<FileInfo> getMovie() {
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		Cursor cursor = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, movieCondition, null, null, null);
		int n = cursor.getCount();
		cursor.moveToFirst();
		for (int i = 0; i != n; ++i) {
			FileInfo mInfo = new FileInfo();
			mInfo.setSize(cursor.getLong(0));
			mInfo.setName(cursor.getString(1));
			mInfo.setPath(cursor.getString(2));
			mInfo.setDuration(cursor.getInt(3));
			fileInfos.add(mInfo);
			cursor.moveToNext();
		}
		cursor.close();
		return fileInfos;
	}

	String[] musicCondition = new String[] { MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA, MediaStore.Video.Media.DURATION,
			MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM };

	public List<AudioBean> getMusic() {
		List<AudioBean> listDatas = new ArrayList<AudioBean>();
		Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicCondition, null, null, null);
		int n = cursor.getCount();
		cursor.moveToFirst();
		for (int i = 0; i != n; ++i) {
			AudioBean bean = new AudioBean();
			bean.setSize(cursor.getLong(0));
			bean.setName(cursor.getString(1));
			bean.setPath(cursor.getString(2));
			bean.setDuration(cursor.getInt(3));
			bean.setAlbumId(cursor.getLong(4));
			bean.setArtist(cursor.getString(5));
			bean.setSpecial(cursor.getString(6));
			if (bean.getName() != null) {
				String end = bean.getName().substring(bean.getName().lastIndexOf(".") + 1, bean.getName().length()).toLowerCase();
				if (end.equals("") || end.equals("aac") || end.equals("ac3") || end.equals("ape") || end.equals("mid") || end.equals("midi")) {
				} else {
					String str = PingYinUtil.getPingYinFirst(bean.getName());
					bean.setLetter(str);
					listDatas.add(bean);
				}
			}
			cursor.moveToNext();
		}
		cursor.close();
		return listDatas;
	}

	// 将ms转换为分秒时间显示函数
	public static String showTime(int time) {
		// 将ms转换为s
		int result = time / 1000;
		int minute = result / 60;
		int hour = minute / 60;
		int second = result % 60;
		minute %= 60;
		if (time < 60 * 1000) {
			return String.format("%02d:%02d", minute, second);
		} else if (time < 60 * 1000 * 60) {
			return String.format("%02d:%02d", minute, second);
		} else if (time > 60 * 1000 * 60) {
			return String.format("%02d:%02d:%02d", hour, minute, second);
		} else {
			return "";
		}

	}

	// CSVC txh
	public static String stringForTime(int paramInt) {
		paramInt /= 1000;
		int minute = paramInt / 60;
		int hour = minute / 60;
		int second = paramInt % 60;
		minute %= 60;
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}

	// 将ms转换为分秒时间显示函数
	public static String showSize(long byteSize) {
		if (byteSize < 1024) {
			return byteSize + "B";
		} else if (byteSize < (1024 * 1024)) {
			return MathUtil.keep2decimal((byteSize * 1.0) / 1024) + "KB";
		} else if (byteSize < (1024 * 1024 * 1024)) {
			return MathUtil.keep2decimal((byteSize * 1.0) / (1024 * 1024)) + "M";
		} else if (byteSize > (1024 * 1024 * 1024)) {
			return MathUtil.keep2decimal((byteSize * 1.0) / (1024 * 1024 * 1024)) + "G";
		} else {
			return "";
		}
	}

	public static Bitmap getAudioBitmap(Context mContext, int albumid) {
		Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
		Bitmap bm = null;
		try {
			if (albumid < 0) {
				return null;
			} else {
				Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
				ParcelFileDescriptor pfd = mContext.getContentResolver().openFileDescriptor(uri, "r");
				if (pfd != null) {
					FileDescriptor fd = pfd.getFileDescriptor();
					bm = BitmapFactory.decodeFileDescriptor(fd);
				}
				return bm;
			}
		} catch (FileNotFoundException ex) {
			return null;
		}

	}
}
