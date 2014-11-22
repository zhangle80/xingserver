package com.xing.pipeline;

import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

/**
 * ���������Ĺ�����ӿڣ�������������Ҫ���ڹ�����ˮ���з��ŵ�ִ�С���ת�ȹ���
 * @author Leo
 *
 */
public interface ValveContext {
	public String getInfo();
	
	/**
	 * ִ�в���ת����һ������
	 * @param request
	 * @param response
	 */
	public void invokeNext(HttpRequest request, HttpResponse response);

}
