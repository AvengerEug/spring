package com.eugene.sumarry.customize.spring.context.anno;


import com.eugene.sumarry.customize.spring.beans.BeanDefinition;
import com.eugene.sumarry.customize.spring.beans.BeanDefinitionRegistry;
import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;
import com.eugene.sumarry.customize.spring.postprocessor.BeanFactoryPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认注解上下文类
 *
 * 初始化配置类属性、构建扫描路径
 *
 */
public abstract class DefaultAnnotationConfigApplicationContext implements AnnotationContext, BeanDefinitionRegistry {

    protected DefaultListableBeanFactory beanFactory;

    protected final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }

    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        this.beanFactoryPostProcessors.add(postProcessor);
    }

    public DefaultAnnotationConfigApplicationContext() {
        System.out.println("parent");
        this.constructBeanFactory();
    }

    @Override
    public void registry(String beanName, BeanDefinition beanDefinition) {
        this.beanFactory.addBeanDefinition(beanName, beanDefinition);
    }

    public DefaultAnnotationConfigApplicationContext(Class<?> configClz) {
        this.constructBeanFactory();
    }

    private void constructBeanFactory() {
        this.beanFactory = new DefaultListableBeanFactory();
    }


    protected abstract void refresh();
}
