package com.eugene.sumarry.customize.spring.test;

import com.eugene.sumarry.customize.spring.context.anno.AnnotationConfigApplicationContext;
import com.eugene.sumarry.customize.spring.test.config.AppConfig;
import com.eugene.sumarry.customize.spring.test.postprocessor.MyBeanDefinitionRegistryPostProcessor1;
import com.eugene.sumarry.customize.spring.test.postprocessor.MyBeanFactoryPostProcessor1;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.addBeanFactoryPostProcessor(new MyBeanDefinitionRegistryPostProcessor1());
        context.addBeanFactoryPostProcessor(new MyBeanFactoryPostProcessor1());
        context.register(AppConfig.class);
        context.refresh();
        System.out.println(context);

    }
}
