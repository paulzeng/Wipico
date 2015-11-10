package com.chinasvc.wipicophone.adapter;

import java.util.List;

import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.bean.AppInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoIndexAdapter extends BaseAdapter {

	private Context context;
	private List<AppInfo> listDatas;
	public LayoutInflater layoutInflater;

	public VideoIndexAdapter(Context context, List<AppInfo> listFiles) {
		this.context = context;
		this.listDatas = listFiles;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listDatas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listDatas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.activity_application_video_item, null);
			holder.icon = (ImageView) convertView.findViewById(R.id.logo);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.ic_type = (ImageView) convertView.findViewById(R.id.ic_type);
			holder.ic_type_value = (TextView) convertView.findViewById(R.id.ic_type_value);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String name = listDatas.get(position).getName();

		if (name.equals(context.getText(R.string.application_video_youku))) {
			holder.icon.setBackgroundResource(R.drawable.icon_youku);
			holder.name.setText(context.getText(R.string.application_video_youku));
			if (listDatas.get(position).isInstall()) {
				holder.ic_type.setBackgroundResource(R.drawable.btn_application_start);
				holder.ic_type_value.setText(R.string.application_item_start);
			} else {
				holder.ic_type.setBackgroundResource(R.drawable.btn_application_download);
				holder.ic_type_value.setText(R.string.application_item_download);
			}
		} else if (name.equals(context.getText(R.string.application_video_tx))) {
			holder.icon.setBackgroundResource(R.drawable.icon_tencent);
			holder.name.setText(context.getText(R.string.application_video_tx));
			if (listDatas.get(position).isInstall()) {
				holder.ic_type.setBackgroundResource(R.drawable.btn_application_start);
				holder.ic_type_value.setText(R.string.application_item_start);
			} else {
				holder.ic_type.setBackgroundResource(R.drawable.btn_application_download);
				holder.ic_type_value.setText(R.string.application_item_download);
			}
		} else if (name.equals(context.getText(R.string.application_video_pps))) {
			holder.icon.setBackgroundResource(R.drawable.icon_pps);
			holder.name.setText(context.getText(R.string.application_video_pps));
			if (listDatas.get(position).isInstall()) {
				holder.ic_type.setBackgroundResource(R.drawable.btn_application_start);
				holder.ic_type_value.setText(R.string.application_item_start);
			} else {
				holder.ic_type.setBackgroundResource(R.drawable.btn_application_download);
				holder.ic_type_value.setText(R.string.application_item_download);
			}
		} else if (name.equals(context.getText(R.string.application_video_pps))) {
			holder.icon.setBackgroundResource(R.drawable.icon_pps);
			holder.name.setText(context.getText(R.string.application_video_pps));
			if (listDatas.get(position).isInstall()) {
				holder.ic_type.setBackgroundResource(R.drawable.btn_application_start);
				holder.ic_type_value.setText(R.string.application_item_start);
			} else {
				holder.ic_type.setBackgroundResource(R.drawable.btn_application_download);
				holder.ic_type_value.setText(R.string.application_item_download);
			}
		} else if (name.equals(context.getText(R.string.application_video_pptv))) {
			holder.icon.setBackgroundResource(R.drawable.icon_pptv);
			holder.name.setText(context.getText(R.string.application_video_pptv));
			if (listDatas.get(position).isInstall()) {
				holder.ic_type.setBackgroundResource(R.drawable.btn_application_start);
				holder.ic_type_value.setText(R.string.application_item_start);
			} else {
				holder.ic_type.setBackgroundResource(R.drawable.btn_application_download);
				holder.ic_type_value.setText(R.string.application_item_download);
			}
		} else if (name.equals(context.getText(R.string.application_video_sohu))) {
			holder.icon.setBackgroundResource(R.drawable.icon_sohu);
			holder.name.setText(context.getText(R.string.application_video_sohu));
			if (listDatas.get(position).isInstall()) {
				holder.ic_type.setBackgroundResource(R.drawable.btn_application_start);
				holder.ic_type_value.setText(R.string.application_item_start);
			} else {
				holder.ic_type.setBackgroundResource(R.drawable.btn_application_download);
				holder.ic_type_value.setText(R.string.application_item_download);
			}
		}
		return convertView;
	}

	/**
	 * class ViewHolder
	 */
	private class ViewHolder {
		ImageView icon;
		TextView name;
		ImageView ic_type;
		TextView ic_type_value;
	}

}
