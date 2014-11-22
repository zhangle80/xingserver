package com.xing.server;

import com.xing.container.Container;
import com.xing.container.SimpleContainer;
import com.xing.http.connector.HttpConnector;

public final class Bootstrap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello everyone, Xing server is starting...");
		HttpConnector httpConnector=new HttpConnector();
		Container container=new SimpleContainer();
		httpConnector.setContainer(container);
		httpConnector.start();
	}

}
