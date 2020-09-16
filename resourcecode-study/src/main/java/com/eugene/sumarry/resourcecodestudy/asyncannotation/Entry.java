package com.eugene.sumarry.resourcecodestudy.asyncannotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.getBean(AsyncService.class).test();
    }
}

