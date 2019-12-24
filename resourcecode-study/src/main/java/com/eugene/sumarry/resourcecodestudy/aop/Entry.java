package com.eugene.sumarry.resourcecodestudy.aop;

import com.eugene.sumarry.resourcecodestudy.aop.pointcut.UserDao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.getBean(UserDao.class).test();
    }
}
