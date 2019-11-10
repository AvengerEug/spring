package com.eugene.sumarry.ioc.classpathxmltype;

public class UserService {

    private UserDao userDao;

    private String name;

    private IndexDao indexDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIndexDao(IndexDao indexDao) {
        this.indexDao = indexDao;
    }
}
