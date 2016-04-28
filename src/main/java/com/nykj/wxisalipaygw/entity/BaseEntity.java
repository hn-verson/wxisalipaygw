package com.nykj.wxisalipaygw.entity;

import java.io.Serializable;

/**
 * 分页查询条件的实体类
 * @author yuejing
 * @date 2013-8-10 下午5:16:33
 * @version V1.0.0
 */
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -4495987129630008126L;

	private Integer page;

	private Integer size;

	private Integer firstPage;
	
	private Integer orclBegNum;
	private Integer orclEndNum;

	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		if(size == null) {
			size = 10;
		}
		this.size = size;
	}
	public Integer getFirstPage() {
		if(firstPage != null) {
			return firstPage;
		}
		else if(firstPage == null && page != null && size != null) {
			return (page - 1) * size;
		}
		return firstPage;
	}
	public void setFirstPage(Integer firstPage) {
		if(firstPage != null) {
			this.firstPage = firstPage;
		}
		else if(firstPage == null && page != null && size != null) {
			this.firstPage = (page - 1) * size;
		}
	}
	public Integer getOrclBegNum() {
		if(orclBegNum == null && page != null && size != null) {
			orclBegNum = ( (page - 1) * size) + 1;
		}
		return orclBegNum;
	}
	public void setOrclBegNum(Integer orclBegNum) {
		this.orclBegNum = orclBegNum;
	}
	public Integer getOrclEndNum() {
		if(orclEndNum == null && page != null && size != null) {
			orclEndNum = page * size;
		}
		return orclEndNum;
	}
	public void setOrclEndNum(Integer orclEndNum) {
		this.orclEndNum = orclEndNum;
	}
}