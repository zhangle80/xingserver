package com.xing.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class StandardSession implements Session,Serializable,HttpSession {

	private HashMap attributes = new HashMap();
	private long creationTime = 0l;
	private transient boolean expiring = false;
	private transient StandardSessionFacade facade = null;
	private String id=null;
	private long lastAccessedTime = creationTime;
	
	private transient ArrayList listeners = new ArrayList();
	private Manager manager = null;
	private boolean isValid = false;
	private int maxInactiveInterval=-1;
	
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
	public String setCreateTime(long time) {
		this.creationTime=time;
		return this.getCreateTime();
	}

	@Override
	public void setId(String id) {
		this.id=id;
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

}
