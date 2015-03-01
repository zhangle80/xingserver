package com.xing.session;

import javax.servlet.http.HttpSession;

public interface Session {

	public static final String SESSION_CREATED_EVENT="createSession";
	public static final String SESSION_DESTROYED_EVENT="destroySession";
	
	public String getCreateTime();
	public void setCreateTime(long time);
	
	public String getId();
	public void setId(String id);
	
	public long getLastAccessedTime();
	
	public Manager getManager();
	public void setManager(Manager manager);
	
	public HttpSession getSession();
	
	public void setValid(boolean isValid);
	public boolean isValid();
	
	public void setNew(boolean isNew);
	
	public void setMaxInactiveInterval(int interval);
	
	/**
	 * 更新会话读取时间信息，该方法被上下文所调用，调用时间发生在新的请求到达的时候，即使应用没有读取过会话信息
	 */
	public void access();
	
	public void expire();
	
}
