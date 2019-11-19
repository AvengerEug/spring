package com.eugene.sumarry.aop.byXML;

public class UserDao implements Dao {

    @Override
    public void findList() {
        System.out.println("find list");
    }
}
