package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2;

import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * spring-csdn模块入口
 */
public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        // 获取UserService类型的对象, 并调用login方法, 测试是否被代理
        context.getBean(UserService.class).login();

        // 测试AppConfig类以@Bean的方式创建的TestBeanInAppConfig类是否存在
        System.out.println(context.getBean(TestBeanInAppConfig.class));

        // 测试UserDaoImpl类以@Bean的方式创建的TestDaoInUserDaoImpl类是否存在
        System.out.println(context.getBean(TestDaoInUserDaoImpl.class));
    }
}
