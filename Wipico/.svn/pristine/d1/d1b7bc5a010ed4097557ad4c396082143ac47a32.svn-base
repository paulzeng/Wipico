package com.chinasvc.wipico.tools;

import com.chinasvc.wipico.util.WipicoConstant;

import android.content.Intent;
import android.os.Bundle;

/**
 * 多媒体启动器
 * */
public class MediaInitiator {

	/**
	 * @param protocolType
	 *                平台类型
	 * @param intentAction
	 *                action
	 * @param url
	 *                链接
	 * @param ip
	 *                请求 ip
	 * */
	public static Intent startApp(int protocolType, String intentAction, String url, String ip) {
		Intent intent = new Intent(intentAction);
		Bundle bundle = new Bundle();
		bundle.putString(WipicoConstant.BUNDLE_MEDIA_URL_KEY, url);
		bundle.putInt(WipicoConstant.BUNDLE_PROTOCOL_TYPE_KEY, protocolType);
		bundle.putString(WipicoConstant.BUNDLE_PHONE_IP_KEY, ip);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

}
