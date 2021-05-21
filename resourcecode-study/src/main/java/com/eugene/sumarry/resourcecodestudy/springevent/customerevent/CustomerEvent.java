package com.eugene.sumarry.resourcecodestudy.springevent.customerevent;

import org.springframework.context.ApplicationEvent;

public class CustomerEvent extends ApplicationEvent {

    public CustomerEvent(Object msg) {
        super(msg);
    }
}
