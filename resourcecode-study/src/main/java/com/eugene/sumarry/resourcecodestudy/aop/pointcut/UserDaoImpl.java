package com.eugene.sumarry.resourcecodestudy.aop.pointcut;

import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Override
    public void test() {
        System.out.println("test");
    }
}
