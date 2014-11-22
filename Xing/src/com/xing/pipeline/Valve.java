package com.xing.pipeline;

import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

/**
 * 阀门接口，用于安装在流水线中以执行一系列功能
 * @author Leo
 *
 */
public interface Valve {

	public String getInfo();
	
	public void invoke(HttpRequest request, HttpResponse response,ValveContext valveContext);
	
}
