package com.xing.pipeline;

import java.io.IOException;

import javax.servlet.ServletException;

import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

/**
 * ���Žӿڣ����ڰ�װ����ˮ������ִ��һϵ�й���
 * @author Leo
 *
 */
public interface Valve {

	public String getInfo();
	
	public void invoke(HttpRequest request, HttpResponse response,ValveContext valveContext) throws ServletException, IOException;
	
}
