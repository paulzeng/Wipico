package com.chinasvc.wipicophone.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.async.SyncThumbnailExtractor;
import com.chinasvc.wipico.bean.FileInfo;
import com.chinasvc.wipicophone.util.MultimediaUtil;

/**
 * 适配器
 * 
 * @author billy
 **/
public class VideoListAdapter extends BaseAdapter implements OnScrollListener {

	private List<FileInfo> listFiles;
	private LayoutInflater layoutInflater;
	private SyncThumbnailExtractor syncThumbExtractor;
	private Context mContext;

	public VideoListAdapter(Context context, List<FileInfo> listFiles, ListView listView) {
		this.listFiles = listFiles;
		mContext = context;
		syncThumbExtractor = new SyncThumbnailExtractor(context);
		listView.setOnScrollListener(this);
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listFiles.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listFiles.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.activity_local_video_item, null);
			holder.fileType = (ImageView) convertView.findViewById(R.id.video_image_thumb);
			holder.fileName = (TextView) convertView.findViewById(R.id.video_title);
			holder.size = (TextView) convertView.findViewById(R.id.video_size);
			holder.duration = (TextView) convertView.findViewById(R.id.video_duration);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 判断文件类型并设置图标
		if (position > listFiles.size()) {
			return null;
		}

		syncThumbExtractor.decodeThumbnail(holder.fileType, listFiles.get(position).getPath(), 0, -1);

		holder.fileName.setText(listFiles.get(position).getName());
		holder.size.setText(mContext.getString(R.string.video_size) + MultimediaUtil.showSize(listFiles.get(position).getSize()));
		holder.duration.setText(mContext.getString(R.string.video_duration) + MultimediaUtil.showTime(listFiles.get(position).getDuration()));

		return convertView;
	}

	/**
	 * class ViewHolder
	 */
	private class ViewHolder {
		ImageView fileType;
		TextView fileName;
		TextView size;
		TextView duration;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
			syncThumbExtractor.pause();
		} else {
			syncThumbExtractor.resume();
		}
	}

}