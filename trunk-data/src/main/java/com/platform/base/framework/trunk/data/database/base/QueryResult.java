package com.platform.base.framework.trunk.data.database.base;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果集
 */
public class QueryResult<T> implements Serializable {

	private static final long serialVersionUID = 461900815434592315L;
	
	private List<T> list;
	private long total;

	public QueryResult() {

	}

	public QueryResult(List<T> list, long total) {
		super();
		this.list = list;
		this.total = total;
	}

	public List<T> getlist() {
		return list;
	}

	public void setlist(List<T> list) {
		this.list = list;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
