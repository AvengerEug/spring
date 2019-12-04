package com.eugene.sumarry.customize.spring.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory implements BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap(256);

    private volatile List<String> beanDefinitionNames = new ArrayList(256);

    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    public void addBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        if (!containsBeanDefinition(beanName)) beanDefinitionMap.put(beanName, beanDefinition);
    }

    public boolean containsBeanName(String beanName) {
        return beanDefinitionNames.contains(beanName);
    }

    public void addBeanDefinitionName(String beanName) {
        if (!containsBeanName(beanName)) beanDefinitionNames.add(beanName);
    }

}
