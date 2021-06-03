package com.eugene.sumarry.mybatis.service;

import com.eugene.sumarry.mybatis.dao.UserDao;
import com.eugene.sumarry.mybatis.dao.UserDao1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    UserDao userDao;

    UserDao1 userDao1;

    @Autowired
    private TransactionLifecyclePostProcessor transactionLifecyclePostProcessor;

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
        System.out.println(userDao1.list());
    }

    /**
     * @Transactional注解可以添加在类中也可以添加在方法中, 若添加在类中则对类中的所有方法
     * 都添加事务管理,
     *
     * 要想添加事务需要做如下步骤:
     *   1. 开启事务管理, 在一个bean中添加@EnableTransactionManagement注解表示开启事务管理
     *   2. 添加一个事务管理器(也能自己自定义一个事务逻辑, 可以参考MyTransactionManager类)， 如下
     *     @Bean
     *     public PlatformTransactionManager platformTransactionManager() {
     *         DataSourceTransactionManager manager = new DataSourceTransactionManager();
     *         manager.setDataSource(dataSource());
     *         return manager;
     *     }
     *   3. 设置事务回滚的类名, 这里设置了是抛出了exception类型的异常就回滚
     */
    @Transactional(rollbackFor = Exception.class)
    public void update() {
        userDao1.update();
        transactionLifecyclePostProcessor.test();
    }
}
