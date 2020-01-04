package com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Override
    public void test() {
        System.out.println("test");
    }


    @Bean
    public TestBeanAnnotation2 testBeanAnnotation2() {
        System.out.println(">>>>>>>>>>>>>>>>>123");
        return new TestBeanAnnotation2();
    }
}
