package com.eugene.sumarry.ioc.classpathxmltype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl1 implements UserDao {

    @Autowired
    private IndexDao indexDao;
}
