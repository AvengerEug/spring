package com.eugene.sumarry.ioc.annotationtype;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@ComponentScan(
        value = "com.eugene.sumarry.ioc.annotationType",
        nameGenerator = MyBeanNameGenerator.class,
        excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = UnsupportScan.class)}
)
@ImportResource("classpath:spring-string.xml")
@Profile("dev")
public class AppConfig {
}
