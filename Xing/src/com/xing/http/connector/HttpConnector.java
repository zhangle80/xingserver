package com.xing.http.connector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xing.container.IContainer;
import com.xing.http.HttpProcessor;

/**
 * @author Leo
 * HTTP链接类，启动进程处理客户端socket请求
 * 
 */
public class HttpConnector implements Runnable,IConnector {
	boolean termination;			//是否结束
	
	public void setTermination(boolean termination){
		this.termination=termination;
	}
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		int port = 8080;
		
		try {
			serverSocket=new ServerSocket(port,1,InetAddress.getByName("127.0.0.1"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		while(!this.termination){
			Socket socket=null;	
			try {
				socket=serverSocket.accept();				
			} catch (IOException e) {
				e.printStackTrace();
			}
			HttpProcessor httpProcessor = new HttpProcessor(this);
			httpProcessor.process(socket);
		}
	}
	
	public void start(){
		Thread connectorThread=new Thread(this);
		connectorThread.start();
	}
	@Override
	public HttpServletRequest createRequest() {
		return null;
	}
	@Override
	public HttpServletResponse createResponse() {
		return null;
	}
	@Override
	public IContainer getContainer() {
		return null;
	}
	@Override
	public void setContainer(IContainer container) {
		// TODO Auto-generated method stub
		
	}
}
