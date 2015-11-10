package com.chinasvc.wipicophone.async;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore.Images;
import android.widget.ImageView;

import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.util.MultimediaUtil;

public class SyncThumbnailExtractor implements Callback {

	private static final String LOADER_THREAD_NAME = "FileIconLoader";
	/**
	 * Type of message sent by the UI thread to itself to indicate that some
	 * thumbnails need to be extracted.
	 */
	private static final int MESSAGE_REQUEST_EXTRACTING = 1;

	/**
	 * Type of message sent by the loader thread to indicate that some
	 * thumbnails have been extracted.
	 */
	private static final int MESSAGE_THUMBNAIL_EXTRACTED = 2;

	private boolean mPaused;
	private boolean mDecodingRequested = false;

	final Handler mMainHandler = new Handler(this);
	ExtractorThread mExtractorThread;
	private Context mContext;

	private final ConcurrentHashMap<ImageView, FileInfo> mPendingRequests = new ConcurrentHashMap<ImageView, FileInfo>();
	private final static ConcurrentHashMap<String, ImageHolder> mImageCache = new ConcurrentHashMap<String, ImageHolder>();

	private static abstract class ImageHolder {
		public static final int NEEDED = 0;
		public static final int EXTRACTING = 1;
		public static final int EXTRACTED = 2;
		int state;

		public static ImageHolder create(int mimeType) {
			switch (mimeType) {
			case 0:
				return new BitmapHolder();
			case 1:
				return new BitmapHolder();
			case 2:
				return new BitmapHolder();
			default:
				return new DrawableHolder();
			}
		};

		public abstract boolean setImageView(ImageView v);

		public abstract boolean isNull();

		public abstract void setImage(Object image);
	}

	private static class BitmapHolder extends ImageHolder {
		SoftReference<Bitmap> bitmapRef;

		@Override
		public boolean setImageView(ImageView v) {
			if (bitmapRef.get() == null)
				return false;
			v.setImageBitmap(bitmapRef.get());
			return true;
		}

		@Override
		public boolean isNull() {
			return bitmapRef == null;
		}

		@Override
		public void setImage(Object image) {
			bitmapRef = image == null ? null : new SoftReference<Bitmap>((Bitmap) image);
		}
	}

	private static class DrawableHolder extends ImageHolder {
		SoftReference<Drawable> drawableRef;

		@Override
		public boolean setImageView(ImageView v) {
			if (drawableRef.get() == null)
				return false;

			v.setImageDrawable(drawableRef.get());
			return true;
		}

		@Override
		public boolean isNull() {
			return drawableRef == null;
		}

		@Override
		public void setImage(Object image) {
			drawableRef = image == null ? null : new SoftReference<Drawable>((Drawable) image);
		}
	}

	private static class FileInfo {
		public FileInfo(String path, int mimeType, int albumid) {
			this.path = path;
			this.mimeType = mimeType;
			this.albumid = albumid;

		}

		public String path;
		public int mimeType;
		public int albumid;
	}

	public SyncThumbnailExtractor(Context context) {
		mContext = context;
	}

	public void clear() {
		mPaused = false;
		mImageCache.clear();
		mPendingRequests.clear();
	}

	// 当前Activity调用OnDestory时，将<span
	// ExtractorThread退出，并清空缓存
	public void stop() {
		pause();

		if (mExtractorThread != null) {
			mExtractorThread.quit();
			mExtractorThread = null;
		}

		clear();
	}

	public void resume() {
		mPaused = false;
		if (!mPendingRequests.isEmpty()) {
			requestExtracting();
		}
	}

	public void pause() {
		mPaused = true;
	}

	/**
	 * Load thumbnail into the supplied image view. If the thumbnail is
	 * already cached, it is displayed immediately. Otherwise a request is
	 * sent to load the thumbnail from the database.
	 * 
	 * @param id
	 *                , database id
	 */
	public boolean decodeThumbnail(ImageView view, String path, int mimeType, int albumid) {
		boolean extracted = loadCache(view, path, mimeType);
		if (extracted) {
			mPendingRequests.remove(view);
		} else {
			mPendingRequests.put(view, new FileInfo(path, mimeType, albumid));
			if (!mPaused) {
				// Send a request to start loading thumbnails
				requestExtracting();
			}
		}
		return extracted;
	}

	// set default icon by MimeType for unextracted mefile
	private void setImageByMimeType(ImageView image, int mimeType) {
		switch (mimeType) {
		case 0:
			image.setImageResource(R.drawable.ic_video_default);
			break;
		case 1:
			image.setImageResource(R.drawable.history_audio);
			break;
		case 2:
			image.setImageResource(R.drawable.history_image);
			break;
		default:
			image.setImageResource(R.drawable.history_unknow);
			break;
		}
	}

	/**
	 * Checks if the thumbnail is present in cache. If so, sets the
	 * thumbnail on the view, otherwise sets the state of the thumbnail to
	 * BitmapHolde
	 */
	private boolean loadCache(ImageView view, String path, int mimeType) {
		ImageHolder holder = mImageCache.get(path);

		if (holder == null) {
			holder = ImageHolder.create(mimeType);
			if (holder == null)
				return false;
			mImageCache.put(path, holder);
		} else if (holder.state == ImageHolder.EXTRACTED) {
			if (holder.isNull()) {
				setImageByMimeType(view, mimeType);
				return true;
			}
			// failing to set imageview means that the soft
			// reference was
			// released by the GC, we need to reload the thumbnail.
			if (holder.setImageView(view)) {
				return true;
			}

			holder.setImage(null);
		}

		setImageByMimeType(view, mimeType);
		holder.state = ImageHolder.NEEDED;
		return false;
	}

	/**
	 * Sends a message to this thread itself to start loading images. If the
	 * current view contains multiple image views, all of those image views
	 * will get a chance to request their respective thumbnails before any
	 * of those requests are executed. This allows us to load images in
	 * bulk.
	 */
	private void requestExtracting() {
		if (!mDecodingRequested) {
			mDecodingRequested = true;
			mMainHandler.sendEmptyMessage(MESSAGE_REQUEST_EXTRACTING);
		}
	}

	/**
	 * @Description: handle MESSAGE_REQUEST_EXTRACTING message to create
	 *               ExtractorThread and start to extract thumbnail in
	 *               mPendingRequests's file
	 * @param msg
	 * 
	 * @return
	 */
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MESSAGE_REQUEST_EXTRACTING:
			mDecodingRequested = false;
			if (mExtractorThread == null) {
				mExtractorThread = new ExtractorThread();
				mExtractorThread.start();
			}
			mExtractorThread.requestLoading();
			return true;
		case MESSAGE_THUMBNAIL_EXTRACTED:
			if (!mPaused) {
				processExtractThumbnails();
			}
			return true;
		}
		return false;

	}

	/**
	 * Goes over pending loading requests and displays extracted thumbnails.
	 * If some of the thumbnails still haven't been extracted, sends another
	 * request for image loading.
	 */
	private void processExtractThumbnails() {
		Iterator<ImageView> iterator = mPendingRequests.keySet().iterator();
		while (iterator.hasNext()) {
			ImageView view = iterator.next();
			FileInfo info = mPendingRequests.get(view);
			boolean extracted = loadCache(view, info.path, info.mimeType);
			if (extracted) {
				iterator.remove();
			}
		}

		if (!mPendingRequests.isEmpty()) {
			requestExtracting();
		}
	}

	private class ExtractorThread extends HandlerThread implements Callback {
		private Handler mExtractorHandler;

		/**
		 * @Description:
		 * @param name
		 */
		public ExtractorThread() {
			super(LOADER_THREAD_NAME);
		}

		/**
		 * Sends a message to this thread to extract requested
		 * thumbnails.
		 */
		public void requestLoading() {
			if (mExtractorHandler == null) {
				mExtractorHandler = new Handler(getLooper(), this);
			}
			mExtractorHandler.sendEmptyMessage(0);
		}

		/**
		 * @Description: extract thumbnail
		 * 
		 * @param msg
		 * 
		 * @return
		 */
		@SuppressLint("NewApi")
		@Override
		public boolean handleMessage(Message msg) {
			Iterator<FileInfo> iterator = mPendingRequests.values().iterator();
			while (iterator.hasNext()) {
				FileInfo info = iterator.next();

				ImageHolder holder = mImageCache.get(info.path);
				if (holder != null && holder.state == ImageHolder.NEEDED) {
					// Assuming atomic behavior
					holder.state = ImageHolder.EXTRACTING;
					switch (info.mimeType) {
					case 0:
						holder.setImage(ThumbnailUtils.createVideoThumbnail(info.path, Images.Thumbnails.MINI_KIND));
						break;
					case 1:
						Bitmap bitmap = MultimediaUtil.getAudioBitmap(mContext, info.albumid);
						if (bitmap != null) {
							holder.setImage(bitmap);
						}
						break;
					case 2:
						holder.setImage(getImageThumbnail(info.path, 100, 100));
						break;
					default:
						holder.setImage(R.drawable.history_unknow);
						break;
					}
					holder.state = BitmapHolder.EXTRACTED;
					mImageCache.put(info.path, holder);
				}
			}
			mMainHandler.sendEmptyMessage(MESSAGE_THUMBNAIL_EXTRACTED);
			return true;
		}
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param imagePath
	 *                图像的路径
	 * @param width
	 *                指定输出图像的宽度
	 * @param height
	 *                指定输出图像的高度
	 * @return 生成的缩略图
	 */
	@SuppressLint({ "NewApi", "InlinedApi" })
	public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

}
