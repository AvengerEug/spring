package com.eugene.sumarry.proxy.dynamictype.jdk;

import com.eugene.sumarry.proxy.StudentDao;
import com.eugene.sumarry.proxy.StudentDaoImpl;
import com.eugene.sumarry.proxy.UserDao;
import com.eugene.sumarry.proxy.UserDaoImpl;

public class Entry {

    public static void main(String[] args) {

        UserDao userDao = (UserDao) ProxyUtils.newInstance(new UserDaoImpl(), new Class<?>[]{UserDao.class});
        userDao.findList();
        System.out.println("\n");

        StudentDao studentDao = (StudentDao) ProxyUtils.newInstance(new StudentDaoImpl(), new Class<?>[]{StudentDao.class});
        System.out.println(studentDao.findIdList());
        System.out.println("\n");
        System.out.println(studentDao.getNameById(1111199999L));
        System.out.println("\n");
    }
}
