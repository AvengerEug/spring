package com.eugene.sumarry.customize.spring.beans;

public interface BeanDefinitionRegistry {

    void registry(String beanName, BeanDefinition beanDefinition);
}
