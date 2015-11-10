package com.chinasvc.wipicophone.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.bean.PopMenu;
import com.chinasvc.wipicophone.bean.PopMenu.PopType;

/**
 * 文件列表适配器
 * */
@SuppressLint("DefaultLocale")
public class PopMenuAdapter extends BaseAdapter {

	private List<PopMenu> listDatas;
	public LayoutInflater layoutInflater;

	public PopMenuAdapter(Context context, List<PopMenu> listDatas) {
		layoutInflater = LayoutInflater.from(context);
		this.listDatas = listDatas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listDatas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listDatas.get(arg0);
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.pop_list_menu_item, null);
			holder.fileType = (ImageView) convertView.findViewById(R.id.imageView);
			holder.fileName = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PopMenu bean = listDatas.get(position);
		if (bean.getType() == PopType.LOCAL_PLAY) {
			holder.fileType.setImageResource(R.drawable.pop_local_play);
		} else if (bean.getType() == PopType.REMOTE_PLAY) {
			holder.fileType.setImageResource(R.drawable.pop_push_remote);
		} else if (bean.getType() == PopType.USER) {
			holder.fileType.setImageResource(R.drawable.pop_ic_phone);
		} else if (bean.getType() == PopType.MULTI) {
			holder.fileType.setImageResource(R.drawable.pop_ic_multi);
		} else if (bean.getType() == PopType.SHARE) {
			holder.fileType.setImageResource(R.drawable.pop_ic_share);
		} else if (bean.getType() == PopType.DELETE) {
			holder.fileType.setImageResource(R.drawable.pop_ic_delete);
		} else if (bean.getType() == PopType.CANCEL) {
			holder.fileType.setImageResource(R.drawable.pop_ic_delete);
		} else if (bean.getType() == PopType.PROPERTY) {
			holder.fileType.setImageResource(R.drawable.pop_ic_property);
		}
		holder.fileName.setText(bean.getName());
		return convertView;
	}

	/**
	 * class ViewHolder
	 */
	private class ViewHolder {
		ImageView fileType;
		TextView fileName;
	}

}
