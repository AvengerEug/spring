package com.eugene.sumarry.resourcecodestudy.springevent;

import com.eugene.sumarry.resourcecodestudy.springevent.customerevent.CustomerEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        //ContextRefreshEvent 不需要手动触发, 在构造方法中就被触发了
        //context.refresh();

        // ContextStartEvent、ContextStopEvent、ContextCloseEvent需要手动调用触发
//        context.start();
//        context.stop();
//        context.close();

        // 触发自定义事件
        context.publishEvent(new CustomerEvent("123"));
    }
}
