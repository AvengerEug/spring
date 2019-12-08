package com.eugene.sumarry.implementmapperscan.dao;

import com.eugene.sumarry.implementmapperscan.anno.Select;

public interface UserDao {

    @Select("SELECT * FROM user")
    void findList();
}
