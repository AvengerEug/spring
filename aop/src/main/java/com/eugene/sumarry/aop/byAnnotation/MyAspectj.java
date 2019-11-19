package com.eugene.sumarry.aop.byAnnotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
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
     * 切点主要表达式: 可以精确到方法及方法携带的参数个数和类型
     *
     * 里面的execution表达式叫做连接点
     */
    @Pointcut("execution(* com.eugene.sumarry.aop.byAnnotation.dao..*.*(..))")
    public void pointcutExecution() {
    }

    /**
     *  设置了后通知----- After Advice(位置, 让增强的代码置于某个地方) => 在切点的逻辑执行完后再执行这段代码
     *
     *  里面的 "myPointcut()"  表示通知在哪个切点对应的连接点
     */
    @After("pointcutExecution()")
    public void afterPointcutExecution() {
        System.out.println("afterPointcutExecution after advice");
    }


    /**
     * Within粒度比较大, 只能精确包下面的类
     */
    @Pointcut("within(com.eugene.sumarry.aop.byAnnotation.dao.*)")
    public void pointcutWithin() {
    }

    @Before("pointcutWithin()")
    public void beforePointcutWithin() {
        System.out.println("pointcutWithin before");
    }

    /**
     * 精确到可扫描包的所有第一个参数为Integer的方法
     */
    @Pointcut("args(java.lang.Integer, ..)")
    public void pointcutArgs() {
    }

    @Before("pointcutArgs()")
    public void beforePointcutArgs() {
        System.out.println("beforePointcutArgs around");
    }

    /**
     * 对dao包及子包下的所有方法做增强, 除了第一个参数为string第二个参数为integer的方法
     *
     * 通知中里面执行的切点可以是对应的函数 也可以是表达式
     */
    @After("pointcutExecution() && !execution(* com.eugene.sumarry.aop.byAnnotation.dao..*.*(java.lang.String, java.lang.Integer)))")
    public void conditionAfter() {
        System.out.println("condition after");
    }


    /**
     * 定义了一个切点, 表示带了@AspectAnnotation注解的才会被增强
     */
    @Pointcut("@annotation(com.eugene.sumarry.aop.byAnnotation.annotation.AspectAnnotation)")
    public void pointcutAnnotation() {
    }

    @Before("pointcutAnnotation()")
    public void beforePointcutAnnotation() {
        System.out.println("带了@annotation注解的方法或者类才会被增强");
    }

    /**
     * 定义参数加了@args的方法才会增强的切点
     */
    @Pointcut("@args(com.eugene.sumarry.aop.byAnnotation.annotation.AspectArgs)")
    public void pointcutArgsAnnotation() {
    }

    @After("pointcutArgsAnnotation()")
    public void afterArgsAnnotation() {
        System.out.println("参数上加了args注解才会被增强");
    }

    /**
     * 定义在aop环境下添加了注解的类中的所有方法会被增强
     */
    @Pointcut("@within(com.eugene.sumarry.aop.byAnnotation.annotation.AspectWithin)")
    public void pointcutWithinAnnotation() {
    }

    @After("pointcutWithinAnnotation()")
    public void afterWithinAnnotation() {
        System.out.println("类上加上@AspwctWithin注解时, 里面的方法才会被增强");
    }

    /**
     * 定义一个切点, 只对dao包下的第一个参数为long的方法增强
     */
    @Pointcut("execution(* com.eugene.sumarry.aop.byAnnotation.dao..*(java.lang.Long, ..))")
    public void aroundPointcut() {
    }

    @Around("aroundPointcut()")
    public void testAroundPointcut(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("环绕通知---- 入口");
        Object[] args = proceedingJoinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                args[i] = (Long)args[i] + 90L;
            }
        }

        try {
            proceedingJoinPoint.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        System.out.println("环绕结束");
    }

}
