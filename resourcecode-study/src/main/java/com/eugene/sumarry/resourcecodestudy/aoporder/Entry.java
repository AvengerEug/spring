package com.eugene.sumarry.resourcecodestudy.aoporder;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        context.getBean(BeanA.class).test();
    }

    @EnableAspectJAutoProxy
    @ComponentScan("com.eugene.sumarry.resourcecodestudy.aoporder")
    public static class AppConfig {

    }

}
