package com.beibeilian.beibeilian.me.model;
/**
 *
 * 版本更新实体类
 *
 */
public class Version {

	private String Code;//版本号

	private String Size;//文件大小

	private String Url;//更新地址

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Version() {
	}

	public Version(String code, String size, String url) {
		Code = code;
		Size = size;
		Url = url;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}


	public String getSize() {
		return Size;
	}

	public void setSize(String size) {
		Size = size;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}
}
