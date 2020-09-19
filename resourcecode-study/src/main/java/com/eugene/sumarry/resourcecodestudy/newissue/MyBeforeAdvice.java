package com.eugene.sumarry.resourcecodestudy.newissue;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

public class MyBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("before invoke method [ " + method.getName() + " ], aop before logic invoked");
    }
}
