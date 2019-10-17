package com.eugene.sumarry.spring.configrationannotation.model;

import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;

/**
 * 创建对象初始化的后置处理器的实现方式:
 *   1. 通过注解@PostContruct
 *   2. model实现InitializingBean接口的init方法
 *   3. XML配置
 *
 *   spring后置处理器执行顺序:
 *   @PostContruct > 实现InitializingBean接口的init方法 > XML配置的init方法
 *   具体代码逻辑:
 *     在spring源码中初始化对象后首先处理了@PostContruct注解后置处理器
 *     再处理了实现InitializingBean接口的后置处理器
 *     再处理xml配置的后置处理器
 *
 */
public class User implements InitializingBean {

    public User() {
        System.out.println("Init user has been finished");
    }

    @PostConstruct
    public void test() {
        System.out.println("创建对象后回调");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("创建对象后回调 InitializingBean");
    }

}
