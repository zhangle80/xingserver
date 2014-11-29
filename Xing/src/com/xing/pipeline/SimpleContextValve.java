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
		Container context=this.getContainer();	//找到其容器
		Container wrapper = ((SimpleContext)context).findChild("ThirdServlet");//根据名称找到相关子容器（包装类）
		
		wrapper.invoke(request, response);		//触发器
	}

}
