package com.xing.server;

import com.xing.http.connector.HttpConnector;

public final class Bootstrap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello everyone, Xing server is starting...");
		HttpConnector httpConnector=new HttpConnector();
		httpConnector.start();
	}

}
