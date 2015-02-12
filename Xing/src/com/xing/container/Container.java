package com.xing.container;

import com.xing.classloader.SimpleLoader;
import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

public interface Container {
	
	/**
	 * 容器的触发方法，通常是委托其流水线的触发方法
	 * @param request
	 * @param response
	 */
	public void invoke(HttpRequest request, HttpResponse response);
	
	/**
	 * 添加子容器
	 * @param child
	 */
	public void addChild(Container child);
	
	/**
	 * 移除子容器
	 * @param child
	 */
	public void removeChild(Container child);
	
	/**
	 * 根据名称返回子容器
	 * @param name
	 * @return
	 */
	public Container findChild(String name);
	
	/**
	 * 返回所有子容器
	 * @return
	 */
	public Container[] findChildren(); 
	
	public SimpleLoader getLoader();
	
	public void setLoader(SimpleLoader loader);
	
	public void setParent(Container parent);
}
