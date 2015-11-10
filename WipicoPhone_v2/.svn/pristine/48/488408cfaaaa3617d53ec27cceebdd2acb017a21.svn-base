package com.chinasvc.wipicophone.image;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.Build;
import android.os.StatFs;
import android.util.Log;

import com.chinasvc.wipicophone.util.FileUtils;

/**
 * 文件Hash码工具
 * */
public class FileHashKey {

	private MessageDigest hash;

	private final File mCacheBase;
	private final String mCachePrefix, mCacheSuffix;

	/**
	 * Creates a new disk cache.
	 * 
	 * @param cacheBase
	 *                The base directory within which all the cache files
	 *                will be stored.
	 * @param cachePrefix
	 *                If you want a prefix to the filenames, place one here.
	 *                Otherwise, pass null.
	 * @param cacheSuffix
	 *                A suffix to the cache filename. Null is also ok here.
	 *                (.jpg /.png)
	 */
	public FileHashKey(File cacheBase, String cachePrefix, String cacheSuffix) {
		mCacheBase = cacheBase;
		mCachePrefix = cachePrefix;
		mCacheSuffix = cacheSuffix;
		Log.i("", "创建文件" + cacheBase.getPath() + cachePrefix);
		FileUtils.createIfNoExists(cacheBase.getPath() + cachePrefix);

		try {
			hash = MessageDigest.getInstance("SHA-1");
		} catch (final NoSuchAlgorithmException e) {
			try {
				hash = MessageDigest.getInstance("MD5");
			} catch (final NoSuchAlgorithmException e2) {
				final RuntimeException re = new RuntimeException("No available hashing algorithm");
				re.initCause(e2);
				throw re;
			}
		}
	}

	/**
	 * Gets the amount of space free on the cache volume.
	 * 
	 * @return free space in bytes.
	 */
	public long getFreeSpace() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return mCacheBase.getUsableSpace();
		} else {
			// maybe make singleton
			final StatFs stat = new StatFs(mCacheBase.getAbsolutePath());
			return (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
		}
	}

	/**
	 * Gets the cache filename for the given key.
	 * 
	 * @param key
	 * @return
	 */
	public File getFile(String key) {
		return new File(mCacheBase, (mCachePrefix != null ? mCachePrefix : "") + hash(key) + (mCacheSuffix != null ? mCacheSuffix : ""));
	}

	/**
	 * Using the key's {@link Object#toString() toString()} method,
	 * generates a string suitable for using as a filename.
	 * 
	 * @param key
	 * @return a string uniquely representing the the key.
	 */
	public String hash(String key) {
		final byte[] ba;

		// MessageDigest isn't threadsafe, so we need to ensure it
		// doesn't tread on itself.
		synchronized (hash) {
			hash.update(key.toString().getBytes());
			ba = hash.digest();
		}
		final BigInteger bi = new BigInteger(1, ba);
		final String result = bi.toString(16);
		if (result.length() % 2 != 0) {
			return "0" + result;
		}
		return result;
	}

}
