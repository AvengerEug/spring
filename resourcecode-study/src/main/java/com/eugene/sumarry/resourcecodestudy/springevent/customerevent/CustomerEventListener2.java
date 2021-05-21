package com.eugene.sumarry.resourcecodestudy.springevent.customerevent;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventListener2 implements ApplicationListener<CustomerEvent> {

    @Override
    public void onApplicationEvent(CustomerEvent event) {
        System.out.println(event + "----------------2");
    }
}
