package com.chinasvc.wipicophone.adapter;

import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.chinasvc.wipicophone.R;
import com.chinasvc.wipico.bean.FileInfo;
import com.chinasvc.wipicophone.util.AlphComparator;
import com.chinasvc.wipicophone.util.MultimediaUtil;

@SuppressLint("DefaultLocale")
public class MusicAdapter extends BaseAdapter implements SectionIndexer {

	private List<FileInfo> listDatas = null;
	private Context mContext;

	public MusicAdapter(Context mContext, List<FileInfo> list) {
		this.mContext = mContext;
		this.listDatas = list;
		AlphComparator comparator = new AlphComparator();
		Collections.sort(this.listDatas, comparator);
	}

	public int getCount() {
		return this.listDatas.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.activity_local_audio_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.image = (ImageView) view.findViewById(R.id.image);
			viewHolder.duration = (TextView) view.findViewById(R.id.duration);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final FileInfo bean = listDatas.get(position);

		if (position == 0) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(bean.getLetter());
		} else {
			String lastCatalog = listDatas.get(position - 1).getLetter();
			if (bean.getLetter().equals(lastCatalog)) {
				viewHolder.tvLetter.setVisibility(View.GONE);
			} else {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(bean.getLetter());
			}
		}

		viewHolder.tvTitle.setText(bean.getName());
		viewHolder.duration.setText(MultimediaUtil.showTime(bean.getDuration()));
		return view;
	}

	final static class ViewHolder {
		ImageView image;
		TextView tvTitle;
		TextView tvLetter;
		TextView duration;
	}

	public Object[] getSections() {
		return null;
	}

	public int getSectionForPosition(int position) {
		return 0;
	}

	public int getPositionForSection(int section) {
		FileInfo bean;
		String l;
		if (section == '#') {
			return 0;
		} else {
			for (int i = 0; i < getCount(); i++) {
				bean = (FileInfo) listDatas.get(i);
				l = bean.getLetter();
				char firstChar = l.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}
		}
		bean = null;
		l = null;
		return -1;
	}

}