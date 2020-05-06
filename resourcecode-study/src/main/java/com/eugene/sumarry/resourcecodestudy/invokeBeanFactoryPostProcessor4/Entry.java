package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor4;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        System.out.println(context.getBean(UserService.class));
    }
}
