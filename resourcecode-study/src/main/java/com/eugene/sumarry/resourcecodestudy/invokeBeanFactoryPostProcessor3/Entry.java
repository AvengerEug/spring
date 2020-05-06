package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor3;

import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 可以只注册一个类, 这里在spring源码系列的第一篇就提到了
 */
public class Entry {

    public static void main(String[] args) {
	    System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "g://cglib");
        new AnnotationConfigApplicationContext(AppConfig.class);
    }
}
