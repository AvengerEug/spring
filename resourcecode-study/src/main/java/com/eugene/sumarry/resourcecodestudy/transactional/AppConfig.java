package com.eugene.sumarry.resourcecodestudy.transactional;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ComponentScan("com.eugene.sumarry.resourcecodestudy.transactional")
@Configuration
public class AppConfig {
}
