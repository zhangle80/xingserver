package com.xing.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 类加载器，由WebAppLoader调用
 * @author Leo
 *
 */
public class WebappClassLoader extends URLClassLoader {
	/**
	 * 不允许加载的类
	 */
	private static final String[] triggers ={"javax.servlet.Servlet"};
	/**
	 * 不允许加载的包
	 */
	private static final String[] packegTriggers={
		"javax","org.xml.sax","org.w3c.dom","org.apache.xerces","org.apache.xalan"
	};

	public WebappClassLoader(URL[] arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
