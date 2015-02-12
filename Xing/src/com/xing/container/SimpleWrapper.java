package com.xing.container;


import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.xing.classloader.SimpleLoader;
import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;
import com.xing.lifecycle.Lifecycle;
import com.xing.lifecycle.LifecycleListener;
import com.xing.lifecycle.LifecycleSupport;
import com.xing.pipeline.Pipeline;
import com.xing.pipeline.SimplePipeline;
import com.xing.pipeline.SimpleWrapperValve;
import com.xing.pipeline.Valve;

/**
 * �򵥰�װ������ʵ�ְ�װ�ӿں���ˮ�߽ӿڣ��������Բ�ʵ����ˮ�߽ӿڣ�����ˮ����Ϊ������һ���������������з��ţ��������������ŵĹ���
 * �Լ���������̫���㣬�ʴ˽�����ʵ������ˮ�߽ӿ�
 * @author Leo
 *
 */
public class SimpleWrapper implements Wrapper,Pipeline,Lifecycle {
	private SimpleLoader loader;
	private LifecycleSupport lifecycleSupport;
	private Container parent;
	private Pipeline pipeline=new SimplePipeline();
	private String servletClass="";
	private boolean started=false;
	private Servlet instance=null;

	public SimpleWrapper(){
		this.lifecycleSupport=new LifecycleSupport(this);
		Valve valve=new SimpleWrapperValve(this);//����������Ҫָ���������������Ը��������ķ��䷽�������Servlet��
		this.pipeline.setBasic(valve);
	}
	
	public void setServletClass(String servletClass){
		this.servletClass=servletClass;
	}
	
	@Override
	public Servlet allocate() throws ServletException {		
		
		Servlet servlet=null;
		if(this.servletClass==null||this.servletClass.equals("")){
			return servlet;
		}
		if(this.instance==null){
			this.load();
		}
		servlet=this.instance;

		System.out.println("servlet process...");
		return servlet;
	}

	@Override
	public void load() throws ServletException {
		if(this.instance!=null){
			return;
		}
		Servlet servlet=null;
		try {
			Class<?> servletClass=this.getLoader().loadClass(this.servletClass);
			servlet =(Servlet) servletClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} 
		this.instance=servlet;
		this.instance.init(null);//Ӧ�ô���servletConfig����
	}

	@Override
	public void addChild(Container child) {
		// TODO Auto-generated method stub

	}

	@Override
	public Container findChild(String name) {
		return null;
	}

	@Override
	public Container[] findChildren() {
		return null;
	}

	@Override
	public void invoke(HttpRequest request, HttpResponse response) {
		try {
			this.pipeline.invoke(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeChild(Container child) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public SimpleLoader getLoader(){
		if(loader!=null){
			return loader;
		}
		if(parent!=null){
			return parent.getLoader();
		}
		return null;
	}

	@Override
	public void addValve(Valve valve) {
		this.pipeline.addValve(valve);
	}

	@Override
	public Valve getBasic() {
		return this.pipeline.getBasic();
	}

	@Override
	public Valve[] getValve() {
		return this.pipeline.getValve();
	}

	@Override
	public void removeValve(Valve valve) {
		this.pipeline.removeValve(valve);
	}

	@Override
	public void setBasic(Valve valve) {
		this.pipeline.setBasic(valve);
	}

	@Override
	public void setLoader(SimpleLoader loader) {
		this.loader=loader;
	}

	@Override
	public String getName() {
		return this.servletClass;
	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		this.lifecycleSupport.addLifecycleListener(listener);
	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		return this.lifecycleSupport.findLifecycleListener();
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		this.lifecycleSupport.removeLifecycleListener(listener);
	}

	@Override
	public void start() {
		System.out.println("Starting Wrapper "+this.servletClass);
		if(this.started){
			try {
				throw new Exception("Wrapper is started");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.lifecycleSupport.fireLifecycleEvent(BEFORE_START_EVENT, null);
		this.started=true;
		
		if(this.loader!=null&&(this.loader instanceof Lifecycle)){
			((Lifecycle)this.loader).start();
		}
		
		if(this.pipeline!=null&&(this.pipeline instanceof Lifecycle)){
			((Lifecycle)this.pipeline).start();
		}
		this.lifecycleSupport.fireLifecycleEvent(START_EVENT, null);
		this.lifecycleSupport.fireLifecycleEvent(AFTER_START_EVENT, null);
	}

	@Override
	public void stop() {
		System.out.println("wrapper is stoping");
		
		this.instance.destroy();//����servlet
		this.instance=null;		//������������
		
		if(!this.started){
			try {
				throw new Exception("SimpleContext has not be started");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.started=false;
		
		this.lifecycleSupport.fireLifecycleEvent(BEFORE_STOP_EVENT, null);
		this.lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);
		
		if(this.pipeline!=null&&(this.pipeline instanceof Lifecycle)){
			((Lifecycle)this.pipeline).stop();
		}
		
		if(this.loader!=null&&(this.loader instanceof Lifecycle)){
			((Lifecycle)this.loader).stop();
		}
		
		this.lifecycleSupport.fireLifecycleEvent(AFTER_STOP_EVENT, null);
		
	}

	@Override
	public void setParent(Container parent) {
		this.parent=parent;
	}

}
