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
 * �����������WebAppLoader����
 * ����WebӦ�õ�����ص���org.apache.catalina.loader.WebappClassLoader���������Ƚ���Ҫ�ķ�����
 * findClass(),loadClass(),findClassInternal(),findResourceInternal().
 * �����������������һ�����ʱ��loadClass()�ᱻ���ã�loadClass()�����findClass()��
 * ������������WebappClassLoader��˽�з�����findClass()����findClassInternal()������class���󣬶�findClassInternal()����ҪfindResourceInternal()������.class�ļ���
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
	 * ��������ص���
	 */
	private static final String[] triggers ={"javax.servlet.Servlet"};
	/**
	 * ��������صİ�
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
	 * �Ƿ�ί�и�������������
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
		
		clazz = this.findLoadedClass0(name);	//�ӻ����м���
		if(clazz!=null){
			logger.log("get class from cache(findLoadedClass0)");
			if(this.resolve){
				this.resolveClass(clazz);
			}
			return clazz;
		}
		
		clazz = this.findLoadedClass(name);		//�ӻ����м���
		if(clazz!=null){
			logger.log("get class from cache(findLoadedClass)");
			if(this.resolve){
				this.resolveClass(clazz);
			}
			return clazz;
		}
		
		try {
			clazz = system.loadClass(name);		//���Դ�ϵͳ��������л�ȡ��,��ֹ�û��Լ�ʵ��J2EE����
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
		
		boolean delegateLoad=this.delegate || this.filter(name);//ί�и����������������Ƶİ���
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
		
		logger.log("Searching local repositories");			//�������ϴ�����̾�δ��ȡ������������ʼ�������ؿ�
		try {
			clazz = findClass(name);						//�÷���Ϊ���ķ��������ļ��صĵط�
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
		
		if(!delegateLoad){									//�������ִ�е������˵���������з�����û�м��ص��࣬��������ٳ��Խ���������������
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
			throw new ClassNotFoundException(name);		//������а취��û�ޣ�ֻ���׳��쳣
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
		
		if(this.securityManager!=null){						//��ȫ������֤
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
		}					//���ؿ��м�����
		
		if(clazz==null&&this.hasExternalRepositories){	//���Ҳ����࣬��ί�и�������������
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
	 * �ڱ��ؿ��м����࣬���صķ�ʽ�Ǹ�������Դ��Ϣ������
	 * @param name
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws NamingException 
	 */
	@SuppressWarnings("unchecked")
	private Class<?> findClassInternal(String name) throws ClassNotFoundException, NamingException {
		//��֤�����Ƿ�Ϊ�ջ��Ƿ�Ϊ"java."��ͷ����
		if(!this.validate(name)){
			throw new ClassNotFoundException(name);
		}
		
        String tempPath = name.replace('.', '/');	//�滻��"."Ϊ"/"
        String classPath = tempPath + ".class";		//�Ӻ�׺��".class"
        
        ResourceEntry entry = null;					//����Դʵ�������ڹ�������Դ
        
        if(this.securityManager!=null){
        	PrivilegedAction dp=new PrivilegedFindResource(name, classPath); 
        	entry = (ResourceEntry)AccessController.doPrivileged(dp);
        }else{
        	entry = this.findResourceInternal(name, classPath);//��ȡ����Դ��Ϣ�����ں��ķ���
        }
        
        if(entry==null||entry.binaryContent==null){
        	throw new ClassNotFoundException(name);
        }
        Class<?> clazz=entry.loadClass;				//����Դ�лỺ���Ѿ����ع����࣬�������loadClass������
        if(clazz!=null){							//˵��֮ǰ���ع����ֱ࣬�ӷ���
        	return clazz;
        }
        //����Դ��Ϣδû�л��浽�࣬���������Դ��Ϣ�����´���1������package;2����package��ȫ���;3������class���󣨲�����������
        String packageName = null;					//��ʼ�������Ϣ
        int pos = name.lastIndexOf('.');			//�������һ��"."
        if (pos != -1){								//�Ƿ��ܲ��ҵ�"."
            packageName = name.substring(0, pos);	//˵���������İ�·����ʽ
        }
        Package pkg = null;
        
        if(packageName!=null){
        	pkg=getPackage(packageName);			//���෽������ȡ��
        	if(pkg==null){
        		if(entry.manifest==null){
        			pkg=this.definePackage(packageName, null, null, null, null, null, null, null);
        		}else{
        			pkg=this.definePackage(packageName, entry.manifest, entry.CodeBase);
        		}
        	}
        }
        
        CodeSource codeSource=new CodeSource(entry.CodeBase,entry.certificates);
        
        if (securityManager != null) {				//���ܷ��飨����
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
        
        if(entry.loadClass==null){					//���������е�����˵��û�л��浽��Ϊ�δ˴���Ҫ���ж��Ƿ��л��棬��Ϊ�Ƕ���̣���������������һ�������ˣ�����Ҫ�ж�һ��
        	synchronized (this) {
        		if(entry.loadClass==null){			//�˴����߳�ͬ����һ�����ü���������Ҫ�ж����Σ��ٽ�����Ľ��̽����ٽ�����Ҫ�����жϣ���Ϊ����߳�ִ�й�֮�󣬸��߳̿ɲ�����ִ��
        			clazz=this.defineClass(name, entry.binaryContent, 0, entry.binaryContent.length, codeSource);//������
        			entry.loadClass = clazz;		//���໺������
        		}else{
        			clazz=entry.loadClass;			//�ӻ����ж�ȡ
        		}
        	}
        }else{
        	clazz=entry.loadClass;					//�ӻ����ж�ȡ
        }
        
		return clazz;
	}



	/**
	 * ��������Դ��������Դ������
	 * Ҫ�ȼ������ʵ����Դ(.jar) �ټ��ز��ҵ���Դ����
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
		
		ResourceEntry entry = this.resourceEntries.get(name);	//���Դӻ����л�ȡ��Դ
		if(entry!=null){
			return entry;										//������ֱ�ӷ���
		}
		int contentLength = -1;									//��Դ���������ݳ���
		InputStream binaryStream=null;							//��ȡ�������ļ���(�ֽ����ļ�)
		
        int jarFilesLength = jarFiles.length;					//jar�ļ�������classpath�е�jar������
        int repositoriesLength = repositories.length;			//���ؿ�������ֿ���(classpathÿһ�γ�Ϊrepository�ֿ�)
        
        int i;
        Resource resource = null;								//���ص���Դʵ��
        //��ÿ���ֿ������ֱ���ҵ���Ӧ��entry��������ҵ���Դ��һ���������ļ���������������Բ��ҵ���Ӧ��Դ��
        //����ǰ�����jar���е��࣬��δ��벢�����ҳ����Ӧ����Դ
        for(i=0;(entry == null) && (i < repositoriesLength); i++){
        	 String fullPath = repositories[i] + classPath;		//�ֿ�·�� ����Դ·���õ�ȫ·��
        	 
        	 try {
				Object lookupResult = this.resources.lookup(fullPath);//����Դ���в�����Դ
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
			//��ȡ��Դ���Ⱥ�����޸�ʱ��
			ResourceAttributes attributes = (ResourceAttributes) this.resources.getAttributes(fullPath);
			contentLength = (int) attributes.getContentLength();
			entry.lastModify = attributes.getLastModified();
			//��Դ�ҵ���������������������binaryStream
			if(resource!=null){
				try {
					binaryStream = resource.streamContent();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//����Դ������޸�ʱ��ӵ��б���ȥ
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
        
        //��ʼ��jar���в���
        JarEntry jarEntry = null;
        for (i = 0; (entry == null) && (i < jarFilesLength); i++) {
            
        	jarEntry = jarFiles[i].getJarEntry(classPath);	//����·����jar���в�����Դ
            if (jarEntry != null) {							//���jar�����ҵ���Դ������entry�����������������ݸ���entry��ͬʱ��jar����ѹ��workdir.
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
      //�Ӷ�����������Դ���ݶ���
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
        //����Դ�ӵ�������
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
	 * ���������Ƿ��������Ƶİ���
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
