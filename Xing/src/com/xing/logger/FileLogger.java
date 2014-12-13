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
	protected boolean started=false;	//�Ƿ�ʼ 
	private String date;				//��־�ĵ�����
	private PrintWriter writer = null;	//д���ַ��� 
	private boolean timestamp=true;		//�Ƿ��¼ʱ���
	private String directory;			//�ļ�·�� 
	private String prefix = "xing.";	//ǰ׺
	private String suffix = ".log";		//��׺
	
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
