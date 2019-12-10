package com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean;

import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Override
    public void test() {
        System.out.println("test");
    }
}
