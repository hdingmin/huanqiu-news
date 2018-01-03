package com.news.huanqiu.utils;

import java.io.Serializable;

public class ResultBase implements Serializable {
	private static final long serialVersionUID = 4450573784578247466L;
	private int code = 0;
	private String msg = "";

	public ResultBase() {

	}

	public ResultBase(String msg) {
		this.msg = msg;
	}

	public ResultBase(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
