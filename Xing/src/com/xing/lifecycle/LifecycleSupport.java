package com.xing.lifecycle;

/**
 * @author Leo
 * �������ڽӿڵĸ����࣬��Ҫ������������ʵ�����й��ڹ۲���ģʽ��ʵ�ֲ���
 * ����˽�����ԣ�lifecycle�Ǹ���Ҫ���������������࣬listeners��ע��Ĺ۲���
 * �����˺ܶ�ͬ���Ĵ��룬������Ϊ��Ӧ��wrapper�Ķ��߳�����
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
	 * ����֪ͨ�۲���ִ��
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
	 * �Ƴ��۲���
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
