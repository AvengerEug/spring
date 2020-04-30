package com.eugene.sumarry.dynamic.mul.datasource.dao;

import com.eugene.sumarry.dynamic.mul.datasource.Enum.DataSourceType;
import com.eugene.sumarry.dynamic.mul.datasource.anno.DataSourceApplied;
import com.eugene.sumarry.dynamic.mul.datasource.model.Student1;
import com.eugene.sumarry.dynamic.mul.datasource.model.User;

import java.util.List;

public interface Student1Dao {

    @DataSourceApplied(DataSourceType.SLAVE)
    List<User> list();

    @DataSourceApplied
    void insert(Student1 student1);

    @DataSourceApplied
    void update(Student1 student1);
}
