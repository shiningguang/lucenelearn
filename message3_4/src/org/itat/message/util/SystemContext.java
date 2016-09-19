package org.itat.message.util;

public class SystemContext {
	private static ThreadLocal<Integer> pageSize = new ThreadLocal<Integer>();
	private static ThreadLocal<Integer> pageOffset = new ThreadLocal<Integer>();
	private static ThreadLocal<String> sort = new ThreadLocal<String>();
	private static ThreadLocal<String> order = new ThreadLocal<String>();
	private static ThreadLocal<String> realPath = new ThreadLocal<String>();
	
	public static String getRealPath() {
		return realPath.get();
	}
	
	public static void setRealPath(String _realPath) {
		realPath.set(_realPath);
	}
	
	public static void removeRealPath() {
		realPath.remove();
	}
	
	/**
	 * 每页显示多数条
	 * @return
	 */
	public static int getPageSize() {
		return pageSize.get();
	}
	/**
	 * 设置每页显示多少条信息
	 * @param _pageSize
	 */
	public static void setPageSize(int _pageSize) {
		pageSize.set(_pageSize);
	}
	/**
	 * 移除对象
	 */
	public static void removePageSize() {
		pageSize.remove();
	}
	/**
	 * 获取limit的start的值
	 * @return
	 */
	public static int getPageOffset() {
		return pageOffset.get();
	}
	/**
	 * 设置limit的start的值
	 * @param _pageOffset
	 */
	public static void setPageOffset(int _pageOffset) {
		pageOffset.set(_pageOffset);
	}
	
	public static void removePageOffset() {
		pageOffset.remove();
	}
	/**
	 * 获取排序的字段
	 * @return
	 */
	public static String getSort() {
		return sort.get();
	}
	/**
	 * 设置排序的字段
	 * @param _sort
	 */
	public static void setSort(String _sort) {
		sort.set(_sort);
	}
	
	public static void removeSort() {
		sort.remove();
	}
	
	/**
	 * 获取排序的顺序,asc或者desc
	 * @return
	 */
	public static String getOrder() {
		return order.get();
	}
	/**
	 * 设置排序的顺序
	 * @param _order
	 */
	public static void setOrder(String _order) {
		order.set(_order);
	}
	
	public static void removeOrder() {
		order.remove();
	}
	
}
