package com.eugene.sumarry.resourcecodestudy.aoporder;

import org.springframework.stereotype.Component;

@Component
public class BeanA {


    @AspectAop1.AOP1
    @AspectAop2.AOP2
    public void test() {
        System.out.println("beanA's test method.");
    }
}
