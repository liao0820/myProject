package com.web.oa.utils;

import java.util.List;

public class Page<T> {
private int pageSize=3;
private int pageNow;
private int rowCount; 
private int pageCount;
private List<T> list;
public int getPageSize() {
	return pageSize;
}
public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
	setPageCount();
}
public int getPageNow() {
	return pageNow;
}
public void setPageNow(int pageNow) {
	this.pageNow = pageNow;
}
public int getRowCount() {
	return rowCount;
}
public void setRowCount(int rowCount) {
	this.rowCount = rowCount;
	setPageCount();
}
public int getPageCount() {
	return pageCount;
}
public void setPageCount() {
	this.pageCount = rowCount%pageSize==0?rowCount/pageSize:rowCount/pageSize+1;
}
public List<T> getList() {
	return list;
}
public void setList(List<T> list) {
	this.list = list;
}
}
