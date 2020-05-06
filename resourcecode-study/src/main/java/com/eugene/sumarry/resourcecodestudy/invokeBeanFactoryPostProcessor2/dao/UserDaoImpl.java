package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.dao;

import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.TestDaoInUserDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

/**
 * 模拟dao层对象
 */
@Repository
public class UserDaoImpl {


    /**
     * 这里先引入一个问题: 这个bean(TestDaoInUserDaoImpl)会不会被创建出来？
     */
    @Bean
    public TestDaoInUserDaoImpl testDaoInUserDaoImpl() {
        return new TestDaoInUserDaoImpl();
    }

    public void test() {
        System.out.println("dao模拟查询数据逻辑");
    }

}
