package com.eugene.sumarry.resourcecodestudy.fbandbf;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        // 返回false, 因为testBeanFactory类型为TestBean
        System.out.println(context.getBean("&testBeanFactoryBean"));
        System.out.println(context.getBean("testBeanFactoryBean"));
    }
}
