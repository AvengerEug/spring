package com.eugene.sumarry.customize.spring.context;

import com.eugene.sumarry.customize.spring.beans.BeanDefinition;

public interface BeanNameGenerator {

    String generateBeanName(BeanDefinition beanDefinition);
}
