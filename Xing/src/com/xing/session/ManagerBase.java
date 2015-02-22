package com.xing.session;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.xing.container.Container;
import com.xing.logger.PropertyChangeListener;

public class ManagerBase implements Manager {
	/**
	 * 默认的信息摘要算法
	 */
	protected static final String DEFAULT_ALGORITHM = "MD5";
	
	/**
	 * 用于存放SESSION_ID（随机数）的字节数
	 */
	protected static final int SESSION_ID_BYTES = 16;
	
	/**
	 * 生成会话标示符时候的信息摘要算法，该算法必须由本地平台的<code>java.security.MessageDigest</code>支持
	 */
	protected String algorithm = DEFAULT_ALGORITHM;
	
	/**
	 * 返回生成会话标示符的信息摘要算法实现类
	 */
	protected MessageDigest digest = null;
	
	/**
	 * 是否采用分布式方式
	 */
	protected boolean distributable;
	
	/**
	 * 增加我们的随机数发生器的初始化熵
	 */
	protected String entropy = null;
	
	protected int maxInactiveInterval = 60;
	
	protected static final String info = "ManagerBase/1.0";
	
	protected static String name = "ManagerBase";
	
	/**
	 * 生成会话标示符的随机数生成器
	 */
	protected Random random=null;
	
	/**
	 * 默认随机数生成器类
	 */
	protected String randomClass = "java.security.SecureRandom";
	
	@SuppressWarnings("unchecked")
	protected ArrayList recycled = new ArrayList();
	
	protected HashMap sessions = new HashMap();
	
	protected PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	/**
	 * Manager 所关联的容器
	 */
	protected Container container;

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		String oldAlgorithm=this.algorithm;
		this.algorithm = algorithm;
		support.firePropertyChange("algorithm", oldAlgorithm, this.algorithm);
	}

	@Override
	public void add(Session session) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Session createSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session findSession(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session[] findSessions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container getContainer() {
		return this.container;
	}

	@Override
	public boolean getDistributable() {
		return false;
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void load() throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Session session) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePropertyChangeListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContainer(Container container) {
		Container oldContainer = this.container;
		this.container = container;
		support.firePropertyChange("container", oldContainer, this.container);
	}

	@Override
	public void setDistributable(boolean distributable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unload() throws IOException {
		// TODO Auto-generated method stub

	}

}
