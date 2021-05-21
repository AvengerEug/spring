package com.eugene.sumarry.resourcecodestudy.aop;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("com.eugene.sumarry.resourcecodestudy.aop")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfig {
}
