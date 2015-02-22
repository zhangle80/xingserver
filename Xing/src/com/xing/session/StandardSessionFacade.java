package com.xing.session;

import java.io.Serializable;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class StandardSessionFacade implements Session,Serializable,HttpSession {
	
	private StandardSession session;
	public StandardSessionFacade(StandardSession session){
		this.session=session;
	}
	@Override
	public String getCreateTime() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Manager getManager() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String setCreateTime(long time) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setManager(Manager manager) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setValid(boolean isValid) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		
	}
}
