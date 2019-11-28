package com.eugene.sumarry.customize.spring.test;

import com.eugene.sumarry.customize.spring.context.anno.AnnotationConfigApplicationContext;
import com.eugene.sumarry.customize.spring.test.config.AppConfig;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    }
}
