package com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SimpleBean.class);
        System.out.println(context.getBean("userDaoEugene"));
    }
}
