package com.xing.handle;

import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

public class StaticResourceProcessor extends AbstractProcessor  {

	public StaticResourceProcessor(HttpRequest request, HttpResponse response) {
		super(request, response);
	}

	@Override
	public void process() {
		this.response.sendStaticResource();
		System.out.println("static resource process...");
	}

}
