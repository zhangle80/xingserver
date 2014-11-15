package com.xing.http.connector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xing.container.IContainer;
import com.xing.http.HttpProcessor;
import com.xing.server.Constants;
import com.xing.server.Lifecycle;

/**
 * @author Leo
 * HTTP�����࣬�������̴���ͻ���socket����
 * 
 */
public class HttpConnector implements Runnable,IConnector,Lifecycle {
	boolean termination;			//�Ƿ����
	private Stack<HttpProcessor> processorsPool;
	private int curProcessors=0;
	
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
	public IContainer getContainer() {
		return null;
	}
	@Override
	public void setContainer(IContainer container) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * ��ʼ����������
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
	public void recycle(HttpProcessor processor) {
		this.processorsPool.push(processor);
	}
	private HttpProcessor newProcessor() {
		HttpProcessor processor=new HttpProcessor(this);
		this.curProcessors+=1;
		return processor;
	}

	private HttpProcessor dispatchProcessor(){
		HttpProcessor processor;
		processor=this.processorsPool.pop();
		if(processor==null&&this.curProcessors<Constants.MAX_PROCESSORS){
			processor=newProcessor();
			recycle(processor);
		}
		return processor;
	}
}
