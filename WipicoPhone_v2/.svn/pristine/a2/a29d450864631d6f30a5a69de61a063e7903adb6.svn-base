package com.chinasvc.wipicophone.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {

	public static byte[] bitmap2Byte(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap byte2bitmap(byte[] bytes) {
		Bitmap bmpout = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return bmpout;
	}

}
