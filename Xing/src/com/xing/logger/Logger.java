package com.xing.logger;

import com.xing.container.Container;

public interface Logger {

	public static final int FATAL = Integer.MIN_VALUE;
	public static final int ERROR = 1;
	public static final int WARNING = 2;
	public static final int INFORMATION = 3;
	public static final int DEBUG = 4;
	
	public Container getContainer();
	public void setContainer(Container container);
	public String getInfo();
	public int getVerbosity();				//ศ฿ำเ
	public void setVerbosity(int verbosity);//ศ฿ำเ
	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void log(String message);
	public void log(Exception exception,String message);
	public void log(String message,Throwable throwable);
	public void log(String message,int verbosity);
	public void log(String message,Throwable throwable,int verbosity);
	public void removePropertyChangeListener(Container getContainer);
}
