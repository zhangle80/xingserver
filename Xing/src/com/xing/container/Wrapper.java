package com.xing.container;

import javax.servlet.Servlet;

/**
 * ��װ����������Ҫ���ڰ�װServelet������ײ��������һ����װ������ֻ��װһ��Servlet
 * @author Leo
 *
 */
public interface Wrapper extends Container {

	/**
	 * ����װ�ඨλ��������װ��Servletʵ��
	 * @return
	 * @throws javax.servlet.ServletException
	 */
	public Servlet allocate() throws javax.servlet.ServletException;
	
	/**
	 * ������غͳ�ʼ��Servletʵ��
	 * @throws javax.servlet.ServletException
	 */
	public void load() throws javax.servlet.ServletException;
	
	public void setServletClass(String servletClass);
	
	public String getName();
}
