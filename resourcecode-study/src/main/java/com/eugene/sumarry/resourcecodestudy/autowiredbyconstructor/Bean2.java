package com.eugene.sumarry.resourcecodestudy.autowiredbyconstructor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Bean2 implements InitializingBean, BeanPostProcessor {

    @Autowired
    private Bean1 bean1;

    @PostConstruct
    public void post() {
        System.out.println(1);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(1);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
