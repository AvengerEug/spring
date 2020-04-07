package com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {

    public static void main(String[] args) {
        Log log = LogFactory.getLog("log4j");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.addBeanFactoryPostProcessor(new MyBeanDefinitionRegistryPostProcessor());
        //context.getBeanFactory().addBeanPostProcessor(new MyBeanPostProcessor2());
        context.register(AppConfig.class);
        context.refresh();
        //System.out.println(context.getBean("userDaoEugene"));
        UserDao userDao = context.getBean(UserDao.class);
        userDao.test();
        log.info("打印日志");
        System.out.println(log);
    }
}
