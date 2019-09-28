package com.eugene.sumarry.spring.configrationannotation;

import com.eugene.sumarry.spring.configrationannotation.util.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        AppConfig appConfig = context.getBean(AppConfig.class);
    }
}
