package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK动态代理类
 */
public class MyInvocationHandler implements InvocationHandler {

    private Object targetObject;

    public MyInvocationHandler(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理方法");
        return method.invoke(targetObject, args);
    }
}
