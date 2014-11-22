package com.xing.pipeline;

import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

/**
 * 流水线接口，每个容器类可以设置一个流水线，通过在流水线中设置阀门来执行一系列类
 * 这种处理方式类似于责任链，但不知为何没有采用责任链方式，而是在使用了一个阀门上下文管理类来
 * 管理这些阀门的执行和流转。
 * @author Leo
 */
public interface Pipeline {
	/**
	 * 设置基本阀门
	 * @param valve
	 * 
	 */
	public void setBasic(Valve valve);
	/**
	 * @return
	 * 获取基本阀门
	 */
	public Valve getBasic();
	/**
	 * @param valve
	 * 安装普通阀门
	 */
	public void addValve(Valve valve);
	/**
	 * @return
	 * 获取所有普通阀门
	 */
	public Valve[] getValve();
	/**
	 * @param valve
	 * 移除阀门
	 */
	public void removeValve(Valve valve);
	/**
	 * 触发流水线方法
	 * @param request
	 * @param response
	 */
	public void invoke(HttpRequest request, HttpResponse response);
}
