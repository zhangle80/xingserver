package com.xing.classloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import com.xing.lifecycle.Lifecycle;
import com.xing.lifecycle.LifecycleListener;
import com.xing.server.Constants;

public class SimpleLoader implements Lifecycle {
	private URLClassLoader loader = null;  //该类加载器用于从指向 JAR文件和目录的 URL的搜索路径加载类和资源

	public SimpleLoader(){
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
	}
	
	public Class<?> loadClass(String servletName){
		Class<?> servletClass=null;
		try {
			servletClass=loader.loadClass(servletName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return servletClass;
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
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		System.out.println("i am simpleload, and i am be started!");
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		System.out.println("i am simpleload, and i am be stopped!");
	}
}
