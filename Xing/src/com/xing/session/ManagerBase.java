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
	 * Ĭ�ϵ���ϢժҪ�㷨
	 */
	protected static final String DEFAULT_ALGORITHM = "MD5";
	
	/**
	 * ���ڴ��SESSION_ID������������ֽ���
	 */
	protected static final int SESSION_ID_BYTES = 16;
	
	/**
	 * ���ɻỰ��ʾ��ʱ�����ϢժҪ�㷨�����㷨�����ɱ���ƽ̨��<code>java.security.MessageDigest</code>֧��
	 */
	protected String algorithm = DEFAULT_ALGORITHM;
	
	/**
	 * �������ɻỰ��ʾ������ϢժҪ�㷨ʵ����
	 */
	protected MessageDigest digest = null;
	
	/**
	 * �Ƿ���÷ֲ�ʽ��ʽ
	 */
	protected boolean distributable;
	
	/**
	 * �������ǵ�������������ĳ�ʼ����
	 */
	protected String entropy = null;
	
	protected int maxInactiveInterval = 60;
	
	protected static final String info = "ManagerBase/1.0";
	
	protected static String name = "ManagerBase";
	
	/**
	 * ���ɻỰ��ʾ���������������
	 */
	protected Random random=null;
	
	/**
	 * Ĭ���������������
	 */
	protected String randomClass = "java.security.SecureRandom";
	
	protected ArrayList<Session> recycled = new ArrayList<Session>();
	
	protected HashMap<String,Session> sessions = new HashMap<String,Session>();
	
	protected PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	/**
	 * Manager ������������
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
	 * ������ϢժҪ�㷨ʵ������һ�ε��ø÷�����ʱ������ʵ�����Ժ󷵻�ʵ���������ж��߳����Լ�ͬ��
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
	 * ���ɻỰ��ʾ���ַ���
	 * @return
	 */
	protected synchronized String generateSessionId() {
		Random random =this.getRandom();
		byte bytes[] = new byte[SESSION_ID_BYTES];//����һ��BYTE�������ڴ�ŻỰ��ʶ��
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
		synchronized(sessions){//���ܻ���д������������Ҫͬ��
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
	 * ������������������������ɻỰ��ʾ��
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
