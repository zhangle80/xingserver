package com.xing.classloader;

/**
 * ֻ���������Ƿ����仯����������仯��֪ͨLoader,Ȼ��Loader֪ͨ�������������������ɷ����仯�����Լ�ͨ�����������¼����Լ�
 * @author Leo
 *
 */
public interface Reload {
	public void addRepository(String repository);
	public String[] findRepositories();
	
	public boolean modified();
}
