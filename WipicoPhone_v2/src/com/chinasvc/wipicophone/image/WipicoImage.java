package com.chinasvc.wipicophone.image;

public class WipicoImage {

	private String path;
	private String url;
	private String name;
	private boolean isPlay = false;
	private int size;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPlay() {
		return isPlay;
	}

	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}

	@Override
	public String toString() {
		return "WipicoImage [path=" + path + ", url=" + url + ", name=" + name + ", isPlay=" + isPlay + ", size=" + size + "]";
	}

}
