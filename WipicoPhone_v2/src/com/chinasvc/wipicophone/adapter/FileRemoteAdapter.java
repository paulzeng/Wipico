package com.chinasvc.wipicophone.adapter;

import java.util.List;

import com.chinasvc.wipicophone.R;
import com.chinasvc.wipico.bean.FileInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 文件列表适配器
 * */
@SuppressLint("DefaultLocale")
public class FileRemoteAdapter extends BaseAdapter {

	private List<FileInfo> listFiles;
	public LayoutInflater layoutInflater;

	public FileRemoteAdapter(Context context, List<FileInfo> listFiles) {
		layoutInflater = LayoutInflater.from(context);
		this.listFiles = listFiles;
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
		ViewHolder holder;
		FileInfo bean = listFiles.get(position);
		if (bean.getAlbumId() == 1) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.activity_file_empty_item, null);
			holder.fileType = (ImageView) convertView.findViewById(R.id.fileIcon);
			holder.fileName = (TextView) convertView.findViewById(R.id.fileName);
		} else {
			if (convertView != null && convertView.getTag() != null) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				holder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.activity_remote_file_item, null);
				holder.fileType = (ImageView) convertView.findViewById(R.id.fileIcon);
				holder.fileName = (TextView) convertView.findViewById(R.id.fileName);
				holder.fileMsg = (TextView) convertView.findViewById(R.id.fileMsg);
				convertView.setTag(holder);
			}
		}
		if (bean.getAlbumId() != 1) {
			String name = listFiles.get(position).getName();
			if (listFiles.get(position).isDirectory()) {
				holder.fileType.setImageResource(R.drawable.file_file);
			} else if (name.endsWith(".apk")) {
				holder.fileType.setImageResource(R.drawable.history_apk);
			} else if (name.endsWith(".mp4") || name.endsWith(".avi") || name.endsWith(".3gp") || name.endsWith(".rmvb")) {
				holder.fileType.setImageResource(R.drawable.history_video);
			} else if (name.endsWith(".mp3") || name.endsWith(".mid") || name.endsWith(".wav")) {
				holder.fileType.setImageResource(R.drawable.history_audio);
			} else if (name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png") || name.endsWith(".jpeg") || name.endsWith(".bmp")) {
				holder.fileType.setImageResource(R.drawable.history_image);
			} else if (name.endsWith(".txt")) {
				holder.fileType.setImageResource(R.drawable.history_txt);
			} else if (name.endsWith(".ppt") || name.endsWith(".pps") || name.endsWith(".pptx")) {
				holder.fileType.setImageResource(R.drawable.history_ppt);
			} else if (name.endsWith(".doc") || name.endsWith(".docx")) {
				holder.fileType.setImageResource(R.drawable.history_doc);
			} else if (name.endsWith(".xls") || name.endsWith(".xlsx")) {
				holder.fileType.setImageResource(R.drawable.history_excel);
			} else if (name.endsWith(".pdf")) {
				holder.fileType.setImageResource(R.drawable.history_pdf);
			} else {
				holder.fileType.setImageResource(R.drawable.history_unknow);
			}
		} else {
			// holder.fileType.setImageResource(R.drawable.history_unknow);
		}
		holder.fileName.setText(listFiles.get(position).getName());
		if (holder.fileMsg != null) {
			holder.fileMsg.setText(listFiles.get(position).getPath());
		}
		return convertView;
	}

	/**
	 * class ViewHolder
	 */
	private class ViewHolder {
		ImageView fileType;
		TextView fileName;
		TextView fileMsg;
	}

}
