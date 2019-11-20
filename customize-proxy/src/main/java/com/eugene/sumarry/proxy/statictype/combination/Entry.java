package com.eugene.sumarry.proxy.statictype.combination;

import com.eugene.sumarry.proxy.UserDao;
import com.eugene.sumarry.proxy.UserDaoImpl;

public class Entry {

    public static void main(String[] args) {
        // 增强日志功能
        UserDao userDaoLog = new UserDaoLogImpl(new UserDaoImpl());
        userDaoLog.findList();

        // 增强记录时间功能
        UserDao userDaoTimer = new UserDaoTimerImpl(new UserDaoImpl());
        userDaoTimer.findList();

        // 同时拥有记录日志和记录时间的功能呢
        UserDao userDao = new UserDaoLogImpl(userDaoTimer);
        userDao.findList();
    }
}
