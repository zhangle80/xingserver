package com.xing.http;

public class HttpContext {
	private HttpRequest request;
	private HttpResponse response;
	
	public HttpContext(HttpRequest request,HttpResponse response){
		this.request=request;
		this.response=response;
	}
	
	public HttpRequest getRequest() {
		return request;
	}

	public HttpResponse getResponse() {
		return response;
	}
}
