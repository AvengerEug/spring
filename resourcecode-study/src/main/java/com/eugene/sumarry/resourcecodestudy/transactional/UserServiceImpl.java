package com.eugene.sumarry.resourcecodestudy.transactional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Transactional
    @Override
    public void test() {

    }
}
