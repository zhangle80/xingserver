package com.xing.session;

import java.beans.PropertyChangeListener;
import java.io.IOException;

import com.xing.container.Container;

public interface Manager {
	
	/**
	 * 获取容器
	 * @return
	 */
	public Container getContainer();
	
	/**
	 * 设置容器
	 * @param container
	 */
	public void setContainer(Container container);
	
	/**
	 * 获取分布式标志
	 * @return
	 */
	public boolean getDistributable();
	
	/**
	 * 设置支持分布式标志，若该标志被设置，则所有被MANAGER管理的SESSION必须实现SERIALIABLE接口
	 * @param distributable
	 */
	public void setDistributable(boolean distributable);
	
	public String getInfo();
	
	/**
	 * 获取最大失效时间间隔
	 * @return
	 */
	public int getMaxInactiveInterval();
	
	/**
	 * 设置最大失效间隔时间
	 * @param interval
	 */
	public void setMaxInactiveInterval(int interval);
	
	/**
	 * 添加SESSION到集合中
	 * @param session
	 */
	public void add(Session session);
	
	public void addPropertyChangeListener(PropertyChangeListener listener);
	
	/**
	 * 生成SESSION
	 * @return
	 */
	public Session createSession();
	
	/**
	 * 根据ID返回某个SESSION数据
	 * @param id
	 * @return
	 */
	public Session findSession(String id);
	
	/**
	 * 返回所有SESSION数据
	 * @return
	 */
	public Session[] findSessions();
	
	/**
	 * 将已经持久化的SESSION数据加载到内存中
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void load() throws ClassNotFoundException,IOException;
	
	/**
	 * 移除某个SESSION
	 * @param session
	 */
	public void remove(Session session);
	
	public void removePropertyChangeListener(PropertyChangeListener listener);
	
	/**
	 * 将SESSION数据持久化
	 * @throws IOException
	 */
	public void unload() throws IOException;
}
