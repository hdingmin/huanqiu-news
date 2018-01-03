package com.news.huanqiu.utils;

public class DataResult<T> extends ResultBase {
	private static final long serialVersionUID = -1735832511833830696L;
	private T data;

	public DataResult() {

	}

	public DataResult(T data) {
		this.data = data;
	}

	public DataResult(int code, String msg, T data) {
		super(code, msg);
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
