package com.eugene.sumarry.resourcecodestudy.aop;

import com.eugene.sumarry.resourcecodestudy.aop.pointcut.UserDao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserDao bean = context.getBean(UserDao.class);
        // debugger到此处，查看bean对象中所有方法，你会发现并没有private修饰的方法，因此可以确定：cglib并不会对private方法进行增强，并且也不会报错
        bean.test();
    }
}
