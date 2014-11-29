package com.xing.lifecycle;

/**
 * @author leo
 *	生命周期函数,检测容器状态变化,以及通知其他关注该类的组件执行动作
 *	通知采用的是观察者模式,该接口是观察者模式中的统治者接口
 */
public interface Lifecycle {
	public static final String START_EVENT = "start";
	public static final String BEFORE_START_EVENT = "before_start";
	public static final String AFTER_START_EVENT = "after_start";
	public static final String STOP_EVENT = "stop";
	public static final String BEFORE_STOP_EVENT = "before_stop";
	public static final String AFTER_STOP_EVENT = "after_stop";
	/**
	 * Lifecycle初始化
	 */
	public void initialize();
	
	/**
	 * Lifecycle开始
	 */
	public void start();
	
	/**
	 * Lifecycle结束
	 */
	public void stop();
	
	/**
	 * Lifecycle添加观察者
	 * @param listener
	 */
	public void addLifecycleListener(LifecycleListener listener);
	
	/**
	 * 返回所有观察者
	 * @return
	 */
	public LifecycleListener[] findLifecycleListeners();
	
	/**
	 * 移除观察者
	 * @param listener
	 */
	public void removeLifecycleListener(LifecycleListener listener);

}
