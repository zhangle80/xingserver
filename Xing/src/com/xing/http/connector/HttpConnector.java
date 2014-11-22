package com.xing.http.connector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xing.container.Container;
import com.xing.http.HttpProcessor;
import com.xing.server.Constants;
import com.xing.server.Lifecycle;

/**
 * @author Leo
 * HTTP链接类，启动进程处理客户端socket请求
 * 
 */
public class HttpConnector implements Runnable,Connector,Lifecycle {
	boolean termination;			//是否结束
	private Stack<HttpProcessor> processorsPool;
	private int curProcessors=0;
	private Container container;
	
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
				HttpProcessor httpProcessor = dispatchProcessor();
				if(httpProcessor==null){
					System.out.println("processor is busy,has no processor");
					socket.close();
				}else{
					httpProcessor.assign(socket);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void start(){
		Thread connectorThread=new Thread(this);
		connectorThread.start();
		initHttpProcessorStack();
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
	public Container getContainer() {
		return this.container;
	}
	@Override
	public void setContainer(Container container) {
		// TODO Auto-generated method stub
		this.container=container;
	}
	
	/**
	 * 初始化处理器池，按照处理器最小值来初始化
	 */
	private void initHttpProcessorStack(){
		if(processorsPool==null){
			processorsPool=new Stack<HttpProcessor>();
		}
		while(curProcessors<Constants.MIN_PROCESSORS){
			if((Constants.MAX_PROCESSORS>0)&&curProcessors>Constants.MAX_PROCESSORS){
				break;
			}
			HttpProcessor processor=newProcessor();
			recycle(processor);
		}
	}
	/**
	 * 循环利用
	 * @param processor
	 */
	public void recycle(HttpProcessor processor) {
		this.processorsPool.push(processor);
		System.out.println("I am be recycled,and i'm is "+processor.getProcessorId());
		System.out.println("current pool has "+this.processorsPool.size()+" processors");
	}
	/**
	 * 生成新处理器
	 * @return
	 */
	private HttpProcessor newProcessor() {
		HttpProcessor processor=new HttpProcessor(this);
		processor.start();		//启动线程
		this.curProcessors+=1;
		return processor;
	}

	/**
	 * 分配处理器
	 * @return
	 */
	private HttpProcessor dispatchProcessor(){
		HttpProcessor processor;
		processor=this.processorsPool.pop();
		
		System.out.println("I am be dispatch,and i'm is "+processor.getProcessorId());
		System.out.println("current pool has "+this.processorsPool.size()+" processors");
		
		if(processor==null&&this.curProcessors<Constants.MAX_PROCESSORS){
			processor=newProcessor();
		}
		return processor;
	}
}
