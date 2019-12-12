package com.eugene.sumarry.resourcecodestudy.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyMethodInterceptor implements MethodInterceptor {

    /**
     *
     * @param o 代理对象
     * @param method 目标对象的方法
     * @param objects 目标对象方法的参数
     * @param methodProxy 代理对象的代理方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("代理对象方法");
        System.out.println(methodProxy);
        // 调用父类的方法, 即TestCglibProxy中的对应的method
        return methodProxy.invokeSuper(o, objects);
    }
}
