package com.xing.pipeline;

import java.io.IOException;

import javax.servlet.ServletException;

import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;
import com.xing.lifecycle.Lifecycle;
import com.xing.lifecycle.LifecycleListener;

public class SimplePipeline implements Pipeline,ValveContext,Lifecycle {
	private Valve basic;
	private Valve[] valves=new Valve[0];
	private int stage=0;
	
	public SimplePipeline(){
	}

	@Override
	public void addValve(Valve valve) {
		Valve[] valves=new Valve[this.valves.length+1];
		System.arraycopy(valves, 0, this.valves, 0, this.valves.length);
		valves[this.valves.length]=valve;
		
		this.valves=valves;
	}

	@Override
	public Valve getBasic() {
		return this.basic;
	}

	@Override
	public Valve[] getValve() {
		return this.valves;
	}

	@Override
	public void invoke(HttpRequest request, HttpResponse response) throws ServletException, IOException {
		this.stage=0;
		this.invokeNext(request, response);
	}

	@Override
	public void removeValve(Valve valve) {

	}

	@Override
	public void setBasic(Valve valve) {
		this.basic=valve;
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invokeNext(HttpRequest request, HttpResponse response) throws ServletException, IOException {
		int subscript=this.stage;
		this.stage++;
		
		if(subscript<this.valves.length){
			this.valves[subscript].invoke(request, response, this);
		}else if((subscript==this.valves.length)&&(this.basic!=null)){
			this.basic.invoke(request, response, this);
		}else{
			try {
				throw new ServletException("this pipeline has no valves");
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		System.out.println("i am simplepipeline, and i am be started!");
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		System.out.println("i am simplepipeline, and i am be stopped!");
	}

}
