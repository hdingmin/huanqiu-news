package com.news.huanqiu.utils;

import java.util.List;

public class PageResult<T> extends ResultBase {
	private static final long serialVersionUID = 7408150035520840246L;
	private int pageIndex;
	private int pageSize;
	private int total;
	private List<T> rows;

	public PageResult() {
		super();
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
