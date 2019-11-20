package com.eugene.sumarry.proxy.statictype.combination;

import com.eugene.sumarry.proxy.UserDao;

/**
 * 使用装饰者模式对接口中的某个类进行增强,
 * 只需要将需要增强的目标对象传入即可
 */
public class UserDaoLogImpl implements UserDao {

    private UserDao target;

    public UserDaoLogImpl(UserDao target) {
        this.target = target;
    }

    @Override
    public void findList() {
        System.out.println("记录日志");
        target.findList();
    }

    @Override
    public void getById(Long userId) {
        System.out.println("记录日志");
        target.getById(userId);
    }
}
