package com.eugene.sumarry.resourcecodestudy.transactional;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);


        UserService userService = context.getBean(UserService.class);
        //是否是JDK动态代理
        System.out.println("isJdkDynamicProxy => " + AopUtils.isJdkDynamicProxy(userService));
        //是否是CGLIB代理
        System.out.println("isCglibProxy => " + AopUtils.isCglibProxy(userService));
        //代理类的类型
        System.out.println("proxyClass => " + userService.getClass());
        //代理类的父类的类型
        System.out.println("parentClass => " + userService.getClass().getSuperclass());
        //代理类的父类实现的接口
        System.out.println("parentClass's interfaces => " + Arrays.asList(userService.getClass().getSuperclass().getInterfaces()));
        //代理类实现的接口
        System.out.println("proxyClass's interfaces => " + Arrays.asList(userService.getClass().getInterfaces()));
        //代理对象
        System.out.println("proxy => " + userService);
        /**
         * 目标对象
         * TODO 这里的目标对象和上面的代理对象打印出来的地址都是一样，其实是不一样的，打印出来是
         * 一样的是因为toString方法
         */
        System.out.println("target => " + AopProxyUtils.getSingletonTarget(userService));
        //代理对象和目标对象是不是同一个
        System.out.println("proxy == target => " + (userService == AopProxyUtils.getSingletonTarget(userService)));
        //目标类的类型
        System.out.println("targetClass => " + AopProxyUtils.getSingletonTarget(userService).getClass());
        //目标类实现的接口
        System.out.println("targetClass's interfaces => " + Arrays.asList(AopProxyUtils.getSingletonTarget(userService).getClass().getInterfaces()));

        System.out.println("----------------------------------------------------");
        
    }
}
