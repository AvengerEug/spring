package com.eugene.sumarry.resourcecodestudy.springevent;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class CustomerApplicationContextEvent extends ApplicationContextEvent {

    public CustomerApplicationContextEvent(ApplicationContext source) {
        super(source);
    }
}
