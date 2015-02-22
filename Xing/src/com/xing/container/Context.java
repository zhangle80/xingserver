package com.xing.container;

import com.xing.logger.Logger;
import com.xing.session.Manager;

/**
 * 上下文容器接口，一个上下文代表一个WEB应用系统，也可以称之为网站
 * @author Leo
 *
 */
public interface Context extends Container {
	public void addWrapper(Wrapper wrapper);
	
	public void createWrapper();
	
	public void setLog(Logger logger);
	
	public Logger getLog();
	
	public void reload();
	
	public void setPath(String path);
	
	public void setDocBase(String docBase);
	
	public Manager getManager();

}
