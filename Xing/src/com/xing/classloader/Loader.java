package com.xing.classloader;

import com.xing.container.Container;
import com.xing.container.Context;
import com.xing.logger.PropertyChangeListener;

/**
 * ¼ÓÔØÆ÷½Ó¿Ú
 * @author Leo
 *
 */
public interface Loader {
	public ClassLoader getClassLoader();
	public Container getContainer();
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
	public void addRepository(String repository);
	public String[] findRepositories();
	public boolean modified();
	
}
