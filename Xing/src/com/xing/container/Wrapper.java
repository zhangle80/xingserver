package com.xing.container;

import javax.servlet.Servlet;

/**
 * 包装类容器，主要用于包装Servelet，是最底层的容器，一个包装类容器只包装一个Servlet
 * @author Leo
 *
 */
public interface Wrapper extends Container {

	/**
	 * 将包装类定位到它所包装的Servlet实例
	 * @return
	 * @throws javax.servlet.ServletException
	 */
	public Servlet allocate() throws javax.servlet.ServletException;
	
	/**
	 * 负责加载和初始化Servlet实例
	 * @throws javax.servlet.ServletException
	 */
	public void load() throws javax.servlet.ServletException;
	
	public void setServletClass(String servletClass);
	
	public String getName();
}
