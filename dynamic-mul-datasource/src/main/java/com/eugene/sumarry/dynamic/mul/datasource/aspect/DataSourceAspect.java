package com.eugene.sumarry.dynamic.mul.datasource.aspect;

import com.eugene.sumarry.dynamic.mul.datasource.anno.DataSourceApplied;
import com.eugene.sumarry.dynamic.mul.datasource.common.DataSourceExchange;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DataSourceAspect {

    @Pointcut("@annotation(com.eugene.sumarry.dynamic.mul.datasource.anno.DataSourceApplied)")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method calledMethod = methodSignature.getMethod();
        DataSourceApplied dataSourceApplied = calledMethod.getAnnotation(DataSourceApplied.class);
        if (dataSourceApplied != null) {
            DataSourceExchange.put(dataSourceApplied.value());
        }

        System.out.println("切面befor==>" + calledMethod.getName() + "方法当前数据源: " + DataSourceExchange.get());
    }

    @After("pointcut()")
    public void after(JoinPoint joinPoint) {
        System.out.println("切面after==>" + joinPoint.getSignature().getName() + "方法当前数据源: " + DataSourceExchange.get());
        DataSourceExchange.clear();
    }

}

