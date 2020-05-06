package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.service.impl;

import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.dao.UserDaoImpl;
import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.service.UserService;
import org.springframework.stereotype.Service;

/**
 * service层, 未使用@Autowired注解来自动装配
 */
@Service
public class UserServiceImpl implements UserService {

    private UserDaoImpl userDaoImpl;

    public void setUserDaoImpl(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }

    @Override
    public void login() {
        //userDaoImpl.test();
        System.out.println("校验登录逻辑");
    }

}
