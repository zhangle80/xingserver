package com.xing.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import com.xing.lifecycle.Lifecycle;
import com.xing.lifecycle.LifecycleException;
import com.xing.lifecycle.LifecycleListener;
import com.xing.lifecycle.LifecycleSupport;

public class FileLogger extends LoggerBase implements Lifecycle {

	protected final String info="xing server logger file/1.0";
	protected LifecycleSupport lifecycleSupport=new LifecycleSupport(this);
	protected boolean started=false;	//是否开始 
	private String date;				//日志文档日期
	private PrintWriter writer = null;	//写入字符流 
	private boolean timestamp=true;		//是否记录时间戳
	private String directory;			//文件路径 
	private String prefix = "xing.";	//前缀
	private String suffix = ".log";		//后缀
	
	public boolean isTimestamp() {
		return timestamp;
	}

	public void setTimestamp(boolean timestamp) {
		this.timestamp = timestamp;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public FileLogger(String directory){
		this.directory=directory;
	}
	
	@Override
	public void log(String message) {
		Timestamp ts=new Timestamp(System.currentTimeMillis());
		String tsString=ts.toString().substring(0, 19);
		String tsDate=ts.toString().substring(0,10);
		
		if(!tsDate.equals(this.date)){
			synchronized(this){
				if(!tsDate.equals(this.date)){
					this.close();
					this.date=tsDate;					
					this.open();
				}
			}
		}
		if(this.writer!=null){
			this.writer.println("");
			if(this.timestamp){
				this.writer.print(tsString+":");
			}
			this.writer.print(message);
			this.writer.flush();
		}
	}
	
	private void open(){
		File dir=new File(this.directory);
		if(dir.isAbsolute()){
			dir=new File(System.getProperty("xing.base.logger"),this.directory);
		}
		dir.mkdirs();
		String fileName=dir.getAbsolutePath()+File.separator+this.prefix+this.date+this.suffix;
		try {
			this.writer=new PrintWriter(new FileWriter(fileName,true),true);
		} catch (IOException e) {
			this.writer=null;
			e.printStackTrace();
		}
	}
	
	private void close(){
		if(this.writer==null){
			return;
		}
		this.writer.flush();
		this.writer.close();
		this.writer = null;
		this.date="";
	}
	
	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		this.lifecycleSupport.addLifecycleListener(listener);
	}
	@Override
	public LifecycleListener[] findLifecycleListeners() {
		return this.lifecycleSupport.findLifecycleListener();
	}
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		this.lifecycleSupport.removeLifecycleListener(listener);
	}
	@Override
	public void start() {
		if(this.started){
			try {
				throw new LifecycleException("fileLogger.alreadyStarted");
			} catch (LifecycleException e) {
				e.printStackTrace();
			}
		}
		this.lifecycleSupport.fireLifecycleEvent(START_EVENT, null);
		this.started=true;
	}
	@Override
	public void stop() {
		if(!this.started){
			try {
				throw new LifecycleException("fileLogger.alreadyStopped");
			} catch (LifecycleException e) {
				e.printStackTrace();
			}
		}
		this.lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);
		this.started=false;
	}

}
