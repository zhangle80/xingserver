package com.xing.container;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.xing.http.HttpRequest;
import com.xing.http.HttpRequestFacade;
import com.xing.http.HttpResponse;
import com.xing.http.HttpResponseFacade;
import com.xing.server.Constants;

public class SimpleContainer implements Container {
	@Override
	public void invoke(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		String uri=request.getRequestURI();
		System.out.println("uri="+uri);
		
		if(uri.startsWith("/servlet/")){
			this.process(request,response);
		}else{
			System.out.println("static resource, please wait...");
		}
	}
	private void process(HttpRequest request, HttpResponse response) {
		String uri = request.getRequestURI();
		String servletName=uri.substring(uri.lastIndexOf("/")+1);
		
		URLClassLoader loader = null;	//该类加载器用于从指向 JAR文件和目录的 URL的搜索路径加载类和资源
		URL[] urls=new URL[1];
		URLStreamHandler streamHandler = null;
		
		try {
			File classPath=new File(Constants.BIN);
			String repository=(new URL("file",null,classPath.getCanonicalPath()+File.separator)).toString();//定义资源库（一个资源库可以找到SERVLET的地方）
			urls[0] = new URL(null,repository,streamHandler);
			loader = new URLClassLoader(urls);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Class<?> servletClass=null;
		try {
			servletClass=loader.loadClass(servletName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
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
