package com.xing.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.servlet.ServletException;

import com.xing.http.connector.HttpConnector;
import com.xing.http.connector.SocketInputStream;
import com.xing.lifecycle.Lifecycle;
import com.xing.lifecycle.LifecycleListener;

/**
 * @author Leo
 * Http�����࣬����Connector���յ��Ŀͻ���Socket����
 */
public class HttpProcessor implements Runnable,Lifecycle {
	private HttpConnector connector;
	private HttpRequest request;
	private HttpResponse response;

	private static final String SHUTDOWN_COMMAND="/shutdown";	
	private boolean shutdown = false;	//������ֹͣ
	private boolean stop=false;			//�Լ�ֹͣ
	
	private Socket socket;
	private boolean available=false;
	
	private long processorId;
	
	public HttpProcessor(HttpConnector connector){
		this.connector=connector;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * �߳�����
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
		//�ر�
	}
	
	/**
	 * ָ��Socket���µ����̣�����Ҫ�ж���һ�ε�socket�Ƿ�����ϣ�ͬʱ�÷���Ҫ��ͬ������
	 * ͬ����ԭ����ÿ��Processor��˳���ύSocket֮�󶼻��������
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
		System.out.println("I am process,i'm be assigned,and my pid is "+this.getProcessorId());
	}
	
	private synchronized Socket await() {
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

	/**
	 * ʵ�ʵĴ�����
	 * @param socket
	 */
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
			
			if(!this.socketIsAvailable(socket)){//��ʱ�жϷ�����δ���ᱻRequest Head�е�Content-length����
				socket.close();
				return;
			}
			
			Thread.sleep(500);					//ģ���߳�����ʱ��
			this.handleAccept(input, output);
			socket.close();	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
			System.out.println("this link socket is over...");
		}

	}
	
	private void handleAccept(SocketInputStream input,OutputStream output) throws IOException{		
		this.request=new HttpRequest(input);		
		this.response=new HttpResponse(output);
		this.response.setRequest(this.request);
		this.response.setHeader("Server", "Pyrmont Servlet Container");
		
		try {
			this.request.parse();//��Ҫ����parseRequest(����������)��parseHeader(����ͷ��)
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
			
		this.connector.getContainer().invoke(request, response);
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		Thread processorThread=new Thread(this);
		processorThread.start();
		this.processorId=processorThread.getId();
		System.out.println("I am a processor,now i'm be created and my pid is "+this.processorId);
	}

	public long getProcessorId(){
		return this.processorId;
	}
	
	/**
	 * �÷��������������socket�Ƿ���Ч������ʱ������������Ҫ��Request Head �� Content-length�Ƿ�Ϊ0ȡ��
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	private boolean socketIsAvailable(Socket socket) throws IOException{
		int count=0;
		int index=0;
		while (count == 0) { 
		   count = socket.getInputStream().available(); 
		   index++;
		   if(index>100){
			   System.out.println("this socket available check count expend 100,is null socket!");
			   return false;
		   }
		}
		return true;
	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
