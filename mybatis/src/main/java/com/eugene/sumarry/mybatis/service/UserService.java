package com.eugene.sumarry.mybatis.service;

import com.eugene.sumarry.mybatis.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public void list() {
        System.out.println(userDao.list());
    }
}
