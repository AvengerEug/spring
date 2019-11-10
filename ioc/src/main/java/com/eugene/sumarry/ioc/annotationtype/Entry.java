package com.eugene.sumarry.ioc.annotationtype;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
    }
}
