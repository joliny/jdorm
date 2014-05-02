package com.jd.framework.orm.common;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * <p>Description: 扫描指定包（包括jar）下的class文件 (网友提供)</p>

 */
public class ScanClassPathUtil{
	
	//private static final Logger logger = Logger.getLogger(ScanClassPathUtil.class);
	private static final Logger logger=LoggerFactory.getLogger(ScanClassPathUtil.class); 
	/**
	 * 是否排除内部类 true->是 false->否
	 */
	private boolean excludeInner = true;
	/**
	 * 过滤规则适用情况 true—>搜索符合规则的 false->排除符合规则的
	 */
	private boolean checkInOrEx = true;

	/**
	 * 过滤规则列表 如果是null或者空，即全部符合不过滤
	 */
	private List<String> classFilters = null;

	/**
	 * 
	 * <br/>Description:无参构造器，默认是排除内部类、并搜索符合规则
	
	 */
	public ScanClassPathUtil(){
		
	}

	/**
	 * 
	 * <br/>Description:有参数据的构造器
	
	 * 
	 * @param excludeInner:是否排除内部类 true->是 false->否<br>
	 * @param checkInOrEx：过滤规则适用情况 true—>搜索符合规则的 false->排除符合规则的<br>
	 * @param classFilters：自定义过滤规则，如果是null或者空，即全部符合不过滤
	 */
	public ScanClassPathUtil(Boolean excludeInner,Boolean checkInOrEx,List<String> classFilters){
		this.excludeInner = excludeInner;
		this.checkInOrEx = checkInOrEx;
		this.classFilters = classFilters;

	}

	/**
	 * 
	 * <br/>Description:扫描包
	
	 * 
	 * @param basePackage
	 * @param recursive
	 * @return
	 */
	public Set<Class<?>> getPkgClassAll(String basePackage,boolean recursive){
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		String packageName = basePackage;
		if(packageName.endsWith(".")){
			packageName = packageName.substring(0,packageName.lastIndexOf('.'));
		}
		String package2Path = packageName.replace('.','/');
		
		Enumeration<URL> dirs;
		try{
			dirs = Thread.currentThread().getContextClassLoader().getResources(package2Path);
			while(dirs.hasMoreElements()){
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if("file".equals(protocol) || "vfs".equals(protocol)){
					String filePath = URLDecoder.decode(url.getFile(),"UTF-8");
					scanPackageClassesByFile(classes,packageName,filePath,recursive);
				}else if("jar".equals(protocol)){
					scanPackageClassesByJar(packageName,url,recursive,classes);
				}
			}
		}catch(IOException e){
			logger.error("IOException error:",e);
		}

		return classes;
	}

	/**
	 * 
	 * <br/>Description:以jar的方式扫描包下的所有Class文件
	
	 * 
	 * @param basePackage
	 * @param url
	 * @param recursive
	 * @param classes
	 */
	private void scanPackageClassesByJar(String basePackage,URL url,final boolean recursive,Set<Class<?>> classes){
		String packageName = basePackage;
		String package2Path = packageName.replace('.','/');
		JarFile jar;
		try{
			jar = ((JarURLConnection) url.openConnection()).getJarFile();
			Enumeration<JarEntry> entries = jar.entries();
			while(entries.hasMoreElements()){
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				if( ! name.startsWith(package2Path) || entry.isDirectory()){
					continue;
				}

				// 判断是否递归搜索子包
				if( ! recursive && name.lastIndexOf('/') != package2Path.length()){
					continue;
				}
				// 判断是否过滤 inner class
				if(this.excludeInner && name.indexOf('$') != - 1){
//					logger.info("exclude inner class with name:" + name);
					continue;
				}
				String classSimpleName = name.substring(name.lastIndexOf('/') + 1);
				// 判定是否符合过滤条件
				if(this.filterClassName(classSimpleName)){
					String className = name.replace('/','.');
					className = className.substring(0,className.length() - 6);
					try{
						classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
					}catch(ClassNotFoundException e){
						logger.error("Class.forName error:",e);
					}
				}
			}
		}catch(IOException e){
			logger.error("IOException error:",e);
		}
	}

	/**
	 * 
	 * <br/>Description:以文件的方式扫描包下的所有Class文件
	
	 * 
	 * @param classes
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 */
	private void scanPackageClassesByFile(Set<Class<?>> classes,String packageName,String packagePath,boolean recursive){
		File dir = new File(packagePath);
		if( ! dir.exists() || ! dir.isDirectory()){ return; }
		final boolean fileRecursive = recursive;
		File[] dirfiles = dir.listFiles(new FileFilter(){
			// 自定义文件过滤规则
			public boolean accept(File file){
				if(file.isDirectory()){ return fileRecursive; }
				String filename = file.getName();
				if(excludeInner && filename.indexOf('$') != - 1){
//					logger.info("exclude inner class with name:" + filename);
					return false;
				}
				return filterClassName(filename);
			}
		});
		for(File file:dirfiles){
			if(file.isDirectory()){
				scanPackageClassesByFile(classes,packageName + "." + file.getName(),file.getAbsolutePath(),recursive);
			}else{
				String className = file.getName().substring(0,file.getName().length() - 6);
				try{
					classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));

				}catch(ClassNotFoundException e){
					logger.error("IOException error:",e);
				}
			}
		}
	}

	/**
	 * 
	 * <br/>Description:根据过滤规则判断类名
	
	 * 
	 * @param className
	 * @return
	 */
	private boolean filterClassName(String className){
		if( ! className.endsWith(".class")){ return false; }
		if(null == this.classFilters || this.classFilters.isEmpty()){ return true; }
		String tmpName = className.substring(0,className.length() - 6);
		boolean flag = false;
		for(String str:classFilters){
			String tmpreg = "^" + str.replace("*",".*") + "$";
			Pattern p = Pattern.compile(tmpreg);
			if(p.matcher(tmpName).find()){
				flag = true;
				break;
			}
		}
		return (checkInOrEx && flag) || ( ! checkInOrEx && ! flag);
	}

	public boolean isExcludeInner(){
		return excludeInner;
	}

	public void setExcludeInner(boolean excludeInner){
		this.excludeInner = excludeInner;
	}

	public boolean isCheckInOrEx(){
		return checkInOrEx;
	}

	public void setCheckInOrEx(boolean checkInOrEx){
		this.checkInOrEx = checkInOrEx;
	}

	public List<String> getClassFilters(){
		return classFilters;
	}

	public void setClassFilters(List<String> classFilters){
		this.classFilters = classFilters;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args){

		// 自定义过滤规则
		List<String> classFilters = new ArrayList<String>();
		// classFilters.add("*Path");
		// classFilters.add("test*");
		// 创建一个扫描处理器，排除内部类 扫描符合条件的类
		 classFilters.clear();
		 classFilters.add("*");
		 ScanClassPathUtil handler = new ScanClassPathUtil(false,true,classFilters);
		//ScanClassPathUtil handler = new ScanClassPathUtil();

		// System.out.println("开始递归扫描jar文件的包：org.apache.commons.io 下符合自定义过滤规则的类...");
		// Set<Class<?>> calssList =
		// handler.getPkgClassAll("com.Eriloan.EasyPage",true);
		// for(Class<?> cla:calssList){
		// System.out.println(cla.getName());
		// }
		// System.out.println("开始递归扫描file文件的包：michael.hessian 下符合自定义过滤规则的类...");
		
		 Set<Class<?>> calssList = handler.getPkgClassAll(".com.yespme.framework.",true);
		 System.out.println(calssList.size());
		 for(Class<?> cla:calssList){
		 System.out.println(cla.getName());
		 }
	}
}
