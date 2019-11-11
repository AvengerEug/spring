package com.eugene.sumarry.ioc.annotationtype;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@ComponentScan(value = "com.eugene.sumarry.ioc.annotationType", nameGenerator = MyBeanNameGenerator.class)
@ImportResource("classpath:spring-string.xml")
public class AppConfig {
}
