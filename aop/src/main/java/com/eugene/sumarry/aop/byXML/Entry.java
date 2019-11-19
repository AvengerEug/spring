package com.eugene.sumarry.aop.byXML;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Entry {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-byXML.xml");
        Dao dao = ((Dao) context.getBean("userDao"));

        System.out.println("============================================");
        dao.findList();

        System.out.println("============================================");
        /*Dao dao1 = ((Dao) context.getBean("studentDao"));
        dao1.findList();*/

    }
}
