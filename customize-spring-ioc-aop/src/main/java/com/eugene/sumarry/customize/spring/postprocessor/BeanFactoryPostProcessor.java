package com.eugene.sumarry.customize.spring.postprocessor;

import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;

public interface BeanFactoryPostProcessor {

    void postProcessBeanFactory(DefaultListableBeanFactory beanFactory);
}
