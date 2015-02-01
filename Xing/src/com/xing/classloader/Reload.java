package com.xing.classloader;

/**
 * 只负责监控类是否发生变化，如果发生变化则通知Loader,然后Loader通知其关联的容器，最后是由发生变化的类自己通过上下文重新加载自己
 * @author Leo
 *
 */
public interface Reload {
	public void addRepository(String repository);
	public String[] findRepositories();
	
	public boolean modified();
}
