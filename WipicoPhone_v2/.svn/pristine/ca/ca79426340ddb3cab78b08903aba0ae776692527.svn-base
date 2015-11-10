package com.chinasvc.wipicophone.adapter;

import java.util.List;

import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.bean.GameBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GameIndexAdapter extends BaseAdapter {

	private Context context;
	private List<GameBean> listDatas;
	public LayoutInflater layoutInflater;

	public GameIndexAdapter(Context context, List<GameBean> listFiles) {
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
		holder.ic_type.setBackgroundResource(R.drawable.btn_application_start);
		holder.ic_type_value.setText(R.string.application_item_start);
		holder.icon.setImageBitmap(listDatas.get(position).getIconBitmap());
		holder.name.setText(name);
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
