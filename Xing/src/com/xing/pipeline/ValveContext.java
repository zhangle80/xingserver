package com.xing.pipeline;

import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

/**
 * 阀门上下文管理类接口，阀门上下文主要用于管理流水线中阀门的执行、流转等功能
 * @author Leo
 *
 */
public interface ValveContext {
	public String getInfo();
	
	/**
	 * 执行并流转到下一个阀门
	 * @param request
	 * @param response
	 */
	public void invokeNext(HttpRequest request, HttpResponse response);

}
