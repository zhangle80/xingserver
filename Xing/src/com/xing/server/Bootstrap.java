package com.xing.server;

import com.xing.classloader.Loader;
import com.xing.classloader.WebAppLoader;
import com.xing.container.Context;
import com.xing.container.SimpleContext;
import com.xing.container.SimpleContextLifecycleListener;
import com.xing.container.SimpleWrapper;
import com.xing.container.Wrapper;
import com.xing.http.connector.Connector;
import com.xing.http.connector.HttpConnector;
import com.xing.lifecycle.Lifecycle;
import com.xing.lifecycle.LifecycleListener;
import com.xing.logger.FileLogger;
import com.xing.pipeline.Pipeline;
import com.xing.pipeline.RequestDateTimeValve;
import com.xing.pipeline.Valve;

public final class Bootstrap {

	/**
	 * 系统入口方法
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("xing.base.logger", System.getProperty("user.dir"));
		FileLogger logger=new FileLogger("");
		logger.setPrefix("FileLog_");
		logger.setSuffix(".txt");
		logger.setTimestamp(true);
		
		logger.log("Hello everyone, Xing server is starting...");
		Connector connector=new HttpConnector();
		
		Context context = new SimpleContext();
		context.setLog(logger);
		LifecycleListener listener = new SimpleContextLifecycleListener();
		((Lifecycle)context).addLifecycleListener(listener);
		
		Loader loader=new WebAppLoader();
		context.setLoader(loader);
				
		Wrapper wrapper=new SimpleWrapper();
		wrapper.setServletClass("ThirdServlet");
		
		Valve requestDateTimeValve=new RequestDateTimeValve();
		((Pipeline)wrapper).addValve(requestDateTimeValve);
			
		context.addWrapper(wrapper);
		((Lifecycle)context).start();
		
		connector.setContainer(context);
		((Lifecycle)connector).start();		
	}

}
