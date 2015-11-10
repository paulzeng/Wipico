package com.chinasvc.wipicophone.bean;

import com.chinasvc.wipicophone.service.User;

public class PopMenu {

	public enum PopType {
		LOCAL_PLAY // 本地播放
		, REMOTE_PLAY // 远程播放
		, SHARE // 分享
		, MULTI // 多选
		, DELETE // 删除
		, CANCEL // 取消
		, PROPERTY// 属性
		, USER// 用户
	};

	private PopType type;
	private String name;

	private User user;

	public PopMenu() {
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PopMenu(PopType type, String name) {
		this.type = type;
		this.name = name;
	}

	public PopMenu(PopType type, String name, User user) {
		this.type = type;
		this.name = name;
		this.user = user;
	}

	public PopType getType() {
		return type;
	}

	public void setType(PopType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "PopMenu [type=" + type + ", name=" + name + ", user=" + user + "]";
	}

}
