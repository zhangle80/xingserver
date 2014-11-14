package com.xing.handle;

import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

public abstract class AbstractProcessor {
	protected HttpRequest request;
	protected HttpResponse response;
	
	public AbstractProcessor(HttpRequest request,HttpResponse response){
		this.request=request;
		this.response=response;
	}
	
	public HttpRequest getRequest() {
		return request;
	}

	public HttpResponse getResponse() {
		return response;
	}
	
	public abstract void process();
}
