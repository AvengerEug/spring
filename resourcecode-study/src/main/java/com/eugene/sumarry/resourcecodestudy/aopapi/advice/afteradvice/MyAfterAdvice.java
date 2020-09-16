package com.eugene.sumarry.resourcecodestudy.aopapi.advice.afteradvice;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class MyAfterAdvice implements AfterReturningAdvice {

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("after invoke method [ " + method.getName() + " ], aop after running logic invoked");
    }
}
