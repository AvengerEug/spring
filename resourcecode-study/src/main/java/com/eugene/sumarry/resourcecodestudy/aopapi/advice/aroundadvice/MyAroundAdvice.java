package com.eugene.sumarry.resourcecodestudy.aopapi.advice.aroundadvice;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Service;

@Service
public class MyAroundAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("around before advice invoked");
        Object proceed = invocation.proceed();
        System.out.println("around after advice invoked");

        return proceed;
    }
}
