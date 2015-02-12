package com.xing.container;

import java.io.IOException;

import javax.servlet.ServletException;

import com.xing.classloader.SimpleLoader;
import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;
import com.xing.lifecycle.Lifecycle;
import com.xing.lifecycle.LifecycleListener;
import com.xing.lifecycle.LifecycleSupport;
import com.xing.logger.Logger;
import com.xing.pipeline.Pipeline;
import com.xing.pipeline.SimpleContextValve;
import com.xing.pipeline.SimplePipeline;
import com.xing.pipeline.Valve;

public class SimpleContext implements Context, Lifecycle, Pipeline {

	private Container[] children=new Container[0];
	private LifecycleSupport lifecycleSupport;
	private SimpleLoader loader; 
	private Pipeline pipeline=new SimplePipeline();
	private Container parent;
	private boolean started=false;
	private Logger logger;
	private String path;
	private String docBase;
	
	public SimpleContext(){
		this.lifecycleSupport=new LifecycleSupport(this);
		Valve basic=new SimpleContextValve(this);
		this.pipeline.setBasic(basic);
	}
	
	@Override
	public void addWrapper(Wrapper wrapper) {
		this.addChild(wrapper);
		wrapper.setParent(this);
	}

	@Override
	public void createWrapper() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addChild(Container child) {
		Container[] children=new Container[this.children.length+1];
		System.arraycopy(children, 0, this.children, 0, this.children.length);
		children[this.children.length]=child;
		
		this.children=children;
	}

	@Override
	public Container findChild(String name) {
		Container child=null;
		for(Container temp:this.children){
			Wrapper wrapper=(Wrapper)temp;
			if(name.equals(wrapper.getName())){
				child=wrapper;
				break;
			}
		}
		return child;
	}

	@Override
	public Container[] findChildren() {
		return this.children;
	}

	@Override
	public SimpleLoader getLoader() {
		if(loader!=null){
			return loader;
		}
		if(parent!=null){
			return parent.getLoader();
		}
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
	public void setLoader(SimpleLoader loader) {
		this.loader=loader;
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
		if(this.started){
			try {
				throw new Exception("SimpleContext is started");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.lifecycleSupport.fireLifecycleEvent(BEFORE_START_EVENT, null);
		this.started=true;
		
		if(this.loader!=null&&(this.loader instanceof Lifecycle)){
			((Lifecycle)this.loader).start();
		}
		
		for(Container child:this.children){
			if(child!=null&&(child instanceof Lifecycle)){
				((Lifecycle)child).start();
			}
		}
		
		if(this.pipeline!=null&&(this.pipeline instanceof Lifecycle)){
			((Lifecycle)this.pipeline).start();
		}
		this.lifecycleSupport.fireLifecycleEvent(START_EVENT, null);
		this.lifecycleSupport.fireLifecycleEvent(AFTER_START_EVENT, null);
		this.logger.log("context is start!");
	}

	@Override
	public void stop() {
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
		
		for(Container child:this.children){
			if(child!=null&&(child instanceof Lifecycle)){
				((Lifecycle)child).stop();
			}
		}
		
		if(this.loader!=null&&(this.loader instanceof Lifecycle)){
			((Lifecycle)this.loader).stop();
		}
		
		this.lifecycleSupport.fireLifecycleEvent(AFTER_STOP_EVENT, null);
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
	public void setLog(Logger logger) {
		this.logger=logger;
	}

	@Override
	public Logger getLog() {
		return this.logger;
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDocBase(String docBase) {
		// TODO Auto-generated method stub
		this.docBase=docBase;
	}

	@Override
	public void setPath(String path) {
		// TODO Auto-generated method stub
		this.path=path;
	}

	@Override
	public void setParent(Container parent) {
		this.parent=parent;
	}

	public String getPath() {
		return path;
	}

	public String getDocBase() {
		return docBase;
	}

}
