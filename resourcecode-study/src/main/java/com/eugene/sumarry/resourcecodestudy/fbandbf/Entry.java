package com.eugene.sumarry.resourcecodestudy.fbandbf;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        // 返回false, 因为testBeanFactory类型为TestBean
        System.out.println(context.getBean("&testBeanFactoryBean"));
        System.out.println(context.getBean("testBeanFactoryBean"));

        // 返回false, 不知道为何不是接口类型, 需要确认这个类是否被增强了
        // System.out.println(context.getBean("testBeanFactoryBean") instanceof BeanFactory);

        // 返回true, 因为实现BeanFactory接口的bean的name 默认会加上&符号,
        // System.out.println(context.getBean("&testBeanFactoryBean") instanceof TestBeanFactoryBean);
    }
}
