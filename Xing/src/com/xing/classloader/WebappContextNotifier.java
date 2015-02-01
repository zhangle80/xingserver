package com.xing.classloader;

import com.xing.container.Container;
import com.xing.container.Context;

public class WebappContextNotifier implements Runnable {
	private Container container;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		((Context)container).reload();
	}

}
