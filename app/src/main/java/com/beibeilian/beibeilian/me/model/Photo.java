package com.beibeilian.beibeilian.me.model;

public class Photo {

	public String id;
	public String time;
	public String url;

	public Photo(String id, String time, String url) {
		this.id = id;
		this.time = time;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
