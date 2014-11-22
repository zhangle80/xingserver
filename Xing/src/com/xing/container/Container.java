package com.xing.container;

import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

public interface Container {
	
	public void invoke(HttpRequest request, HttpResponse response);
	
	public void addChild(Container child);
	
	public void removeChild(Container child);
	
	public Container findChild(String name);
	
	public Container[] findChildren(); 
}
