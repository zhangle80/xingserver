package com.xing.container;


import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.xing.classloader.SimpleLoader;
import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;
import com.xing.pipeline.Pipeline;
import com.xing.pipeline.SimplePipeline;
import com.xing.pipeline.SimpleWrapperValve;
import com.xing.pipeline.Valve;

/**
 * 简单包装容器，实现包装接口和流水线接口，本来可以不实现流水线接口，将流水线作为容器的一个属性来控制所有阀门，但是这样做阀门的管理
 * 以及触发都不太方便，故此将容器实现了流水线接口
 * @author Leo
 *
 */
public class SimpleWrapper implements Wrapper,Pipeline {
	private SimpleLoader loader;
	private Container parent;
	private Pipeline pipeline=new SimplePipeline();
	private String servletClass="";

	public SimpleWrapper(){
		Valve valve=new SimpleWrapperValve(this);//基本阀门需要指向其所在容器，以根据容器的分配方法来获得Servlet类
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
		try {
			Class<?> servletClass=this.loader.loadClass(this.servletClass);
			servlet =(Servlet) servletClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} 
		
		System.out.println("servlet process...");
		return servlet;
	}

	@Override
	public void load() throws ServletException {
		// TODO Auto-generated method stub

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

}
