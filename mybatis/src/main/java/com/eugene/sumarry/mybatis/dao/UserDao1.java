package com.eugene.sumarry.mybatis.dao;

import com.eugene.sumarry.mybatis.model.User;

import java.util.List;

public interface UserDao1 {

    List<User> list();

    void update();
}
