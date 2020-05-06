package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 配置类, 里面维护了两个bean(Bean1、Bean2)
 * 
 * 可以猜测如下两个问题:
 *   1. 控制台会不会打印"creating bean1" 和 "creating bean2"？
 *   2. "creating bean1" 会打印几次？
 *
 */
@Configuration
@Component
public class AppConfig {


    @Bean
    public Bean1 bean1() {
        System.out.println("creating bean1");
        return new Bean1();
    }

    @Bean
    public Bean2 bean2() {
        bean1();
        System.out.println("creating bean2");
        return new Bean2();
    }
}
