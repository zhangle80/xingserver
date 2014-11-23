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
 * �򵥰�װ������ʵ�ְ�װ�ӿں���ˮ�߽ӿڣ��������Բ�ʵ����ˮ�߽ӿڣ�����ˮ����Ϊ������һ���������������з��ţ��������������ŵĹ���
 * �Լ���������̫���㣬�ʴ˽�����ʵ������ˮ�߽ӿ�
 * @author Leo
 *
 */
public class SimpleWrapper implements Wrapper,Pipeline {
	private SimpleLoader loader;
	private Container parent;
	private Pipeline pipeline=new SimplePipeline();
	private String servletClass="";

	public SimpleWrapper(){
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
