package com.eugene.sumarry.customize.spring.context.anno;

import com.eugene.sumarry.customize.spring.annotation.Description;
import com.eugene.sumarry.customize.spring.annotation.Lazy;
import com.eugene.sumarry.customize.spring.annotation.Primary;
import com.eugene.sumarry.customize.spring.annotation.Scope;
import com.eugene.sumarry.customize.spring.beans.BeanDefinition;
import com.eugene.sumarry.customize.spring.beans.BeanDefinitionRegistry;
import com.eugene.sumarry.customize.spring.beans.anno.AnnotationBeanDefinition;
import com.eugene.sumarry.customize.spring.context.BeanNameGenerator;
import com.eugene.sumarry.customize.spring.util.AnnotationConfigUtils;
import com.eugene.sumarry.customize.spring.util.AnnotationUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class AnnotatedBeanDefinitionReader {

    private List<Class> allClass = new ArrayList<>();

    private BeanDefinitionRegistry registry;

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    public String generateBeanName(BeanDefinition beanDefinition) {
        return this.beanNameGenerator.generateBeanName(beanDefinition);
    }

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
        AnnotationConfigUtils.registerAnnotationConfigProcessors(registry);
    }

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
            BeanDefinition beanDefinition = AnnotationUtils.fullInBeanDefinition(new AnnotationBeanDefinition(), clazz);
            beanDefinition.setBeanClassName(this.beanNameGenerator.generateBeanName(beanDefinition));
            if (((AnnotationBeanDefinition) beanDefinition).getAnnotations().size() > 0) {
                ((AnnotationConfigApplicationContext)registry).getBeanFactory().addBeanDefinitionName(beanDefinition.getBeanClassName());
                ((AnnotationConfigApplicationContext)registry).getBeanFactory().addBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
            }
        }
    }
}