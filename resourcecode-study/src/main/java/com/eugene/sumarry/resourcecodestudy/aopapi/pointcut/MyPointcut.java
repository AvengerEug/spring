package com.eugene.sumarry.resourcecodestudy.aopapi.pointcut;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

public class MyPointcut implements Pointcut {

    @Override
    public ClassFilter getClassFilter() {
        // 在类级别上不进行拦截
        return ClassFilter.TRUE;
    }

    @Override
    public MethodMatcher getMethodMatcher() {

        // 对toString方法不拦截
        return new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                return !"toString".equals(method.getName());
            }
        };
    }
}
