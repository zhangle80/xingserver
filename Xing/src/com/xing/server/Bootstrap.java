package com.xing.server;

import com.xing.classloader.SimpleLoader;
import com.xing.container.SimpleWrapper;
import com.xing.container.Wrapper;
import com.xing.http.connector.HttpConnector;
import com.xing.pipeline.Pipeline;
import com.xing.pipeline.RequestDateTimeValve;
import com.xing.pipeline.Valve;

public final class Bootstrap {

	/**
	 * 系统入口方法
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello everyone, Xing server is starting...");
		HttpConnector httpConnector=new HttpConnector();
		
		Wrapper wrapper=new SimpleWrapper();
		wrapper.setServletClass("ThirdServlet");
		Valve requestDateTimeValve=new RequestDateTimeValve();
		((Pipeline)wrapper).addValve(requestDateTimeValve);
		
		SimpleLoader loader=new SimpleLoader();
		wrapper.setLoader(loader);
		
		httpConnector.setContainer(wrapper);
		httpConnector.start();
	}

}
