package com.xing.server;

/**
 * @author leo
 *	生命周期函数
 */
public interface Lifecycle {
	
	/**
	 * 初始化
	 */
	public void initialize();
	
	/**
	 * 开始
	 */
	public void start();

}
