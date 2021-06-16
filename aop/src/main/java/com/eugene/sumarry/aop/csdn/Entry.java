package com.eugene.sumarry.aop.csdn;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ObjectService objectService = context.getBean(ObjectService.class);
        objectService.list("hi");
    }

    // 配置扫描类，启动了aop功能
    @Configuration
    @ComponentScan("com.eugene.sumarry.aop.csdn")
    @EnableAspectJAutoProxy(proxyTargetClass = false, exposeProxy = true)
    public static class AppConfig {
    }

    // 被这个注解标识的方法会被增强
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface AspectAnnotation {
    }
}
