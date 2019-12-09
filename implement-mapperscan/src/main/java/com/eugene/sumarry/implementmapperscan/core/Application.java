package com.eugene.sumarry.implementmapperscan.core;

import com.eugene.sumarry.implementmapperscan.anno.MapperScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    private static String resourcePath = "";

    static {
        resourcePath = Application.class.getResource("/").toString();
    }

    public static void run(Class<?> appConfig) {

        String scanPkg = null;

        if (appConfig.isAnnotationPresent(MapperScan.class)) {
            MapperScan mapperScan = appConfig.getAnnotation(MapperScan.class);
            scanPkg = mapperScan.value();
        }

        if (scanPkg == null || scanPkg.length() == 0) {
            throw new RuntimeException("配置类必须包含MapperScan注解");
        }


        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //context.addBeanFactoryPostProcessor(new MapperScanBeanDefinitionRegistryPostProcessor());
        context.register(appConfig);
        context.refresh();
        /*UserDao userDao1 = context.getBean(UserDao.class);
        UserDao userDao2 = context.getBean(UserDao.class);
        System.out.println(userDao1.getClass());
        userDao1.findList();
        System.out.println(userDao1 == userDao2);*/
        //System.out.println(userDao1.hashCode() + " ========= " + userDao2.hashCode());
    }
}
