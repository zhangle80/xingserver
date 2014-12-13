package com.xing.container;

import com.xing.lifecycle.Lifecycle;
import com.xing.lifecycle.LifecycleEvent;
import com.xing.lifecycle.LifecycleListener;

public class SimpleContextLifecycleListener implements LifecycleListener {

	@Override
	public void lifecycleEvent(LifecycleEvent event) {
		System.out.println("SimpleCotextLifecycleListener event is "+event.getType());
		if(Lifecycle.START_EVENT.equals(event.getType())){
			System.out.println("Starting context");
		}
		if(Lifecycle.STOP_EVENT.equals(event.getType())){
			System.out.println("Starting context");
		}
	}

}
