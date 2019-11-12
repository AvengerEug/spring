package com.eugene.sumarry.ioc.annotationtype;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 使用prod配置类 AppConfigProd.class  会报错, 因为在UserService中userDao类型的bean有多个, 而且没有名字叫userDaoImpl1Eugene的bean,
        // @Autowired退化成@Resource后会报找不到名字叫userDaoImpl1Eugene的bean, 把环境切换成dev环境并使用dev环境的配置类启动即可(因为dev环境中自定义了bean name的生成规则)
        /*context.getEnvironment().setActiveProfiles("prod");
        context.register(AppConfigProd.class);*/

        context.getEnvironment().setActiveProfiles("dev");
        context.register(AppConfig.class);
        context.refresh();

        UserService userService = context.getBean(UserService.class);
        userService.sout("");
        UserService userService1 = context.getBean(UserService.class);
        userService1.sout("eugene1");
        UserService userService2 = context.getBean(UserService.class);
        userService2.sout("eugene2");
    }
}
