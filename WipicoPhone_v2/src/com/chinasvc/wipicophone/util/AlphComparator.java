package com.chinasvc.wipicophone.util;

import java.util.Comparator;

import com.chinasvc.wipicophone.bean.AudioBean;

public class AlphComparator implements Comparator<Object> {

	public int compare(Object arg0, Object arg1) {
		AudioBean user0 = (AudioBean) arg0;
		AudioBean user1 = (AudioBean) arg1;
		int flag = user0.getLetter().compareTo(user1.getLetter());
		return flag;
	}

}
