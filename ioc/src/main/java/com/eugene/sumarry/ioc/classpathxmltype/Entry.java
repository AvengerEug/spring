package com.eugene.sumarry.ioc.classpathxmltype;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Entry {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        UserService userService = context.getBean(UserService.class);
        UserDao userDao = (UserDao) context.getBean("userDaoImpl1");
    }
}
