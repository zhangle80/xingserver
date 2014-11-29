package com.xing.pipeline;

import java.io.IOException;

import javax.servlet.ServletException;

import com.xing.container.Container;
import com.xing.container.SimpleContext;
import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

public class SimpleContextValve implements Valve {
	private Container container;
	
	public SimpleContextValve(){
		
	}
	
	public SimpleContextValve(Container container){
		this.container=container;
	}
	
	public Container getContainer(){
		return this.container;
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invoke(HttpRequest request, HttpResponse response,
			ValveContext valveContext) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Container context=this.getContainer();	//�ҵ�������
		Container wrapper = ((SimpleContext)context).findChild("ThirdServlet");//���������ҵ��������������װ�ࣩ
		
		wrapper.invoke(request, response);		//������
	}

}
