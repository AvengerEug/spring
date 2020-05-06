package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * spring-csdn模块入口
 */
public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        // 手动添加BeanFactoryPostProcessor, 可以思考下, 它可以放在哪些位置？
        context.addBeanFactoryPostProcessor(new ManualImportBeanDefinitionRegistryPostProcessor());
        context.addBeanFactoryPostProcessor(new ManualImportBeanFactoryPostProcessor());

        context.register(AppConfig.class);
        context.refresh();
    }
}
