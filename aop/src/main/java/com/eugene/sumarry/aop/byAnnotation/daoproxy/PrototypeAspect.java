package com.eugene.sumarry.aop.byAnnotation.daoproxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 1. @Scope("prototype")表示每次获取的代理对象都是原型的
 * 2. ("perthis(this(com.eugene.sumarry.aop.main.daoproxy.PrototypeDao))")
 *    表示当代理对象的类型是com.eugene.sumarry.aop.main.daoproxy.PrototypeDao
 *    时, 代理对象是原型的, 其它的为单例的
 * 3. 每一次获取PrototypeDao类型的bean的时候, 代理对象都是最新的.
 * 4. 当目标对象是单例的, 但是代理对象是原型的, 在ProceedingJoinPoint类中获取代理对象和目标对象都是最新的。
 */
@Component
@Aspect("perthis(this(com.eugene.sumarry.aop.main.daoproxy.PrototypeDao))")
@Scope("prototype")
public class PrototypeAspect {

    /**
     * 对com.eugene.sumarry.aop.main.daoproxy.PrototypeDao类下的所有方法进行增强,
     * 并且当代理对象的类型是com.eugene.sumarry.aop.main.daoproxy.PrototypeDao时,
     * 代理对象的scope为prototype
     */
    @Pointcut("execution(* com.eugene.sumarry.aop.byAnnotation.daoproxy.PrototypeDao.*(..))")
    public void prototypePointcut() {
    }

    @Around("prototypePointcut()")
    public void testPrototypeAspect(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            // this等同于proceedingJoinPoint.getThis()
            System.out.println("环绕前");
            System.out.println("代理对象hashcode: " + this.hashCode());
            System.out.println("目标对象hashcode: " + proceedingJoinPoint.getTarget().hashCode());
            proceedingJoinPoint.proceed();
            System.out.println("环绕后");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
