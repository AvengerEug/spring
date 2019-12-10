package com.eugene.sumarry.customize.spring.util;

import com.eugene.sumarry.customize.spring.beans.BeanDefinition;
import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;
import com.eugene.sumarry.customize.spring.postprocessor.BeanDefinitionRegistryPostProcessor;
import com.eugene.sumarry.customize.spring.postprocessor.BeanFactoryPostProcessor;
import com.eugene.sumarry.customize.spring.postprocessor.Ordered;
import com.eugene.sumarry.customize.spring.postprocessor.PriorityOrdered;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PostProcessorRegistrationDelegate {

    /**
     * 剩余调用BeanFactoryPostProcessor的后置处理器步骤未写, 步骤与
     * 调用BeanDefinitionRegistryPostProcessor一致,
     * 先调用实现PriorityOrdered接口的
     * 再调用实现Ordered接口的
     * 最后调用未实现PriorityOrdered接口和Ordered接口的后置处理器
     *
     * @param beanFactory
     * @param beanFactoryPostProcessors
     */
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

        Set<BeanDefinition> registryProcessorsBeanDefinitionRepeat = new HashSet<>();
        List<BeanDefinition> definitions = beanFactory.getBeanDefinitionByCondidate(BeanDefinitionRegistryPostProcessor.class, PriorityOrdered.class);
        registryProcessorsBeanDefinitionRepeat.addAll(definitions);
        System.out.println("\n======================");
        System.out.println("调用实现了BeanDefinitionRegistryPostProcessor和PriorityOrdered接口的后置处理器, 从bean工厂的beanDefinitionMap中取");
        for (BeanDefinition definition : definitions) {
            invokeBeanDefinitionRegistryPostProcessors(beanFactory, definition);
        }
        // 调用实现了BeanDefinitionRegistryPostProcessor和PriorityOrdered接口的后置处理器

        definitions.clear();
        System.out.println("\n======================");
        System.out.println("调用实现了BeanDefinitionRegistryPostProcessor和Ordered接口的后置处理器, 从bean工厂的beanDefinitionMap中取, \n" +
                "自己实现的没有筛选出spring内置的, 在spring源码中, 此处会把内置的也找出来, 但因为做了标示, 并没有再次执行它");
        definitions = beanFactory.getBeanDefinitionByCondidate(BeanDefinitionRegistryPostProcessor.class, Ordered.class);
        registryProcessorsBeanDefinitionRepeat.addAll(definitions);
        for (BeanDefinition definition : definitions) {
            invokeBeanDefinitionRegistryPostProcessors(beanFactory, definition);
        }

        System.out.println("\n======================");
        System.out.println("调用实现了BeanDefinitionRegistryPostProcessor以及没实现Ordered和PriorityOrdered接口的后置处理器, \n" +
                "从bean工厂的beanDefinitionMap中取 \n" +
                "自己实现的没有筛选出spring内置的, 在spring源码中, 此处会把内置的也找出来, 但因为做了标示, 并没有再次执行它");
        definitions = beanFactory.getBeanDefinitionByCondidate(BeanDefinitionRegistryPostProcessor.class);
        definitions.removeAll(registryProcessorsBeanDefinitionRepeat);
        for (BeanDefinition definition : definitions) {
            invokeBeanDefinitionRegistryPostProcessors(beanFactory, definition);
        }

        System.out.println("\n执行手动添加的BeanFactoryPostProcessor后置处理器");
        invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
        invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);

    }

    public static void invokeBeanFactoryPostProcessors(List<? extends BeanFactoryPostProcessor> beanFactoryPostProcessors, DefaultListableBeanFactory beanFactory) {
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessors) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    public static void invokeBeanDefinitionRegistryPostProcessors(DefaultListableBeanFactory beanFactory, BeanDefinition beanDefinition) {
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
