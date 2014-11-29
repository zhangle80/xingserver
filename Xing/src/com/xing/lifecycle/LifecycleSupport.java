package com.xing.lifecycle;

/**
 * @author Leo
 * 生命周期接口的辅助类，主要用于生命周期实现类中关于观察者模式的实现部分
 * 两个私有属性，lifecycle是该类要辅助的生命周期类，listeners是注册的观察者
 * 运用了很多同步的代码，可能是为了应多wrapper的多线程问题
 */
public final class LifecycleSupport {

	private Lifecycle lifecycle=null;
	private LifecycleListener[] listeners=new LifecycleListener[0];
	
	public LifecycleSupport(Lifecycle lifecycle){
		super();
		this.lifecycle=lifecycle;
	}
	
	public void addLifecycleListener(LifecycleListener listener){
		synchronized(this.listeners){
			LifecycleListener[] listeners=new LifecycleListener[this.listeners.length+1];
			System.arraycopy(listeners, 0, this.listeners, 0, this.listeners.length);
			listeners[this.listeners.length]=listener;
		
			this.listeners=listeners;
		}
	}
	
	public LifecycleListener[] findLifecycleListener(){
		return this.listeners;
	}
	
	/**
	 * 触发通知观察者执行
	 * @param type
	 * @param data
	 */
	public void fireLifecycleEvent(String type,Object data ){
		LifecycleEvent event=new LifecycleEvent(this.lifecycle,type,data);
		LifecycleListener[] interest=null;
		
		synchronized(this.listeners){
			interest=(LifecycleListener[])this.listeners.clone();
		}
		
		for(LifecycleListener listener:interest){
			listener.lifecycleEvent(event);
		}
	}
	
	/**
	 * 移除观察者
	 * @param listener
	 */
	public void removeLifecycleListener(LifecycleListener listener){
		
		synchronized(this.listeners){
			int n=-1;
			for(int i=0;i<this.listeners.length;i++){
				LifecycleListener temp=this.listeners[i];
				if(temp==listener){
					n=i;
					break;
				}
			}
			if(n<0){
				return;
			}
			LifecycleListener[] listeners=new LifecycleListener[this.listeners.length-1];
			int i=0;
			for(int j=0;j<this.listeners.length;j++){
				if(j!=n){
					listeners[i]=this.listeners[j];
					i+=1;
				}
			}
		}
	}
}
