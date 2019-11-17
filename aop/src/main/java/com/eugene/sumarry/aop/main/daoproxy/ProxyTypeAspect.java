package com.eugene.sumarry.aop.main.daoproxy;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ProxyTypeAspect {

    /**
     * 表示当生成的代理对象的类型是com.eugene.sumarry.aop.main.daoproxy.UserDaoImpl时,
     * 改切点才会生效,
     *
     * 所以采用cglib代理生成的对象才会满足该切点的条件
     */
    @Pointcut("this(com.eugene.sumarry.aop.main.daoproxy.UserDaoImpl)")
    public void thisPointcut() {
    }

    @Before("thisPointcut()")
    public void beforeThisPointcut() {
        System.out.println("代理对象类型是UserDaoImpl时, 会被增强");
    }

    /**
     * 表示当生成的代理对象的目标对象类型是com.eugene.sumarry.aop.main.daoproxy.UserDaoImpl时,
     * 该切点才会生效
     */
    @Pointcut("target(com.eugene.sumarry.aop.main.daoproxy.UserDaoImpl)")
    public void targetPointcut() {
    }

    @Before("thisPointcut()")
    public void beforeTargetPointcut() {
        System.out.println("代理对象的目标对象类型是UserDaoImpl时, 会被增强");
    }

}
