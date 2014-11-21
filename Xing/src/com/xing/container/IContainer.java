package com.xing.container;

import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

public interface IContainer {
	public void invoke(HttpRequest request, HttpResponse response);
}
