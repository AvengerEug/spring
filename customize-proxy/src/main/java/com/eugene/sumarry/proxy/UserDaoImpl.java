package com.eugene.sumarry.proxy;

import java.util.concurrent.TimeUnit;

public class UserDaoImpl implements UserDao {

    @Override
    public void findList() {
        try {
            System.out.println("===================查询所有用户数据===================");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getById(Long userId) {
        System.out.println("根据用户ID查找用户数据");
    }
}
