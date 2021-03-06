package com.eugene.sumarry.mybatis.dao;

import com.eugene.sumarry.mybatis.model.User;
import org.apache.ibatis.annotations.CacheNamespace;

import java.util.List;

public interface UserDao {

    List<User> list();

    void update();
}
