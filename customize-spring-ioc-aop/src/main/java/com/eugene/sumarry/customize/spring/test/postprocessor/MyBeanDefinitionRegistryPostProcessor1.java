package com.eugene.sumarry.customize.spring.test.postprocessor;

import com.eugene.sumarry.customize.spring.beans.BeanDefinitionRegistry;
import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;
import com.eugene.sumarry.customize.spring.postprocessor.BeanDefinitionRegistryPostProcessor;

public class MyBeanDefinitionRegistryPostProcessor1 implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        System.out.println("postProcessBeanDefinitionRegistry1");
    }

    @Override
    public void postProcessBeanFactory(DefaultListableBeanFactory beanFactory) {
        System.out.println("MyBeanDefinitionRegistryPostProcessor1 postProcessBeanFactory");
    }
}
