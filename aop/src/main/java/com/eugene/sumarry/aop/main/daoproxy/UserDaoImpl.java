package com.eugene.sumarry.aop.main.daoproxy;

import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Override
    public void findList() {
        System.out.println("find user list");
    }

    @Override
    public void findById(Integer id) {
        System.out.println("find by id");
    }
}
