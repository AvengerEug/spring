package com.eugene.sumarry.customize.spring.context.anno;

import com.eugene.sumarry.customize.spring.annotation.Component;
import com.eugene.sumarry.customize.spring.annotation.Repository;
import com.eugene.sumarry.customize.spring.beans.BeanDefinition;
import com.eugene.sumarry.customize.spring.beans.BeanDefinitionRegistry;
import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;
import com.eugene.sumarry.customize.spring.beans.ScannedGenericBeanDefinition;
import com.eugene.sumarry.customize.spring.beans.anno.AnnotationBeanDefinition;
import com.eugene.sumarry.customize.spring.context.BeanNameGenerator;
import com.eugene.sumarry.customize.spring.util.AnnotationConfigUtils;
import com.eugene.sumarry.customize.spring.util.AnnotationUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassPathBeanDefinitionScanned {

    private List<Class> allClass = new ArrayList<>();

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanned(BeanDefinitionRegistry registry) {
        this.registry = registry;
        AnnotationConfigUtils.registerAnnotationConfigProcessors(registry);
    }

    public String generateBeanName(BeanDefinition beanDefinition) {
        return this.beanNameGenerator.generateBeanName(beanDefinition);
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
            fullInBeanDefinition(allClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void fullInBeanDefinition(List<Class> classes) {
        classes = classes == null ? allClass : classes;
        for (Class clazz : classes) {
            if (isChangedToBeanDefinition(clazz)) {
                BeanDefinition beanDefinition = AnnotationUtils.fullInBeanDefinition(new ScannedGenericBeanDefinition(), clazz);
                beanDefinition.setBeanClassName(this.beanNameGenerator.generateBeanName(beanDefinition));
                if (beanDefinition instanceof AnnotationBeanDefinition) {
                    if (registry instanceof AnnotationConfigApplicationContext) {
                        ((AnnotationConfigApplicationContext)registry).getBeanFactory().addBeanDefinitionName(beanDefinition.getBeanClassName());
                        ((AnnotationConfigApplicationContext)registry).getBeanFactory().addBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
                    } else if (registry instanceof DefaultListableBeanFactory) {
                        ((DefaultListableBeanFactory) registry).addBeanDefinitionName(beanDefinition.getBeanClassName());
                        ((DefaultListableBeanFactory) registry).addBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
                    }
                }
            }
        }
    }

    public boolean isChangedToBeanDefinition(Class<?> clazz) {
        Class<? extends Annotation> annotations[] = new Class[] {
                Repository.class,
                Component.class
        };

        for (Class<? extends Annotation> annotation : annotations) {
            if (clazz.isAnnotationPresent(annotation)) return true;
        }

        return false;
    }
}
