package com.eugene.sumarry.dynamic.mul.datasource.service.impl;

import com.eugene.sumarry.dynamic.mul.datasource.dao.UserDao;
import com.eugene.sumarry.dynamic.mul.datasource.model.User;
import com.eugene.sumarry.dynamic.mul.datasource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void add(User user) {
        userDao.insert(user);
    }

    @Override
    public List<User> list() {
        return userDao.list();
    }


    /**
     * 无事务的情况下，读和写用的是不同的数据源
     * @param user
     */
    @Override
    public void mulCaseWithoutTransaction(User user) {
        System.out.println(userDao.list());
        userDao.update(user);
        userDao.insert(user);
        System.out.println(userDao.list());
    }

    /**
     * 测试一个service中涉及多个dao操作
     * 事务传播机制 =============>                           结论
     *   REQUIRED(没有则开启，有则共享)           尽管是读写分离, 读操作还是能读取到事务修改过的数据(因为只会在第一次获取数据源，后续的切面尽管设置了ThreadLocal中的值，但是没有调用com.eugene.sumarry.dynamic.mul.datasource.common.DynamicDataSource#determineCurrentLookupKey()方法来获取数据源)
     *
     * 输出结果:
     * 当前真实数据源: MASTER
     * 切面befor==>list方法当前数据源: SLAVE
     * 切面after==>list方法当前数据源: SLAVE
     * [User{userId=1, userName='xxx', password='xxx'}, User{userId=2, userName='eugene0.8866451885837224', password='7b7c48c6-1a1d-4905-a3d6-7765ccf49f93'}]
     * 切面befor==>update方法当前数据源: MASTER
     * 切面after==>update方法当前数据源: MASTER
     * 切面befor==>insert方法当前数据源: MASTER
     * 切面after==>insert方法当前数据源: MASTER
     * 切面befor==>list方法当前数据源: SLAVE
     * 切面after==>list方法当前数据源: SLAVE
     * [User{userId=1, userName='eugene0.09530567168537707', password='cf22f65a-fe3b-4f1d-9b43-b4dac668cab7'}, User{userId=2, userName='eugene0.8866451885837224', password='7b7c48c6-1a1d-4905-a3d6-7765ccf49f93'}, User{userId=18, userName='eugene0.09530567168537707', password='cf22f65a-fe3b-4f1d-9b43-b4dac668cab7'}]
     * Disconnected from the target VM, address: '127.0.0.1:56318', transport: 'socket'
     *
     * @param user
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void mulCaseRequiredTransaction(User user) {
        System.out.println(userDao.list());
        userDao.update(user);
        userDao.insert(user);
        System.out.println(userDao.list());
        int x = 1 / 0;
    }


    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void mulCaseNotSupportedTransaction(User user) {
        System.out.println(userDao.list());
        userDao.update(user);
        userDao.insert(user);
        System.out.println(userDao.list());
        int x = 1 / 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mulCaseRequiresNewTransaction(User user) {
        System.out.println(userDao.list());
        userDao.update(user);
        userDao.insert(user);
        System.out.println(userDao.list());
        int x = 1 / 0;
    }
}
