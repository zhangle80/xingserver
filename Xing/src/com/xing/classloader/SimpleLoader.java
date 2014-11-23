package com.xing.classloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import com.xing.server.Constants;

public class SimpleLoader {
	private URLClassLoader loader = null;  //������������ڴ�ָ�� JAR�ļ���Ŀ¼�� URL������·�����������Դ

	public SimpleLoader(){
		URL[] urls=new URL[1];
		URLStreamHandler streamHandler = null;
		
		try {
			File classPath=new File(Constants.BIN);
			String repository=(new URL("file",null,classPath.getCanonicalPath()+File.separator)).toString();//������Դ�⣨һ����Դ������ҵ�SERVLET�ĵط���
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
}
