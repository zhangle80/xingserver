package com.xing.classloader;

import java.net.URL;
import java.security.cert.Certificate;
import java.util.jar.Manifest;

/**
 * Դʵ����������������ص����ΪԴ��Ϊ�˺���Ĺ���������ص��࣬��Ҫ������һ������������ЩԴ
 * @author Leo
 *
 */
public class ResourceEntry {

	public long lastModify=-1;
	public byte[] binaryContent=null;
	public Class<?> loadClass=null;
	public URL source=null;
	public URL CodeBase=null;
	public Manifest manifest;
	public Certificate[] certificates=null;
}
