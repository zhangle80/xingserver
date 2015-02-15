package com.xing.container;

import com.xing.classloader.Loader;
import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

public interface Container {
	
	/**
	 * �����Ĵ���������ͨ����ί������ˮ�ߵĴ�������
	 * @param request
	 * @param response
	 */
	public void invoke(HttpRequest request, HttpResponse response);
	
	/**
	 * ���������
	 * @param child
	 */
	public void addChild(Container child);
	
	/**
	 * �Ƴ�������
	 * @param child
	 */
	public void removeChild(Container child);
	
	/**
	 * �������Ʒ���������
	 * @param name
	 * @return
	 */
	public Container findChild(String name);
	
	/**
	 * ��������������
	 * @return
	 */
	public Container[] findChildren(); 
	
	public Loader getLoader();
	
	public void setLoader(Loader loader);
	
	public void setParent(Container parent);
}
