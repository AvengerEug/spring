package com.eugene.sumarry.resourcecodestudy.transactional;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class Entry {

    public static void main(String[] args) {
        // 如下代码会将jdk的代理对象保存到硬盘中，其中默认路径为：idea工作目录的com/sum/proxy目录下
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);
        printClass(userService);

        /**
         * @EnableTransactionManagement(proxyTargetClass = false)
         * 此注解如果不手动指定proxyTargetClass为true，那么默认使用的jdk动态代理来创建对象
         *
         * 1、在没有指定proxyTargetClass为false时
         *   1.1）、如果目标对象实现了接口，此时则使用jdk动态代理
         *   1.2）、如果目标对象未实现接口，此时使用的时cglib代理
         *
         * 2、在没有指定proxyTargetClass为true时
         *   1.1)、直接使用cglib代理
         */
        UserService2 userService2 = context.getBean(UserService2.class);
        printClass(userService2);
    }

    public static void printClass(Object obj) {
        System.out.println("正在处理类: " + obj);

        //是否是JDK动态代理
        System.out.println("isJdkDynamicProxy => " + AopUtils.isJdkDynamicProxy(obj));
        //是否是CGLIB代理
        System.out.println("isCglibProxy => " + AopUtils.isCglibProxy(obj));
        //代理类的类型
        System.out.println("proxyClass => " + obj.getClass());
        //代理类的父类的类型
        System.out.println("parentClass => " + obj.getClass().getSuperclass());
        //代理类的父类实现的接口
        System.out.println("parentClass's interfaces => " + Arrays.asList(obj.getClass().getSuperclass().getInterfaces()));
        //代理类实现的接口
        System.out.println("proxyClass's interfaces => " + Arrays.asList(obj.getClass().getInterfaces()));
        //代理对象
        System.out.println("proxy => " + obj);
        /**
         * 目标对象
         * TODO 这里的目标对象和上面的代理对象打印出来的地址都是一样，其实是不一样的，打印出来是一样的原因是执行了toString方法
         */
        System.out.println("target => " + AopProxyUtils.getSingletonTarget(obj));
        //代理对象和目标对象是不是同一个
        System.out.println("proxy == target => " + (obj == AopProxyUtils.getSingletonTarget(obj)));
        //目标类的类型
        System.out.println("targetClass => " + AopProxyUtils.getSingletonTarget(obj).getClass());
        //目标类实现的接口
        System.out.println("targetClass's interfaces => " + Arrays.asList(AopProxyUtils.getSingletonTarget(obj).getClass().getInterfaces()));

        System.err.println("*************************************************************");
    }
}
