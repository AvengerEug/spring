package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor1;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 由spring扫描出来的BeanDefinitionRegistryPostProcessor
 * 实现了Ordered接口, 且权重为2  => 权重低, 优先执行
 */
@Component
public class ScanBeanDefinitionRegistryPostProcessorImplOrdered2 implements BeanDefinitionRegistryPostProcessor, Ordered {

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("Scan BeanDefinitionRegistryPostProcessor impl Ordered2: postProcessBeanDefinitionRegistry");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("Scan BeanDefinitionRegistryPostProcessor impl Ordered2: postProcessBeanFactory");
    }
}
