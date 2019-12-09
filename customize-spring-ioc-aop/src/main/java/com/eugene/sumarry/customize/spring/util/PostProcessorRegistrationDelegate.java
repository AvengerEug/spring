package com.eugene.sumarry.customize.spring.util;

import com.eugene.sumarry.customize.spring.beans.BeanDefinition;
import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;
import com.eugene.sumarry.customize.spring.postprocessor.BeanDefinitionRegistryPostProcessor;
import com.eugene.sumarry.customize.spring.postprocessor.BeanFactoryPostProcessor;
import com.eugene.sumarry.customize.spring.postprocessor.PriorityOrdered;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public final class PostProcessorRegistrationDelegate {

    public static void invokeBeanFactoryPostProcessors(
            DefaultListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

        List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
        List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();

        // 手动添加的后置处理器在这第一次被调用
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessors) {
            if (beanFactoryPostProcessor instanceof BeanDefinitionRegistryPostProcessor) {
                BeanDefinitionRegistryPostProcessor postProcessor = ((BeanDefinitionRegistryPostProcessor) beanFactoryPostProcessor);
                postProcessor.postProcessBeanDefinitionRegistry(beanFactory);
                registryProcessors.add(postProcessor);
            } else {
                regularPostProcessors.add(beanFactoryPostProcessor);
            }
        }

        // Spring中定义该list用来存储自己的BeanPostProcessor
        List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();
        List<BeanDefinition> definitions = beanFactory.getBeanDefinitionByCondidate(BeanDefinitionRegistryPostProcessor.class, PriorityOrdered.class);

        for (BeanDefinition definition : definitions) {
            invokeBeanFactoryPostProcessors(beanFactory, definition);
        }
        // 调用实现了BeanDefinitionRegistryPostProcessor和PriorityOrdered接口的后置处理器

    }

    public static void invokeBeanFactoryPostProcessors(DefaultListableBeanFactory beanFactory, BeanDefinition beanDefinition) {
        try {
            Class<?> clazz = beanDefinition.getBeanClass();
            Constructor constructor = clazz.getConstructor();
            BeanDefinitionRegistryPostProcessor postProcessor = (BeanDefinitionRegistryPostProcessor) constructor.newInstance();
            beanFactory.addSingleBean(beanDefinition.getBeanClassName(), postProcessor);
            postProcessor.postProcessBeanDefinitionRegistry(beanFactory);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Invoke bean factory post processors failed.");
        }
    }
}
