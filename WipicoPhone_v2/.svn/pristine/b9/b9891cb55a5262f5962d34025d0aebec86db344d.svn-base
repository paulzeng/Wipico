package com.chinasvc.wipicophone.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class GameBean {

	private long id;
	private String gamePackage;
	private String picUrl;
	private Bitmap iconBitmap;
	private Drawable iconDrawable;
	private String name;
	private GameState gameState = GameState.UNINSTALL;
	private String downUrl;

	public static enum GameState {
		UNINSTALL, INSTALL, INSTALLING, UNINSTALLING;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gamePackage == null) ? 0 : gamePackage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameBean other = (GameBean) obj;
		if (gamePackage == null) {
			if (other.gamePackage != null)
				return false;
		} else if (!gamePackage.equals(other.gamePackage))
			return false;
		return true;
	}

	public String getDownUrl() {
		return downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public Drawable getIconDrawable() {
		return iconDrawable;
	}

	public void setIconDrawable(Drawable iconDrawable) {
		this.iconDrawable = iconDrawable;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGamePackage() {
		return gamePackage;
	}

	public void setGamePackage(String gamePackage) {
		this.gamePackage = gamePackage;
	}

	public Bitmap getIconBitmap() {
		return iconBitmap;
	}

	public void setIconBitmap(Bitmap iconBitmap) {
		this.iconBitmap = iconBitmap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "GameBean [id=" + id + ", gamePackage=" + gamePackage + ", picUrl=" + picUrl + ", iconBitmap=" + iconBitmap + ", iconDrawable=" + iconDrawable + ", name=" + name
				+ ", gameState=" + gameState + ", downUrl=" + downUrl + "]";
	}

}
