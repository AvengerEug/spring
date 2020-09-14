package com.eugene.sumarry.resourcecodestudy.transactional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService2 {

    @Transactional
    public void testNoInterface() {

    }
}
