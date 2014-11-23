package com.xing.pipeline;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.xing.container.Container;
import com.xing.container.SimpleWrapper;
import com.xing.container.Wrapper;
import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

public class SimpleWrapperValve implements Valve {
	private Container container;
	public SimpleWrapperValve(){
		
	}
	public SimpleWrapperValve(Container container){
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
		
		Wrapper wrapper=(SimpleWrapper)this.getContainer();
		Servlet servlet = wrapper.allocate();
		servlet.service(request, response);
	}

}
