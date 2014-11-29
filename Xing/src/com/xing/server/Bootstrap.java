package com.xing.server;

import com.xing.classloader.SimpleLoader;
import com.xing.container.Context;
import com.xing.container.SimpleContext;
import com.xing.container.SimpleContextLifecycleListener;
import com.xing.container.SimpleWrapper;
import com.xing.container.Wrapper;
import com.xing.http.connector.HttpConnector;
import com.xing.lifecycle.Lifecycle;
import com.xing.lifecycle.LifecycleListener;
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
		
		Context context = new SimpleContext();
		LifecycleListener listener = new SimpleContextLifecycleListener();
		((Lifecycle)context).addLifecycleListener(listener);
		
		Wrapper wrapper=new SimpleWrapper();
		wrapper.setServletClass("ThirdServlet");
		
		Valve requestDateTimeValve=new RequestDateTimeValve();
		((Pipeline)wrapper).addValve(requestDateTimeValve);
		
		SimpleLoader loader=new SimpleLoader();
		wrapper.setLoader(loader);
		
		context.addWrapper(wrapper);
		((Lifecycle)context).start();
		
		httpConnector.setContainer(context);
		httpConnector.start();
	}

}
