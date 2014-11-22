package com.xing.container;

/**
 * 上下文容器接口，一个上下文代表一个WEB应用系统，也可以称之为网站
 * @author Leo
 *
 */
public interface Context extends Container {
	public void addWrapper();
	
	public void createWrapper();

}
