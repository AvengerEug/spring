package com.eugene.sumarry.aop.byXML;

import org.aspectj.lang.ProceedingJoinPoint;

public class XMLAspectBean {

    private StudentDao studentDao;

    public void before() {
        System.out.println("before");
    }

    public void after() {
        System.out.println("after");
    }

    public void around(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            System.out.println("before around");
            proceedingJoinPoint.proceed();
            System.out.println("after around");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
