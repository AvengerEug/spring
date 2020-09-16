package com.eugene.sumarry.resourcecodestudy.asyncannotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ComponentScan("com.eugene.sumarry.resourcecodestudy.asyncannotation")
@EnableAsync
public class AppConfig {



}
