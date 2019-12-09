package com.eugene.sumarry.customize.spring.beans;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

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

    @Override
    public void registry(String beanName, BeanDefinition beanDefinition) {
        this.addBeanDefinition(beanName, beanDefinition);
        this.addBeanDefinitionName(beanName);
    }

    public List<BeanDefinition> getBeanDefinitionByCondidate(Class<?> postProcessorClz, Class<?> interOrderedClz) {
        List<BeanDefinition> beanDefinitions = new ArrayList<>(256);

        for (BeanDefinition value : beanDefinitionMap.values()) {
            if (postProcessorClz.isAssignableFrom(value.getBeanClass())) {
                boolean currentLoopOver = false;

                for (Class<?> anInterface : value.getBeanClass().getInterfaces()) {
                    if (anInterface.equals(interOrderedClz)) {
                        beanDefinitions.add(value);
                        currentLoopOver = true;
                        break;
                    }
                }

                if (currentLoopOver) continue;
            }
        }

        return beanDefinitions;
    }

    public LinkedList<BeanDefinition> getBeanDefinitionByCondidate() {
        LinkedList<BeanDefinition> beanDefinitions = new LinkedList<BeanDefinition>();

        for (BeanDefinition value : beanDefinitionMap.values()) {
            beanDefinitions.add(value);
        }

        return beanDefinitions;
    }

    private boolean containsSingleBean(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    public void addSingleBean(String beanName, Object bean) {
        if (!this.containsSingleBean(beanName)) this.singletonObjects.put(beanName, bean);
    }
}
