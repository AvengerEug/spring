package com.eugene.sumarry.ioc.annotationtype;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@ComponentScan(value = "com.eugene.sumarry.ioc.annotationType")
@ImportResource("classpath:spring-string.xml")
@Profile("prod")
public class AppConfigProd {
}
