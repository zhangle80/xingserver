package com.xing.classloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.util.HashMap;

import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;


import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import com.xing.lifecycle.Lifecycle;
import com.xing.lifecycle.LifecycleListener;
import com.xing.logger.Logger;
import com.xing.logger.SystemOutLogger;


/**
 * 类加载器，由WebAppLoader调用
 * 负责Web应用的类加载的是org.apache.catalina.loader.WebappClassLoader，它几个比较重要的方法：
 * findClass(),loadClass(),findClassInternal(),findResourceInternal().
 * 类加载器被用来加载一个类的时候，loadClass()会被调用，loadClass()则调用findClass()。
 * 后两个方法是WebappClassLoader的私有方法，findClass()调用findClassInternal()来创建class对象，而findClassInternal()则需要findResourceInternal()来查找.class文件。
 * @author Leo
 *
 */
public class WebappClassLoader extends URLClassLoader implements Reload,Lifecycle {
	
    @SuppressWarnings("unchecked")
	protected class PrivilegedFindResource implements PrivilegedAction {

    private String name;
    private String path;

    PrivilegedFindResource(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public Object run() {
        try {
			return findResourceInternal(name, path);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
    }
}
	/**
	 * 不允许加载的类
	 */
	private static final String[] triggers ={"javax.servlet.Servlet"};
	/**
	 * 不允许加载的包
	 */
	private static final String[] packegTriggers={
		"javax","org.xml.sax","org.w3c.dom","org.apache.xerces","org.apache.xalan"
	};
	
	private ClassLoader parent = null;
	
	private boolean hasExternalRepositories=false;
	
	protected boolean started = false;
	
	protected HashMap<String,ResourceEntry> resourceEntries = new HashMap<String,ResourceEntry>();
	
	private Logger logger = new SystemOutLogger();
	
	private boolean resolve = false;
	
	private ClassLoader system = null;
	 
	private SecurityManager securityManager = null;
	
	protected String[] repositories = new String[0];
	
	protected JarFile[] jarFiles = new JarFile[0];
	 
	protected DirContext resources = null;
	
	protected File[] files = new File[0];
	
	private Permission allPermission = new java.security.AllPermission();
	
	protected long[] lastModifiedDates = new long[0];
	
	protected String[] paths = new String[0];
	
	@SuppressWarnings("unchecked")
	protected HashMap notFoundResources = new HashMap();
	
	protected File[] jarRealFiles = new File[0];
	/**
	 * 是否委托给父加载器加载
	 */
	private boolean delegate=false;
	
    public WebappClassLoader() {

        super(new URL[0]);
        this.parent = getParent();
        system = getSystemClassLoader();
        securityManager = System.getSecurityManager();

        if (securityManager != null) {
            refreshPolicy();
        }

    }
    
	public WebappClassLoader(URL[] arg0) {
		super(arg0);
        this.parent = getParent();
        this.system = getSystemClassLoader();
        this.securityManager = System.getSecurityManager();
        
        if (securityManager != null) {
            refreshPolicy();
        }
	}

	private void refreshPolicy() {
		// TODO Auto-generated method stub
		Policy policy=Policy.getPolicy();
		policy.refresh();
	}

	@Override
	public void addRepository(String repository) {
		if(repository.startsWith("/WEB-INF/lib")||repository.startsWith("/WEB-INF/classes")){
			return;
		}
		try{
			URL url=new URL(repository);
			super.addURL(url);
			this.hasExternalRepositories = true;
		}catch(MalformedURLException e){
			throw new IllegalArgumentException(e.toString());
		}
	}

	@Override
	public String[] findRepositories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean modified() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		this.started=true;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isDelegate() {
		return delegate;
	}

	public void setDelegate(boolean delegate) {
		this.delegate = delegate;
	}

	public static String[] getTriggers() {
		return triggers;
	}

	private Class<?> findLoadedClass0(String name){
		Class<?> clazz=null;
		ResourceEntry entry = this.resourceEntries.get(name);
		if(entry!=null){
			clazz=entry.loadClass;
		}
		return clazz;
	}
	
	public Class<?> loadClass(String name, boolean resolve){
		Class<?> clazz=null;
		logger.log("loadClass(" + name + ", " + resolve + ")");
		
		if(!this.started){
			logger.log("classLoader is not started");
			try {
				throw new ClassNotFoundException(name);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		clazz = this.findLoadedClass0(name);	//从缓存中加载
		if(clazz!=null){
			logger.log("get class from cache(findLoadedClass0)");
			if(this.resolve){
				this.resolveClass(clazz);
			}
			return clazz;
		}
		
		clazz = this.findLoadedClass(name);		//从缓存中加载
		if(clazz!=null){
			logger.log("get class from cache(findLoadedClass)");
			if(this.resolve){
				this.resolveClass(clazz);
			}
			return clazz;
		}
		
		try {
			clazz = system.loadClass(name);		//尝试从系统类加载器中获取类,阻止用户自己实现J2EE的类
			if(clazz!=null){
				logger.log("get class from system class loader");
				if(this.resolve){
					this.resolveClass(clazz);
				}
				return clazz;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if(this.securityManager!=null){
			int i=name.lastIndexOf(".");
			if(i>0){
				try {
					this.securityManager.checkPackageAccess(name.substring(0, i));
				}catch(SecurityException se){
                    String error = "Security Violation, attempt to use " +
                    "Restricted Class: " + name;
                    logger.log(error);
					try {
						throw new ClassNotFoundException(error);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}					
				}
			}
		}
		
		boolean delegateLoad=this.delegate || this.filter(name);//委托父加载器或在受限制的包内
		if(delegateLoad){
			logger.log("delegate to parent class loader");
			ClassLoader loader=this.parent;
			if(loader==null){
				loader=this.system;
			}
			try {
				clazz=loader.loadClass(name);
				if(clazz!=null){
					logger.log("get class from parent class loader");
					if(this.resolve){
						this.resolveClass(clazz);
					}
					return clazz;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		logger.log("Searching local repositories");			//经过以上处理过程均未获取到加载器，开始搜索本地库
		try {
			clazz = findClass(name);						//该方法为核心方法，核心加载的地方
			if(clazz!=null){
				logger.log("get class from local repositories");
				if(this.resolve){
					this.resolveClass(clazz);
				}
				return clazz;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if(!delegateLoad){									//代码如果执行到了这里，说明以上所有方法都没有加载到类，所以最后再尝试交给父加载器加载
			logger.log("every methods can't loading class successfully,so delegate to parent class loader");
			ClassLoader loader=this.parent;
			if(loader==null){
				loader=this.system;
			}
			try {
				clazz=loader.loadClass(name);
				if(clazz!=null){
					logger.log("get class from parent class loader");
					if(this.resolve){
						this.resolveClass(clazz);
					}
					return clazz;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		try {
			throw new ClassNotFoundException(name);		//最后所有办法都没辙，只好抛出异常
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
		
	}
	
	/* (non-Javadoc)
	 * @see java.net.URLClassLoader#findClass(java.lang.String)
	 * 
	 */
	protected Class<?> findClass(String name) throws ClassNotFoundException{
		logger.log("find class:"+name);
		
		if(this.securityManager!=null){						//安全管理验证
			int i=name.lastIndexOf(".");
			if(i>0){
				try {
					logger.log("securityManager.checkPackageDefinition");
					this.securityManager.checkPackageDefinition(name.substring(0, i));
				}catch(SecurityException se){
                    String error = "Security Violation, attempt to use " +
                    "Restricted Class: " + name;
                    logger.log(error);
					try {
						throw new ClassNotFoundException(error);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}					
				}
			}
		}
		
		Class<?> clazz=null;
		logger.log("findClassInternal(" + name + ")");
		
		try {
			clazz=findClassInternal(name);
		} catch (NamingException e) {
			e.printStackTrace();
		}					//本地库中加载类
		
		if(clazz==null&&this.hasExternalRepositories){	//若找不到类，则委托给父加载器加载
			clazz=super.findClass(name);
		}
		
		if(clazz==null){
			throw new ClassNotFoundException(name);
		}
		
		logger.log("Returning class " + clazz);
		
		if(clazz!=null){
			logger.log("Loaded by " + clazz.getClassLoader());
		}
		return clazz;
		
	}

	/**
	 * 在本地库中加载类，加载的方式是根据类资源信息来进行
	 * @param name
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws NamingException 
	 */
	@SuppressWarnings("unchecked")
	private Class<?> findClassInternal(String name) throws ClassNotFoundException, NamingException {
		//验证类名是否为空或是否为"java."开头的类
		if(!this.validate(name)){
			throw new ClassNotFoundException(name);
		}
		
        String tempPath = name.replace('.', '/');	//替换掉"."为"/"
        String classPath = tempPath + ".class";		//加后缀名".class"
        
        ResourceEntry entry = null;					//类资源实例，用于管理类资源
        
        if(this.securityManager!=null){
        	PrivilegedAction dp=new PrivilegedFindResource(name, classPath); 
        	entry = (ResourceEntry)AccessController.doPrivileged(dp);
        }else{
        	entry = this.findResourceInternal(name, classPath);//获取类资源信息，属于核心方法
        }
        
        if(entry==null||entry.binaryContent==null){
        	throw new ClassNotFoundException(name);
        }
        Class<?> clazz=entry.loadClass;				//类资源中会缓存已经加载过的类，该类放在loadClass属性中
        if(clazz!=null){							//说明之前加载过该类，直接返回
        	return clazz;
        }
        //类资源信息未没有缓存到类，则根据类资源信息做如下处理：1、定义package;2、对package安全检查;3、创建class对象（并缓存下来）
        String packageName = null;					//开始处理包信息
        int pos = name.lastIndexOf('.');			//查找最后一个"."
        if (pos != -1){								//是否能查找到"."
            packageName = name.substring(0, pos);	//说明是正常的包路径格式
        }
        Package pkg = null;
        
        if(packageName!=null){
        	pkg=getPackage(packageName);			//父类方法，获取包
        	if(pkg==null){
        		if(entry.manifest==null){
        			pkg=this.definePackage(packageName, null, null, null, null, null, null, null);
        		}else{
        			pkg=this.definePackage(packageName, entry.manifest, entry.CodeBase);
        		}
        	}
        }
        
        CodeSource codeSource=new CodeSource(entry.CodeBase,entry.certificates);
        
        if (securityManager != null) {				//做密封检查（？）
            // Checking sealing
            if (pkg != null) {
                boolean sealCheck = true;
                if (pkg.isSealed()) {
                    sealCheck = pkg.isSealed(entry.CodeBase);
                } else {
                    sealCheck = (entry.manifest == null)
                        || !isPackageSealed(packageName, entry.manifest);
                }
                if (!sealCheck)
                    throw new SecurityException
                        ("Sealing violation loading " + name + " : Package "
                         + packageName + " is sealed.");
            }
        }
        
        if(entry.loadClass==null){					//程序能运行到这里说明没有缓存到，为何此处还要再判断是否有缓存，因为是多进程，可能其它进程先一步缓存了，所以要判断一次
        	synchronized (this) {
        		if(entry.loadClass==null){			//此处是线程同步的一个惯用伎俩，条件要判断两次，临界区外的进程进入临界区后要重新判断，因为别的线程执行过之后，该线程可不必再执行
        			clazz=this.defineClass(name, entry.binaryContent, 0, entry.binaryContent.length, codeSource);//生成类
        			entry.loadClass = clazz;		//将类缓存下来
        		}else{
        			clazz=entry.loadClass;			//从缓存中读取
        		}
        	}
        }else{
        	clazz=entry.loadClass;					//从缓存中读取
        }
        
		return clazz;
	}



	/**
	 * 记载类资源（类由资源来管理）
	 * 要先加载相关实体资源(.jar) 再加载查找的资源本身
	 * @param name
	 * @param classPath
	 * @return
	 * @throws NamingException 
	 */
	@SuppressWarnings("unchecked")
	private ResourceEntry findResourceInternal(String name, String classPath) throws NamingException {
		if(!this.started){
			logger.log("Lifecycle error : ClassLoad stopped");
			return null;
		}
		if(name==null||classPath==null){
			return null;
		}
		
		ResourceEntry entry = this.resourceEntries.get(name);	//尝试从缓存中获取资源
		if(entry!=null){
			return entry;										//命中则直接返回
		}
		int contentLength = -1;									//资源二进制数据长度
		InputStream binaryStream=null;							//读取二进制文件流(字节码文件)
		
        int jarFilesLength = jarFiles.length;					//jar文件个数，classpath中的jar包个数
        int repositoriesLength = repositories.length;			//本地库个数，仓库数(classpath每一段称为repository仓库)
        
        int i;
        Resource resource = null;								//加载的资源实体
        //对每个仓库迭代，直接找到相应的entry，如果查找的资源是一个独立的文件，在这个代码块可以查找到相应资源，
        //如果是包含在jar包中的类，这段代码并不能找出其对应的资源
        for(i=0;(entry == null) && (i < repositoriesLength); i++){
        	 String fullPath = repositories[i] + classPath;		//仓库路径 加资源路径得到全路径
        	 
        	 try {
				Object lookupResult = this.resources.lookup(fullPath);//从资源库中查找资源
				if(lookupResult!=null&&(lookupResult instanceof Resource)){
					resource=(Resource)lookupResult;
				}
				
				entry=new ResourceEntry();
				entry.source=getURL(new File(files[i], classPath));
				entry.CodeBase=entry.source;
				
			} catch (NamingException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			//获取资源长度和最后修改时间
			ResourceAttributes attributes = (ResourceAttributes) this.resources.getAttributes(fullPath);
			contentLength = (int) attributes.getContentLength();
			entry.lastModify = attributes.getLastModified();
			//资源找到，将二进制输入流赋给binaryStream
			if(resource!=null){
				try {
					binaryStream = resource.streamContent();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//将资源的最后修改时间加到列表中去
                synchronized (allPermission) {
                    int j;
                    long[] result2 = new long[lastModifiedDates.length + 1];
                    
                    for (j = 0; j < lastModifiedDates.length; j++) {
                        result2[j] = lastModifiedDates[j];
                    }
                    result2[lastModifiedDates.length] = entry.lastModify;
                    lastModifiedDates = result2;

                    String[] result = new String[paths.length + 1];
                    for (j = 0; j < paths.length; j++) {
                        result[j] = paths[j];
                    }
                    result[paths.length] = fullPath;
                    paths = result;
                }
			}
        }
        
        if(entry==null&&(notFoundResources.containsKey(name))){
        	return null;
        }
        
        //开始从jar包中查找
        JarEntry jarEntry = null;
        for (i = 0; (entry == null) && (i < jarFilesLength); i++) {
            
        	jarEntry = jarFiles[i].getJarEntry(classPath);	//根据路径从jar包中查找资源
            if (jarEntry != null) {							//如果jar包中找到资源，则定义entry并将二进制流等数据赋给entry，同时将jar包解压到workdir.
                entry = new ResourceEntry();
                try {
                    entry.CodeBase = getURL(jarRealFiles[i]);
                    String jarFakeUrl = entry.CodeBase.toString();
                    jarFakeUrl = "jar:" + jarFakeUrl + "!/" + classPath;
                    entry.source = new URL(jarFakeUrl);
                } catch (MalformedURLException e) {
                    return null;
                }
                contentLength = (int) jarEntry.getSize();
                try {
                    entry.manifest = jarFiles[i].getManifest();
                    binaryStream = jarFiles[i].getInputStream(jarEntry);
                } catch (IOException e) {
                    return null;
                }
            }
        }
        
        if (entry == null) {
            synchronized (notFoundResources) {
                notFoundResources.put(name, name);
            }
            return null;
        }
      //从二进制流将资源内容读出
        if(binaryStream!=null){
        	byte[] binaryContent = new byte[contentLength];
        	
        	int pos=0;
        	try {
        		while(true){
        			int n=binaryStream.read(binaryContent, 0, binaryContent.length - pos);
        			if(n<0){
        				break;
        			}
        			pos+=n;
        		}
				binaryStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
			entry.binaryContent=binaryContent;
            if (jarEntry != null) {
                entry.certificates = jarEntry.getCertificates();
            }
        }
        //将资源加到缓存中
        synchronized(this.resourceEntries){
        	ResourceEntry entry2= this.resourceEntries.get(name);       	
        	if(entry2==null){
        		this.resourceEntries.put(name, entry);
        	}else{
        		entry=entry2;
        	}
        }
        
		return entry;
	}
	
	@SuppressWarnings("deprecation")
	private URL getURL(File file) throws MalformedURLException {
		// TODO Auto-generated method stub
		File realFile=file;
		try {
			realFile=realFile.getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return realFile.toURL();
	}

	private boolean isPackageSealed(String name, Manifest manifest) {
        String path = name + "/";
        Attributes attr = manifest.getAttributes(path);
        String sealed = null;
        if (attr != null) {
            sealed = attr.getValue(Name.SEALED);
        }
        if (sealed == null) {
            if ((attr = manifest.getMainAttributes()) != null) {
                sealed = attr.getValue(Name.SEALED);
            }
        }
        return "true".equalsIgnoreCase(sealed);
	}
	
	private boolean validate(String name) {
		// TODO Auto-generated method stub
		if(name==null){
			return false;
		}
		if(name.startsWith("java.")){
			return false;
		}
		return true;
	}

	/**
	 * 过滤类名是否在受限制的包内
	 * @param name
	 * @return
	 */
	private boolean filter(String name) {
		if(name==null||name.length()==0){
			return false;
		}
		String packageName=null;
		int pos=name.lastIndexOf(".");
		if(pos!=-1){
			packageName=name.substring(0,pos);
			for(int i=0;i<WebappClassLoader.packegTriggers.length;i++){
				if(packageName.startsWith(WebappClassLoader.packegTriggers[i])){
					return true;
				}
			}
		}
		return false;
	}

}
