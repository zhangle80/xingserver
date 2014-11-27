package com.xing.lifecycle;

import java.util.EventObject;

public class LifecycleEvent extends EventObject {
	private Lifecycle lifecycle=null;
	private String type=null;
	private Object data=null;

	public LifecycleEvent(Object arg0) {
		super(arg0);
	}
	
	public LifecycleEvent(Lifecycle lifecycle,String type){
		this(lifecycle,type,null);
	}
	
	public LifecycleEvent(Lifecycle lifecycle,String type,Object data){
		super(lifecycle);
		this.setLifecycle(lifecycle);
		this.setType(type);
		this.setData(data);
	}

	private void setLifecycle(Lifecycle lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Lifecycle getLifecycle() {
		return lifecycle;
	}

	private void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	private void setData(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
