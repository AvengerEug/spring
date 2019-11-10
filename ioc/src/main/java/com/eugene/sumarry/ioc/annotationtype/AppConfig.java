package com.eugene.sumarry.ioc.annotationtype;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@ComponentScan("com.eugene.sumarry.ioc.annotationType")
@ImportResource("classpath:spring-string.xml")
public class AppConfig {
}
