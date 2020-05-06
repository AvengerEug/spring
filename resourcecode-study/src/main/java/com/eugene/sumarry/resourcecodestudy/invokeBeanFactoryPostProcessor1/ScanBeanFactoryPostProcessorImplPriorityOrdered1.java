package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor1;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * 由spring扫描出来的BeanFactoryPostProcessor
 * 实现了PriorityOrdered接口, 且权重为1 => 权重越低, 越优先执行
 */
@Component
public class ScanBeanFactoryPostProcessorImplPriorityOrdered1 implements BeanFactoryPostProcessor, PriorityOrdered {

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("Scan ScanBeanFactoryPostProcessor impl PriorityOrdered1: postProcessBeanFactory");
    }
}
