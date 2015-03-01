package com.xing.session;

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.xing.container.SimpleContext;

@SuppressWarnings("deprecation")
public class StandardSession implements Session,Serializable,HttpSession {

	/**
	 * ������Session�е��û��������Լ���
	 */
	private HashMap attributes = new HashMap();
	private transient String authType=null;
	private long creationTime = 0l;
	/**
	 * ����һ���Ự���ڣ��ƹ�ĳЩillegalstateexception���顣ע�⣺��ֵ�������ڸö�������л��汾��
	 */
	private transient boolean expiring = false;
	/**
	 * ���������
	 */
	private transient StandardSessionFacade facade = null;
	/**
	 * �Ự��ʶ���ַ���
	 */
	private String id=null;
	private long lastAccessedTime = creationTime;
	
	/**
	 * �Ự������
	 */
	private transient ArrayList listeners = new ArrayList();
	/**
	 * ���������
	 */
	private Manager manager = null;
	private boolean isValid = false;
	private boolean isNew =false;
	private transient Principal principal = null;
	/**
	 * �����Ч���ʱ�䣬�����뵥λ
	 */
	private int maxInactiveInterval=-1;
	
    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	public StandardSession(Manager manager){
		super();
		this.manager=manager;
	}
	
	@Override
	public String getCreateTime() {
		return String.valueOf(this.creationTime);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public long getLastAccessedTime() {
		return this.lastAccessedTime;
	}

	@Override
	public Manager getManager() {
		return this.manager;
	}

	@Override
	public HttpSession getSession() {
		if(this.facade==null){
			this.facade=new StandardSessionFacade(this);
		}
		return this.facade;
	}

	@Override
	public boolean isValid() {
		return this.isValid;
	}

	@Override
	public void setCreateTime(long time) {
		this.creationTime=time;
		this.lastAccessedTime=time;
	}

	@Override
	public void setId(String id) {
		if((this.id!=null)&&(this.manager!=null)){
			manager.remove(this);
		}
		
		this.id=id;
		
		if(this.manager!=null){
			this.manager.add(this);
		}
		//֪ͨSession�¼������ߣ�֪ͨsession�����¼���
		//֪ͨContext�¼������ߣ�֪ͨsession�����¼���
	}

	@Override
	public void setManager(Manager manager) {
		this.manager=manager;
	}

	@Override
	public void setValid(boolean isValid) {
		this.isValid=isValid;
	}

	@Override
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCreationTime() {
		return this.creationTime;
	}

	@Override
	public int getMaxInactiveInterval() {
		if(!this.isValid){
			throw new IllegalStateException("standardSession.getMaxInactiveInterval.ise");
		}
		return this.maxInactiveInterval;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeValue(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		this.maxInactiveInterval=arg0;
	}

	@Override
	public void setNew(boolean isNew) {
		this.isNew=isNew;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		String oldAuthType = this.authType;
		this.authType = authType;
		support.firePropertyChange("authType", oldAuthType, this.authType);
	}

	public Principal getPrincipal() {
		return principal;
	}

	public void setPrincipal(Principal principal) {
		Principal oldPrincipal=this.principal;
		this.principal = principal;
		support.firePropertyChange("principal", oldPrincipal, this.principal);
	}

	@Override
	public void access() {
		// TODO Auto-generated method stub
		this.isNew = false;
		this.lastAccessedTime= System.currentTimeMillis();
	}

	@Override
	public void expire() {
		// TODO Auto-generated method stub
		
	}

}
