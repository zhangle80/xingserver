package com.xing.session;

import java.beans.PropertyChangeListener;
import java.io.IOException;

import com.xing.container.Container;

public interface Manager {
	
	/**
	 * ��ȡ����
	 * @return
	 */
	public Container getContainer();
	
	/**
	 * ��������
	 * @param container
	 */
	public void setContainer(Container container);
	
	/**
	 * ��ȡ�ֲ�ʽ��־
	 * @return
	 */
	public boolean getDistributable();
	
	/**
	 * ����֧�ֲַ�ʽ��־�����ñ�־�����ã������б�MANAGER�����SESSION����ʵ��SERIALIABLE�ӿ�
	 * @param distributable
	 */
	public void setDistributable(boolean distributable);
	
	public String getInfo();
	
	/**
	 * ��ȡ���ʧЧʱ����
	 * @return
	 */
	public int getMaxInactiveInterval();
	
	/**
	 * �������ʧЧ���ʱ��
	 * @param interval
	 */
	public void setMaxInactiveInterval(int interval);
	
	/**
	 * ���SESSION��������
	 * @param session
	 */
	public void add(Session session);
	
	public void addPropertyChangeListener(PropertyChangeListener listener);
	
	/**
	 * ����SESSION
	 * @return
	 */
	public Session createSession();
	
	/**
	 * ����ID����ĳ��SESSION����
	 * @param id
	 * @return
	 */
	public Session findSession(String id);
	
	/**
	 * ��������SESSION����
	 * @return
	 */
	public Session[] findSessions();
	
	/**
	 * ���Ѿ��־û���SESSION���ݼ��ص��ڴ���
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void load() throws ClassNotFoundException,IOException;
	
	/**
	 * �Ƴ�ĳ��SESSION
	 * @param session
	 */
	public void remove(Session session);
	
	public void removePropertyChangeListener(PropertyChangeListener listener);
	
	/**
	 * ��SESSION���ݳ־û�
	 * @throws IOException
	 */
	public void unload() throws IOException;
}
