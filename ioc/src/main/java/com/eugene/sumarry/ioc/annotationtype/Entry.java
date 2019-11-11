package com.eugene.sumarry.ioc.annotationtype;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        userService.sout("");
        UserService userService1 = context.getBean(UserService.class);
        userService1.sout("eugene1");
        UserService userService2 = context.getBean(UserService.class);
        userService2.sout("eugene2");
    }
}
