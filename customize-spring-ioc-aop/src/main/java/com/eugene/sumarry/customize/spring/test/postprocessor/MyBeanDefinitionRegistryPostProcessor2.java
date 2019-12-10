package com.eugene.sumarry.customize.spring.test.postprocessor;

import com.eugene.sumarry.customize.spring.annotation.Component;
import com.eugene.sumarry.customize.spring.beans.BeanDefinitionRegistry;
import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;
import com.eugene.sumarry.customize.spring.postprocessor.BeanDefinitionRegistryPostProcessor;
import com.eugene.sumarry.customize.spring.postprocessor.Ordered;
import com.eugene.sumarry.customize.spring.postprocessor.PriorityOrdered;

@Component
public class MyBeanDefinitionRegistryPostProcessor2 implements BeanDefinitionRegistryPostProcessor, Ordered {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        System.out.println("postProcessBeanDefinitionRegistry2");
    }

    @Override
    public void postProcessBeanFactory(DefaultListableBeanFactory beanFactory) {
        System.out.println("MyBeanDefinitionRegistryPostProcessor2 postProcessBeanFactory");
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
