package com.eugene.sumarry.proxy.statictype.extend;

import com.eugene.sumarry.proxy.UserDao;

public class Entry {

    public static void main(String[] args) {
        // 先记录时间再记录日志
        //UserDao userDao = new UserDaoTimerAndLogImpl();

        // 先记录日志再记录时间
        UserDao userDao = new UserDaoLogAndTimerImpl();

        // 虽然使用继承的方式对UserDao接口的findList接口进行增强了, 但是需求越多,
        // 类数量就会越多。比如此时的同时拥有记录日志和时间的需求
        userDao.findList();
    }
}
