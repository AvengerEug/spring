package com.eugene.sumarry.dynamic.mul.datasource;

import com.eugene.sumarry.dynamic.mul.datasource.config.AppConfig;
import com.eugene.sumarry.dynamic.mul.datasource.model.User;
import com.eugene.sumarry.dynamic.mul.datasource.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);
        User user = new User();
        user.setUserId(1L);
        user.setUserName("eugene" + Math.random());
        user.setPassword(UUID.randomUUID().toString());

        // 测试无事务的情况下，读写是否用的是自己的数据源
        // 结论，每个dao使用自己的数据源
        // userService.mulCaseWithoutTransaction(user);

        // 测试spring事务传播机制为Required时，读写是否用自己的数据源
        // 结论: 尽管在ThreadLocal中设置了当前数据源的key，
        // 但是spring并未针对每次的dao调用都去获取一次数据源，只会在第一次时获取
        // 并且在调用dao代码之前就会去获取。
        // userService.mulCaseRequiredTransaction(user);

        // 测试spring事务传播机制为Not supported时，读写是否用自己的数据源
        // 结论: 加了事务机制后，spring也只会获取一次数据源，但是是在执行dao代码时
        // 才会去获取数据源。所以读和写操作使用的数据源取决于第一个dao执行过程中使用的数据源
        // userService.mulCaseNotSupportedTransaction(user);


        // 测试spring事务传播机制为Requires new时，读写是否用自己的数据源
        // 结论: 因为Requires new的机制是自己用自己的事务，
        // 但是spring并未针对每次的dao调用都去获取一次数据源，只会在第一次时获取
        // 并且在调用dao代码之前就会去获取。
        userService.mulCaseRequiresNewTransaction(user);


        // 结论, service方法中有开启事务，那么读写操作都会共用一个数据源
        // 同时要注意，只有事务传播机制为NotSupported类型是，
        // 是在调用dao代码时才会去获取数据源，此时依赖的数据源可以由
        // 切面决定。否则Requires和Requires new类型在dao的切面的前面
        // 就获取了数据源

    }
}
