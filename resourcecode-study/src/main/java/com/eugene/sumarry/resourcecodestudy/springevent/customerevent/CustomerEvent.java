package com.eugene.sumarry.resourcecodestudy.springevent.customerevent;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class CustomerEvent extends ApplicationContextEvent {

    public CustomerEvent(ApplicationContext source) {
        super(source);
    }
}
