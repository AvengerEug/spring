package com.eugene.sumarry.dynamic.mul.datasource.dao;

import com.eugene.sumarry.dynamic.mul.datasource.Enum.DataSourceType;
import com.eugene.sumarry.dynamic.mul.datasource.anno.DataSourceApplied;
import com.eugene.sumarry.dynamic.mul.datasource.model.User;

import java.util.List;

public interface UserDao {

    @DataSourceApplied(DataSourceType.SLAVE)
    List<User> list();

    @DataSourceApplied
    void insert(User user);

    @DataSourceApplied
    void update(User user);
}
