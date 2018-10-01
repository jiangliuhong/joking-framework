package org.jokingframework.core.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author jiangliuhong
 * @since 1.0
 */
public final class ClassUtil {

    /**
     * get ClassLoader
     * 
     * @return
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * load class
     * 
     * @param className
     * @param isInitialized
     * @return
     */
    public static Class<?> loadClass(String className, Boolean isInitialized) {
        try {
            return Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定包下所有类
     * 
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url == null) {
                    break;
                }
                String protocol = url.getProtocol();
                if (StringUtil.equals(protocol, "file")) {
                    String packagePath = url.getPath();
                    addClass(classSet, packagePath, packageName);
                } else if (StringUtil.equals(protocol, "jar")) {
                    addJarClass(classSet, url);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classSet;
    }

    private static void addJarClass(Set<Class<?>> classSet, URL jarUrl) throws IOException {
        JarURLConnection jarURLConnection = (JarURLConnection)jarUrl.openConnection();
        jarUrl.openConnection();
        if (jarURLConnection == null) {
            return;
        }
        JarFile jarFile = jarURLConnection.getJarFile();
        if (jarFile == null) {
            return;
        }
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if (name.endsWith(".class")) {
                String className = name.substring(0, name.lastIndexOf(".")).replaceAll("/", ".");
                addClass(classSet, className);
            }
        }
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return ((pathname.isFile()) && pathname.getName().endsWith(".class")) || pathname.isDirectory();
            }
        });
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                // file
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtil.isNotBlank(packageName)) {
                    className = packageName + "." + className;
                }
                addClass(classSet, className);
            } else {
                // filename is floder path
                String subPackageName = fileName;
                String subPackagePath = fileName;
                if (StringUtil.isNotBlank(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                if (StringUtil.isNotBlank(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    private static void addClass(Set<Class<?>> classSet, String className) {
        if (classSet == null) {
            classSet = new HashSet<Class<?>>();
        }
        classSet.add(loadClass(className, false));
    }
}
