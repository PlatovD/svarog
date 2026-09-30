package io.github.PlatovD.svarog.scanner;

import io.github.PlatovD.svarog.exception.SvarogScannerException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassScanner {

    private static final String PACKAGE_SEPARATOR = ".";
    private static final String CLASS_SUFFIX = ".class";
    private static final String PATH_SEPARATOR = "/";

    public Set<Class<?>> scan(String packageName, boolean recursive) {
        if (packageName == null) {
            throw new SvarogScannerException("packageName must not be null");
        }

        Set<Class<?>> result = new HashSet<>();
        String path = packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassScanner.class.getClassLoader();
        }

        try {
            Enumeration<URL> urls = classLoader.getResources(path);

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();

                if ("file".equals(url.getProtocol())) {
                    scanDirectory(url, packageName, recursive, result);
                } else if ("jar".equals(url.getProtocol())) {
                    scanJar(url, path, recursive, result);
                }
            }
        } catch (IOException e) {
            throw new SvarogScannerException("Failed to scan package " + packageName, e);
        }

        return result;
    }

    private void scanDirectory(URL url, String packageName, boolean recursive, Set<Class<?>> result) {
        File directory;
        try {
            directory = new File(url.toURI());
        } catch (Exception e) {
            throw new SvarogScannerException("Failed to read directory " + url, e);
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                if (!recursive) {
                    continue;
                }
                String subPackage = packageName + PACKAGE_SEPARATOR + file.getName();
                scanDirectory(fileToUrl(file), subPackage, recursive, result);
            } else if (file.getName().endsWith(CLASS_SUFFIX)) {
                String className = packageName + PACKAGE_SEPARATOR + stripClassSuffix(file.getName());
                addClassIfValid(className, result);
            }
        }
    }

    private void scanJar(URL url, String path, boolean recursive, Set<Class<?>> result) throws IOException {
        JarURLConnection conn = (JarURLConnection) url.openConnection();

        try (JarFile jar = conn.getJarFile()) {
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (!name.endsWith(CLASS_SUFFIX)) {
                    continue;
                }
                if (!name.startsWith(path)) {
                    continue;
                }

                String relative = name.substring(path.length() + 1);
                if (!recursive && relative.contains(PATH_SEPARATOR)) {
                    continue;
                }

                String className = name
                        .substring(0, name.length() - CLASS_SUFFIX.length())
                        .replace(PATH_SEPARATOR, PACKAGE_SEPARATOR);

                addClassIfValid(className, result);
            }
        }
    }

    private void addClassIfValid(String className, Set<Class<?>> result) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className, false,
                    Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
            return;
        }

        if (clazz.isInterface()) {
            return;
        }
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return;
        }
        if (clazz.isAnonymousClass()) {
            return;
        }
        if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
            return;
        }

        result.add(clazz);
    }

    private static String stripClassSuffix(String fileName) {
        return fileName.substring(0, fileName.length() - CLASS_SUFFIX.length());
    }

    private static URL fileToUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (Exception e) {
            throw new SvarogScannerException("Failed to convert file to URL: " + file, e);
        }
    }
}