package com.eugene.sumarry.customize.spring.postprocessor;

import com.eugene.sumarry.customize.spring.annotation.ComponentScan;
import com.eugene.sumarry.customize.spring.annotation.Config;
import com.eugene.sumarry.customize.spring.beans.AnnotatedGenericBeanDefinition;
import com.eugene.sumarry.customize.spring.beans.BeanDefinition;
import com.eugene.sumarry.customize.spring.beans.BeanDefinitionRegistry;
import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;
import com.eugene.sumarry.customize.spring.context.anno.ClassPathBeanDefinitionScanned;

import java.util.LinkedList;

import static com.eugene.sumarry.customize.spring.context.anno.AnnotationContext.FILE_SEPARATOR;
import static com.eugene.sumarry.customize.spring.context.anno.AnnotationContext.SPORT;

public final class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    private String resourcePath;

    private void initResourcePath(Class<?> configClz) {
        this.resourcePath = configClz.getResource("/").toString();
    }

    private void generatePkgPath(Class<?> configClz) {
        ComponentScan componentScan = configClz.getAnnotation(ComponentScan.class);
        resourcePath = resourcePath + componentScan.value().replace(SPORT, FILE_SEPARATOR);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        // 1. 循环遍历bean工厂的BeanDefinitionMap
        // 2. 判断是否为配置类
        // 3. 解析配置类
        if (registry instanceof DefaultListableBeanFactory) {
            DefaultListableBeanFactory factory = (DefaultListableBeanFactory) registry;
            LinkedList<BeanDefinition> beanDefinitions = factory.getBeanDefinitionByCondidate();
            for (BeanDefinition beanDefinition : beanDefinitions) {
                // 通过register方法注册的类对应的bd是AnnotatedGenericBeanDefinition, 自定义实现, 有这个注解则表示它是配置类
                Class<?> beanClass = beanDefinition.getBeanClass();
                if (beanDefinition instanceof AnnotatedGenericBeanDefinition && beanClass.isAnnotationPresent(Config.class)) {
                    doProcessorConfigurationClass(beanClass, factory);
                }
            }
        }
    }

    private void doProcessorConfigurationClass(Class<?> beanClass, DefaultListableBeanFactory factory) {
        initResourcePath(beanClass);
        generatePkgPath(beanClass);
        // 这里与spring一样. Spring解析配置类扫描包的时候 也是新new出来的, 而不是用上下文中的scanned
        ClassPathBeanDefinitionScanned scanned = new ClassPathBeanDefinitionScanned(factory);
        scanned.doScan(this.resourcePath);
    }

    @Override
    public void postProcessBeanFactory(DefaultListableBeanFactory beanFactory) {

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
