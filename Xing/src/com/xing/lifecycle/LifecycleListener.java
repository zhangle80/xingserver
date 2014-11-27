package com.xing.lifecycle;

/**
 * 生命周期监听接口(观察者模式中的观察者接口) 
 * @author leo
 *
 */
public interface LifecycleListener {
	public void lifecycleEvent(LifecycleEvent event);
}
