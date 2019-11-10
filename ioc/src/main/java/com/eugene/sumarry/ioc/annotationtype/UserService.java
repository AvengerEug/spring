package com.eugene.sumarry.ioc.annotationtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private String name;

    @Autowired
    private IndexDao indexDao;
}
