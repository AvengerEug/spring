package com.eugene.sumarry.aop.main;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Configuration
@EnableAspectJAutoProxy
@Component
@ComponentScan("com.eugene.sumarry.aop.main")
public class AppConfig {
}
