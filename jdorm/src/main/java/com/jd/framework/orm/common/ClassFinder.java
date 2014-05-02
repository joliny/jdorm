package com.jd.framework.orm.common;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassFinder {
    /**
     * 从包package中获取所有的Class
     * 
     * @return
     */
    public static Set<String> getClasses(String packageName, boolean recursive) {
        Set<String> classNameSet = new LinkedHashSet<String>();
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                getClassesInFileSystem(packageName, new File(url.getFile()), recursive, classNameSet);
            } else if ("jar".equals(protocol)) {
                getClassesInJar(packageName, url, recursive, classNameSet);
            }
        }

        return classNameSet;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     */
    private static void getClassesInFileSystem(String packageName, File packagePath, final boolean recursive, Set<String> classNameSet) {
        if (!packagePath.exists() || !packagePath.isDirectory()) {
            return;
        }
        File[] dirfiles = packagePath.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                getClassesInFileSystem(packageName + "." + file.getName(), file, recursive, classNameSet);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                classNameSet.add(packageName + '.' + className);
            }
        }
    }

    private static void getClassesInJar(String packageName, URL jarUrl, final boolean recursive, Set<String> classNameSet) {
        String packageDirName = packageName.replace('.', '/');

        JarFile jar;
        try {
            jar = ((JarURLConnection) jarUrl.openConnection()).getJarFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }
            if (name.startsWith(packageDirName)) {
                // 如果以"/"结尾 是一个包
                int idx = name.lastIndexOf('/');
                if (idx != -1) {
                    packageName = name.substring(0, idx).replace('/', '.');
                }
                // 如果可以迭代下去 并且是一个包
                if ((idx != -1) || recursive) {
                    // 如果是一个.class文件 而且不是目录
                    if (name.endsWith(".class") && !entry.isDirectory()) {
                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                        classNameSet.add(packageName + '.' + className);
                    }
                }
            }
        }
    }

}
