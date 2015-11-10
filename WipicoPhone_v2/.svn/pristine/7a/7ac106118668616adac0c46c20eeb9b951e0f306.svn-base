package com.chinasvc.wipicophone.adapter;

import java.util.List;

import com.chinasvc.wipicophone.R;
import com.chinasvc.wipicophone.bean.History;
import com.chinasvc.wipicophone.util.DateUtil;
import com.chinasvc.wipicophone.util.FileTools;
import com.chinasvc.wipicophone.util.FileUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 文件列表适配器
 * */
@SuppressLint("DefaultLocale")
public class HistoryAdapter extends BaseAdapter {

	private List<History> listFiles;
	public LayoutInflater layoutInflater;
	private Context mContext;

	public HistoryAdapter(Context context, List<History> listFiles) {
		mContext = context;
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
		ViewHolder holder = null;
		History bean = listFiles.get(position);
		if (convertView == null) {
			if (bean.getTransfer() == History.TRANSFER_RECEIVE) {
				holder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.activity_history_left_item, null);
				holder.type = (ImageView) convertView.findViewById(R.id.type);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.value = (TextView) convertView.findViewById(R.id.value);
				holder.user = (TextView) convertView.findViewById(R.id.user);
				holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
				convertView.setTag(R.id.history_left, holder);
			} else if (bean.getTransfer() == History.TRANSFER_SEND) {
				holder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.activity_history_right_item, null);
				holder.type = (ImageView) convertView.findViewById(R.id.type);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.value = (TextView) convertView.findViewById(R.id.value);
				holder.user = (TextView) convertView.findViewById(R.id.user);
				holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
				convertView.setTag(R.id.history_right, holder);
			}
		} else {
			if (bean.getTransfer() == History.TRANSFER_RECEIVE) {
				if (convertView.getTag(R.id.history_left) == null) {
					holder = new ViewHolder();
					convertView = layoutInflater.inflate(R.layout.activity_history_left_item, null);
					holder.type = (ImageView) convertView.findViewById(R.id.type);
					holder.title = (TextView) convertView.findViewById(R.id.title);
					holder.value = (TextView) convertView.findViewById(R.id.value);
					holder.user = (TextView) convertView.findViewById(R.id.user);
					holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
					convertView.setTag(R.id.history_left, holder);
				} else {
					holder = (ViewHolder) convertView.getTag(R.id.history_left);
				}
			} else if (bean.getTransfer() == History.TRANSFER_SEND) {
				if (convertView.getTag(R.id.history_right) == null) {
					holder = new ViewHolder();
					convertView = layoutInflater.inflate(R.layout.activity_history_right_item, null);
					holder.type = (ImageView) convertView.findViewById(R.id.type);
					holder.title = (TextView) convertView.findViewById(R.id.title);
					holder.value = (TextView) convertView.findViewById(R.id.value);
					holder.user = (TextView) convertView.findViewById(R.id.user);
					holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
					convertView.setTag(R.id.history_right, holder);
				} else {
					holder = (ViewHolder) convertView.getTag(R.id.history_right);
				}
			}
		}

		// 设置图标
		if (FileUtils.isImage(bean.getPath())) {
			holder.type.setImageResource(R.drawable.history_image);
		} else if (FileUtils.isVideo(bean.getPath())) {
			holder.type.setImageResource(R.drawable.history_video);
		} else if (FileUtils.isAudio(bean.getPath())) {
			holder.type.setImageResource(R.drawable.history_audio);
		} else if (FileUtils.isExcel(bean.getPath())) {
			holder.type.setImageResource(R.drawable.history_excel);
		} else if (FileUtils.isPdf(bean.getPath())) {
			holder.type.setImageResource(R.drawable.history_pdf);
		} else if (FileUtils.isWord(bean.getPath())) {
			holder.type.setImageResource(R.drawable.history_doc);
		} else if (FileUtils.isPPT(bean.getPath())) {
			holder.type.setImageResource(R.drawable.history_ppt);
		} else if (FileUtils.isTXT(bean.getPath())) {
			holder.type.setImageResource(R.drawable.history_txt);
		} else {
			holder.type.setImageResource(R.drawable.history_unknow);
		}

		if (bean.getProgress() == 100) {
			bean.setState(History.STATE_FINISH);
		}

		if (bean.getTransfer() == History.TRANSFER_RECEIVE) {
			holder.user.setText(DateUtil.getTime(Long.parseLong(bean.getTime())) + "  " + mContext.getString(R.string.history_receive_from) + bean.getUserName());
			if (bean.getState() == History.STATE_WAIT) {
				holder.progressBar.setVisibility(View.VISIBLE);
				holder.progressBar.setProgress(bean.getProgress());
				holder.value.setText(mContext.getString(R.string.history_waiting_to_receive_files) + " | " + FileTools.formetFileSize(bean.getSize()));
			} else if (bean.getState() == History.STATE_TRANSFER) {
				holder.progressBar.setVisibility(View.VISIBLE);
				holder.progressBar.setProgress(bean.getProgress());
				holder.value.setText(bean.getProgress() + "%" + " | " + FileTools.formetFileSize(bean.getSize()));
			} else if (bean.getState() == History.STATE_FINISH) {
				holder.progressBar.setVisibility(View.GONE);
				holder.progressBar.setProgress(bean.getProgress());
				holder.value.setText(FileTools.formetFileSize(bean.getSize()));
			} else if (bean.getState() == History.STATE_FAIL) {
				holder.progressBar.setVisibility(View.GONE);
				holder.progressBar.setProgress(bean.getProgress());
				holder.value.setText(mContext.getString(R.string.history_receive_fail) + " | " + FileTools.formetFileSize(bean.getSize()));
			}
		} else if (bean.getTransfer() == History.TRANSFER_SEND) {
			holder.user.setText(DateUtil.getTime(Long.parseLong(bean.getTime())) + "  " + mContext.getString(R.string.history_send_to) + bean.getUserName());
			if (bean.getState() == History.STATE_WAIT) {
				holder.progressBar.setVisibility(View.VISIBLE);
				holder.progressBar.setProgress(bean.getProgress());
				holder.value.setText(mContext.getString(R.string.history_waiting_to_send_the_file) + " | " + FileTools.formetFileSize(bean.getSize()));
			} else if (bean.getState() == History.STATE_TRANSFER) {
				holder.progressBar.setVisibility(View.VISIBLE);
				holder.progressBar.setProgress(bean.getProgress());
				holder.value.setText(bean.getProgress() + "%" + " | " + FileTools.formetFileSize(bean.getSize()));
			} else if (bean.getState() == History.STATE_FINISH) {
				holder.progressBar.setVisibility(View.GONE);
				holder.progressBar.setProgress(bean.getProgress());
				holder.value.setText(FileTools.formetFileSize(bean.getSize()));
			} else if (bean.getState() == History.STATE_FAIL) {
				holder.progressBar.setVisibility(View.GONE);
				holder.progressBar.setProgress(bean.getProgress());
				holder.value.setText(mContext.getString(R.string.history_send_fail) + " | " + FileTools.formetFileSize(bean.getSize()));
			}
		}
		holder.title.setText(bean.getName());
		return convertView;
	}

	/**
	 * class ViewHolder
	 */
	private class ViewHolder {
		ImageView type;
		TextView title;
		TextView value;
		TextView user;
		ProgressBar progressBar;
	}

}
