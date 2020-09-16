package com.eugene.sumarry.resourcecodestudy.aopapi.config;

import com.eugene.sumarry.resourcecodestudy.aopapi.advice.beforeadvice.MyBeforeAdvice;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(value = "com.eugene.sumarry.resourcecodestudy.aopapi",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AppConfigAutoProxyByBeanNameAutoProxyCreator.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AppConfigByProxyFactoryBean.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AppConfigAutoProxyByAnnotationAwareAspectJAutoProxyCreator.class),
        }
)
public class AppConfigAutoProxyByDefaultAdvisorAutoProxyCreator {

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(MyBeforeAdvice myBeforeAdvice) {
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        defaultPointcutAdvisor.setAdvice(myBeforeAdvice);

        return defaultPointcutAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setUsePrefix(true);
        // TODO 使用beanName前缀生成代理失败
//        creator.setAdvisorBeanNamePrefix("targetService");
        creator.setProxyTargetClass(true);
        return creator;
    }
}
