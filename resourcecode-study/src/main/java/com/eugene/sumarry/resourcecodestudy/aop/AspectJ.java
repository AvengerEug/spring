package com.eugene.sumarry.resourcecodestudy.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 所以AOP的核心就是:
 *  1. 通过@Import注解导入了一个类型为ImportBeanDefinitionRegistrar类型的类,
 *  这个类提供的api中可以拿到自己被谁导入的类的所有注解信息, 所以可以根据注解
 *  中设置的值来做具体的事情
 *
 *  2. 在导入的类中动态的给bean工厂添加了一个实现了BeanPostProcessor接口的后置处理器, 为什么
 *  使用这个扩展点？ 很简单, 有了目标对象后, 再根据配置来决定是否生成代理
 */
@Component
@Aspect
public class AspectJ {

    @Pointcut("execution(* com.eugene.sumarry.resourcecodestudy.aop.pointcut.*.*(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before() {
        System.out.println("before");
    }
}
