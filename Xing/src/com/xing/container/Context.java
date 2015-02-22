package com.xing.container;

import com.xing.logger.Logger;
import com.xing.session.Manager;

/**
 * �����������ӿڣ�һ�������Ĵ���һ��WEBӦ��ϵͳ��Ҳ���Գ�֮Ϊ��վ
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
