package com.xing.classloader;

import com.xing.container.Container;
import com.xing.container.Context;
import com.xing.logger.PropertyChangeListener;

/**
 * 加载器接口
 * @author Leo
 *
 */
public interface Loader {
	public ClassLoader getClassLoader();
	/**
	 * 一个Tomcat的加载器通常跟一个上下文相关联
	 * @return
	 */
	public Container getContainer();
	/**
	 * 一个Tomcat的加载器通常跟一个上下文相关联
	 * @param container
	 */
	public void setContainer(Container container);
	public Context getContext();
	public void setContext(Context context);
	public boolean getDelegate();
	public void setDelegate(boolean delegate);
	public String getInfo();
	public boolean getReloadable();
	public void setReloadable(boolean reloadable);
	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(PropertyChangeListener listener);
	/**
	 * 方法用于添加一个库
	 * @param repository
	 */
	public void addRepository(String repository);
	/**
	 * 方法用于返回一个所有库的队列
	 * @return
	 */
	public String[] findRepositories();
	public boolean modified();
	
}
