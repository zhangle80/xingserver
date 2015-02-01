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
 * web应用加载器（针对的是应用上下文的加载器，而非类加载类，具体的类加载在WebAppClassLoader中）
 * @author Leo
 *
 */
public class WebAppLoader implements Loader, Lifecycle, Runnable {
	/**
	 * 类加载器的路径名称，默认指向WebAppClassLoader
	 */
	private String loaderClass;
	private ClassLoader parentClassLoader;
	
	private boolean threadDone=false;
	private boolean started=false;
	
	private String[] repository=new String[0];

	public String getLoaderClass() {
		return loaderClass;
	}

	public void setLoaderClass(String loaderClass) {
		this.loaderClass = loaderClass;
	}
	
	/**
	 * 创建类加载器，应用上下文加载器是通过类加载器来实现类的加载
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
			//this.parentClassLoader=Thread.currentThread().getContextClassLoader();//Tomcat 5.0以上
		}else{
			Class<?>[] argTypes = {ClassLoader.class};
			Object[] arg = {this.parentClassLoader};
			Constructor<?> constructor = clazz.getConstructor(argTypes);
			classLoader =(WebappClassLoader)constructor.newInstance(arg);
		}
		
		return classLoader;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRepository(String repository) {
		String[] temp=new String[this.repository.length+1];
		System.arraycopy(this.repository, 0, temp, 0, this.repository.length);
		temp[this.repository.length]=repository;
		this.repository=temp;
	}

	@Override
	public String[] findRepositories() {
		return this.repository;
	}

	@Override
	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return null;
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
	public void start() {
		// TODO Auto-generated method stub
		//Creating a class loader 
		try {
			this.createClassLoader();
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
