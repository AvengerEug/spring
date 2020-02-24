package com.eugene.sumarry.resourcecodestudy.autowiredbyconstructor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        AutowiredConstructor autowiredContructor = context.getBean(AutowiredConstructor.class);
        System.out.println("111");
    }
}
