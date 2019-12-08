package com.eugene.sumarry.implementmapperscan;

import com.eugene.sumarry.implementmapperscan.conf.AppConfig;
import com.eugene.sumarry.implementmapperscan.dao.UserDao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.getBean(UserDao.class).findList();
    }
}
