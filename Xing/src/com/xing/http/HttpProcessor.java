package com.xing.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.servlet.ServletException;

import com.xing.handle.AbstractProcessor;
import com.xing.handle.ServletProcessor;
import com.xing.handle.StaticResourceProcessor;
import com.xing.http.connector.HttpConnector;
import com.xing.http.connector.SocketInputStream;

/**
 * @author Leo
 * Http处理类，处理Connector接收到的客户端Socket请求
 */
public class HttpProcessor {
	private HttpConnector connector;
	private HttpRequest request;
	private HttpResponse response;

	private static final String SHUTDOWN_COMMAND="/shutdown";	
	private boolean shutdown = false;
	
	public HttpProcessor(HttpConnector connector){
		this.connector=connector;
	}
	
	public void process(Socket socket){
		if(socket==null){
			return;
		}
		System.out.println("new link socket is coming...");
		SocketInputStream input=null;
		OutputStream output=null;
		try {
			input=new SocketInputStream(socket.getInputStream(),2048);
			output=socket.getOutputStream();
			
			this.handleAccept(input, output);
			socket.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			System.out.println("this link socket is over...");
		}

	}
	
	private void handleAccept(SocketInputStream input,OutputStream output) throws IOException{		
		this.request=new HttpRequest(input);		
		this.response=new HttpResponse(output);
		this.response.setRequest(this.request);
		this.response.setHeader("Server", "Pyrmont Servlet Container");
		
		try {
			this.request.parse();//主要包括parseRequest(解析请求行)和parseHeader(解析头部)
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (ServletException e) {
			e.printStackTrace();
			return;
		}
		
		String uri=this.request.getRequestURI();
		System.out.println("uri="+uri);
		shutdown=uri.equals(SHUTDOWN_COMMAND);
		this.connector.setTermination(shutdown);
		if(shutdown){
			return;
		}
			
		AbstractProcessor processor;
		if(uri.startsWith("/servlet/")){
			processor=new ServletProcessor(this.request,this.response);
		}else{
			processor=new StaticResourceProcessor(this.request,this.response);
		}
		processor.process();
	}
}
