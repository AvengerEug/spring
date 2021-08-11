package com.eugene.sumarry.resourcecodestudy.aoporder;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Aspect
@Component
public class AspectAop2 implements Ordered {

    @Override
    public int getOrder() {
        return 3;
    }

    @Pointcut("@annotation(com.eugene.sumarry.resourcecodestudy.aoporder.AspectAop2.AOP2)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println("AspectAop2 before");

        Object proceed = proceedingJoinPoint.proceed(); // 实际方法执行

        System.out.println("AspectAop2 after");
    }


    @Target(value = ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AOP2 {
    }

}
