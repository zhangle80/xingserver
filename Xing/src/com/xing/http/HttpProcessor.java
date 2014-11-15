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
public class HttpProcessor implements Runnable {
	private HttpConnector connector;
	private HttpRequest request;
	private HttpResponse response;

	private static final String SHUTDOWN_COMMAND="/shutdown";	
	private boolean shutdown = false;	//连接器停止
	private boolean stop=false;			//自己停止
	
	private Socket socket;
	private boolean available=false;
	
	public HttpProcessor(HttpConnector connector){
		this.connector=connector;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * 线程运行
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!this.stop){
			Socket socket=await();
			if(socket==null){
				continue;
			}
			this.process(socket);
			this.connector.recycle(this);
		}
		//关闭
	}
	
	/**
	 * 指派Socket给新的流程，但是要判断上一次的socket是否处理完毕，同时该方法要做同步处理
	 * 同步的原因是每个Processor在顺利提交Socket之后都会回收利用
	 */
	public synchronized void assign(Socket socket){
		while(this.available){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.socket=socket;
		this.available=true;
		notifyAll();
	}
	
	private synchronized Socket await() {
		// TODO Auto-generated method stub
		while(!this.available){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Socket socket=this.socket;
		this.available=false;
		notifyAll();
		if(socket!=null){
			System.out.println("The incoming request has been awaited");
		}
		return socket;
	}

	private void process(Socket socket){
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
