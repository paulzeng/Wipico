package com.chinasvc.wipicophone.adapter;

import java.util.List;

import com.chinasvc.wipico.bean.Device;
import com.chinasvc.wipicophone.R;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

/**
 * PopWindow item 适配器
 * 
 * @author billy
 * */
public class WlanAdapter extends BaseAdapter {

	private List<ScanResult> listDevices;
	private Context context;
	public LayoutInflater layoutInflater;

	public WlanAdapter() {
	}

	public WlanAdapter(Context context, List<ScanResult> listDevices) {
		this.listDevices = listDevices;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listDevices.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listDevices.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		layoutInflater = LayoutInflater.from(context);
		convertView = layoutInflater.inflate(R.layout.dialog_connect_list_item, null);
		holder = new ViewHolder();
		holder.item = (CheckedTextView) convertView.findViewById(R.id.item);
		holder.item.setText(listDevices.get(position).SSID);
		return convertView;
	}

	/** class ViewHolder */
	private class ViewHolder {
		CheckedTextView item;
	}

}
