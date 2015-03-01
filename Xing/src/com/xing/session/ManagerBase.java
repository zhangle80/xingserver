package com.xing.session;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.xing.container.Container;

public abstract class ManagerBase implements Manager {
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
	
	protected ArrayList<Session> recycled = new ArrayList<Session>();
	
	protected HashMap<String,Session> sessions = new HashMap<String,Session>();
	
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
	
	/**
	 * 返回信息摘要算法实例，第一次调用该方法的时候生成实例，以后返回实例，由于有多线程所以加同步
	 * @return
	 */
	public synchronized MessageDigest getDigest(){
		if(this.digest == null){
			try {
				this.digest = MessageDigest.getInstance(this.algorithm);
			} catch (NoSuchAlgorithmException e) {
				try {
					this.digest = MessageDigest.getInstance(DEFAULT_ALGORITHM);
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
					this.digest = null;
				}
				e.printStackTrace();
			}
		}
		return this.digest;
	}

	@Override
	public void add(Session session) {
		synchronized(this.sessions){
			this.sessions.put(session.getId(), session);
		}
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.support.addPropertyChangeListener(listener);
	}

	@Override
	public Session createSession() {
		Session session = null;
		synchronized(this.recycled){
			int size = this.recycled.size();
			session = this.recycled.get(size-1);
			this.recycled.remove(size-1);
		}
		if(session!=null){
			session.setManager(this);
		}else{
			session = new StandardSession(this);
		}
		session.setNew(true);
		session.setValid(true);
		session.setCreateTime(System.currentTimeMillis());
		session.setMaxInactiveInterval(this.maxInactiveInterval);
		String sessionId=this.generateSessionId();
		session.setId(sessionId);
		return session;
	}

	/**
	 * 生成会话标示符字符串
	 * @return
	 */
	protected synchronized String generateSessionId() {
		Random random =this.getRandom();
		byte bytes[] = new byte[SESSION_ID_BYTES];//生成一个BYTE数组用于存放会话标识符
		random.nextBytes(bytes);
		bytes = this.getDigest().digest(bytes);
		
		StringBuffer result = new StringBuffer();
		for(int i=0; i<bytes.length;i++){
            byte b1 = (byte) ((bytes[i] & 0xf0) >> 4);
            byte b2 = (byte) (bytes[i] & 0x0f);
            if (b1 < 10)
                result.append((char) ('0' + b1));
            else
                result.append((char) ('A' + (b1 - 10)));
            if (b2 < 10)
                result.append((char) ('0' + b2));
            else
                result.append((char) ('0' + (b2 - 10)));
		}
		return result.toString();
	}

	@Override
	public Session findSession(String id) {
		if(id==null){
			return null;
		}
		synchronized(sessions){//可能会有写入的情况，所以要同步
			Session session = this.sessions.get(id);
			return session;
		}
	}

	@Override
	public Session[] findSessions() {
		Session[] results=null;
		synchronized(sessions){
			results=(Session[])sessions.values().toArray(results);
		}
		return results;
	}

	@Override
	public Container getContainer() {
		return this.container;
	}

	@Override
	public boolean getDistributable() {
		return this.distributable;
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	public String getEntropy() {
		if(this.entropy==null){
			this.setEntropy(this.toString());
		}
		return entropy;
	}

	public void setEntropy(String entropy) {
		String oldEntropy = entropy;
		this.entropy = entropy;
		support.firePropertyChange("entropy", oldEntropy, this.entropy);
	}

	@Override
	public void remove(Session session) {
		synchronized(sessions){
			sessions.remove(session.getId());
		}
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.support.removePropertyChangeListener(listener);
	}

	@Override
	public void setContainer(Container container) {
		Container oldContainer = this.container;
		this.container = container;
		support.firePropertyChange("container", oldContainer, this.container);
	}

	@Override
	public void setDistributable(boolean distributable) {
		boolean oldDistributable=this.distributable;
		this.distributable=distributable;
		support.firePropertyChange("distributable", new Boolean(oldDistributable), new Boolean(this.distributable));
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
        int oldMaxInactiveInterval = this.maxInactiveInterval;
        this.maxInactiveInterval = interval;
        support.firePropertyChange("maxInactiveInterval",
                                   new Integer(oldMaxInactiveInterval),
                                   new Integer(this.maxInactiveInterval));
	}

	public String getName(){
		return name;
	}
	
	/**
	 * 返回随机数生成器，用以生成会话标示符
	 * @return
	 */
	public Random getRandom() {
		if(this.random == null){
			long seed = System.currentTimeMillis();
			char entropy[] = this.getEntropy().toCharArray();
			for(int i=0;i<entropy.length;i++){
				long update = ((byte)entropy[i])<<((i%8)*8);
				seed ^=update;
			}
			
			try {
				Class<?> clazz = Class.forName(this.randomClass);
				this.random = (Random) clazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				this.random = new Random();
			} finally{
				this.random.setSeed(seed);
			}
			
		}
		return random;
	}


	public String getRandomClass() {
		return randomClass;
	}

	public void setRandomClass(String randomClass) {
		String oldRandomClass = randomClass;
		this.randomClass = randomClass;
		support.firePropertyChange("randomClass", oldRandomClass, this.randomClass);
	}

	
	void recycle(Session session){
		synchronized(this.recycled){
			recycled.add(session);
		}
	}

}
