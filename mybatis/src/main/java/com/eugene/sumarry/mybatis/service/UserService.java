package com.eugene.sumarry.mybatis.service;

import com.eugene.sumarry.mybatis.dao.UserDao;
import com.eugene.sumarry.mybatis.dao.UserDao1;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserDao userDao;

    UserDao1 userDao1;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserDao1(UserDao1 userDao1) {
        this.userDao1 = userDao1;
    }

    public void list() {
        // mybatis 集成spring后 一级缓存失效了,
        // 原因: spring管理了session, 每次查询都会新开session和关闭session
        // 具体到时候再看源码一探究竟
        System.out.println(userDao.list());
        System.out.println(userDao.list());
    }

    public void update() {
        userDao1.update();
    }
}
