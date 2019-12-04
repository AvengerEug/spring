package com.eugene.sumarry.customize.spring.context.anno;


import com.eugene.sumarry.customize.spring.beans.BeanDefinitionRegistry;
import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;

/**
 * 默认注解上下文类
 *
 * 初始化配置类属性、构建扫描路径
 *
 */
public abstract class DefaultAnnotationConfigApplicationContext implements AnnotationContext, BeanDefinitionRegistry {

    protected Class<?> configClz;

    protected String resourcePath;

    protected DefaultListableBeanFactory beanFactory;

    public DefaultAnnotationConfigApplicationContext() {
        System.out.println("parent");
        this.constructBeanFactory();
    }

    public DefaultAnnotationConfigApplicationContext(Class<?> configClz) {
        this.initResourcePath(configClz);
        this.constructBeanFactory();
    }

    private void constructBeanFactory() {
        this.beanFactory = new DefaultListableBeanFactory();
    }

    protected void initResourcePath(Class<?> configClz) {
        this.configClz = configClz;
        this.resourcePath = configClz.getResource("/").toString();
    }

    protected abstract void refresh();
}
