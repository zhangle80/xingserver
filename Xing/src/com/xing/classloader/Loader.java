package com.xing.classloader;

import com.xing.container.Container;
import com.xing.container.Context;
import com.xing.logger.PropertyChangeListener;

/**
 * �������ӿ�
 * @author Leo
 *
 */
public interface Loader {
	public ClassLoader getClassLoader();
	/**
	 * һ��Tomcat�ļ�����ͨ����һ�������������
	 * @return
	 */
	public Container getContainer();
	/**
	 * һ��Tomcat�ļ�����ͨ����һ�������������
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
	 * �����������һ����
	 * @param repository
	 */
	public void addRepository(String repository);
	/**
	 * �������ڷ���һ�����п�Ķ���
	 * @return
	 */
	public String[] findRepositories();
	public boolean modified();
	
}
