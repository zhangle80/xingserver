package com.xing.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * �����������WebAppLoader����
 * @author Leo
 *
 */
public class WebappClassLoader extends URLClassLoader {
	/**
	 * ��������ص���
	 */
	private static final String[] triggers ={"javax.servlet.Servlet"};
	/**
	 * ��������صİ�
	 */
	private static final String[] packegTriggers={
		"javax","org.xml.sax","org.w3c.dom","org.apache.xerces","org.apache.xalan"
	};

	public WebappClassLoader(URL[] arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
