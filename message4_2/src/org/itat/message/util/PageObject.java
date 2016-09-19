package org.itat.message.util;

import java.util.List;

public class PageObject<E> {
	private List<E> datas;
	private int pageSize;
	private int offset;
	private int totalRecord;
	private int totalPage;
	
	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	/**
	 * 分页中所存储的对象列表，使用了泛型
	 * @return 分页中的存储对象
	 */
	public List<E> getDatas() {
		return datas;
	}
	/**
	 * 分页中所存储的对象列表，使用了泛型
	 * @param datas 分页中的存储对象
	 */
	public void setDatas(List<E> datas) {
		this.datas = datas;
	}
	/**
	 * 每页显示多少条信息
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * 设置每页显示的信息数
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * 获取总记录数
	 * @return
	 */
	public int getTotalRecord() {
		return totalRecord;
	}
	/**
	 * 设置总记录数
	 * @param totalRecord
	 */
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	/**
	 * 获取总页数
	 * @return
	 */
	public int getTotalPage() {
		return totalPage;
	}
	/**
	 * 设置总页数
	 * @param totalPage
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	
}
