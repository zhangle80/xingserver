package com.xing.logger;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import com.xing.container.Container;
import com.xing.lifecycle.LifecycleException;

public abstract class LoggerBase implements Logger {
	
	protected final String info="xing server logger base/1.0";
	protected int verbosity=Logger.INFORMATION;
	protected Container container;

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {

	}

	@Override
	public Container getContainer() {
		return this.container;
	}

	@Override
	public String getInfo() {
		return this.info;
	}

	@Override
	public int getVerbosity() {
		return this.verbosity;
	}

	@Override
	public abstract void log(String message);

	@Override
	public void log(Exception exception, String message) {
		this.log(message, exception);
	}

	@Override
	public void log(String message, Throwable throwable) {
        CharArrayWriter buf = new CharArrayWriter();
        PrintWriter writer = new PrintWriter(buf);
        writer.println(message);
        throwable.printStackTrace(writer);
        Throwable rootCause = null;
        if (throwable instanceof LifecycleException)
            rootCause = ((LifecycleException) throwable).getThrowable();
        else if (throwable instanceof ServletException)
            rootCause = ((ServletException) throwable).getRootCause();
        if (rootCause != null) {
            writer.println("----- Root Cause -----");
            rootCause.printStackTrace(writer);
        }
        log(buf.toString());
	}

	@Override
	public void log(String message, int verbosity) {
		if(this.verbosity>=verbosity){
			this.log(message);
		}
	}

	@Override
	public void log(String message, Throwable throwable, int verbosity) {
		if(this.verbosity>=verbosity){
			this.log(message, throwable);
		}
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {

	}

	@Override
	public void setContainer(Container container) {
		this.container=container;
	}

	@Override
	public void setVerbosity(int verbosity) {
		switch(verbosity){
		case Logger.ERROR: this.verbosity=verbosity;
		break;
		case Logger.WARNING: this.verbosity=verbosity;
		break;
		case Logger.DEBUG:this.verbosity=verbosity;
		break;
		case Logger.INFORMATION:this.verbosity=verbosity;
		break;
		case Logger.FATAL:this.verbosity=verbosity;
		break;
		default:this.verbosity=Logger.INFORMATION;
		break;
		}
	}

}
