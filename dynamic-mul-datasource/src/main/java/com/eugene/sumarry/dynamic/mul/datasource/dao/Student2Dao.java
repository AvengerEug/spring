package com.eugene.sumarry.dynamic.mul.datasource.dao;

import com.eugene.sumarry.dynamic.mul.datasource.Enum.DataSourceType;
import com.eugene.sumarry.dynamic.mul.datasource.anno.DataSourceApplied;
import com.eugene.sumarry.dynamic.mul.datasource.model.Student2;
import com.eugene.sumarry.dynamic.mul.datasource.model.User;

import java.util.List;

public interface Student2Dao {

    @DataSourceApplied(DataSourceType.SLAVE)
    List<User> list();

    @DataSourceApplied
    void insert(Student2 student2);

    @DataSourceApplied
    void update(Student2 student2);
}
