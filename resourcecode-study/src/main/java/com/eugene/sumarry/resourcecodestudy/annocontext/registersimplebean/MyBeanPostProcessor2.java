package com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * 该处理器会加入到一个列表中, 然后在创建bean的时候统一调用所有的后置处理器
 */
@Component
public class MyBeanPostProcessor2 implements BeanPostProcessor, PriorityOrdered {

    @Override
    public int getOrder() {
        return 101;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("MyBeanPostProcessor2: before " + beanName);

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("MyBeanPostProcessor2: before " + beanName);
        return bean;
    }
}
