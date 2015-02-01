package com.xing.classloader;

import java.net.URL;
import java.security.cert.Certificate;
import java.util.jar.Manifest;

/**
 * 源实例，被类加载器加载的类称为源，为了合理的管理这里加载的类，需要单独用一个类来管理这些源
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
