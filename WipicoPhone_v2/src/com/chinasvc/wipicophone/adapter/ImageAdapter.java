package com.chinasvc.wipicophone.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.image.WipicoImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageAdapter extends BaseAdapter {

	private List<WipicoImage> listImages;
	private LayoutInflater layoutInflater;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public ImageAdapter(Context context, List<WipicoImage> listImages) {
		layoutInflater = LayoutInflater.from(context);
		this.listImages = listImages;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.bg_image_default).showImageForEmptyUri(R.drawable.bg_image_default).showImageOnFail(R.drawable.bg_image_default)
				.cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		return listImages.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = layoutInflater.inflate(R.layout.activity_image_item, null);
			viewHolder.image = (ImageView) view.findViewById(R.id.thumb);
			viewHolder.imageState = (ImageView) view.findViewById(R.id.imageState);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final WipicoImage bean = listImages.get(position);

		if (bean.isPlay()) {
			viewHolder.imageState.setVisibility(View.VISIBLE);
		} else {
			viewHolder.imageState.setVisibility(View.GONE);
		}
		String imageUrl = "";
		if (bean.getUrl().startsWith("assets")) {
			imageUrl = bean.getUrl();
		} else {
			imageUrl = "file://" + bean.getUrl();
		}

		imageLoader.displayImage(imageUrl, viewHolder.image, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				bean.setUrl("assets://image_default.png");
				notifyDataSetChanged();
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			}
		});
		return view;
	}

	private class ViewHolder {
		ImageView image;
		ImageView imageState;
	}

}