package com.xing.pipeline;

import java.io.IOException;

import javax.servlet.ServletException;

import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

/**
 * ��ˮ�߽ӿڣ�ÿ���������������һ����ˮ�ߣ�ͨ������ˮ�������÷�����ִ��һϵ����
 * ���ִ���ʽ������������������֪Ϊ��û�в�����������ʽ��������ʹ����һ�����������Ĺ�������
 * ������Щ���ŵ�ִ�к���ת��
 * @author Leo
 */
public interface Pipeline {
	/**
	 * ���û�������
	 * @param valve
	 * 
	 */
	public void setBasic(Valve valve);
	/**
	 * @return
	 * ��ȡ��������
	 */
	public Valve getBasic();
	/**
	 * @param valve
	 * ��װ��ͨ����
	 */
	public void addValve(Valve valve);
	/**
	 * @return
	 * ��ȡ������ͨ����
	 */
	public Valve[] getValve();
	/**
	 * @param valve
	 * �Ƴ�����
	 */
	public void removeValve(Valve valve);
	/**
	 * ������ˮ�߷���
	 * @param request
	 * @param response
	 * @throws ServletException 
	 * @throws IOException 
	 */
	public void invoke(HttpRequest request, HttpResponse response) throws ServletException, IOException;
}
