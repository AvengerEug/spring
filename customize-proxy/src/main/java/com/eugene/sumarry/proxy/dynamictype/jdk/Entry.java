package com.eugene.sumarry.proxy.dynamictype.jdk;

import com.eugene.sumarry.proxy.StudentDao;
import com.eugene.sumarry.proxy.StudentDaoImpl;
import com.eugene.sumarry.proxy.UserDao;
import com.eugene.sumarry.proxy.UserDaoImpl;
import com.sun.deploy.net.proxy.ProxyUtils;

import java.net.Proxy;

public class Entry {

    public static void main(String[] args) {

        /*UserDao userDao = (UserDao) ProxyUtils.newInstance(new UserDaoImpl(), new Class<?>[]{UserDao.class});
        //userDao.findList();
        System.out.println("\n");

        StudentDao studentDao = (StudentDao) ProxyUtils.newInstance(new StudentDaoImpl(), new Class<?>[]{StudentDao.class});
        System.out.println(studentDao.findIdList());
        System.out.println("\n");
        System.out.println(studentDao.getNameById(1111199999L));
        System.out.println("\n");


        UserDao userDaoProxyJdk = ((UserDao) Proxy.newProxyInstance(UserDao.class.getClassLoader(), new Class[]{UserDao.class}, new MyInvocationHandler(new UserDaoImpl())));
        userDaoProxyJdk.findList();*/


        UserDao proxyDao = (UserDao) ProxyUtilsJDK.newInstance(UserDaoImpl.class, new Class<?>[]{UserDao.class}, new ProxyCustomizeProxyObject(new UserDaoImpl()));
        proxyDao.getById(55555L);

        StudentDao studentDaoProxy = (StudentDao)ProxyUtilsJDK.newInstance(StudentDaoImpl.class, new Class<?>[]{StudentDao.class}, new ProxyCustomizeProxyObject(new StudentDaoImpl()));
        System.out.println(studentDaoProxy.getNameById(5555888L));

    }
}
