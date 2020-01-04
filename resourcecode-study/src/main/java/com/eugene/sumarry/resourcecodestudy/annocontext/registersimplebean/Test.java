package com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.addBeanFactoryPostProcessor(new MyBeanDefinitionRegistryPostProcessor());
        //context.getBeanFactory().addBeanPostProcessor(new MyBeanPostProcessor2());
        context.register(AppConfig.class);
        context.refresh();
        //System.out.println(context.getBean("userDaoEugene"));
        UserDao userDao = context.getBean(UserDao.class);
        userDao.test();
    }
}
