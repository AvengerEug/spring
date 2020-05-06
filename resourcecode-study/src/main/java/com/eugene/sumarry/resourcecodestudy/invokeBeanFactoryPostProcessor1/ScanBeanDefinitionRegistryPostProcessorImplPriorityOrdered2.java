package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor1;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * 由spring扫描出来的BeanDefinitionRegistryPostProcessor
 * 实现了PriorityOrdered接口, 且权重为2  => 权重低, 优先执行
 */
@Component
public class ScanBeanDefinitionRegistryPostProcessorImplPriorityOrdered2 implements BeanDefinitionRegistryPostProcessor,
        PriorityOrdered {

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("Scan BeanDefinitionRegistryPostProcessor impl PriorityOrdered2: " +
                "postProcessBeanDefinitionRegistry");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("Scan BeanDefinitionRegistryPostProcessor impl PriorityOrdered2: postProcessBeanFactory");
    }
}
