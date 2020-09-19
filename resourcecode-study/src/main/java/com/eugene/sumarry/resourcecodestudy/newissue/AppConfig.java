package com.eugene.sumarry.resourcecodestudy.newissue;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.Bean;

public class AppConfig {
    @Bean
    public ProxyFactoryBean proxyFactoryBean(TargetService targetService) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setProxyTargetClass(true);
        proxyFactoryBean.setTarget(targetService);
        proxyFactoryBean.setInterceptorNames("myBeforeAdvice");
        return proxyFactoryBean;
    }
}
