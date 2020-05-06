package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 后置处理器, 在每一个bean的创建之前打印
 * "========ImportEugene========"
 */
public class ImportEugeneBeanFactoryProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean,
        String beanName) throws BeansException {

        System.out.println("========ImportEugene========");
        return bean;
    }
}
