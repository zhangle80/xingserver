package com.xing.container;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.xing.classloader.SimpleLoader;
import com.xing.http.HttpRequest;
import com.xing.http.HttpRequestFacade;
import com.xing.http.HttpResponse;
import com.xing.http.HttpResponseFacade;

public class SimpleContainer implements Container {
	@Override
	public void invoke(HttpRequest request, HttpResponse response) {
		String uri=request.getRequestURI();
		System.out.println("uri="+uri);
		
		if(uri.startsWith("/servlet/")){
			this.process(request,response);
		}else{
			System.out.println("static resource, please wait continue codeing...");
		}
	}
	private void process(HttpRequest request, HttpResponse response) {
		String uri = request.getRequestURI();
		String servletName=uri.substring(uri.lastIndexOf("/")+1);
		
		SimpleLoader loader=new SimpleLoader();		
		Class<?> servletClass=loader.loadClass(servletName);
		
		Servlet servlet=null;
		try {
			servlet =(Servlet) servletClass.newInstance();
			HttpRequestFacade requestFacade=new HttpRequestFacade(request);
			HttpResponseFacade responseFacade=new HttpResponseFacade(response);
			
			servlet.service(requestFacade, responseFacade);
			response.finishResponse();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("servlet process...");
	}
	@Override
	public void addChild(Container child) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Container findChild(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Container[] findChildren() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void removeChild(Container child) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public SimpleLoader getLoader() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setLoader(SimpleLoader loader) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setParent(Container parent) {
		// TODO Auto-generated method stub
		
	}


}
/*
		AbstractProcessor processor;
		if(uri.startsWith("/servlet/")){
			processor=new ServletProcessor(request,response);
		}else{
			processor=new StaticResourceProcessor(request,response);
		}
		processor.process();
 * */
