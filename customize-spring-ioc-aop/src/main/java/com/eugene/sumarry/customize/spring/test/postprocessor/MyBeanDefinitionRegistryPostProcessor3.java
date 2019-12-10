package com.eugene.sumarry.customize.spring.test.postprocessor;

import com.eugene.sumarry.customize.spring.annotation.Component;
import com.eugene.sumarry.customize.spring.beans.BeanDefinitionRegistry;
import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;
import com.eugene.sumarry.customize.spring.postprocessor.BeanDefinitionRegistryPostProcessor;
import com.eugene.sumarry.customize.spring.postprocessor.Ordered;

@Component
public class MyBeanDefinitionRegistryPostProcessor3 implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        System.out.println("postProcessBeanDefinitionRegistry3");
    }

    @Override
    public void postProcessBeanFactory(DefaultListableBeanFactory beanFactory) {
        System.out.println("postProcessBeanDefinitionRegistry3 postProcessBeanFactory");
    }
}
