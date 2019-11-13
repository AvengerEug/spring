package com.eugene.sumarry.aop.main;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 创建了一个切面: 承载了切点、连接点、通知
 *
 * 使用@Aspect注解表示, 它支持Aspect的语法, 但他们是实现AOP的两个产品
 */
@Component
@Aspect
public class MyAspectj {


    /**
     * 切点: 包含许多连接点,  代表着一张表
     * 将com.eugene.sumarry.aop.main.dao包或子包下的任意参数、任意返回值的所有方法作为一个切点
     *
     * 里面的execution表达式叫做连接点
     */
    @Pointcut("execution(* com.eugene.sumarry.aop.main.dao..*.*(..))")
    public void myPointcut() {
    }

    /**
     *  设置了后通知----- After Advice(位置, 让增强的代码置于某个地方) => 在切点的逻辑执行完后再执行这段代码
     *
     *  里面的 "myPointcut()"  表示通知在哪个切点对应的连接点
     */
    @After("myPointcut()")
    public void afterAdvice() {
        System.out.println("after advice");
    }
}
