package com.eugene.sumarry.customize.spring.test.postprocessor;

import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;
import com.eugene.sumarry.customize.spring.postprocessor.BeanFactoryPostProcessor;

public class MyBeanFactoryPostProcessor1 implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(DefaultListableBeanFactory beanFactory) {
        System.out.println("MyBeanFactoryPostProcessor1 postProcessBeanFactory");
    }
}
