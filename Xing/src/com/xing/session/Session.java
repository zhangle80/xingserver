package com.xing.session;

import javax.servlet.http.HttpSession;

public interface Session {

	public static final String SESSION_CREATED_EVENT="createSession";
	public static final String SESSION_DESTROYED_EVENT="destroySession";
	
	public String getCreateTime();
	public String setCreateTime(long time);
	
	public String getId();
	public void setId(String id);
	
	public long getLastAccessedTime();
	
	public Manager getManager();
	public void setManager(Manager manager);
	
	public HttpSession getSession();
	
	public void setValid(boolean isValid);
	public boolean isValid();
}
