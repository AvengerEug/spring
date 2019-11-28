package com.eugene.sumarry.customize.spring.context.anno;

import com.eugene.sumarry.customize.spring.annotation.Description;
import com.eugene.sumarry.customize.spring.annotation.Lazy;
import com.eugene.sumarry.customize.spring.annotation.Primary;
import com.eugene.sumarry.customize.spring.annotation.Scope;
import com.eugene.sumarry.customize.spring.beans.BeanDefinition;
import com.eugene.sumarry.customize.spring.beans.anno.AnnotationBeanDefinition;
import com.eugene.sumarry.customize.spring.context.BeanNameGenerator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class AnnotatedBeanDefinitionReader {

    private List<Class> allClass = new ArrayList<>();

    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();


    private String processPkgFilePath(String pkg) {
        String result = null;
        try {
            result = new URL(pkg).getFile();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return result;
    }

    public void doScan(String pkg) {
        File file = new File(pkg.contains("file:") ? processPkgFilePath(pkg) : pkg);
        if (!file.exists()) {
            throw new RuntimeException(pkg + " not found.");
        }

        File[] fileList = file.listFiles();
        if (fileList != null && fileList.length > 0) {

            for (File fileInner : fileList) {

                if (fileInner.isDirectory()) {
                    doScan(fileInner.getPath());
                } else {
                    getAllClass(fileInner);
                }
            }
        }
    }

    private void getAllClass(File file) {
        try {
            String filePath = file.getPath();
            String classFilePath = filePath.substring(
                    filePath.indexOf(AnnotationContext.CLASSES) + AnnotationContext.CLASSES.length());
            String clazz = classFilePath.substring(0, classFilePath.indexOf(AnnotationContext.CLASS_SUFFIX))
                    .replace(AnnotationContext.FILE_SEPARATOR, AnnotationContext.SPORT);
            allClass.add(Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void fullInBeanDefinition(List<Class> classes) {
        classes = classes == null ? allClass : classes;
        for (Class clazz : classes) {
            BeanDefinition beanDefinition = new AnnotationBeanDefinition();
            beanDefinition.setBeanClassName(clazz.getName());
            ((AnnotationBeanDefinition) beanDefinition).setAnnotations(Arrays.asList(clazz.getAnnotations()));

            if (clazz.isAnnotationPresent(Scope.class)) {
                Scope scope = (Scope) clazz.getAnnotation(Scope.class);
                beanDefinition.setScope(scope.values());
            }

            if (clazz.isAnnotationPresent(Lazy.class)) {
                Lazy lazy = (Lazy) clazz.getAnnotation(Lazy.class);
                beanDefinition.setLazyInit(lazy.values());
            }

            if (clazz.isAnnotationPresent(Primary.class)) {
                Primary primary = (Primary) clazz.getAnnotation(Primary.class);
                beanDefinition.setPrimary(primary.values());
            }

            if (clazz.isAnnotationPresent(Description.class)) {
                Description description = (Description) clazz.getAnnotation(Description.class);
                beanDefinition.setDescription(description.values());
            }

            beanDefinitions.put(clazz.getName(), beanDefinition);
        }
    }
}
