package com.eugene.sumarry.aop.main.dao;

import com.eugene.sumarry.aop.annotation.Entity;
import com.eugene.sumarry.aop.main.annotation.AspectAnnotation;
import com.eugene.sumarry.aop.main.annotation.AspectArgs;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDao {

    public void findList() {
        System.out.println("find list");
    }

    public void findList(String x) {
        System.out.println(x);
    }

    public void findList(Integer x) {
        System.out.println(x);
    }

    public void findList(String x, Integer y) {
        System.out.println(x + "   " + y);
    }

    @AspectAnnotation
    @AspectArgs
    public void testAnnotationExecution() {
        System.out.println("testAnnotationExecution");
    }

    public void testArgsAnnotationExecution(@AspectArgs String argsAnnotationParameter) {
        // testAnnotationExecution方法的AspectArgs注解无效
        System.out.println("只有加了方法参数加了@AspectArgs注解的方法才会被增强");
    }
}
