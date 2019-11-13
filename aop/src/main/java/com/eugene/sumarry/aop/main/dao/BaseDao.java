package com.eugene.sumarry.aop.main.dao;

import org.springframework.stereotype.Repository;

@Repository
public class BaseDao {

    public void findList() {
        System.out.println("find list");
    }
}
