package com.chinasvc.wipicophone.async;

import java.io.File;

import android.os.AsyncTask;

import com.chinasvc.wipicophone.async.AsyncCallBack.UploadCallBack;
import com.chinasvc.wipico.bean.FileInfo;
import com.chinasvc.wipico.file.WipicoFileClient;

/**
 * 本地文件列表的异步机制类
 * 
 * @author billy
 * */
public class OfficeUploadAsyncTask extends AsyncTask<Void, Void, Void> {

	private UploadCallBack callBack;

	private FileInfo bean;

	private WipicoFileClient mWipicoFileClient;

	private String workPath;// 工作路径

	public OfficeUploadAsyncTask(UploadCallBack callBack, String workPath, FileInfo listDatas, WipicoFileClient ftpClientUtil) {
		this.callBack = callBack;
		this.bean = listDatas;
		this.workPath = workPath;
		this.mWipicoFileClient = ftpClientUtil;
	}

	@Override
	protected void onPreExecute() {
		callBack.preUploadView();
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... voidList) {
		callBack.startUploadData();
		mWipicoFileClient.upFolder(workPath, new File(bean.getPath()));
		return null;
	}

	@Override
	protected void onPostExecute(Void vale) {
		callBack.completeUploadView(bean.getName());
	}

}
