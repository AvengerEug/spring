package com.eugene.sumarry.resourcecodestudy.newissue;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                TargetService.class,
                MyBeforeAdvice.class,
                AppConfig.class);

        context.getBean(TargetService.class).testAopApi();
    }
}
