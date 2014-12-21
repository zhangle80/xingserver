package com.xing.classloader;

public interface Reload {
	public void addRepository(String repository);
	public String[] findRepositories();
	
	public boolean modified();
}
