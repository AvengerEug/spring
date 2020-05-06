package com.eugene.sumarry.resourcecodestudy.circularreference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class B {

    @Autowired
    A a;
    
    @PostConstruct
    public void aa() {
        System.out.println("aa");
    }
}
