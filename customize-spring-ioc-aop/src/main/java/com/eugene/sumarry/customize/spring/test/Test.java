package com.eugene.sumarry.customize.spring.test;

import com.eugene.sumarry.customize.spring.context.ApplicationContext;
import com.eugene.sumarry.customize.spring.test.config.AppConfig;

public class Test {

    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext(AppConfig.class);
    }
}
