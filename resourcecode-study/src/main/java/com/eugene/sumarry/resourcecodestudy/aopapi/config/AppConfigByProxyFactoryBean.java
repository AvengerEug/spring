package com.eugene.sumarry.resourcecodestudy.aopapi.config;

import com.eugene.sumarry.resourcecodestudy.aopapi.target.TargetService;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(value = "com.eugene.sumarry.resourcecodestudy.aopapi",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AppConfigAutoProxyByBeanNameAutoProxyCreator.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AppConfigAutoProxyByDefaultAdvisorAutoProxyCreator.class)
        })
public class AppConfigByProxyFactoryBean {

    @Bean
    public ProxyFactoryBean proxyFactoryBean(TargetService targetService) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        // 使用cglib代理
        proxyFactoryBean.setProxyTargetClass(true);

        proxyFactoryBean.setTarget(targetService);
        proxyFactoryBean.setInterceptorNames("myBeforeAdvice", "myAroundAdvice");
        return proxyFactoryBean;
    }

}
