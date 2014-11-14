package com.xing.server;

import com.xing.http.connector.HttpConnector;

/**
 * @author Leo
 * 该类过期无用
 */
public class HttpServer {
		
	public static void main(String[] args){
		HttpServer server =new HttpServer();
		server.await();
	}
	public void await(){
		HttpConnector httpConnector=new HttpConnector();
		httpConnector.start();
	}
}
