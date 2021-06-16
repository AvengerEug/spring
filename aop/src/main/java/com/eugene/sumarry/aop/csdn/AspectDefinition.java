package com.eugene.sumarry.aop.csdn;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AspectDefinition {

    /**
     * 指定包含@AspectAnnotation注解的方法才会被增强
     */
    @Pointcut("@annotation(com.eugene.sumarry.aop.csdn.Entry.AspectAnnotation)")
    public void pointcutAnnotation() {
    }

    /**
     * 前置通知：执行目标方法前 触发
     */
    @Before(value = "pointcutAnnotation()")
    public void methodBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("前置通知：方法名：" + methodName + "，参数" + Arrays.asList(joinPoint.getArgs()));
    }

    /**
     * 后置通知：执行目标方法后触发
     */
    @After(value = "pointcutAnnotation()")
    public void methodAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("后置通知：方法名：" + methodName + "，参数" + Arrays.asList(joinPoint.getArgs()));
    }

    /**
     * 返回通知：目标方法执行完并返回参数后触发。
     */
    @AfterReturning(value = "pointcutAnnotation()", returning = "result")
    public void methodAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("返回通知：方法名：" + methodName + "，" +
                "参数" + Arrays.asList(joinPoint.getArgs()) + "，" +
                "返回结果：");
        if (result instanceof String[]) {
            Arrays.stream((String[]) result).forEach(System.out::println);
        } else {
            System.out.println(result);
        }
    }

    /**
     * 异常通知：目标方法抛出异常后触发
     */
    @AfterThrowing(value = "pointcutAnnotation()", throwing="ex")
    public void methodExceptionOccurred(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("异常通知：方法名：" + methodName + "，参数" + Arrays.asList(joinPoint.getArgs()) + "，异常信息：" + ex.getMessage());
    }

}
