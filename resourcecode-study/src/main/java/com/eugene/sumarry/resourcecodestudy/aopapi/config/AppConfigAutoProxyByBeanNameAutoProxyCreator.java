package com.eugene.sumarry.resourcecodestudy.aopapi.config;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(value = "com.eugene.sumarry.resourcecodestudy.aopapi",
    excludeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AppConfigByProxyFactoryBean.class),
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AppConfigAutoProxyByDefaultAdvisorAutoProxyCreator.class),
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AppConfigAutoProxyByAnnotationAwareAspectJAutoProxyCreator.class),
    }
)
public class AppConfigAutoProxyByBeanNameAutoProxyCreator {

    @Bean
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
        BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
        creator.setProxyTargetClass(true);
        // 自动代理将所有待targetService打头的bean
        creator.setBeanNames("targetService*");
        // 后置通知
        creator.setInterceptorNames("myAfterAdvice");
        return creator;
    }
}
