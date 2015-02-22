package com.xing.classloader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.xing.container.Container;
import com.xing.container.Context;
import com.xing.lifecycle.Lifecycle;
import com.xing.lifecycle.LifecycleListener;
import com.xing.logger.Logger;
import com.xing.logger.PropertyChangeListener;
import com.xing.logger.SystemOutLogger;

/**
 * webӦ�ü���������Ե���Ӧ�������ĵļ�����������������࣬������������WebAppClassLoader�У�
 * @author Leo
 *
 */
public class WebAppLoader implements Loader, Lifecycle, Runnable {
	/**
	 * ���������·�����ƣ�Ĭ��ָ��WebAppClassLoader
	 */
	private String loaderClass="com.xing.classloader.WebappClassLoader";;
	private ClassLoader parentClassLoader;
	private ClassLoader classLoader;
	
	private boolean threadDone=false;
	private boolean started=false;
	
	private String[] repositories=new String[0];
	
	private Logger logger = new SystemOutLogger();
	/**
	 * �Ƿ�ί�и�������������
	 */
	private boolean delegate=false;
	
	public String getLoaderClass() {
		return loaderClass;
	}

	public void setLoaderClass(String loaderClass) {
		this.loaderClass = loaderClass;
	}
	
	/**
	 * �������������Ӧ�������ļ�������ͨ�����������ʵ����ļ���
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private WebappClassLoader createClassLoader() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
		Class<?> clazz = Class.forName(this.loaderClass);
		WebappClassLoader classLoader=null;
		if(this.parentClassLoader==null){
			classLoader=(WebappClassLoader)clazz.newInstance();
			//this.parentClassLoader=Thread.currentThread().getContextClassLoader();//Tomcat 5.0����
		}else{
			Class<?>[] argTypes = {ClassLoader.class};
			Object[] arg = {this.parentClassLoader};
			Constructor<?> constructor = clazz.getConstructor(argTypes);
			classLoader =(WebappClassLoader)constructor.newInstance(arg);
		}
		
		if(classLoader!=null){
	        classLoader.setDelegate(this.delegate);
	        for (int i = 0; i < repositories.length; i++) {
	            classLoader.addRepository(repositories[i]);
	        }
		}
		
		return classLoader;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRepository(String repository) {
		String[] temp=new String[this.repositories.length+1];
		System.arraycopy(this.repositories, 0, temp, 0, this.repositories.length);
		temp[this.repositories.length]=repository;
		this.repositories=temp;
	}

	@Override
	public String[] findRepositories() {
		return this.repositories;
	}

	@Override
	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return this.classLoader;
	}

	@Override
	public Container getContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getDelegate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getReloadable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modified() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContainer(Container container) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContext(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDelegate(boolean delegate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setReloadable(boolean reloadable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(){
		// TODO Auto-generated method stub
		//Creating a class loader 
        if (started){
            try {
				throw new Exception("webappLoader.alreadyStarted");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        logger.log("webappLoader.starting");
		this.started = true;
		
		try {
			this.classLoader=this.createClassLoader();
            
            if (classLoader instanceof Lifecycle){
                ((Lifecycle) classLoader).start();
            }
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		//Setting repositories 
		this.addRepository("WEB-INF/classes");
		//Setting the class path 
		//Setting permissions 
		//Starting a new thread for auto-reload.
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Logger log = new SystemOutLogger();
		log.log("Background thread starting...");
		while(!threadDone){
			threadSleep();
			if(!started){
				break;
			}
			//modify()
			nodifyContext();
			break;
		}
		log.log("Background thread stopping...");
	}

	private void nodifyContext() {
		WebappContextNotifier notifer = new WebappContextNotifier();
		(new Thread(notifer)).start();
	}

	private void threadSleep() {
		// TODO Auto-generated method stub
		
	}

}
